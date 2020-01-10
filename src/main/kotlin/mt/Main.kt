package mt

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel
import javax.swing.*
import javax.swing.table.TableModel

val mainForm = MainForm()
val frame = JFrame("MainForm").apply { title = "Машина Тьюринга" }
var currentCellIndex = 0
var tm = tests.first()()
    set(tm) {
        currentCellIndex = 0
        field = tm
        repaintTape()
        repaintProgram()
        clearLog()
    }

var logList = DefaultListModel<String>()

fun createGUI() {
    UIManager.setLookAndFeel(WindowsLookAndFeel())
    initMenu(frame)

    mainForm.run {
        buttonLogClear.addActionListener {
            clearLog()
        }

        tableTape.run {
            tableHeader = null
            model = TapeTable()
        }

        val model = ProgramTable()
        tableProgram.run {
            tableHeader = null
            this.model = model

            enableCellAutoCreate(model)
        }

        buttonLeft.addActionListener {
            --currentCellIndex
            reloadFromTape()
            repaintTape()
        }

        buttonRight.addActionListener {
            ++currentCellIndex
            reloadFromTape()
            repaintTape()
        }

        spinnerStepsCount.model = SpinnerNumberModel(1, 1, 1_000_000, 1)

        listLog.model = logList

        buttonRun.addActionListener {
            reloadFromTape()
            val stepsCount = spinnerStepsCount.value as Int
            tm.asSequence().take(stepsCount).forEach {
                val currentStep = tm.stepsCount
                val tape = tm.tape
                currentCellIndex = tm.currentPosition
                val currentSymbol = tape[currentCellIndex] ?: tm.defaultSymbol
                val currentState = tm.currentState
                val currentRule = tm.currentRule
                val s = currentRule?.let { " ${it.nextState} ${it.writeSymbol} ${it.move} " } ?: "$currentState $currentSymbol"
                logList.addElement("%2d) %s <-\t%s".format(currentStep, s, (tableTape.model as TapeTable)._toString()))
            }
            if (tm.currentState == tm.lastState) {
                logList.addElement("Программа завершена")
            } else if (!tm.hasNext()) {
                logList.addElement("Нет правила для ${tm.currentState} ${tm.tape[tm.currentPosition] ?: tm.defaultSymbol}")
            }
            repaintTape()
            repaintProgram()
        }
    }

    repaintTape()

    frame.run {
        contentPane = mainForm.panelMain
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        pack()
        isVisible = true
    }
}

fun clearLog() {
    logList = DefaultListModel()
    mainForm.listLog.model = logList
}

fun TableModel.columnIsEmpty(column: Int): Boolean =
        (1 until rowCount)
                .mapNotNull { row ->
                    try {
                        getValueAt(row, column) as String?
                    } catch(e: ArrayIndexOutOfBoundsException) {
                        null
                    }
                }
                .all(String::isEmpty)

fun TableModel.rowIsEmpty(row: Int): Boolean =
        (1 until columnCount)
                .mapNotNull { column ->
                    try {
                        getValueAt(row, column) as String?
                    } catch(e: ArrayIndexOutOfBoundsException) {
                        null
                    }
                }
                .all(String::isEmpty)

fun JTable.enableCellAutoCreate(model: ProgramTable) {
    selectionModel.addListSelectionListener { event ->
        if (event.valueIsAdjusting)
            return@addListSelectionListener

        val selectedColumn = selectedColumn
        val selectedRow = selectedRow

        val column = if (selectedColumn == columnCount - 1 && !model.columnIsEmpty(selectedColumn)) {
            model.addColumn("")
            selectedColumn + 1
        } else selectedColumn

        val row = if (selectedRow == rowCount - 1 && !model.rowIsEmpty(selectedRow)) {
            model.addRow(arrayOf(""))
            selectedRow + 1
        } else selectedRow

        scrollRectToVisible(getCellRect(row, column, true))
    }
}

fun repaintTape() = with(mainForm.tableTape) {
    val model = model
    model as TapeTable
    model.modifying = true
    val columnCount = columnCount
    repeat(columnCount) { i ->
        val pos = currentCellIndex - columnCount / 2 + i
        val s = tm.tape[pos] ?: tm.defaultSymbol
        val str = if (pos == tm.currentPosition) "[$s]" else s
        setValueAt(pos, 0, i)
        setValueAt(str, 1, i)
    }
    model.modifying = false
}

fun TapeTable._toString() = buildString {
    val min = tm.tape.keys.min()!!
    val max = tm.tape.keys.max()!!
    for (i in min..max) {
        val s = (tm.tape[i] ?: tm.defaultSymbol).first().toString()
        val str = if (i == tm.currentPosition) "$s(${tm.currentState})" else s
        append(str)
    }
}

fun reloadFromTape() = with(mainForm.tableTape) {
    for (column in 0 until columnCount) {
        val v = getValueAt(1, column) as String? ?: return
        val tapeValue = v.trim('[', ']')
        val model = mainForm.tableProgram.model as ProgramTable
        if (tapeValue !in model.symbols) {
            setValueAt(tm.defaultSymbol, 1, column)
            return
        }
        val tapeIndex = getValueAt(0, column) as Int
        if (tm.tape[tapeIndex] != tapeValue)
            tm.tape[tapeIndex] = tapeValue
    }
}

fun repaintProgram() = with(mainForm.tableProgram) {
    val model = ProgramTable()
    for (row in 0 until model.rowCount)
        for (column in 0 until model.columnCount)
            model.repaintCell(row, column)
    this.model = model
}

fun initMenu(frame: JFrame) {
    val menuBar = JMenuBar()
    val menu = JMenu("Программы")

    val newProgram = JMenuItem("Новая программа")
    newProgram.addActionListener { tm = getEmptyTm() }
    menu.add(newProgram)

    tests.forEachIndexed { i, getTm ->
        val jMenuItem = JMenuItem("Тест ${i + 1}")
        jMenuItem.addActionListener { tm = getTm() }
        menu.add(jMenuItem)
    }

    menuBar.add(menu)
    frame.jMenuBar = menuBar
}
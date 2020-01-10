package mt

import java.util.*
import javax.swing.table.DefaultTableModel

class ProgramTable : DefaultTableModel() {
    val symbols: MutableList<String>
    val states: MutableList<String>
    private var modifying = false

    init {
        val statesSet = mutableSetOf<String>()
        val symbolsSet = mutableSetOf<String>()

        tm.rules.forEach { (state, tapeSymbol, nextState, writeSymbol) ->
            statesSet += state
            statesSet += nextState
            symbolsSet += tapeSymbol
            symbolsSet += writeSymbol
        }

        statesSet -= tm.lastState

        states = if (statesSet.isEmpty()) {
            mutableListOf("q1", "q2", "q3")
        } else {
            statesSet.toMutableList().apply { sort() }
        }
        symbols = if (symbolsSet.isEmpty()) {
            mutableListOf("0", "1", "2", "S0")
        } else {
            symbolsSet.toMutableList().apply { sort() }
        }

        columnCount = symbols.size + 1

        addRow(arrayOf("", *symbols.toTypedArray()))
        states.forEach { s -> addRow(arrayOf(s)) }
        tm.rules.forEach { r ->
            setValueAt(r._toString(), 1 + states.indexOf(r.state), 1 + symbols.indexOf(r.tapeSymbol))
        }

        addTableModelListener {
            if (modifying) {
                modifying = false
                return@addTableModelListener
            }
            val column = it.column
            val row = it.firstRow
            if (column == -1 || row == -1)
                return@addTableModelListener

            val rule = repaintCell(row, column)
            val state = getValueAt(row, 0) as String?
            val tapeSymbol = getValueAt(0, column) as String?
            if (rule == null && state != null && tapeSymbol != null) {
                val i = tm.rules.indexOfFirst { it.tapeSymbol == tapeSymbol && it.state == state }
                if (i != -1) {
                    tm.rules.removeAt(i)
                }
            }
            if (rule != null) {
                val i = tm.rules.indexOfFirst { it.tapeSymbol == rule.tapeSymbol && it.state == rule.state }
                if (i== -1)
                    tm.rules.add(rule)
                else {
                    tm.rules[i] = rule
                }
                val writeSymbol = rule.writeSymbol
                if (writeSymbol !in symbols) {
                    val v = getValueAt(0, columnCount - 1) as String?
                    if (!v.isNullOrEmpty()) {
                        addColumn(writeSymbol)
                    }
                    modifying = true
                    setValueAt(writeSymbol, 0, columnCount - 1)
                    symbols += writeSymbol
                }
                val nextState = rule.nextState
                if (nextState !in states) {
                    val v = getValueAt(rowCount - 1, 0) as String?
                    if (!v.isNullOrEmpty()) {
                        addRow(arrayOf(nextState))
                    }
                    modifying = true
                    setValueAt(nextState, rowCount - 1, 0)
                    states += nextState
                }
            }
        }
    }

    fun repaintCell(row: Int, column: Int): TuringMachine.Rule<String, String>? {
        if (row < 1 || column < 1) return null
        val text = getValueAt(row, column) as String? ?: return null
        val state = getValueAt(row, 0) as String? ?: return null
        val tapeSymbol = getValueAt(0, column) as String? ?: return null
        val rule = parseRule(text, state, tapeSymbol)
        val newText = rule?._toString()?.let {
            if (state == tm.currentState && tapeSymbol == tm.tape[tm.currentPosition])
                "[$it]"
            else it
        } ?: ""

        modifying = true
        setValueAt(newText, row, column)
        return parseRule(text, state, tapeSymbol)
    }

    private fun parseRule(s: String, state: String, tapeSymbol: String): TuringMachine.Rule<String, String>? {
        val words = s.trim().split(' ')

        if (words.size != 3) {
            return null
        }

        val (nextState, symbol, moveString) = words

        val move = when (moveString.toLowerCase()) {
            "l" -> TuringMachine.Move.LEFT
            "c" -> TuringMachine.Move.NONE
            "r" -> TuringMachine.Move.RIGHT
            else -> return null
        }

        return TuringMachine.Rule(state, tapeSymbol, nextState, symbol, move)
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return !(column == 0 && row == 0)
    }

    fun removeColumn(column: Int) {
        @Suppress("UNCHECKED_CAST")
        (dataVector as Vector<Vector<*>>).forEach {
            it.removeElementAt(column)
        }

        columnIdentifiers.removeElementAt(column)
        fireTableStructureChanged()
    }
}

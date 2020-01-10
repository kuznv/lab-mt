package mt

import javax.swing.table.DefaultTableModel

class TapeTable : DefaultTableModel(2, 9) {
    var modifying = false

    init {
        addTableModelListener {
            if (!modifying)
                reloadFromTape()
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return row == 1
    }
}
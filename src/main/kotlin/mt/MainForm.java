package mt;

import javax.swing.*;

public class MainForm {
    JPanel panelMain;
    JTable tableTape;
    JTable tableProgram;
    JButton buttonLogClear;
    JList<String> listLog;
    JPanel panelBottom;
    JPanel panelTop;
    JButton buttonRight;
    JButton buttonLeft;
    JButton buttonRun;
    JSpinner spinnerStepsCount;

    public static void main(String[] args) {
        MainKt.createGUI();
    }
}

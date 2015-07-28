package IntelliForge;

import javax.swing.*;
import java.awt.event.*;

public class GetForgeVersionNoJSONCheck extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;

    public String data;

    public GetForgeVersionNoJSONCheck() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        data = textField1.getText();

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        data = "";

        dispose();
    }

    public static String get() {
        GetForgeVersionNoJSONCheck dialog = new GetForgeVersionNoJSONCheck();
        dialog.pack();
        dialog.setVisible(true);

        return dialog.data;
    }
}

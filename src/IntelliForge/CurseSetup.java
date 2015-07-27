package IntelliForge;

import javax.swing.*;
import java.awt.event.*;

public class CurseSetup extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel APIkey;
    private JPasswordField passwordField1;
    private JTextField textField1;
    private JTextArea textArea1;
    private JRadioButton RadioType;
    private JRadioButton betaRadioButton;
    private JRadioButton alphaRadioButton1;

    public CurseSetup() {
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
        String projectID = textField1.getText();
        String api_key = String.valueOf(passwordField1.getPassword());
        String changelog = textArea1.getText();
        CurseHandler.ReleaseType t;
        if (alphaRadioButton1.isEnabled()){
            t = CurseHandler.ReleaseType.ALPHA;
        }
        else if (betaRadioButton.isEnabled()){
            t = CurseHandler.ReleaseType.BETA;
        }
        else if (RadioType.isEnabled()){
            t = CurseHandler.ReleaseType.RELEASE;
        }
        else {
            t = CurseHandler.ReleaseType.RELEASE;
        }

        CurseHandler.doCurse(api_key, projectID, changelog, t);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void show_2(){
        CurseSetup dialog = new CurseSetup();
        dialog.pack();
        dialog.setVisible(true);
    }
}

package IntelliForge;

import IntelliForge.Actions.CurseHandler;

import javax.swing.*;
import java.awt.event.*;

public class CurseUpdate extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JRadioButton radioBeta;
    private JRadioButton radioAlpha;
    private JRadioButton radioRelease;

    public CurseUpdate() {
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

        ButtonGroup smith = new ButtonGroup();
        smith.add(radioAlpha);
        smith.add(radioBeta);
        smith.add(radioRelease);


    }

    private void onOK() {
// add your code here
        String changelog = textArea1.getText();
        CurseHandler.ReleaseType t;
        if (radioAlpha.isSelected()){
            t = CurseHandler.ReleaseType.ALPHA;
        }
        else if (radioBeta.isSelected()){
            t = CurseHandler.ReleaseType.BETA;
        }
        else if (radioRelease.isSelected()){
            t = CurseHandler.ReleaseType.RELEASE;
        }
        else {
            t = CurseHandler.ReleaseType.RELEASE;
        }
        CurseHandler.updateCurse(changelog, t);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void setup2() {
        CurseUpdate dialog = new CurseUpdate();
        dialog.pack();
        dialog.setVisible(true);
    }
}

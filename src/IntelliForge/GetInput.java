package IntelliForge;

import javax.swing.*;
import java.awt.event.*;

public class GetInput extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;

    public String theData;
    public boolean send;


    public GetInput() {

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
       theData = textField1.getText();
        send = true;


        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        theData = "";
        send = false;

        dispose();
    }

    public static class OnData {
        public final String data;
        public final boolean is;

        public OnData(String data, boolean is){

            this.data = data;
            this.is = is;
        }
    }

    public static OnData get(){
        GetInput dialog = new GetInput();
        dialog.pack();
        dialog.setVisible(true);
        return new OnData(dialog.theData, dialog.send);
    }
}

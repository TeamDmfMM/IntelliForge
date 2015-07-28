package IntelliForge.Helper;

import IntelliForge.Actions.IntelliForgeToolWindow;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class ExecuteCommandThread extends Thread{

    private final String osCMD;
    private final String cmd;
    private final String fileLoc;
    private final boolean windows;

    private static SimpleAttributeSet redunderlined;
    private static SimpleAttributeSet regualr;

    static {

        SimpleAttributeSet d = new SimpleAttributeSet();
        StyleConstants.setForeground(d, Color.decode("#0066FF"));
        StyleConstants.setUnderline(d, true);
        StyleConstants.setBold(d, true);
        StyleConstants.setFontFamily(d, "Courier New");
        StyleConstants.setFontSize(d, 14);
        redunderlined = d;
        d = new SimpleAttributeSet();
        StyleConstants.setFontFamily(d, "Courier New");
        StyleConstants.setFontSize(d, 14);
        regualr = d;

    }

    public ExecuteCommandThread(String osCMD, String cmd, String FileLoc, boolean windows){

        this.osCMD = osCMD;
        this.cmd = cmd;
        fileLoc = FileLoc;
        this.windows = windows;
    }

    public void run() {

        executeCMD(osCMD, cmd, fileLoc, windows);

    }

    private static void scrollToBottom()
    {
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        IntelliForgeToolWindow.theScrollPane.getVerticalScrollBar().setValue(IntelliForgeToolWindow.theScrollPane.getVerticalScrollBar().getMaximum());
                    }
                });
    }

    private static void executeCMD(String osCMD, String cmd, String FileLoc, boolean windows){

        if (IntelliForgeToolWindow.theToolWindow != null){
            try {
                IntelliForgeToolWindow.theDocument.insertString(IntelliForgeToolWindow.theDocument.getLength(), FileLoc + "> " + osCMD + " " + cmd + "\n", redunderlined);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            //RED TEXT
        }
        try {
            Process p;
            if(!windows){
                Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(FileLoc));
            }
            p =  Runtime.getRuntime().exec(osCMD + " " + cmd, null, new File(FileLoc));
            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (IntelliForgeToolWindow.theToolWindow != null){



                    IntelliForgeToolWindow.theDocument.insertString(IntelliForgeToolWindow.theDocument.getLength(), line + "\n", regualr);
                    IntelliForgeToolWindow.theTextPane.setCaretPosition(IntelliForgeToolWindow.theDocument.getLength());
                }
            }
            p.waitFor();
            input.close();
        } catch (IOException | InterruptedException e) {} catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

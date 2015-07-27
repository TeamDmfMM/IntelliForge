package IntelliForge.Helper;

import IntelliForge.Actions.IntelliForgeToolWindow;

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

    public ExecuteCommandThread(String osCMD, String cmd, String FileLoc, boolean windows){

        this.osCMD = osCMD;
        this.cmd = cmd;
        fileLoc = FileLoc;
        this.windows = windows;
    }

    public void run() {

        executeCMD(osCMD, cmd, fileLoc, windows);

    }

    private static void executeCMD(String osCMD, String cmd, String FileLoc, boolean windows){

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
                    IntelliForgeToolWindow.theTextArea.append(line + "\n");
                    IntelliForgeToolWindow.theTextArea.setCaretPosition(IntelliForgeToolWindow.theTextArea.getDocument().getLength());
                }
            }
            p.waitFor();
            input.close();
        } catch (IOException | InterruptedException e) {}
    }
}

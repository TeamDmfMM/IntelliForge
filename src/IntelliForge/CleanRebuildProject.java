package IntelliForge;


import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CleanRebuildProject extends AnAction{

    public static CleanRebuildProject INSTANCE = new CleanRebuildProject();
    public CleanRebuildProject(){
        super("Clean/Rebuild Project");
    }


    @Override
    public void actionPerformed(AnActionEvent event) {


        if (IntelliForgeToolWindow.theToolWindow != null){
            IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                @Override
                public void run() {

                }
            });
        }

        String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();



        MultipleExecuteCommandThread th = new MultipleExecuteCommandThread(
                new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "clean", project, OperatingSystemHelper.systemHelper.isWindows()),
            new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupDecompWorkspace", project, OperatingSystemHelper.systemHelper.isWindows()),
            new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "idea", project, OperatingSystemHelper.systemHelper.isWindows()));
        th.start();


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
                }
            }
            p.waitFor();
            input.close();
        } catch (IOException | InterruptedException e) {}
    }
}

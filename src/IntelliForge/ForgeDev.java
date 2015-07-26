package IntelliForge;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by David on 24/07/2015.
 */
public class ForgeDev{

    public static class GenPatches extends AnAction{
        public GenPatches(){
            super("Generate Patches");
        }
        @Override
        public void actionPerformed(AnActionEvent event) {
            String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();
            executeCMD(OperatingSystemHelper.systemHelper.getOSexecuteString(), "genPatches", project, OperatingSystemHelper.systemHelper.isWindows());
        }
    }

    public static class SetupForge extends AnAction{
        public SetupForge(){
            super("Setup Forge Workspace");
        }
        @Override
        public void actionPerformed(AnActionEvent event) {
            String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();
            executeCMD(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupForge idea", project, OperatingSystemHelper.systemHelper.isWindows());
        }
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
                System.out.println(line);
            }
            p.waitFor();
            input.close();
        } catch (IOException | InterruptedException e) {}
    }
}

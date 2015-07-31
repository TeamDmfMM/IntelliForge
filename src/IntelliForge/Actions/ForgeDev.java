package IntelliForge.Actions;

import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

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

            ExecuteCommandThread th = new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "genPatches", project, OperatingSystemHelper.systemHelper.isWindows());
            //th.start();

            System.out.println( event.getData(PlatformDataKeys.PROJECT_FILE_DIRECTORY).getCanonicalPath());
        }
    }

    public static class SetupForge extends AnAction{
        public SetupForge(){
            super("setupForge Workspace");
        }
        @Override
        public void actionPerformed(AnActionEvent event) {
            String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();
            ExecuteCommandThread th = new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupForge idea", project, OperatingSystemHelper.systemHelper.isWindows());
            th.start();
        }
    }

}

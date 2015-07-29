package IntelliForge.Actions;


import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.ForgeData.ParseCollection;
import IntelliForge.Helper.MultipleExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

public class CleanRebuildProject extends AnAction{

    public static CleanRebuildProject INSTANCE = new CleanRebuildProject();
    public CleanRebuildProject(){
        super("Clean/Rebuild Project");
    }


    @Override
    public void actionPerformed(AnActionEvent event) {

        ParseCollection c = new ParseCollection(new ParseCollection.VersionPolicy() {
            @Override
            public boolean downloadMcVersion(String version) {
                return true;
            }
        });

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

}

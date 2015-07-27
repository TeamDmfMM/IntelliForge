package IntelliForge.Actions;

import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.MultipleExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.notification.EventLog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.intellij.openapi.ui.Messages.showMessageDialog;


public class FirstWorkspace extends AnAction {

    public FirstWorkspace(){
        super("First-time Workspace Setup");

    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        int yn = Messages.showOkCancelDialog(project, "Is this a pre-setup environment? (ie. Has gradle and a build.gradle file)", "Setup Forge Workspace", "Yes", "No", Messages.getQuestionIcon());
       // System.out.println(yn);  Yes = 0 || No == 2
        if(yn == 0){

            if(new File(project.getBaseDir().getPath() + "/gradle").exists() && new File(project.getBaseDir().getPath() + "/gradlew").exists() && new File(project.getBaseDir().getPath() + "/build.gradle").exists()){
               OperatingSystemHelper.OSTYPE OS = OperatingSystemHelper.OSTYPE.getOS(System.getProperty("os.name").toLowerCase());
                executeBuild(OS, project);
            }else{
                showMessageDialog(project, "Cannot find either: gradle folder, gradlew or build.gradle. Please ensure that this workspace has then in its sub-directories.", "Error", Messages.getErrorIcon());
            }
        }
        //String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
        //Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
    private static void executeBuild(OperatingSystemHelper.OSTYPE ostype, Project project){
        String fileloc = project.getBaseDir().getCanonicalPath();
        if (IntelliForgeToolWindow.theToolWindow != null){
            IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
        MultipleExecuteCommandThread th = new MultipleExecuteCommandThread(
                new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupDecompWorkspace", fileloc, OperatingSystemHelper.systemHelper.isWindows()),
                new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "idea", fileloc, OperatingSystemHelper.systemHelper.isWindows()));
        th.start();
    }
}

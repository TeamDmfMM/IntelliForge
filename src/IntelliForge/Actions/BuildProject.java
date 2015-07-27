package IntelliForge.Actions;

import IntelliForge.CurseSetup;
import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

import java.io.*;


public class BuildProject extends AnAction{

   public static BuildProject INSTANCE = new BuildProject();
    public BuildProject(){
        super("Build Project");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String fileloc = project.getBaseDir().getCanonicalPath();
        if (IntelliForgeToolWindow.theToolWindow != null){
            IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                @Override
                public void run() {}});
        }
        ExecuteCommandThread thread = new ExecuteCommandThread(OperatingSystemHelper.getOSexecuteString(), "build", fileloc, OperatingSystemHelper.systemHelper.isWindows());
        thread.start();
    }


    public static class BuildtoCurse extends AnAction{
        public BuildtoCurse(){
            super("Build to Curse");
        }
        @Override
        public void actionPerformed(AnActionEvent event) {
            String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();
            if(readFile(project)){
                if (IntelliForgeToolWindow.theToolWindow != null){
                    IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                        @Override
                        public void run() {}});
                }
                ExecuteCommandThread thread = new ExecuteCommandThread(OperatingSystemHelper.getOSexecuteString(), "build curse", project, OperatingSystemHelper.systemHelper.isWindows());
                thread.start();
            }

        }

        private static boolean readFile(String project){
            System.out.println(project);
            try(BufferedReader br = new BufferedReader(new FileReader(project + "/build.gradle"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    if(line.contains("curseforge"))
                        return true;
                    line = br.readLine();
                }
            } catch (IOException e) {e.printStackTrace();}
            return false;
        }
    }

}

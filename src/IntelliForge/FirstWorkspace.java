package IntelliForge;

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
               OSTYPE OS = OSTYPE.getOS(System.getProperty("os.name").toLowerCase());
                executeBuild(OS, project);
            }else{
                showMessageDialog(project, "Cannot find either: gradle folder, gradlew or build.gradle. Please ensure that this workspace has then in its sub-directories.", "Error", Messages.getErrorIcon());
            }
        }
        //String txt= Messages.showInputDialog(project, "What is your name?", "Input your name", Messages.getQuestionIcon());
        //Messages.showMessageDialog(project, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
    private static void executeBuild(OSTYPE ostype, Project project){
        String fileloc = project.getBaseDir().getCanonicalPath();
        if(ostype == OSTYPE.WIN){
            //System.out.println("Starting");
            try {
                Process p =  Runtime.getRuntime().exec("cmd /c gradlew.bat setupDecompWorkspace", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
                p.waitFor();
                executeIdea(ostype, project);
            } catch (IOException | InterruptedException e) {}
        } else if(ostype == OSTYPE.MAC){
            try {
                Process p =  Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(fileloc));
                p =  Runtime.getRuntime().exec("bash gradlew setupDecompWorkspace", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
                p.waitFor();
                executeIdea(ostype, project);
            } catch (IOException | InterruptedException e) { }
        } else if(ostype == OSTYPE.LINUX){
            try {
                Process p = Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(fileloc));
                p =  Runtime.getRuntime().exec("./gradlew setupDecompWorkspace", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
                p.waitFor();
                executeIdea(ostype, project);
            } catch (IOException | InterruptedException e) {}
        }
    }

    private static void executeIdea(OSTYPE ostype, Project project){
        String fileloc = project.getBaseDir().getCanonicalPath();
        if(ostype == OSTYPE.WIN){
            try {
                Process p =  Runtime.getRuntime().exec("cmd /c gradlew.bat idea", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (IOException e) { }
        } else if(ostype == OSTYPE.MAC){
            try {
                Process p =  Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(fileloc));
                p =  Runtime.getRuntime().exec("bash gradlew idea", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (IOException e) { }
        } else if(ostype == OSTYPE.LINUX){
            try {
                Process p = Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(fileloc));
                p =  Runtime.getRuntime().exec("./gradlew idea", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (IOException e) {}
        }
    }
    private static enum OSTYPE {
        WIN("win"),
        MAC("mac"),
        LINUX("nux");

        OSTYPE(String shortname){
            this.name = shortname;
        }
        private String name;

        public static OSTYPE getOS(String os){
            if(os.indexOf(WIN.name) >= 0)
                return WIN;
            else if(os.indexOf(MAC.name) >= 0)
                return MAC;
            else if(os.indexOf(LINUX.name) >= 0)
                return LINUX;
            return null;
        }
    }
}

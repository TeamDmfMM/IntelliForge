package IntelliForge;

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
        OSTYPE ostype = OSTYPE.getOS(System.getProperty("os.name").toLowerCase());
        if(ostype == OSTYPE.WIN){
            //System.out.println("Starting");
            try {
                Process p =  Runtime.getRuntime().exec("cmd /c gradlew.bat build", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (IOException e) {}
        } else if(ostype == OSTYPE.MAC){
            try {
                Process p =  Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(fileloc));
                p =  Runtime.getRuntime().exec("bash gradlew build", null, new File(fileloc));
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
                p =  Runtime.getRuntime().exec("./gradlew build", null, new File(fileloc));
                String line;
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            } catch (IOException e) {}
        }
    }

    public static class BuildtoCurse extends AnAction{
        public BuildtoCurse(){
            super("Build to Curse");
        }
        @Override
        public void actionPerformed(AnActionEvent event) {
            String project = event.getData(PlatformDataKeys.PROJECT).getBaseDir().getCanonicalPath();
            if(readFile(project)){
                executeCMD(OperatingSystemHelper.systemHelper.getOSexecuteString(), "build curse", project, OperatingSystemHelper.systemHelper.isWindows());
            } else{
                CurseSetup.show_2();
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

        private static boolean readFile(String project){
            try(BufferedReader br = new BufferedReader(new FileReader("build.gradle"))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    if(line.contains("curse"))
                        return true;
                    line = br.readLine();
                }
            } catch (IOException e) {e.printStackTrace();}
            return false;
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

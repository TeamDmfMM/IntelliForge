package IntelliForge.Actions;


import IntelliForge.CurseSetup;
import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CurseUpdate extends AnAction{

    public CurseUpdate(){
        super("Update Changelog and Build");
    }
    @Override
    public void actionPerformed(AnActionEvent t) {
        if(readFile(t.getProject().getBasePath())){
            //Do update only!
        } else{
            //Add plugin to build script
            CurseSetup.show_2();
            if (IntelliForgeToolWindow.theToolWindow != null){
                IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                    @Override
                    public void run() {}});
            }
            ExecuteCommandThread e = new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(),
                    "build curse", t.getData(PlatformDataKeys.PROJECT_FILE_DIRECTORY).getCanonicalPath(),
                    OperatingSystemHelper.systemHelper.isWindows());
            e.start();
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

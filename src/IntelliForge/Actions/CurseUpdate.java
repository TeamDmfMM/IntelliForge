package IntelliForge.Actions;


import IntelliForge.CurseSetup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vcs.actions.CommonCheckinProjectAction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//import com.intellij.dvcs.push.VcsPushAction;

public class CurseUpdate extends AnAction{

    public CurseUpdate(){
        super("Update Changelog and Build");
    }
    @Override
    public void actionPerformed(AnActionEvent t) {
        if(readFile(t.getProject().getBasePath())){
            //Do update only!
                 // System.out.println("WE HAVE CURSEFORGE!");
            IntelliForge.CurseUpdate.setup2();
            new CommonCheckinProjectAction().actionPerformed(t);
        } else{
            //Add plugin to build script
            CurseSetup.show_2();
        }
    }

    private static boolean readFile(String project){
        System.out.println(project);
        try(BufferedReader br = new BufferedReader(new FileReader(project + "/build.gradle"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if(line.contains("curseforge")){
                    System.out.println(line);
                    return true;
                }
                line = br.readLine();
            }
        } catch (IOException e) {e.printStackTrace();}
        return false;
    }
}

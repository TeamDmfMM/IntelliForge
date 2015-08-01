package IntelliForge.Actions;

import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class CurseHandler {

    public static enum ReleaseType {

        ALPHA,
        BETA,
        RELEASE,
        NULL;

        public static String getType(ReleaseType type) {
            switch (type) {
                case ALPHA:
                    return "\"alpha\"";
                case BETA:
                    return "\"beta\"";
                case RELEASE:
                    return "\"release\"";
                default:
                    return "\"release\"";
            }
        }


    }

    public static void doCurse(String api_key, String projectID, String changelog, ReleaseType type) {
       /* curse {
            apiKey = System.getenv("APIKEY") // saved in the build environment vars
            projectId = "222348" // my project url is http://minecraft.curseforge.com/mc-mods/222348-extra-food
            changelog = """
            Update to new forge with new fluid code!
                    """
            releaseType = "release"
        }*/

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(ProjectManager.getInstance().getOpenProjects()[0]);
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("Recreating build.gradle, adding Curseforge plugin", MessageType.INFO, null).setFadeoutTime(7500).createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);

        List<String> curse = new ArrayList<>();
        curse.add("\n");
        curse.add("curse {");
        curse.add("    apiKey = " + api_key);
        curse.add("    projectId = \"" + projectID + "\"");
        curse.add("    changelog = \"\"\"");
        curse.add(changelog);
        curse.add("     \"\"\"");
        curse.add("    releaseType = " + ReleaseType.getType(type));
        curse.add("}");
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        String file = project.getBaseDir().getCanonicalPath() + "/build.gradle";
        // File gradle = new File(file);
        List<String> lines = null;
        try {
            System.out.println("HERERER");
            lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
            for (int l = 0; l < lines.size(); l++) {
                String lino = lines.get(l);
                //System.out.println(lino);
                if (lino.contains("apply plugin: 'forge'")) {
                    //System.out.println("FOUND IT!!!1");
                    lino = lines.get(l - 1);
                    if (lino == null || lino.equals("")) {
                        lines.set(l - 1, "apply plugin: 'curseforge'");
                        break;
                    } else {
                        lino = lines.get(l + 1);
                        if (lino == null || lino.equals("")) {
                            lines.set(l + 1, "apply plugin: 'curseforge'");
                            break;
                        }
                    }
                }

            }

            lines.addAll(lines.indexOf("processResources {"), curse);
            FileWriter writer = new FileWriter(project.getBaseDir().getCanonicalPath() + "/build.gradle");
            for (String str : lines) {
                writer.write(str + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {


            if (IntelliForgeToolWindow.theToolWindow != null) {
                IntelliForgeToolWindow.theToolWindow.activate(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
            ExecuteCommandThread e = new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(),
                    "build curse", project.getBaseDir().getCanonicalPath(), OperatingSystemHelper.systemHelper.isWindows());
            //     System.out.println("HACKYSTARTERS");
            e.start();
        }

    }

    public static void updateCurse(String changelog, ReleaseType releaseType) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        String file = project.getBaseDir().getCanonicalPath() + "/build.gradle";
        // File gradle = new File(file);
        List<String> lines = null;
        int Start = -1;
        int Finish = -1;
        try {
            lines = Files.readAllLines(Paths.get(file), Charset.defaultCharset());
            for (int l = 0; l < lines.size(); l++) {
                String lino = lines.get(l);
                if (lino.contains("releaseType =")) {
                        lines.set(l, "releaseType ="+ CurseHandler.ReleaseType.getType(releaseType));
                }
                if(lino.contains("\"\"\"")){
                    if(Start == -1){
                        Start = l;
                    } else if(Finish == -1){
                        Finish = l;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Start != Finish && Finish != -1 && Start != -1){
            for(int s = Start + 1; s < Finish - 1; s++){
                lines.remove(s);
            }
        }
        lines.add(Start + 1, changelog);

        try {
            FileWriter writer = new FileWriter(project.getBaseDir().getCanonicalPath() + "/build.gradle");
            for (String str : lines) {
                writer.write(str + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecuteCommandThread e = new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(),
                "build curse", project.getBaseDir().getCanonicalPath(), OperatingSystemHelper.systemHelper.isWindows());
        //     System.out.println("HACKYSTARTERS");
        e.start();
    }
}

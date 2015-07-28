package IntelliForge;

import IntelliForge.Actions.*;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import org.jetbrains.annotations.NotNull;


public class IntelliForgePluginRegex implements ApplicationComponent{
    @Override
    public void initComponent() {
        ActionManager am = ActionManager.getInstance();
        FirstWorkspace firstWorkspace = new FirstWorkspace();
        BuildProject buildProject = BuildProject.INSTANCE;
        CleanRebuildProject rebuildProject = CleanRebuildProject.INSTANCE;
        ForgeDev.GenPatches patches = new ForgeDev.GenPatches();
        ForgeDev.SetupForge setupForge = new ForgeDev.SetupForge();
        BuildProject.BuildtoCurse buildtoCurse = new BuildProject.BuildtoCurse();
        CurseUpdate Cupdate = new CurseUpdate();
        UpdateForgeAction Updateforge = new UpdateForgeAction();


        CurseGroup curse = new CurseGroup();

        am.registerAction("IntelliForgeAction", firstWorkspace);
        am.registerAction("IntelliForgebuildProject", buildProject);
        am.registerAction("IntelliForgecleanRebuildProject", rebuildProject);
        am.registerAction("IntelliForgepatches", patches);
        am.registerAction("IntelliForgesetupForge", setupForge);
        am.registerAction("IntelliForgebuildCurse", buildtoCurse);
        am.registerAction("IntelliForgeCurseUpdate", Cupdate);
        am.registerAction("IntellifForgeupdateforge", Updateforge);

        am.registerAction("IntelliForgeGroup", curse);


        DefaultActionGroup windowM = (DefaultActionGroup) am.getAction("IntelliForge.Menu");
        //windowM.addSeparator();
        windowM.add(firstWorkspace);
        windowM.add(rebuildProject);

        windowM.addSeparator();

        windowM.add(buildProject);
        //windowM.add(buildtoCurse);
        windowM.add(curse);
        windowM.add(Updateforge);

        windowM.addSeparator();

        windowM.add(setupForge);
        windowM.add(patches);
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "IntelliForge";
    }
}

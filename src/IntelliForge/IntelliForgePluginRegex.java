package IntelliForge;

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
        //  BuildProject.BuildtoCurse

        am.registerAction("IntelliForgeAction", firstWorkspace);
        am.registerAction("IntelliForgebuildProject", buildProject);
        am.registerAction("IntelliForgecleanRebuildProject", rebuildProject);
        am.registerAction("IntelliForgepatches", patches);
        am.registerAction("IntelliForgesetupForge", setupForge);

        DefaultActionGroup windowM = (DefaultActionGroup) am.getAction("IntelliForge.Menu");
        //windowM.addSeparator();
        windowM.add(firstWorkspace);
        windowM.addSeparator();
        windowM.add(rebuildProject);
        windowM.add(buildProject);
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

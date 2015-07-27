package IntelliForge.Actions;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.ui.popup.PopupFactoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 26/07/2015.
 */
public class CurseGroup extends ActionGroup implements DumbAware{

    public CurseGroup(){
        super("Curse", true);
    }
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(getEventProject(e) != null);
    }



    @NotNull
    @Override
    public AnAction[] getChildren(AnActionEvent anActionEvent) {
        List<AnAction> actions = new ArrayList<AnAction>();
        actions.add(new BuildProject.BuildtoCurse());
        actions.add(new CurseUpdate());
        return actions.toArray(new AnAction[actions.size()]);
    }

}

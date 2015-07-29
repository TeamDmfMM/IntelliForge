package IntelliForge.Actions;

import IntelliForge.GetForgeVersionNoJSONCheck;
import IntelliForge.Helper.ForgeData.ParseCollection;
import IntelliForge.Helper.PSIHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class UpdateForgeAction extends AnAction {

    public UpdateForgeAction(){
        super("Change forge version");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {

        String fvers = GetForgeVersionNoJSONCheck.get();



        PSIHelper.edit_field_mcversion(e, fvers);

    }
}

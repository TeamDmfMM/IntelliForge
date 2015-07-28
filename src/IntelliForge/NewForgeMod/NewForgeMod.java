package IntelliForge.NewForgeMod;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by David on 27/07/2015.
 */
public class NewForgeMod extends ModuleType<NewForgeModBuilder> {

    public NewForgeMod() {
        super("IntelliForge-Project");
    }

    public static NewForgeMod getInstance(){
        return (NewForgeMod) ModuleTypeManager.getInstance().findByID("IntelliForge-Project");
    }

    @NotNull
    @Override
    public NewForgeModBuilder createModuleBuilder() {
        return new NewForgeModBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return "New Minecraft Forge Mod";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Creates and sets up a new minecraft forge development environment";
    }

    @Override
    public Icon getBigIcon() {
        return AllIcons.General.Information;
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return AllIcons.General.Information;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull NewForgeModBuilder modBuilder, @NotNull ModulesProvider modulesProvider){
        return super.createWizardSteps(wizardContext, modBuilder, modulesProvider);
    }
}

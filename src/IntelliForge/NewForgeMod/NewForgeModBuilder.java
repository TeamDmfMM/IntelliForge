package IntelliForge.NewForgeMod;

import IntelliForge.Helper.ForgeData.BuildData;
import IntelliForge.Helper.ForgeData.ParseCollection;
import IntelliForge.NewForgeMod.NewForgeMod;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.xml.actions.xmlbeans.FileUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by David on 27/07/2015.
 */
public class NewForgeModBuilder extends ModuleBuilder implements ModuleBuilderListener{

    private String Version;
    private String MC;

    public NewForgeModBuilder(){
        addListener(this);
    }


    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
       // System.out.println("setupRootModel");
       // modifiableRootModel.getProject().getBaseDir().getCanonicalPath();

    }

    public void setVersion(String s){
        this.Version = s.substring(3);
    }
    public void setMC(String mc){
        this.MC = mc;
    }


    @Override
    public ModuleType getModuleType() {
        return NewForgeMod.getInstance();
    }

    @Nullable
    @Override
    public ModuleWizardStep getCustomOptionsStep(WizardContext wizardContext, Disposable disposable){
        return new NewForgeModSteps(this);
    }

    @Override
    public void moduleCreated(Module module) {
        System.out.print(MC + "\n");
        System.out.print(Version + "\n");
        System.out.println(module.getProject().getBaseDir().getCanonicalPath());
        ParseCollection p = new ParseCollection(new ParseCollection.VersionPolicy() {
            @Override
            public boolean downloadMcVersion(String version) {
                if(version.startsWith("1.6.4")){
                    return true;
                }else if(version.startsWith("1.7")){
                    return true;
                }else if(version.startsWith("1.8")){
                    return true;
                }else {
                    return false;
                }
            }
        });
        BuildData BD = p.getVersion(MC + "-" + Version);
        String downloadLink = BD.downloadLink;
        try {
            URL website = new URL(downloadLink);
            Files.copy(website.openStream(), new File(module.getProject().getBaseDir().getCanonicalPath()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I think i finished!");
    }
}

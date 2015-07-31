package IntelliForge.NewForgeMod;

import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.ForgeData.BuildData;
import IntelliForge.Helper.ForgeData.ParseCollection;
import IntelliForge.Helper.MultipleExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import IntelliForge.Helper.Unzip;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.io.*;
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

        System.out.println(modifiableRootModel.getContentEntries());

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

        this.deleteModuleFile(module.getModuleFilePath());


       // System.out.print(MC + "\n");
       // System.out.print(Version + "\n");
       // System.out.println(module.getProject().getBaseDir().getCanonicalPath());
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
        BuildData BD = p.getVersion(Version);
        String downloadLink = BD.downloadLink;
        try {
            URL website = new URL("http://" + downloadLink);
            Files.copy(website.openStream(), new File(module.getProject().getBaseDir().getCanonicalPath(),  "forge.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(module.getProject().getBaseDir().getCanonicalPath() + "/" + new File(module.getProject().getBaseDir().getCanonicalPath()).getName()).mkdir();
            Unzip.unzip(module.getProject().getBaseDir().getCanonicalPath() + "/forge.zip", module.getProject().getBaseDir().getCanonicalPath() + "/" + new File(module.getProject().getBaseDir().getCanonicalPath()).getName());

        } catch (IOException e) {e.printStackTrace();}
        //System.out.println("I think i finished!");


        MultipleExecuteCommandThread a = new MultipleExecuteCommandThread(new Thing(module.getModuleFile(), module.getProject().getBaseDir().getCanonicalPath(), module.getProject().getBaseDir().getCanonicalPath() + "/" + new File(module.getProject().getBaseDir().getCanonicalPath()).getName()), new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupDecompWorkspace", module.getProject().getBaseDir().getCanonicalPath() + "/" + new File(module.getProject().getBaseDir().getCanonicalPath()).getName(), OperatingSystemHelper.systemHelper.isWindows()),

        new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "idea", module.getProject().getBaseDir().getCanonicalPath() + "/" + new File(module.getProject().getBaseDir().getCanonicalPath()).getName(), OperatingSystemHelper.systemHelper.isWindows()));
        a.start();




        /*try {
            Files.delete(new File(module.getProject().getBaseDir().getCanonicalPath() + "/.idea").toPath());
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.{iml, ipr, iws}");
            Path newbie = new File(module.getProject().getBaseDir().getCanonicalPath()).toPath();
            if (matcher.matches(newbie)){
                Files.delete(newbie);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private static void executeCMD(String osCMD, String cmd, String FileLoc, boolean windows){
        try {
            Process p;
            if(!windows){
                Runtime.getRuntime().exec("chmod +x ./gradlew", null, new File(FileLoc));
            }
            p =  Runtime.getRuntime().exec(osCMD + " " + cmd, null, new File(FileLoc));
            p.waitFor();
        } catch (IOException | InterruptedException e) {}
    }

    private static class Thing implements Runnable {

        public void copyDirectory(File sourceLocation , File targetLocation)
                throws IOException {

            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdir();
                }

                String[] children = sourceLocation.list();
                for (int i=0; i<children.length; i++) {
                    copyDirectory(new File(sourceLocation, children[i]),
                            new File(targetLocation, children[i]));
                }
            } else {

                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }

        VirtualFile ff;

        File t;
        File f;

        public Thing(VirtualFile f, String t, String fff){

            ff =f ;

            this.t = new File(t);
            this.f = new File(fff);


        }


        @Override
        public void run() {


            try {
                copyDirectory(f, t);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }
}

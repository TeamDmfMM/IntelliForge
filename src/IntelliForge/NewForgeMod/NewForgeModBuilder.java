package IntelliForge.NewForgeMod;

import IntelliForge.Actions.IntelliForgeToolWindow;
import IntelliForge.Helper.ExecuteCommandThread;
import IntelliForge.Helper.ForgeData.BuildData;
import IntelliForge.Helper.ForgeData.ParseCollection;
import IntelliForge.Helper.MultipleExecuteCommandThread;
import IntelliForge.Helper.OperatingSystemHelper;
import IntelliForge.Helper.Unzip;
import IntelliForge.IntelliForgePluginRegex;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilderListener;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Created by David on 27/07/2015.
 */
public class NewForgeModBuilder extends ModuleBuilder implements ModuleBuilderListener {//

    private String Version;
    private String MC;

    public NewForgeModBuilder(){
        addListener(this);
    }


    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {
        final ContentEntry contentreg = doAddContentEntry(modifiableRootModel);
        final VirtualFile baseDir = contentreg == null ? null : contentreg.getFile();
        if(baseDir != null){
            setupProject(modifiableRootModel, baseDir);
        }

       // System.out.println("setupRootModel");
       // modifiableRootModel.getProject().getBaseDir().getCanonicalPath();

    }

    public void setupProject(@NotNull final ModifiableRootModel rootModel, @NotNull final VirtualFile basedir){
        if(basedir.exists()){
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
                Files.copy(website.openStream(), new File(basedir.getCanonicalPath(),  "forge.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                new File(basedir.getCanonicalPath() + "/" + new File(basedir.getCanonicalPath()).getName()).mkdir();
                Unzip.unzip(basedir.getCanonicalPath() + "/forge.zip",basedir.getCanonicalPath() + "/" + new File(basedir.getCanonicalPath()).getName());

            } catch (IOException e) {e.printStackTrace();}
            //System.out.println("I think i finished!");

            MultipleExecuteCommandThread a = new MultipleExecuteCommandThread(
                    new Thing(basedir, basedir.getCanonicalPath(), basedir.getCanonicalPath() + "/" + new File(basedir.getCanonicalPath()).getName()),
                    new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "setupDecompWorkspace", basedir.getCanonicalPath() + "/" + new File(basedir.getCanonicalPath()).getName(), OperatingSystemHelper.systemHelper.isWindows()),
                    new ExecuteCommandThread(OperatingSystemHelper.systemHelper.getOSexecuteString(), "idea", basedir.getCanonicalPath() + "/" + new File(basedir.getCanonicalPath()).getName(), OperatingSystemHelper.systemHelper.isWindows()));
            a.start();
        }
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
        StatusBar statusBar = WindowManager.getInstance().getStatusBar(module.getProject());
        JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("\n" + "Setting up workspace. Do not do anything until Intellij reloads the project. This may take a while" + "\n", MessageType.INFO, null)
                .setFadeoutTime(7500).createBalloon().show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
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

                if (sourceLocation.getName().matches("((.*)\\.iml)|((.*)\\.ipr)|((.*)\\.iws)") && targetLocation.exists()){
                    VirtualFile theDestination = LocalFileSystem.getInstance().findFileByIoFile(targetLocation);
                    BufferedReader in = new BufferedReader(new FileReader(sourceLocation));


                    // Copy the bits from instream to outstream

                    String line;

                    String d = "";

                    while ((line = in.readLine()) != null){
                        d = d + line;
                    }


                    final String content = d;

                    final Document t = FileDocumentManager.getInstance().getDocument(theDestination);

                    final Runnable doStuff = new Runnable() {
                        @Override
                        public void run() {
                            t.setText(content);
                        }
                    };



                   ApplicationManager.getApplication().invokeLater(new Runnable() {
                       @Override
                       public void run() {
                           ApplicationManager.getApplication().runWriteAction(doStuff);
                       }
                   });


                in.close();

                }
                else {

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
        }

        VirtualFile ff;

        File t;
        File f;

        public Thing(VirtualFile f, String t, String fff){
            removeAll(f);
            ff =f ;

            this.t = new File(t);
            this.f = new File(fff);


        }

        private void removeAll(VirtualFile base){
            VirtualFile[] files = base.getChildren();
            for(int i = 0; i < files.length; i++){
                VirtualFile file = files[i];
                if(file.getExtension() != null && file.getExtension().equals(".iml")){
                    try {   file.delete("Carl");    } catch (IOException e) {}
                }
            }
        }

        @Override
        public void run() {

            if (new File(t,".idea").exists()){
                new File(t, ".idea").delete();
            }

            try {
                copyDirectory(f, t);
            } catch (IOException e) {
                e.printStackTrace();
            }





        }
    }
}

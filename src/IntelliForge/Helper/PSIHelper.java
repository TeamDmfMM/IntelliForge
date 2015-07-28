package IntelliForge.Helper;

import IntelliForge.GetInput;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.templateLanguages.OuterLanguageElement;

import java.util.ArrayList;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class PSIHelper {

    public static void edit_field_properties(AnActionEvent event, String value, String key){
        String psifile = PsiManager.getInstance(event.getData(PlatformDataKeys.PROJECT)).findFile(event.getData(PlatformDataKeys.PROJECT).getBaseDir().findFileByRelativePath("/build.properties")).getText();

        String[] lines = psifile.split("\n");

        String ne = "";

        for (String line : lines) {

            String newline = line;

            if (line.startsWith(key)){

                newline = key + " = " + value;

            }

            ne = ne + newline + "\n";

        }

        FileDocumentManager.getInstance().getDocument(PsiManager.getInstance(event.getData(PlatformDataKeys.PROJECT)).findFile(event.getData(PlatformDataKeys.PROJECT).getBaseDir().findFileByRelativePath("/build.properties")).getVirtualFile()).setText(ne);


    }


    public static void edit_field_mcversion(AnActionEvent event, String value){

        String psifile = PsiManager.getInstance(event.getData(PlatformDataKeys.PROJECT)).findFile(event.getData(PlatformDataKeys.PROJECT).getBaseDir().findFileByRelativePath("/build.gradle")).getText();

        String[] lines = psifile.split("\n");

        ArrayList<String> section = new ArrayList<>();



        ArrayList<String> lookinfg = new ArrayList<>();
        lookinfg.add("minecraft");

        String ne = "";

        boolean fo = false;

        for (String line : lines){
            if (fo == true) { ne = ne + line + "\n"; continue; }
            if (line.endsWith("{")){
                System.out.println(line);
                String theSectiontest = line.substring(0, line.length() - 2);
                theSectiontest = line.split(" ")[0];

                section.add(theSectiontest);
            }
            else if (line.endsWith("}")){
                section.remove(section.size() - 1);
            }
            else if (line.contains("version")){
                if (section.equals(lookinfg)){

                    String theString;

                    theString = line.substring(line.indexOf("\"")+1,line.lastIndexOf("\""));

                    String mcVersion = theString.split("-")[0];

                    boolean go = false;
                    if (theString == "-"){
                        go = true;
                    }
                    if ((!(theString.matches("([0-9\\.]*)-([0-9\\.-]*)")))){
                        go = true;
                    }
                    System.out.println(theString);

                    section.addAll(0, section);

                    if (go){

                        String theKey;

                        if (PropertiesComponent.getInstance(event.getData(PlatformDataKeys.PROJECT)).isValueSet("intelliforge_p_bprop_keyname")){
                            theKey = PropertiesComponent.getInstance(event.getData(PlatformDataKeys.PROJECT)).getValue("intelliforge_p_bprop_keyname");
                        }
                        else {
                            // Get data from user

                            GetInput.OnData t = GetInput.get();

                            if (t.is) {
                                theKey = t.data;
                                PropertiesComponent.getInstance(event.getData(PlatformDataKeys.PROJECT)).setValue("intelliforge_p_bprop_keyname", theKey);
                            }

                            theKey = null;
                            continue;

                        }

                        edit_field_properties(event, value, theKey);
                        continue;


                    }

                    String toReplace = "    version = \"" + mcVersion + "-" + value + "-" + mcVersion + "\"";

                    ne = ne.concat(toReplace + "\n");
                    fo = true;
                    continue;
                }
            }
            ne = ne + line + "\n";


        }


        FileDocumentManager.getInstance().getDocument(PsiManager.getInstance(event.getData(PlatformDataKeys.PROJECT)).findFile(event.getData(PlatformDataKeys.PROJECT).getBaseDir().findFileByRelativePath("/build.gradle")).getVirtualFile()).setText(ne);






    }

    public static class PSIFieldEditor extends PsiRecursiveElementWalkingVisitor{

        @Override
        public void visitOuterLanguageElement(OuterLanguageElement element) {
            super.visitOuterLanguageElement(element);
            System.out.println(element);
        }

        @Override
        public void visitElement(PsiElement element) {
            super.visitElement(element);
            System.out.println(element);
            
        }
    }

}

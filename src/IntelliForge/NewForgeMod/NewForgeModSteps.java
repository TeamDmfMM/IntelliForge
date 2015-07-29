package IntelliForge.NewForgeMod;

import IntelliForge.Helper.ForgeData.ParseCollection;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;

import javax.swing.*;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import java.awt.*;
import java.util.Iterator;
import java.util.Set;


public class NewForgeModSteps extends ModuleWizardStep {
    private static final String MCVERS = "Minecraft: ";
    @Override
    public JComponent getComponent() {
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
        JPanel jpan = new JPanel();
        JLabel textf1 = new JLabel("Minecraft Version:");
        JComboBox comboBox = new ComboBox();
        Iterator s = p.getMcVersionsLoaded().iterator();
        while(s.hasNext()){
            comboBox.addItem(MCVERS + ((String)s.next()));
        }
        //comboBox.addItem(MCVERS + "1.8");
        //comboBox.addItem(MCVERS + "1.7.10");

        DefaultListModel list2 = new DefaultListModel();
        if(comboBox.getItemAt(comboBox.getSelectedIndex()) != null) {
            Iterator stringgo = p.versions.get("1.8").datas.keySet().iterator();
            while (stringgo.hasNext()) {
                list2.addElement("Forge:   " + stringgo.next() + "  ");
            }
        }
        //list2.addElement("Testing 2");
        //list2.addElement("Testing 3");




        JList list = new JBList(list2);
       // jList.setModel();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        //list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JBScrollPane(list);
        listScrollPane.setMinimumSize(new Dimension(50, 30));





        jpan.add(textf1);
        jpan.add(comboBox);
        jpan.add(new JSeparator(SwingConstants.VERTICAL));
        jpan.add(listScrollPane, BorderLayout.AFTER_LINE_ENDS);
        return jpan;
    }

    @Override
    public void updateDataModel() {

    }
}

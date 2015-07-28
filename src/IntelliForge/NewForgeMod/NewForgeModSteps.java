package IntelliForge.NewForgeMod;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;

import javax.swing.*;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import java.awt.*;


public class NewForgeModSteps extends ModuleWizardStep {
    private static final String MCVERS = "Minecraft: ";
    @Override
    public JComponent getComponent() {
        JPanel jpan = new JPanel();
        JLabel textf1 = new JLabel("Minecraft Version:");
        JComboBox comboBox = new ComboBox();
        comboBox.addItem(MCVERS + "1.8");
        comboBox.addItem(MCVERS + "1.7.10");

        DefaultListModel list2 = new DefaultListModel();
        list2.addElement("Forge:   "+"10.13.4.1492" + "  ");
        list2.addElement("Testing 2");
        list2.addElement("Testing 3");




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

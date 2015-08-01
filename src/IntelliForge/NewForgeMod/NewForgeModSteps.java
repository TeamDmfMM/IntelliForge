package IntelliForge.NewForgeMod;

import IntelliForge.Helper.ForgeData.BuildData;
import IntelliForge.Helper.ForgeData.ParseCollection;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;


public class NewForgeModSteps extends ModuleWizardStep {
    private static final String MCVERS = "Minecraft: ";

    public ParseCollection c;
    private NewForgeModBuilder build;

    private JList l;
    public JProgressBar ptro;
    JComboBox comboBox;
    public JLabel textff;
    public JLabel loading;
    public JButton recomend;
    public JButton latest;
    JScrollPane listScrollPane;


    public NewForgeModSteps(NewForgeModBuilder builder){
        this.build = builder;
    }
    @Override
    public JComponent getComponent() {


        c = null;

        JPanel jpan = new JPanel();

        GroupLayout layout = new GroupLayout(jpan);

        JLabel textf1 = new JLabel("Minecraft Version:");
        JLabel infoMessage = new JLabel("Warning: Using a .idea project does not fully work yet. ");
        JLabel info2 = new JLabel("If you do this, please use the forge setup inside of the folder with the same name as your project. ");
        JLabel info3 = new JLabel("To use .ipr, on the next screen, click advanced, and change the project type to .ipr");
        comboBox = new ComboBox();
        recomend = new JButton("Recommended Build");
        latest = new JButton("Latest Build");
        textff = textf1;



        JLabel textLoad = new JLabel("Loading Forge Versions");

        loading = textLoad;
        JProgressBar probar = new JProgressBar(0, 100);
        ptro = probar;

        textf1.setVisible(false);
        comboBox.setVisible(false);
        recomend.setVisible(false);
        latest.setVisible(false);




        JList list = new JBList();

        l = list;

        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel list2 = new DefaultListModel();

                if (c == null){
                    return;
                }

                if(comboBox.getItemAt(comboBox.getSelectedIndex()) != null) {
                    Iterator stringgo =c.versions.get(

                            ((String)comboBox.getSelectedItem()).substring(((String)comboBox.getSelectedItem()).indexOf(":") + 2)

                    ).datas.keySet().iterator();
                    while (stringgo.hasNext()) {
                        list2.addElement("Forge:   " + stringgo.next() + "  ");
                    }
                }

                l.setModel(list2);
                //list2.addElement("Testing 2");
                //list2.addElement("Testing 3");

            }
        });


       // jList.setModel();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        //list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        listScrollPane = new JBScrollPane(list);
        listScrollPane.setMinimumSize(new Dimension(50, 30));


        ptro.setIndeterminate(true);

        recomend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuildData buildData = c.getRecommended(((String) comboBox.getSelectedItem()).substring(((String) comboBox.getSelectedItem()).indexOf(":") + 2));
                l.setSelectedValue("Forge:   " + buildData.version, true);

            }
        });
        latest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuildData buildData = c.getLatest(((String) comboBox.getSelectedItem()).substring(((String) comboBox.getSelectedItem()).indexOf(":") + 2));
                l.setSelectedValue("Forge:   " + buildData.version, true);

            }
        });

       /* jpan.add(textf1);
        jpan.add(textLoad);
        jpan.add(comboBox);
        jpan.add(new JSeparator(SwingConstants.VERTICAL));
        jpan.add(listScrollPane, BorderLayout.AFTER_LINE_ENDS);
        jpan.add(ptro);
        jpan.add(infoMessage);
        jpan.add(info2);
        jpan.add(info3);*/

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(textf1)
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(recomend)
                                        .addComponent(comboBox)
                                        .addComponent(listScrollPane)
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                        .addComponent(latest)
                                                        .addComponent(infoMessage)
                                                        .addComponent(info2)
                                                        .addComponent(info3)
                                                        .addComponent(ptro)
                                                        .addComponent(textLoad)
                                        )

                        )


        );

        layout.setVerticalGroup(

                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(textf1)
                                        .addComponent(comboBox)

                        )
                        .addComponent(listScrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(recomend)
                                        .addComponent(latest)
                        )
                        .addComponent(textLoad)
                        .addComponent(ptro)
                        .addComponent(infoMessage)
                        .addComponent(info2)
                        .addComponent(info3)


        );

        layout.linkSize(SwingConstants.VERTICAL, comboBox, info2);
        layout.linkSize(SwingConstants.HORIZONTAL, listScrollPane, info2);

        jpan.setLayout(layout);

        (new GetForgeInfoTask(list, comboBox, this)).execute();

        return jpan;
    }

    public void updateDataModel() {
        build.setMC(((String)comboBox.getSelectedItem()).substring(((String)comboBox.getSelectedItem()).indexOf(":") + 2));
        build.setVersion(((String) l.getSelectedValue()).split(":")[1]);
    }

    public static class GetForgeInfoTask extends SwingWorker<ParseCollection, Object> {

        public JList toAddForge;
        public JComboBox toAddMc;

        public NewForgeModSteps theoldthing;

        public GetForgeInfoTask(JList toAddForge, JComboBox toAddMc, NewForgeModSteps theoldthing){

            this.toAddForge = toAddForge;
            this.toAddMc = toAddMc;
            this.theoldthing = theoldthing;
        }


        @Override
        @SuppressWarnings("Exeption")
        protected ParseCollection doInBackground() throws Exception {
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

            Iterator s = p.getMcVersionsLoaded().iterator();
            while(s.hasNext()){
                toAddMc.addItem(MCVERS + ((String) s.next()));
            }

            DefaultListModel list2 = new DefaultListModel();

            if(toAddMc.getItemAt(toAddMc.getSelectedIndex()) != null) {
                Iterator stringgo = p.versions.get("1.8").datas.keySet().iterator();
                while (stringgo.hasNext()) {
                    list2.addElement("Forge:   " + stringgo.next());
                }
            }

            toAddForge.setModel(list2);

            theoldthing.c = p;
            theoldthing.ptro.hide();

            if(!toAddMc.isVisible()){
                toAddMc.setVisible(true);
            }
            if(!toAddForge.isVisible()){
                toAddForge.setVisible(true);
            }

            theoldthing.loading.setVisible(false);
            theoldthing.textff.setVisible(true);
            theoldthing.recomend.setVisible(true);
            theoldthing.latest.setVisible(true);

            return p;

            //comboBox.addItem(MCVERS + "1.8");
            //comboBox.addItem(MCVERS + "1.7.10");


        }
    }
}

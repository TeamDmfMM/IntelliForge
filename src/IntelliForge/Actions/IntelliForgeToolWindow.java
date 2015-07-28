package IntelliForge.Actions;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Created by mincrmatt12. Do not copy this or you will have to face
 * our legal team. (dmf444)
 */
public class IntelliForgeToolWindow implements ToolWindowFactory, DumbAware{

    public static ToolWindow theToolWindow;

    public static JTextPane theTextPane;
    public static StyledDocument theDocument;
    // IDK why we would need this, but it is here for completeness
    public static JScrollPane theScrollPane;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        Component theJContext = toolWindow.getComponent();

        JTextPane textPane = new JTextPane();

        textPane.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textPane);

        theJContext.getParent().add(scrollPane);

        theTextPane = textPane;
        theScrollPane = theScrollPane;
        theDocument = textPane.getStyledDocument();

        theToolWindow = toolWindow;
        //toolWindow.setTitle("IntelliForge-Terminal");



    }
}

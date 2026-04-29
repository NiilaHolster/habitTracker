import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static void applyTheme(Container container, boolean darkMode) {

        Color backGround;
        Color foreGround;
        Color buttonBackGround;
        Color fieldBackGround;

        if (darkMode) {
            backGround = new Color(45, 45, 45);
            foreGround = Color.WHITE;
            buttonBackGround = new Color(70, 70, 70);
            fieldBackGround = new Color(30, 30, 30);
        } else {
            backGround = Color.WHITE;
            foreGround = Color.BLACK;
            buttonBackGround = new Color(220, 220, 220);
            fieldBackGround = Color.WHITE;
        }

        container.setBackground(backGround);

        for (Component comp : container.getComponents()) {

            comp.setForeground(foreGround);

            if (comp instanceof JPanel) {

                comp.setBackground(backGround);
                applyTheme((Container) comp, darkMode);

            } else if (comp instanceof JButton) {

                comp.setBackground(buttonBackGround);

            } else if (comp instanceof JTextField) {

                comp.setBackground(fieldBackGround);
                ((JTextField) comp).setCaretColor(foreGround);

            } else if (comp instanceof JTextArea) {

                comp.setBackground(fieldBackGround);
                ((JTextArea) comp).setCaretColor(foreGround);

            } else if (comp instanceof JScrollPane) {

                JScrollPane scroll = (JScrollPane) comp;
                scroll.getViewport().setBackground(backGround);
                applyTheme(scroll.getViewport(), darkMode);

            } else if (comp instanceof JViewport) {

                applyTheme((Container) comp, darkMode);

            } else if (comp instanceof JRadioButton) {

                comp.setBackground(backGround);
                ((JRadioButton) comp).setOpaque(true);

            } else if (comp instanceof JProgressBar) {

                comp.setBackground(backGround);
                comp.setForeground(Color.GREEN);

            } else if (comp instanceof JMenuItem) {

                comp.setBackground(buttonBackGround);
            }
        }
    }

    public static void applyDialogTheme(boolean darkMode) {

        if (darkMode) {
            UIManager.put("Panel.background", new Color(45, 45, 45));
            UIManager.put("OptionPane.background", new Color(45, 45, 45));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);

            UIManager.put("Button.background", new Color(70, 70, 70));
            UIManager.put("Button.foreground", Color.WHITE);

        } else {
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.BLACK);

            UIManager.put("Button.background", new Color(220, 220, 220));
            UIManager.put("Button.foreground", Color.BLACK);
        }
    }
}

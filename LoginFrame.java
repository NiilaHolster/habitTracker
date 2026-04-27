import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame implements ActionListener, LanguageChangeListener {

    private JTextField userText;
    private JPasswordField passwordText;
    private JLabel success;
    private JButton button;
    private JLabel userLabel;
    private JLabel passwordLabel;

    private JButton languageButton;
    private JPopupMenu languageMenu;

    private LocalizationManager lang;

    public LoginFrame() {

        lang = LocalizationManager.getInstance();
        lang.addListener(this);

        setTitle(lang.getString("login.title"));
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

     
        userLabel = new JLabel(lang.getString("login.username"));
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

    
        passwordLabel = new JLabel(lang.getString("login.password"));
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

      
        button = new JButton(lang.getString("login.button"));
        button.setBounds(10, 80, 90, 25);
        button.addActionListener(this);
        panel.add(button);

      
        languageButton = new JButton("🌐");
        languageButton.setBounds(280, 20, 50, 25);
        panel.add(languageButton);

        languageMenu = new JPopupMenu();

        JMenuItem englishItem = new JMenuItem("EN");
        JMenuItem finnishItem = new JMenuItem("FI");

        englishItem.addActionListener(e ->
                lang.setLocale(Locale.ENGLISH)
        );

        finnishItem.addActionListener(e ->
                lang.setLocale(new Locale("fi"))
        );

        languageMenu.add(englishItem);
        languageMenu.add(finnishItem);

        languageButton.addActionListener(e ->
                languageMenu.show(languageButton, 0, languageButton.getHeight())
        );

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        setVisible(true);
    }

  
    @Override
    public void onLanguageChanged() {
        setTitle(lang.getString("login.title"));

        userLabel.setText(lang.getString("login.username"));
        passwordLabel.setText(lang.getString("login.password"));
        button.setText(lang.getString("login.button"));

        if (!success.getText().isEmpty()) {
            success.setText(lang.getString("login.invalid"));
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String user = userText.getText();
        String password = new String(passwordText.getPassword());

        if (user.equals("Käyttäjä") && password.equals("12345")) {
            showSuccessDialog();
            passwordText.setText("");
            button.setEnabled(false);
        } else {
            success.setText(lang.getString("login.invalid"));
        }
    }


private void showSuccessDialog() {

    JDialog dialog = new JDialog(this, lang.getString("login.success"), true);
    dialog.setSize(300, 150);
    dialog.setLayout(new GridBagLayout());
    dialog.setLocationRelativeTo(this); 

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;

    JLabel message = new JLabel(lang.getString("login.success"));
    message.setFont(new Font("Arial", Font.BOLD, 14));
    dialog.add(message, gbc);

    JButton okButton = new JButton(lang.getString("login.ok"));
    gbc.gridy = 1;
    dialog.add(okButton, gbc);

    okButton.addActionListener(e -> {
        dialog.dispose();
        new TaskTrackerFrame(userText.getText());
    });

    dialog.setVisible(true);
}
}

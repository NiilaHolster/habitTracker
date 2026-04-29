import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TaskTrackerFrame extends JFrame implements LanguageChangeListener {
    private JProgressBar progressBar;
    private int taskProgress = 0;
    private int currentLevel = 1;
    private int tasksPerLevel = 4;
    private boolean usePredefinedTasks = true;
    private boolean isDarkMode = false;
    private String userName;

    private JPanel tasksPanel;
    private JLabel levelLabel;
    private JLabel taskStatusLabel;
    private JButton[] taskButtons;
    private JLabel[] taskLabels;
    private JButton switchModeButton;
    private JLabel helloLabel;
    private JLabel progressTextLabel;
    private JButton settingsButton;
    private JButton statsButton;
    private JButton addTaskButton;
    private JButton viewAllTasksButton;


    private JPopupMenu settingsMenu;
    private JMenuItem helpItem;
    private JMenuItem languageLabel;
    private JMenuItem englishItem;
    private JMenuItem finnishItem;
    private JLabel dateLabel;
    private JRadioButtonMenuItem darkItem;
    private JRadioButtonMenuItem lightItem;
    private JMenuItem themeLabel;

    private LocalizationManager lang;

    public TaskTrackerFrame(String userName) {

    lang = LocalizationManager.getInstance();
    lang.addListener(this);

    this.userName = userName;

    setTitle(lang.getString("app.title"));
    setSize(900, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel topPanel = new JPanel(new BorderLayout());

    JPanel headerPanel = new JPanel(new BorderLayout());

    helloLabel = new JLabel(lang.getString("app.welcome") + " " + userName + "!");
    headerPanel.add(helloLabel, BorderLayout.WEST);

    JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    headerRight.setOpaque(false);

    LocalDate currentDate = LocalDate.now();
    String formattedDate = currentDate.format(
            DateTimeFormatter.ofPattern("EEEE, d MMMM, yyyy")
    );
    dateLabel = new JLabel("📅 " + formattedDate);

    settingsButton = new JButton("⚙");

    settingsMenu = new JPopupMenu();

    englishItem = new JMenuItem("EN");
    finnishItem = new JMenuItem("FI");

    englishItem.addActionListener(e -> lang.setLocale(Locale.ENGLISH));
    finnishItem.addActionListener(e -> lang.setLocale(new Locale("fi")));

    languageLabel = new JMenuItem("🌐 " + lang.getString("settings.language"));
    languageLabel.setEnabled(false);

    settingsMenu.add(languageLabel);
    settingsMenu.add(englishItem);
    settingsMenu.add(finnishItem);
    settingsMenu.addSeparator();

    settingsMenu.addSeparator();


    themeLabel = new JMenuItem("🎨 " + lang.getString("settings.theme"));
    themeLabel.setEnabled(false);
    settingsMenu.add(themeLabel);

     darkItem = new JRadioButtonMenuItem("🌙 " + lang.getString("button.dark_mode"));
     lightItem = new JRadioButtonMenuItem("☀ " + lang.getString("button.light_mode"));

    ButtonGroup themeGroup = new ButtonGroup();
    themeGroup.add(darkItem);
    themeGroup.add(lightItem);


    darkItem.setSelected(isDarkMode);
    lightItem.setSelected(!isDarkMode);

    darkItem.addActionListener(e -> {
    isDarkMode = true;
    ThemeManager.applyTheme(mainPanel, true);
    });

    lightItem.addActionListener(e -> {
    isDarkMode = false;
    ThemeManager.applyTheme(mainPanel, false);
    });

    settingsMenu.add(darkItem);
    settingsMenu.add(lightItem);

    settingsMenu.addSeparator();

    
    helpItem = new JMenuItem(lang.getString("settings.help"));
    helpItem.addActionListener(e -> showHelpDialog());
    settingsMenu.add(helpItem);

    settingsButton.addActionListener(e -> {
    ThemeManager.applyTheme(settingsMenu, isDarkMode);
    settingsMenu.show(settingsButton, 0, settingsButton.getHeight());
    });

    headerRight.add(dateLabel);
    headerRight.add(settingsButton);

    headerPanel.add(headerRight, BorderLayout.EAST);
    topPanel.add(headerPanel, BorderLayout.NORTH);

    
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    actionPanel.setOpaque(false);

    statsButton = new JButton(lang.getString("button.view_stats"));
    statsButton.addActionListener(e -> new StatisticsWindow(isDarkMode));

    switchModeButton = new JButton(lang.getString("mode.predefined"));
    switchModeButton.addActionListener(e -> toggleMode());

    addTaskButton = new JButton(lang.getString("button.add_task"));
    addTaskButton.addActionListener(e -> showAddTaskDialog());


    viewAllTasksButton = new JButton("View all tasks");
    viewAllTasksButton.addActionListener(e -> showAllTasksDialog());

    actionPanel.add(statsButton);
    actionPanel.add(switchModeButton);
    actionPanel.add(addTaskButton);
    actionPanel.add(viewAllTasksButton);

    topPanel.add(actionPanel, BorderLayout.CENTER);

    
    JPanel progressPanel = new JPanel(new BorderLayout());

    levelLabel = new JLabel(lang.getString("app.level") + ": " + currentLevel);
    levelLabel.setHorizontalAlignment(JLabel.CENTER);

    progressBar = new JProgressBar(0, 100);
    progressBar.setStringPainted(true);

    progressTextLabel = new JLabel(lang.getString("app.progress"));

    progressPanel.add(levelLabel, BorderLayout.NORTH);
    progressPanel.add(progressTextLabel, BorderLayout.CENTER);
    progressPanel.add(progressBar, BorderLayout.SOUTH);

    tasksPanel = new JPanel(new GridBagLayout());
    taskStatusLabel = new JLabel(lang.getString("app.complete_tasks"));

    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.add(progressPanel, BorderLayout.NORTH);
    centerPanel.add(tasksPanel, BorderLayout.CENTER);
    centerPanel.add(taskStatusLabel, BorderLayout.SOUTH);

    mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);

    add(mainPanel);

    createTasksForCurrentLevel();

    setVisible(true);
}


    private void toggleMode() {
        usePredefinedTasks = !usePredefinedTasks;
        switchModeButton.setText(usePredefinedTasks ? lang.getString("mode.predefined") : lang.getString("mode.custom"));
        taskStatusLabel.setText(usePredefinedTasks ? lang.getString("mode.predefined_status") : lang.getString("mode.custom_status"));
        currentLevel = 1;
        taskProgress = 0;
        progressBar.setValue(0);
        levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
        createTasksForCurrentLevel();
    }

   private void showAllTasksDialog() {

    JTextArea area = new JTextArea(20, 40);
    area.setEditable(false);

    StringBuilder sb = new StringBuilder();

    sb.append("PREDEFINED TASKS:\n");
    sb.append("------------------\n");

    for (String task : AppData.taskKeys) {
        sb.append("• ").append(lang.getString(task)).append("\n");
    }

    sb.append("\nCUSTOM TASKS:\n");
    sb.append("------------------\n");

    if (AppData.customTasks.isEmpty()) {
        sb.append("No custom tasks\n");
    } else {
        for (String task : AppData.customTasks) {
            sb.append("• ").append(task).append("\n");
        }
    }

    area.setText(sb.toString());

  
    if (isDarkMode) {
        area.setBackground(new Color(30, 30, 30));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
    } else {
        area.setBackground(Color.WHITE);
        area.setForeground(Color.BLACK);
        area.setCaretColor(Color.BLACK);
    }

    JScrollPane scrollPane = new JScrollPane(area);

    if (isDarkMode) {
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
    } else {
        scrollPane.getViewport().setBackground(Color.WHITE);
    }

   
    JDialog dialog = new JDialog(this, "All Tasks", true);
    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    dialog.setLayout(new BorderLayout());

   
    dialog.add(scrollPane, BorderLayout.CENTER);

  
    JButton closeButton = new JButton("Close");

    if (isDarkMode) {
        closeButton.setBackground(new Color(70, 70, 70));
        closeButton.setForeground(Color.WHITE);
    } else {
        closeButton.setBackground(new Color(220, 220, 220));
        closeButton.setForeground(Color.BLACK);
    }

    closeButton.addActionListener(e -> dialog.dispose());

    JPanel buttonPanel = new JPanel();

    if (isDarkMode) {
        buttonPanel.setBackground(new Color(45, 45, 45));
    } else {
        buttonPanel.setBackground(Color.WHITE);
    }

    buttonPanel.add(closeButton);

    dialog.add(buttonPanel, BorderLayout.SOUTH);

    if (isDarkMode) {
        dialog.getContentPane().setBackground(new Color(45, 45, 45));
    } else {
        dialog.getContentPane().setBackground(Color.WHITE);
    }

    dialog.setSize(500, 400);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

    private void showHelpDialog() {
    JTextArea area = new JTextArea(15, 40);
    area.setEditable(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);

    String helpText =
            lang.getString("help.view_stats") + "\n\n" +
            lang.getString("help.clear_history") + "\n\n" +
            lang.getString("help.predefined") + "\n" +
            lang.getString("help.custom") + "\n\n" +
            lang.getString("help.add_task") + "\n" +
            lang.getString("help.add_task_window") + "\n" +
            lang.getString("help.save_task") + "\n\n" +
            lang.getString("help.theme") + "\n";

    area.setText(helpText);

    JScrollPane scrollPane = new JScrollPane(area);
    ThemeManager.applyTheme(scrollPane, isDarkMode);

    JOptionPane optionPane = new JOptionPane(
    scrollPane,
    JOptionPane.INFORMATION_MESSAGE
    );

    JDialog dialog = optionPane.createDialog(this, lang.getString("help.title"));
    ThemeManager.applyTheme(dialog.getContentPane(), isDarkMode);
    dialog.setVisible(true);
}

@Override
public void onLanguageChanged() {

   
    setTitle(lang.getString("app.title"));

 
    helloLabel.setText(lang.getString("app.welcome") + " " + userName + "!");
    levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
    progressTextLabel.setText(lang.getString("app.progress"));
    taskStatusLabel.setText(lang.getString("app.complete_tasks"));

  
    statsButton.setText(lang.getString("button.view_stats"));
    addTaskButton.setText(lang.getString("button.add_task"));
    

   
    if (usePredefinedTasks) {
        switchModeButton.setText(lang.getString("mode.predefined"));
    } else {
        switchModeButton.setText(lang.getString("mode.custom"));
    }

   
    languageLabel.setText("🌐 " + lang.getString("settings.language"));
    helpItem.setText(lang.getString("settings.help"));
    darkItem.setText("🌙 " + lang.getString("button.dark_mode"));
    lightItem.setText("☀ " + lang.getString("button.light_mode"));
    themeLabel.setText("🎨 " + lang.getString("settings.theme"));
    

  
    createTasksForCurrentLevel();
}

    private void createTasksForCurrentLevel() {
        tasksPanel.removeAll();


        TitledBorder predefinedBorder = BorderFactory.createTitledBorder(lang.getString("tasks.predefined_title") + " " + currentLevel);
        TitledBorder customBorder = BorderFactory.createTitledBorder(lang.getString("tasks.custom_title") + currentLevel);

        predefinedBorder.setTitleColor(Color.gray);
        customBorder.setTitleColor(Color.gray);

   
        if (usePredefinedTasks) {
            tasksPanel.setBorder(predefinedBorder);
        } else {
            tasksPanel.setBorder(customBorder);
        }

        tasksPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 2, 2, 2);

      
        List<String> currentTasks;
        if (usePredefinedTasks) {
            currentTasks = AppData.taskKeys;
        } else {
            currentTasks = AppData.customTasks;

          
            if (currentTasks.isEmpty()) {
                constraints.gridy = 0;
                constraints.gridx = 0;
                constraints.gridwidth = 2;
                JLabel emptyLabel = new JLabel(lang.getString("empty.custom_tasks"), JLabel.CENTER);
                emptyLabel.setForeground(Color.gray);
                tasksPanel.add(emptyLabel, constraints);
                tasksPanel.revalidate();
                tasksPanel.repaint();
                return;
            }
        }

   
        int startTaskIndex = (currentLevel - 1) * tasksPerLevel;

       
        if (startTaskIndex >= currentTasks.size()) {
            constraints.gridy = 0;
            constraints.gridx = 0;
            constraints.gridwidth = 2;
            JLabel noTasksLabel = new JLabel(lang.getString("empty.no_tasks"), JLabel.CENTER);
            noTasksLabel.setForeground(Color.RED);
            tasksPanel.add(noTasksLabel, constraints);
            tasksPanel.revalidate();
            tasksPanel.repaint();
            return;
        }

      
        taskButtons = new JButton[tasksPerLevel];
        taskLabels = new JLabel[tasksPerLevel];

      
        for (int i = 0; i < tasksPerLevel; i++) {
            int taskNumber = startTaskIndex + i;

            if (taskNumber < currentTasks.size()) {
                constraints.gridwidth = 1;
                constraints.gridy = i;
                constraints.gridx = 0;
                constraints.weightx = 0.7;

              
                String taskKey = currentTasks.get(taskNumber); 
                taskLabels[i] = new JLabel(lang.getString(taskKey) + " - " + lang.getString("task.pending"));


                if (!usePredefinedTasks) {
                    taskLabels[i].setForeground(new Color(0, 128, 0)); 
                }

                tasksPanel.add(taskLabels[i], constraints);

               
                constraints.gridx = 1;
                constraints.weightx = 0.3;

              String taskText;
            
              if (usePredefinedTasks) {
                  taskText = lang.getString(currentTasks.get(taskNumber));
                  } else {
                     taskText = currentTasks.get(taskNumber);
                    }       
                taskButtons[i] = new JButton(lang.getString("task.start") + " " + taskText);

               
                int finalI = i;
                int finalTaskNumber = taskNumber;
                List<String> finalTaskSource = currentTasks;

                taskButtons[i].addActionListener(new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        completeTask(finalI, finalTaskNumber, taskLabels[finalI], finalTaskSource);
                    }
                });

                tasksPanel.add(taskButtons[i], constraints);
            }
        }

        tasksPanel.revalidate();
        tasksPanel.repaint();

      
        ThemeManager.applyTheme(tasksPanel, isDarkMode);
    }

 private void completeTask(int taskIndex, int taskNumber, JLabel taskLabel, List<String> taskSource) {

        if (taskLabel != null && !taskLabel.getText().contains(lang.getString("task.completed"))) {
            
          
            taskProgress = taskProgress + (100 / tasksPerLevel);
            progressBar.setValue(taskProgress);

           
            String taskKey = taskSource.get(taskNumber);
            taskLabel.setText(lang.getString(taskKey) + " - " + lang.getString("task.completed"));
            taskButtons[taskIndex].setEnabled(false);

          
            taskStatusLabel.setText(lang.getString("app.task_completed") + " " +taskProgress + "%");

         
            LocalDate today = LocalDate.now();

          
            int currentCount = AppData.dailyTaskCount.getOrDefault(today, 0);
            AppData.dailyTaskCount.put(today, currentCount + 1);

          
            if (!AppData.dailyCompletedTasks.containsKey(today)) {
                AppData.dailyCompletedTasks.put(today, new java.util.ArrayList<>());
            }

            String taskType;
            if (usePredefinedTasks) {
                taskType = "[" + lang.getString("task.predefined_prefix") + "] ";
            } else {
                 taskType = "[" + lang.getString("task.custom_prefix") + "] ";
            }


            AppData.dailyCompletedTasks.get(today).add(taskType + lang.getString(taskKey));

          
            if (taskProgress >= 100) {
                levelUp();
            }
        }
    }

    private void levelUp() {
      
        currentLevel++;
        levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);

      
        taskProgress = 0;
        progressBar.setValue(0);

       
        List<String> currentTasks;
        if (usePredefinedTasks) {
            currentTasks = AppData.taskKeys;
        } else {
            currentTasks = AppData.customTasks;
        }

       
        int nextLevelStart = (currentLevel - 1) * tasksPerLevel;

       
        if (nextLevelStart < currentTasks.size()) {
           
            createTasksForCurrentLevel();

            taskStatusLabel.setText(lang.getString("app.level_up") + " " +currentLevel + " " + lang.getString("app.new_tasks"));

           
            if (taskButtons != null) {
                for (JButton button : taskButtons) {
                    if (button != null) {
                        button.setEnabled(true);
                    }
                }
            }
        }
    }

    private void showAddTaskDialog() {
        if (!usePredefinedTasks) {
            JFrame addTaskFrame = new JFrame(lang.getString("dialog.add_task_title"));
            addTaskFrame.setSize(400, 200);
            addTaskFrame.setLocationRelativeTo(null);
            addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); 
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel(lang.getString("dialog.task_name")), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            JTextField taskNameField = new JTextField(20);
            panel.add(taskNameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.weightx = 0;
            JButton saveTaskButton = new JButton(lang.getString("dialog.save_task"));
            saveTaskButton.addActionListener(e -> {
                String taskName = taskNameField.getText().trim();
                if (!taskName.isEmpty()) {
                    AppData.customTasks.add(taskName);
                    JOptionPane.showMessageDialog(addTaskFrame, lang.getString("dialog.task_added"), lang.getString("dialog.success"), JOptionPane.INFORMATION_MESSAGE);
                    addTaskFrame.dispose();

                    currentLevel = 1;
                    taskProgress = 0;
                    progressBar.setValue(0);
                    levelLabel.setText(lang.getString("app.level") + ": " + currentLevel);
                    createTasksForCurrentLevel();
                } else {
                    JOptionPane.showMessageDialog(addTaskFrame, lang.getString("dialog.empty_task"), lang.getString("error.message"), JOptionPane.ERROR_MESSAGE);
                }
            });
            panel.add(saveTaskButton, gbc);

            gbc.gridy = 2;
            JButton cancelButton = new JButton(lang.getString("dialog.cancel"));
            cancelButton.addActionListener(e -> addTaskFrame.dispose());
            panel.add(cancelButton, gbc);

            addTaskFrame.add(panel);
            ThemeManager.applyTheme(panel, isDarkMode);
            addTaskFrame.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, lang.getString("dialog.wrong_mode"), lang.getString("error.message_wrong_mode"), JOptionPane.ERROR_MESSAGE);
        }
    }
}

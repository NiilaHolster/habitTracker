import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsWindow extends JFrame implements LanguageChangeListener {

    private JLabel titleLabel;
    private JLabel statisticLabel;
    private JButton clearHistoryButton;
    private JButton cancelButton;

    private LocalizationManager lang = LocalizationManager.getInstance();

    private boolean isDarkMode;

    public StatisticsWindow(boolean isDarkMode) {

        this.isDarkMode = isDarkMode;

        lang.addListener(this);

        setTitle(lang.getString("statistics.title"));
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel(lang.getString("statistics.title"), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel graphPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBarGraph(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(550, 300);
            }
        };

        if (isDarkMode) {
            graphPanel.setBackground(new Color(45, 45, 45));
        } else {
            graphPanel.setBackground(Color.CYAN);
        }

        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        int totalCompletedTasks = AppData.dailyCompletedTasks.values()
                .stream()
                .mapToInt(List::size)
                .sum();

        int activeDays = AppData.dailyTaskCount.size();

        statisticLabel = new JLabel(
                String.format(lang.getString("statistics.total_completed"), totalCompletedTasks)
                        + " | "
                        + String.format(lang.getString("statistics.active_days"), activeDays),
                JLabel.LEFT
        );

        statisticLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        clearHistoryButton = new JButton(lang.getString("statistics.clear_history"));

        clearHistoryButton.addActionListener(e -> {

            AppData.dailyTaskCount.clear();
            AppData.dailyCompletedTasks.clear();

            graphPanel.repaint();

            statisticLabel.setText(
                    String.format(lang.getString("statistics.total_completed"), 0)
                            + " | "
                            + String.format(lang.getString("statistics.active_days"), 0)
            );
        });

        cancelButton = new JButton(lang.getString("statistics.cancel"));
        cancelButton.addActionListener(e -> dispose());

        mainPanel.add(graphPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottomPanel.add(cancelButton);
        bottomPanel.add(clearHistoryButton);

        controlPanel.add(statisticLabel, BorderLayout.NORTH);
        controlPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(controlPanel, BorderLayout.SOUTH);

        add(mainPanel);

        ThemeManager.applyTheme(mainPanel, isDarkMode);

        setVisible(true);
    }

    private void drawBarGraph(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        int width = 550;
        int height = 250;
        int padding = 50;
        int graphHeight = height - 2 * padding;
        int graphWidth = width - 2 * padding;

        Color axisColor;
        Color textColor;
        Color barColor;

        if (isDarkMode) {
            axisColor = Color.LIGHT_GRAY;
            textColor = Color.WHITE;
            barColor = new Color(100, 149, 237);
        } else {
            axisColor = Color.BLACK;
            textColor = Color.BLACK;
            barColor = new Color(100, 149, 237);
        }

        g2d.setColor(axisColor);

        g2d.drawLine(padding, padding, padding, height - padding);
        g2d.drawLine(padding, height - padding, width - padding, height - padding);

        if (AppData.dailyTaskCount.isEmpty()) {
            g2d.setColor(textColor);
            g2d.drawString(lang.getString("statistics.no_data"), width / 2 - 50, height / 2);
            return;
        }

        List<LocalDate> dates = new ArrayList<>(AppData.dailyTaskCount.keySet());
        dates.sort(LocalDate::compareTo);

        int maxTasks = AppData.dailyTaskCount.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(1);

        if (maxTasks == 0) {
            maxTasks = 1;
        }

        int barWidth = (graphWidth - 20) / dates.size() - 10;

        if (barWidth > 50) {
            barWidth = 50;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE");

        for (int i = 0; i < dates.size(); i++) {

            LocalDate date = dates.get(i);
            int taskCount = AppData.dailyTaskCount.get(date);

            int x = padding + 20 + i * (barWidth + 15);
            int barHeight = (int) ((double) taskCount / maxTasks * (graphHeight - 30));
            int y = height - padding - barHeight;

            g2d.setColor(barColor);
            g2d.fillRect(x, y, barWidth, barHeight);

            g2d.setColor(axisColor);
            g2d.drawRect(x, y, barWidth, barHeight);

            g2d.setColor(textColor);

            g2d.drawString(String.valueOf(taskCount), x + barWidth / 2 - 5, y - 5);
            g2d.drawString(date.format(dateFormatter), x - 5, height - padding + 15);
            g2d.drawString(date.format(dayFormatter), x - 5, height - padding + 30);
        }

        g2d.setColor(textColor);

        for (int i = 0; i <= maxTasks; i++) {

            int y = height - padding - (i * (graphHeight - 30) / maxTasks);

            g2d.drawString(String.valueOf(i), padding - 25, y + 5);
            g2d.drawLine(padding - 5, y, padding, y);
        }

        g2d.drawString(lang.getString("statistics.tasks_completed"), 10, 50);
    }

    @Override
    public void onLanguageChanged() {

        setTitle(lang.getString("statistics.title"));
        titleLabel.setText(lang.getString("statistics.title"));

        int totalCompletedTasks = AppData.dailyCompletedTasks.values()
                .stream()
                .mapToInt(List::size)
                .sum();

        int activeDays = AppData.dailyTaskCount.size();

        statisticLabel.setText(
                lang.getString("statistics.total_completed")
                        + ": "
                        + totalCompletedTasks
                        + " | "
                        + lang.getString("statistics.active_days")
                        + ": "
                        + activeDays
        );

        clearHistoryButton.setText(lang.getString("statistics.clear_history"));
        cancelButton.setText(lang.getString("statistics.cancel"));

        repaint();
    }
}

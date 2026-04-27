import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {
    public static List<String> taskNames = new ArrayList<>();
    public static List<String> customTasks = new ArrayList<>();
    public static Map<LocalDate, Integer> dailyTaskCount = new HashMap<>();
    public static Map<LocalDate, List<String>> dailyCompletedTasks = new HashMap<>();

    public static void initializePredefinedTasks() {
        taskNames.add("Task 1: Clean Kitchen");
        taskNames.add("Task 2: Vacuum The House");
        taskNames.add("Task 3: Cooking");
        taskNames.add("Task 4: Take the dog out");
        taskNames.add("Task 5: Do Programming 4 homework");
        taskNames.add("Task 6: Take trash Out");
        taskNames.add("Task 7: Go to Gym");
        taskNames.add("Task 8: Do Project work");
        taskNames.add("Task 9: Clean Dust");
        taskNames.add("Task 10: Clean the bathroom");
        taskNames.add("Task 11: Dishes");
        taskNames.add("Task 12: Do laundry");
        taskNames.add("Task 13: Go walking");
        taskNames.add("Task 14: Make dinner");
        taskNames.add("Task 15: Call relatives");
        taskNames.add("Task 15: Brush Teeth");
    }
}
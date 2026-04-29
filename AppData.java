import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {

   
    public static List<String> taskKeys = new ArrayList<>();
    public static List<String> customTasks = new ArrayList<>();

    public static Map<LocalDate, Integer> dailyTaskCount = new HashMap<>();
    public static Map<LocalDate, List<String>> dailyCompletedTasks = new HashMap<>();

    public static void initializePredefinedTasks() {

        taskKeys.clear();

        taskKeys.add("task.clean_kitchen");
        taskKeys.add("task.vacuum_house");
        taskKeys.add("task.cooking");
        taskKeys.add("task.walk_dog");
        taskKeys.add("task.homework");
        taskKeys.add("task.trash_out");
        taskKeys.add("task.gym");
        taskKeys.add("task.project_work");
        taskKeys.add("task.clean_dust");
        taskKeys.add("task.clean_bathroom");
        taskKeys.add("task.dishes");
        taskKeys.add("task.laundry");
        taskKeys.add("task.walking");
        taskKeys.add("task.make_dinner");
        taskKeys.add("task.call_relatives");
        taskKeys.add("task.brush_teeth");
    }
}

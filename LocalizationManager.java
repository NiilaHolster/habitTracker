
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

public class LocalizationManager{

    private static LocalizationManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private List<LanguageChangeListener> listeners = new ArrayList<>();


    public static final Locale LOCALE_EN = new Locale("en");
    public static final Locale LOCALE_FI = new Locale("fi");

    private LocalizationManager(){
        currentLocale = LOCALE_EN;
        bundle = ResourceBundle.getBundle("Messages", currentLocale);
    }

    public static LocalizationManager getInstance(){
        if(instance == null){
            instance = new LocalizationManager();
        }
        return instance;
    }

    public void setLocale(Locale locale){
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("Messages", currentLocale);
        
        for(LanguageChangeListener listener : listeners){
            listener.onLanguageChanged();
        }
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public String getString(String key){
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public void updateComponentText(JComponent component, String key){
        if(component instanceof JLabel){
            ((JLabel) component).setText(getString(key));
        }else if (component instanceof JButton){
            ((JButton) component).setText(getString(key));
        }
    }

    public void addListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LanguageChangeListener listener) {
        listeners.remove(listener);
    }



}

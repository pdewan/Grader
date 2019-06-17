package framework.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * This is used to set, get, save, and load settings related to the grader.
 * This is a singleton, so you'll have to do the following {@code GraderSettings.get()} before invoking any method.
 * It looks for the file named ".gradersettings" in the current working directory for the saved settings.
 * TODO: Move the name of the filename to a configuration file
 */
public class GraderSettings {

    private Map<String, String> settings;

    private GraderSettings() {
        settings = new HashMap<String, String>();
        String aSettingsFileName = ".gradersettings";
        try {
//            List<String> lines = FileUtils.readLines(new File(".gradersettings"));
            List<String> lines = FileUtils.readLines(new File(aSettingsFileName));

            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2 && !parts[1].isEmpty())
                    settings.put(parts[0], parts[1]);
            }
        } catch (Exception e) {
            System.out.println("W*** No settings file:" + aSettingsFileName + ". Using dynamic configuration");
        }
    }
    
    public  Map<String, String> getSettings() {
    	return settings;
    }
    

    /**
     * Returns a setting associated with the key
     * @param key The key to use
     * @return The setting
     */
    public String get(String key) {
        return settings.get(key);
    }

    /**
     * Sets the setting associating it with the given key
     * @param key The key to the setting
     * @param value The setting
     */
    public void set(String key, String value) {
        settings.put(key, value);
    }

    /**
     * Checks if there is a setting with the given key.
     * @param key The setting key
     * @return If the setting exists
     */
    public boolean has(String key) {
        return settings.containsKey(key);
    }
    
   

    /**
     * Saves the settings to the file, making it hidden.
     */
    public void save() {
        try {
            FileWriter writer = new FileWriter(".gradersettings");
            String data = "";
            for (String key : settings.keySet())
                data += key + "=" + settings.get(key) + "\n";
            writer.write(data);
            writer.close();

            // Make sure the file is hidden
            //if (GradingEnvironment.get().getOsName().equals("Windows"))
            //    Runtime.getRuntime().exec("attrib +H .gradersettings");
        } catch (IOException e) {
            System.out.println("Error saving settings.");
        }
    }


    // Singleton methods
    private static GraderSettings singleton = null;
    public static GraderSettings get() {
        if (singleton == null)
            singleton = new GraderSettings();
        return singleton;
    }
  
}

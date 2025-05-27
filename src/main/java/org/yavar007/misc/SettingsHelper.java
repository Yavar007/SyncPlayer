package org.yavar007.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.yavar007.models.SettingsModel;

import java.io.File;

public class SettingsHelper {
    private static final String SETTINGS_FILE_PATH = "./settings/settings.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    public SettingsModel LoadSettings(){
        try {
            return objectMapper.readValue(new File(SETTINGS_FILE_PATH), SettingsModel.class);
        } catch (Exception e) {
            System.out.println("Error on loading settings.json");
            return null; }
    }
    public void SaveSettings(SettingsModel settings){
        try {
            objectMapper.writeValue(new File(SETTINGS_FILE_PATH), settings);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

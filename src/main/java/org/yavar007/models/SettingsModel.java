package org.yavar007.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.yavar007.models.LanguageModels.LanguageModel;

import java.util.List;

public class SettingsModel {
    @JsonProperty("isDarkTheme")
    private boolean isDarkTheme;
    @JsonProperty("selectedLanguageCode")
    private String selectedLanguageCode;
    @JsonProperty("languages")
    private List<LanguageModel> languages;

    public SettingsModel() {
    }

    public SettingsModel(boolean isDarkTheme,
                         String selectedLanguageCode,
                         List<LanguageModel> languages) {
        this.isDarkTheme = isDarkTheme;
        this.selectedLanguageCode = selectedLanguageCode;
        this.languages = languages;
    }

    // Getters
    @JsonProperty("isDarkTheme")
    public boolean getIsDarkTheme() { return isDarkTheme; }
    // Setters
    @JsonProperty("isDarkTheme")
    public void setIsDarkTheme(boolean isDarkTheme) { this.isDarkTheme = isDarkTheme; }

    public String getSelectedLanguageCode() {
        return selectedLanguageCode;
    }

    public void setSelectedLanguageCode(String selectedLanguageCode) {
        this.selectedLanguageCode = selectedLanguageCode;
    }

    public List<LanguageModel> getLanguages() {
        return languages;
    }

    public void setLanguages(List<LanguageModel> languages) {
        this.languages = languages;
    }
}

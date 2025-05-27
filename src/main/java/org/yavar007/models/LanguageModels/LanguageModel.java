package org.yavar007.models.LanguageModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LanguageModel {
    @JsonProperty("languageCode")
    private String languageCode;
    @JsonProperty("isRTL")
    private boolean isRTL;
    @JsonProperty("firstForm")
    private FirstFormLanguageModel firstForm;
    @JsonProperty("secondForm")
    private SecondFormLanguageModel secondForm;
    @JsonProperty("mainMessageTexts")
    private MainMessageModel mainMessageTexts;


    public LanguageModel() {
    }

    public LanguageModel(String languageCode,
                         boolean isRTL,
                         FirstFormLanguageModel firstForm,
                         SecondFormLanguageModel secondForm,
                         MainMessageModel mainMessageTexts) {
        this.languageCode = languageCode;
        this.isRTL = isRTL;
        this.firstForm = firstForm;
        this.secondForm = secondForm;
        this.mainMessageTexts = mainMessageTexts;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public boolean isRTL() {
        return isRTL;
    }

    public void setRTL(boolean RTL) {
        isRTL = RTL;
    }

    public FirstFormLanguageModel getFirstForm() {
        return firstForm;
    }

    public void setFirstForm(FirstFormLanguageModel firstForm) {
        this.firstForm = firstForm;
    }

    public SecondFormLanguageModel getSecondForm() {
        return secondForm;
    }

    public void setSecondForm(SecondFormLanguageModel secondForm) {
        this.secondForm = secondForm;
    }

    public MainMessageModel getMainMessageTexts() {
        return mainMessageTexts;
    }

    public void setMainMessageTexts(MainMessageModel mainMessageTexts) {
        this.mainMessageTexts = mainMessageTexts;
    }
}

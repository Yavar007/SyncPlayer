package org.yavar007.models.LanguageModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirstFormLanguageModel{

    @JsonProperty("frameTitle")
    private String frameTitle;
    @JsonProperty("langButtonText")
    private String langButtonText;
    @JsonProperty("themeButtonTextDark")
    private String themeButtonTextDark;
    @JsonProperty("themeButtonTextLight")
    private String themeButtonTextLight;
    @JsonProperty("nickNameHint")
    private String nickNameHint;
    @JsonProperty("nickNameToolTip")
    private String nickNameToolTip;
    @JsonProperty("onNickNameEmpty")
    private String onNickNameEmpty;
    @JsonProperty("clientButtonText")
    private String clientButtonText;
    @JsonProperty("serverButtonText")
    private String serverButtonText;

    public FirstFormLanguageModel() {
    }

    public FirstFormLanguageModel(String frameTitle,
                                  String langButtonText,
                                  String themeButtonTextDark,
                                  String themeButtonTextLight,
                                  String nickNameHint,
                                  String nickNameToolTip,
                                  String onNickNameEmpty,
                                  String clientButtonText,
                                  String serverButtonText) {
        this.frameTitle = frameTitle;
        this.langButtonText = langButtonText;
        this.themeButtonTextDark = themeButtonTextDark;
        this.themeButtonTextLight = themeButtonTextLight;
        this.nickNameHint = nickNameHint;
        this.nickNameToolTip = nickNameToolTip;
        this.onNickNameEmpty = onNickNameEmpty;
        this.clientButtonText = clientButtonText;
        this.serverButtonText = serverButtonText;
    }

    public String getFrameTitle() {
        return frameTitle;
    }

    public void setFrameTitle(String frameTitle) {
        this.frameTitle = frameTitle;
    }

    public String getLangButtonText() {
        return langButtonText;
    }

    public void setLangButtonText(String langButtonText) {
        this.langButtonText = langButtonText;
    }

    public String getThemeButtonTextDark() {
        return themeButtonTextDark;
    }

    public void setThemeButtonTextDark(String themeButtonTextDark) {
        this.themeButtonTextDark = themeButtonTextDark;
    }

    public String getThemeButtonTextLight() {
        return themeButtonTextLight;
    }

    public void setThemeButtonTextLight(String themeButtonTextLight) {
        this.themeButtonTextLight = themeButtonTextLight;
    }

    public String getNickNameHint() {
        return nickNameHint;
    }

    public void setNickNameHint(String nickNameHint) {
        this.nickNameHint = nickNameHint;
    }

    public String getNickNameToolTip() {
        return nickNameToolTip;
    }

    public void setNickNameToolTip(String nickNameToolTip) {
        this.nickNameToolTip = nickNameToolTip;
    }

    public String getOnNickNameEmpty() {
        return onNickNameEmpty;
    }

    public void setOnNickNameEmpty(String onNickNameEmpty) {
        this.onNickNameEmpty = onNickNameEmpty;
    }

    public String getClientButtonText() {
        return clientButtonText;
    }

    public void setClientButtonText(String clientButtonText) {
        this.clientButtonText = clientButtonText;
    }

    public String getServerButtonText() {
        return serverButtonText;
    }

    public void setServerButtonText(String serverButtonText) {
        this.serverButtonText = serverButtonText;
    }
}

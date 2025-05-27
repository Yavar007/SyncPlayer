package org.yavar007.models.LanguageModels;
import com.fasterxml.jackson.annotation.JsonProperty;
public class MainMessageModel {
    @JsonProperty("onConnected")
    private String onConnected;
    @JsonProperty("onDisconnected")
    private String onDisconnected;

    public MainMessageModel() {
    }
    public MainMessageModel(String onConnected,
                            String onDisconnected) {
        this.onConnected = onConnected;
        this.onDisconnected = onDisconnected;
    }

    public String getOnConnected() {
        return onConnected;
    }

    public void setOnConnected(String onConnected) {
        this.onConnected = onConnected;
    }

    public String getOnDisconnected() {
        return onDisconnected;
    }

    public void setOnDisconnected(String onDisconnected) {
        this.onDisconnected = onDisconnected;
    }
}

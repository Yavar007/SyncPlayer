package org.yavar007.models.LanguageModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SecondFormLanguageModel {
    @JsonProperty("frameTitle")
    private String frameTitle;
    @JsonProperty("serverUrlFiledHint")
    private String serverUrlFiledHint;
    @JsonProperty("clientRoomIDFieldHint")
    private String clientRoomIDFieldHint;
    @JsonProperty("serverUrlFiledToolTip")
    private String serverUrlFiledToolTip;
    @JsonProperty("clientRoomIDFieldToolTip")
    private String clientRoomIDFieldToolTip;
    @JsonProperty("onURLEmpty")
    private String onURLEmpty;
    @JsonProperty("onRoomIDEmpty")
    private String onRoomIDEmpty;
    @JsonProperty("invalidURL")
    private String invalidURL;
    @JsonProperty("onRoomIDCopied")
    private String onRoomIDCopied;
    @JsonProperty("onNewUserJoined")
    private String onNewUserJoined;
    @JsonProperty("onRoomOwnerDisconnected")
    private String onRoomOwnerDisconnected;
    @JsonProperty("onUserDisconnected")
    private String onUserDisconnected;
    @JsonProperty("btnServerLoad")
    private String btnServerLoad;
    @JsonProperty("btnClientJoin")
    private String btnClientJoin;
    @JsonProperty("btnPlay")
    private String btnPlay;
    @JsonProperty("btnPause")
    private String btnPause;
    @JsonProperty("btnStop")
    private String btnStop;
    @JsonProperty("btnAudio")
    private String btnAudio;
    @JsonProperty("btnSubtitle")
    private String btnSubtitle;
    @JsonProperty("btnShowUsers")
    private String btnShowUsers;
    @JsonProperty("userListTexts")
    private String userListTexts;
    @JsonProperty("delayLowValue")
    private String delayLowValue;

    public SecondFormLanguageModel() {
    }

    public SecondFormLanguageModel(String frameTitle,
                                   String serverUrlFiledHint,
                                   String clientRoomIDFieldHint,
                                   String serverUrlFiledToolTip,
                                   String clientRoomIDFieldToolTip,
                                   String onURLEmpty,
                                   String invalidURL,
                                   String onRoomIDEmpty,
                                   String onRoomIDCopied,
                                   String onNewUserJoined,
                                   String onRoomOwnerDisconnected,
                                   String onUserDisconnected,
                                   String btnServerLoad,
                                   String btnClientJoin,
                                   String btnPlay,
                                   String btnPause,
                                   String btnStop,
                                   String btnAudio,
                                   String btnSubtitle,
                                   String btnShowUsers,
                                   String userListTexts,
                                   String delayLowValue) {
        this.frameTitle = frameTitle;
        this.serverUrlFiledHint = serverUrlFiledHint;
        this.clientRoomIDFieldHint = clientRoomIDFieldHint;
        this.serverUrlFiledToolTip = serverUrlFiledToolTip;
        this.clientRoomIDFieldToolTip = clientRoomIDFieldToolTip;
        this.onURLEmpty = onURLEmpty;
        this.invalidURL = invalidURL;
        this.onRoomIDEmpty = onRoomIDEmpty;
        this.onRoomIDCopied = onRoomIDCopied;
        this.onNewUserJoined = onNewUserJoined;
        this.onRoomOwnerDisconnected = onRoomOwnerDisconnected;
        this.onUserDisconnected = onUserDisconnected;
        this.btnServerLoad = btnServerLoad;
        this.btnClientJoin = btnClientJoin;
        this.btnPlay = btnPlay;
        this.btnPause = btnPause;
        this.btnStop = btnStop;
        this.btnAudio = btnAudio;
        this.btnSubtitle = btnSubtitle;
        this.btnShowUsers=btnShowUsers;
        this.userListTexts = userListTexts;
        this.delayLowValue=delayLowValue;
    }

    public String getFrameTitle() {
        return frameTitle;
    }

    public void setFrameTitle(String frameTitle) {
        this.frameTitle = frameTitle;
    }

    public String getServerUrlFiledHint() {
        return serverUrlFiledHint;
    }

    public void setServerUrlFiledHint(String serverUrlFiledHint) {
        this.serverUrlFiledHint = serverUrlFiledHint;
    }

    public String getClientRoomIDFieldHint() {
        return clientRoomIDFieldHint;
    }

    public void setClientRoomIDFieldHint(String clientRoomIDFieldHint) {
        this.clientRoomIDFieldHint = clientRoomIDFieldHint;
    }

    public String getServerUrlFiledToolTip() {
        return serverUrlFiledToolTip;
    }

    public void setServerUrlFiledToolTip(String serverUrlFiledToolTip) {
        this.serverUrlFiledToolTip = serverUrlFiledToolTip;
    }

    public String getClientRoomIDFieldToolTip() {
        return clientRoomIDFieldToolTip;
    }

    public void setClientRoomIDFieldToolTip(String clientRoomIDFieldToolTip) {
        this.clientRoomIDFieldToolTip = clientRoomIDFieldToolTip;
    }

    public String getOnURLEmpty() {
        return onURLEmpty;
    }

    public void setOnURLEmpty(String onURLEmpty) {
        this.onURLEmpty = onURLEmpty;
    }

    public String getInvalidURL() {
        return invalidURL;
    }

    public void setInvalidURL(String invalidURL) {
        this.invalidURL = invalidURL;
    }

    public String getOnRoomIDEmpty() {
        return onRoomIDEmpty;
    }

    public void setOnRoomIDEmpty(String onRoomIDEmpty) {
        this.onRoomIDEmpty = onRoomIDEmpty;
    }

    public String getOnRoomIDCopied() {
        return onRoomIDCopied;
    }

    public void setOnRoomIDCopied(String onRoomIDCopied) {
        this.onRoomIDCopied = onRoomIDCopied;
    }

    public String getOnNewUserJoined() {
        return onNewUserJoined;
    }

    public void setOnNewUserJoined(String onNewUserJoined) {
        this.onNewUserJoined = onNewUserJoined;
    }

    public String getOnRoomOwnerDisconnected() {
        return onRoomOwnerDisconnected;
    }

    public void setOnRoomOwnerDisconnected(String onRoomOwnerDisconnected) {
        this.onRoomOwnerDisconnected = onRoomOwnerDisconnected;
    }

    public String getOnUserDisconnected() {
        return onUserDisconnected;
    }

    public void setOnUserDisconnected(String onUserDisconnected) {
        this.onUserDisconnected = onUserDisconnected;
    }

    public String getBtnServerLoad() {
        return btnServerLoad;
    }

    public void setBtnServerLoad(String btnServerLoad) {
        this.btnServerLoad = btnServerLoad;
    }

    public String getBtnClientJoin() {
        return btnClientJoin;
    }

    public void setBtnClientJoin(String btnClientJoin) {
        this.btnClientJoin = btnClientJoin;
    }

    public String getBtnPlay() {
        return btnPlay;
    }

    public void setBtnPlay(String btnPlay) {
        this.btnPlay = btnPlay;
    }

    public String getBtnPause() {
        return btnPause;
    }

    public void setBtnPause(String btnPause) {
        this.btnPause = btnPause;
    }

    public String getBtnStop() {
        return btnStop;
    }

    public void setBtnStop(String btnStop) {
        this.btnStop = btnStop;
    }

    public String getBtnAudio() {
        return btnAudio;
    }

    public void setBtnAudio(String btnAudio) {
        this.btnAudio = btnAudio;
    }

    public String getBtnSubtitle() {
        return btnSubtitle;
    }

    public void setBtnSubtitle(String btnSubtitle) {
        this.btnSubtitle = btnSubtitle;
    }

    public String getBtnShowUsers() {
        return btnShowUsers;
    }

    public void setBtnShowUsers(String btnShowUsers) {
        this.btnShowUsers = btnShowUsers;
    }

    public String getUserListTexts() {
        return userListTexts;
    }

    public void setUserListTexts(String userListTexts) {
        this.userListTexts = userListTexts;
    }

    public String getDelayLowValue() {
        return delayLowValue;
    }

    public void setDelayLowValue(String delayLowValue) {
        this.delayLowValue = delayLowValue;
    }
}

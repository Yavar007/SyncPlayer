package org.yavar007;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialDarkerIJTheme;
import org.eclipse.paho.client.mqttv3.*;
import org.yavar007.database.RoomDatabaseHelper;
import org.yavar007.forms.InitForm;
import org.yavar007.misc.SettingsHelper;
import org.yavar007.misc.VLCUtils;
import org.yavar007.models.*;
import javax.swing.*;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class Main {

    private static final String broker = "tcp://broker.emqx.io:1883";
    private static MqttClient client;
    private static ClientModel clientModel;
    private static boolean isDarkTheme = false;

    public static void main(String[] args) {

        String[] folderNames={"./images","./settings","./data","./fonts"};
        String[] fileNames={"./settings/settings.json",
                "./fonts/yekan.ttf",
                "./images/applogo.png",
                "./images/android.png",
                "./images/linux.png",
                "./images/macos.png",
                "./images/windows.png"};
        HandleFilesFolders(folderNames,fileNames);

        String osName = System.getProperty("os.name");
        String deviceName = "Unknown";
        try {
            deviceName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Device Name could not be determined.");
        }
        String combinedInfo = osName + "-" + deviceName;
        String uniqueID = UUID.nameUUIDFromBytes(combinedInfo.getBytes()).toString();
        clientModel = new ClientModel(uniqueID, deviceName, osName);
        if (VLCUtils.isVLCInstalled() && !VLCUtils.getVLCPath().isEmpty()) {
            System.setProperty("jna.library.path", VLCUtils.getVLCPath());
        }else{
            System.out.println("VLC path could not be determined.");
            System.exit(0);
        }
        try {
            client = new MqttClient(broker, uniqueID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    System.out.println("Connection lost: " + throwable.getMessage());
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) {
                    System.out.println("Message from callback: " + new String(mqttMessage.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    // System.out.println("Message delivered");
                }
            });
            client.connect(options);
            if (client.isConnected()) {
                System.out.println(uniqueID + " connected to broker: " + broker);
                String encjson = "User (" + uniqueID + ") has Joined";
                MqttMessage msg = new MqttMessage(encjson.getBytes());
                msg.setQos(2);
                client.publish("syncplayer/rooms", msg);
                System.out.println("Message sent after connecting");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        SettingsHelper settingsHelper = new SettingsHelper();
        SettingsModel settingsModel = settingsHelper.LoadSettings();
        if (settingsModel == null) {
            System.exit(0);
        }
        isDarkTheme = settingsModel.getIsDarkTheme();

        RoomDatabaseHelper roomDatabaseHelper = new RoomDatabaseHelper();
        roomDatabaseHelper.deleteAllData();

        SwingUtilities.invokeLater(() -> {
            try {
                if (isDarkTheme) {
                    UIManager.setLookAndFeel(new FlatMTMaterialDarkerIJTheme());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                if (client.isConnected()) {
                    // Show the first form
                    new InitForm(client, clientModel);
                }
            } catch (UnsupportedLookAndFeelException e) {
                System.out.println(e.getMessage());
            }
        });
    }
    private static void HandleFilesFolders(String[] folders, String[] files) {
        try {
            for (String f:folders){
                File folder = new File(f);
                if (!folder.exists()){
                    folder.mkdir();
                }
            }
            for (String f:files){
                File file = new File(f);
                if (!file.exists()){
                    String filename=file.getPath().replace("\\","/").split("/")[2];
                    InputStream inputStream=ClassLoader.getSystemResourceAsStream(filename);
                    assert inputStream != null;
                    Files.copy(inputStream, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}

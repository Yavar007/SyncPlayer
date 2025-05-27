package org.yavar007.misc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.yavar007.database.RoomDatabaseHelper;
import org.yavar007.forms.PlayerForm;
import org.yavar007.models.*;
import org.yavar007.models.CommunicationModels.*;

public class MessageHandling {

    private String jsonString = "";
    private final MqttClient client;
    private final ClientModel clientModel;
    private final PlayerForm playerForm;
    private final RoomDatabaseHelper roomDatabaseHelper;

    public MessageHandling(PlayerForm playerForm, MqttClient client, ClientModel clientModel) {
        this.client = client;
        this.clientModel = clientModel;
        this.playerForm = playerForm;
        roomDatabaseHelper = new RoomDatabaseHelper();
    }

    public void Run(boolean isServer, String roomID, String movieLInk, String username) {
        Thread connection = new Thread(() -> {
            try {
                System.out.println(roomID);
                client.subscribe("syncplayer/rooms", (s, mqttMessage) -> {
                    String remsg = new String(mqttMessage.getPayload());
                    if (s.equals("syncplayer/rooms")) {
                        if (remsg.startsWith(roomID)) {
                            remsg = remsg.split(":")[1] + ":" + remsg.split(":")[2];
                            MessageModel message = new ParseMessages().parseMessage(remsg);
                            if (!message.getId().equals(clientModel.getId())) {
                                if (isServer) {
                                    if (message.getType().equals("req")) {
                                        RequestToJoinMessageModel req = (RequestToJoinMessageModel) message;
                                        if (req.getRoomId().equals(roomID)) {
                                            String answer = req.getId();
                                            String username1 = req.getUserName();
                                            String deviceOS = req.getDeviceOs();
                                            String clientRole = req.getClientRole();
                                            if (roomDatabaseHelper.canAddUser()) {
                                                sendMessage(roomID,
                                                        new AcceptMessageModel(clientModel.getId(),
                                                                "acc",
                                                                clientModel.getDeviceName()
                                                                , clientModel.getDeviceOs(),
                                                                "server", username,
                                                                answer, movieLInk, roomID));
                                                UsersInRoomModel spectatorModel = new UsersInRoomModel(answer,
                                                        username1, deviceOS, clientRole, true,
                                                        System.currentTimeMillis());
                                                playerForm.addNewSpectator(spectatorModel);
                                            }
                                        }
                                    } else if (message.getType().equals("alv")) {
                                        AliveMessageModel alive = (AliveMessageModel) message;
                                        if (alive.getRoomId().equals(roomID)) {
                                            playerForm.editSpectator(
                                                    new UsersInRoomModel(alive.getId(),
                                                            alive.getUserName(),
                                                            alive.getDeviceOs(),
                                                            alive.getClientRole(),
                                                            true,
                                                            System.currentTimeMillis()));
                                        }
                                    }
                                } else {
                                    if (message.getType().equals("acc")) {
                                        AcceptMessageModel acc = (AcceptMessageModel) message;
                                        if (acc.getMessage().equals(clientModel.getId())) {
                                            playerForm.setMovieLink(acc.getMovieLink());
                                            UsersInRoomModel server = new UsersInRoomModel(acc.getId(),
                                                    acc.getUserName(), acc.getDeviceOs(), "server", true,
                                                    System.currentTimeMillis());
                                            playerForm.addNewSpectator(server);
                                        }
                                    } else if (message.getType().equals("play")) {
                                        PlayerMessageModel play = (PlayerMessageModel) message;
                                        if (play.getRoomId().equals(roomID)) {
                                            playerForm.setMovieTime(play.getMessage());
                                            UsersInRoomModel server = new UsersInRoomModel(play.getId(),
                                                    play.getUserName()
                                                    , play.getDeviceOs(),
                                                    "server", true,
                                                    System.currentTimeMillis());
                                            playerForm.editSpectator(server);
                                            AliveMessageModel aliveMessageModel = new AliveMessageModel(
                                                    clientModel.getId(), "alv",
                                                    clientModel.getDeviceName(),
                                                    clientModel.getDeviceOs(),
                                                    "client", play.getRoomId(), username
                                            );
                                            sendMessage(roomID, aliveMessageModel);
                                        }

                                    } else if (message.getType().equals("alv") && !message.getId().equals(clientModel.getId())) {
                                        AliveMessageModel alive = (AliveMessageModel) message;
                                        if (alive.getRoomId().equals(roomID)) {
                                            playerForm.editSpectator(
                                                    new UsersInRoomModel(alive.getId(),
                                                            alive.getUserName(),
                                                            alive.getDeviceOs(),
                                                            alive.getClientRole(),
                                                            true,
                                                            System.currentTimeMillis()));
                                        }
                                    }
                                }
                            }
                        }
                    }
                });

            } catch (MqttException e) {
                System.out.println(e.getMessage());
            }
        });
        connection.start();


    }

    public void sendMessage(String roomID, MessageModel messageModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(messageModel);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        String encJson = roomID + ":" + CryptoHelper.encrypt(jsonString);
        MqttMessage msg = new MqttMessage(encJson.getBytes());
        msg.setQos(2);
        try {
            client.publish("syncplayer/rooms", msg);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}

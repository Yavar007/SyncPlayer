package org.yavar007.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.yavar007.models.CommunicationModels.*;

public class ParseMessages {
    public MessageModel parseMessage(String message) {
        String decryptedMessage=CryptoHelper.decrypt(message);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // First, read the JSON as a generic JsonNode to inspect the type field
            JsonNode jsonNode = objectMapper.readTree(decryptedMessage);
            String type = jsonNode.get("type").asText();
            // Deserialize to the correct class based on the type field
            return switch (type) {
                case "play" -> objectMapper.treeToValue(jsonNode, PlayerMessageModel.class);
                case "rej" -> objectMapper.treeToValue(jsonNode, RejectMessageModel.class);
                case "req" -> objectMapper.treeToValue(jsonNode, RequestToJoinMessageModel.class);
                case "acc" -> objectMapper.treeToValue(jsonNode, AcceptMessageModel.class);
                case "alv" -> objectMapper.treeToValue(jsonNode, AliveMessageModel.class);
                case "clj" -> objectMapper.treeToValue(jsonNode, ClientJoinedMessageModel.class);
                default -> objectMapper.treeToValue(jsonNode, MessageModel.class);
                // Default case for unknown types
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}

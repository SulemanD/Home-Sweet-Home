package game;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

    private Map<String, String> messages;

    public Message(String jsonFilePath) {
        loadMessages(jsonFilePath);
    }

    @SuppressWarnings("unchecked")
    public void loadMessages(String jsonFilePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            messages = objectMapper.readValue(new File(jsonFilePath), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public String getFormattedMessage(String key, Map<String, String> placeholders) {
        String message = messages.get(key);
        if (message == null) {
            return null;
        }

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                message = message.replace(placeholder, entry.getValue());
            }
        }

        return message;
    }
}
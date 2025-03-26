package game;

import java.io.IOException;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoadGame {
    Player player;
    NPCManager npcManager;
    
    public static void main(String[] args) {
        
        
    }

    public void loadPlayer() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            player = objectMapper.readValue(new File("../data/player.json"), Player.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}


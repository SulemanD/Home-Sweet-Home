package game;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.*;

import javax.naming.Reference;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Behavior {
    private String type;
    private int maxAttempts;
    private String objective;
    private List<String> dialogue;
    private int attempts;
    private Random random;
    
    private RoomManager roomManager;
    private Message message; 

    public Behavior(String type, List<String> dialogue, String objective){
        this.type = type;
        this.dialogue = dialogue;
        this.objective = objective;
        
    }


    public String getType() {
        // Implementation here
        return this.type;
    }

    public String getObjective() {
        // Implementation here
        return this.objective;
    }

    public void onSuccess() {
        
    }
 
    public void onFail() {
    }

}

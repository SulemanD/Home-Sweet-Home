package game;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Behavior {
    private String type;
    private int maxAttempts;
    private String objective;
    private int attempts;
    private Random random;
    private String questItem;
    
    private RoomManager roomManager;
    private Message message;

    public Behavior(@JsonProperty("type") String type, @JsonProperty("maxAttempts") int maxAttempts, @JsonProperty("objective") String objective, @JsonProperty("questItem") String questItem) {
        this.type = type;
        this.objective = objective;
        this.maxAttempts = maxAttempts;
        this.questItem = questItem;
        this.attempts = 0;
        this.random = new Random();
    }


    public String getType() {
        // Implementation here
        return this.type;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        // Implementation here
        return this.maxAttempts;
    }

    public String getObjective() {
        // Implementation here
        return this.objective;
    }

    public String getQuestItem() {
        // Implementation here
        return this.questItem;
    }



    public void onSuccess() {
        
    }
 
    public void onFail() {
    }

}

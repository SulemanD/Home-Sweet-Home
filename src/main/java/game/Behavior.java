package game;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = RiddleBehavior.class, name = "riddle"),
    @JsonSubTypes.Type(value = QuestBehavior.class, name = "quest")
})
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public String getObjective() {
        return this.objective;
    }

    public String getQuestItem() {
        return this.questItem;
    }

    public void onSuccess() {
    }
 
    public void onFail() {
    }

    public void interact(Player player) {
        
    }
}

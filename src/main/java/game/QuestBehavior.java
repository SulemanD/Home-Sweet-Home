package game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestBehavior extends Behavior{

    private String questItem;
    private Message message;

    @JsonCreator
    public QuestBehavior(
        @JsonProperty("type") String type,
        @JsonProperty("maxAttempts") int maxAttempts,
        @JsonProperty("objective") String objective,
        @JsonProperty("questItem") String questItem) {
        super(type, maxAttempts, objective, questItem);
        this.questItem = questItem;
        this.message = new Message("data/messages.json");
    }

    // Keep existing constructor for programmatic creation
    public QuestBehavior(String questItem) {
        super("quest", -1, null, questItem);
        this.questItem = questItem;
        this.message = new Message("data/messages.json");
    }

    public void onReceiveItem(Item item){
        if (item.getId().equals(questItem)){
            onSuccess(item);
        }
        else {
            onFail(item);
        }
    }

    public void onSuccess(Item item) {
        System.out.println(item.getDescription());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        System.out.println(message.getMessage("ghost_correct"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        System.out.println("Congratulations! You have completed the quest. The game will now exit.");
        System.exit(0);
    }

    public void onFail(Item item) {
        System.out.println(item.getDescription());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        System.out.println(message.getMessage("ghost_wrong"));
    }
}



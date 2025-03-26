package game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestBehavior extends Behavior{

    private String questItem;
    private Message message;

    

    public QuestBehavior(@JsonProperty("questItem") String questItem) {
        super("quest", -1, null, questItem);
        this.questItem = questItem;
        this.message = new Message("../data/messages.json");
     
    }

    public void onReceiveItem(Item item){
        if (item.getId().equals(questItem)){
            onSuccess();
        }
        else {
            onFail();
        }
    }

    public void onSuccess() {
        System.out.println(message.getMessage("ghost_correct"));
    }

    public void onFail() {
        System.out.println(message.getMessage("ghost_wrong"));
    }
}



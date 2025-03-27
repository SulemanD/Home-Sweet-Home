package game;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private String id;
    private String name;
    private String description;
    private boolean isPortable;
    private UseEffect useEffect;
    private boolean disable;
    private List<String> canGiveTo;
    
    private Message message = new Message("data/messages.json");

    @JsonCreator
    public Item(
        @JsonProperty("id") String id, 
        @JsonProperty("name") String name, 
        @JsonProperty("description") String description,
        @JsonProperty("isPortable") boolean isPortable, 
        @JsonProperty("useEffect") UseEffect useEffect,
        @JsonProperty("canGiveTo") List<String> canGiveTo) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.isPortable = isPortable;
        this.useEffect = useEffect;
        this.disable = false;
        this.canGiveTo = canGiveTo;
    }
    
    public boolean useItem(String target) {
        // Check if the item can be given to the specified target
        if (target == null || canGiveTo == null) {
            return false;
        }
        
        for (String recipient : canGiveTo) {
            if (recipient.toLowerCase().equals(target.toLowerCase())) {
                if (useEffect != null) {
                    System.out.println(useEffect.getMessage());
                }
                return true;
            }
        }
        
        System.out.println(message.getMessage("cannot_use"));
        return false;
    }

    public List<String> getCanGiveTo() {
        return canGiveTo;
    }

    public void disableItem() {
        disable = true;
    }

    public boolean isDisabled() {
        return disable;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPortable() {
        return isPortable;
    }
      
}
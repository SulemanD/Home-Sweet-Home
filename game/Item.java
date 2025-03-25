package game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private String id;
    private String name;
    private String description;
    private boolean isPortable;
    private UseEffect useEffect;
    private boolean disable;

    @JsonCreator
    public Item(
        @JsonProperty("id") String id, 
        @JsonProperty("name") String name, 
        @JsonProperty("description") String description,
        @JsonProperty("isPortable") boolean isPortable, 
        @JsonProperty("useEffect") UseEffect useEffect) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.isPortable = isPortable;
        this.useEffect = useEffect;
        this.disable = false;
    }
    

    public void useItem() {
        // Implementation here
        System.out.println(useEffect.getEffect());

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
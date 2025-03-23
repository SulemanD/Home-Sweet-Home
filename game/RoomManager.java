import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;

public class RoomManager{

    private Map<String, Room> rooms;
    private String jsonFilePath;
    private Random random;

    public RoomManager(String jsonFilePath){

        this.jsonFilePath = jsonFilePath;
        this.rooms = new HashMap<>();
        this.random = new Random();
        loadRooms();
        
    }

    public void loadRooms(){
        try{

            ObjectMapper objectMapper = new ObjectMapper();
            rooms = objectMapper.readValue(new File(jsonFilePath), new TypeReference<Map<String, Room>>() {});

            shuffleItems();

        } catch (IOException e){
            e.printStackTrace();
        }
        
        
    }

    public Room getRoom(String id){
        return rooms.get(id);
    }

    public void shuffleItems(){

        for (Room room : rooms.values()){
            Collections.shuffle(room.getItems(), random);
        }
    }

    public void shuffleRooms(){
        List<Integer> floors = new ArrayList<>();
        for (Room room : rooms.values()) {
            floors.add(room.getFloor());
        }
        Collections.shuffle(floors, random);

        int i=0; 
        for (Room room: rooms.values()){
            room.setFloor(floors.get(i));
            i++;
        }
    }


}
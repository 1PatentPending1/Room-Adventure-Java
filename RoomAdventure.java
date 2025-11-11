/*
 * Room Adventure Game
 * Name: Lane Ingles
 * Github: 1PatentPending1
 * Repo Link: https://github.com/1PatentPending1/Room-Adventure-Java.git
 * Date: 11/7/25
 * Description: A simple text-based adventure game where players navigate through different rooms.
 * Additions: 
 */



import java.util.Scanner;

public class RoomAdventure{

    private static Room currentRoom;
    private static String[] inventory = {null, null, null, null, null}; // max 5 items
    private static String status;

    final private static String DEFAULT_STATUS = "sorry, i do not understand, try using [verb] [noun]. Verbs include 'go' 'look' and 'take'";

    // main function
    public static void main(String[] args){
        setupGame();
    

    while(true){
        System.out.println(currentRoom.toString());
        System.out.println("Inventory: ");

        // for loops
        for(int i = 0; i < inventory.length; i++){
            System.out.println(inventory[i] + " ");
        }
        System.out.println("\n What would you like to do? ");

        // taking input
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();

        String[] words = input.split(" ");

        if (words.length != 2){
            status = DEFAULT_STATUS;
        }
        String verb = words[0];
        String noun = words[1];

        switch (verb){
            case "go":
                handleGo(noun);
                break;
            case "look":
                handleLook(noun);
                break;
            case "take":
                handleTake(noun);
                break;
            default: status = DEFAULT_STATUS;
        }
    }
}



private static void handleGo(String noun){
    status = "I don't see that room.";
    for (int i = 0; i < currentRoom.getExitDirections().length; i++){
        if (noun.equals(currentRoom.getExitDirections()[i])){
            //for strings, we use .equals() instead of ==
            currentRoom = currentRoom.getExitDestinations()[i];
            status = "You go " + noun + ".";
            break;
        }
    }
}

private static void handleLook(String noun){
    status = "I don't see that item.";
    String[] items = currentRoom.getItems();
    String[] descriptions = currentRoom.getItemDescriptions();

    for (int i = 0; i < items.length; i++){
        if (noun.equals(items[i])){
            status = descriptions[i];
            break;
        }
    }
}

private static void handleTake(String noun){
    status = "I can't grab that item.";
    String[] grabbables = currentRoom.getGrabbables();
   // maybe make an addToInventory function later
    for (int i = 0; i < grabbables.length; i++){
        if (noun.equals(grabbables[i])){
            for (int j = 0; j < inventory.length; j++){
                if (inventory[j] == null){
                    inventory[j] = noun;
                    status = "item added to inventory.";
                    break;
                }
            }
        }
    }
}



    public static void setupGame(){

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3");
        Room room4 = new Room("Room 4");

        String[] room1ExitDirections = {"north", "east",};
        Room[] room1ExitDestinations = {room2, room3};
        String[] room1Items = {"chair", "desk"};
        String[] room1ItemDescriptions = {"a wooden chair", "a wooden desk, there is a key on the desk"};
        String[] room1Grabbables = {"key"};
        room1.setExitDirections(room1ExitDirections);
        room1.setExitDestinations(room1ExitDestinations);
        room1.setItems(room1Items);
        room1.setItemDescriptions(room1ItemDescriptions);
        room1.setGrabbables(room1Grabbables);

        //Room 2 setup
        String[] room2ExitDirections = {"south", "east"};
        Room[] room2ExitDestinations = {room1, room4};
        String[] room2Items = {"table", "lamp"};
        String[] room2ItemDescriptions = {"a small table", "a bright lamp"};
        String[] room2Grabbables = {"lamp"};
        room2.setExitDirections(room2ExitDirections);
        room2.setExitDestinations(room2ExitDestinations);
        room2.setItems(room2Items);
        room2.setItemDescriptions(room2ItemDescriptions);
        room2.setGrabbables(room2Grabbables);

        //Room 3 setup
        String[] room3ExitDirections = {"west", "north"};
        Room[] room3ExitDestinations = {room2, room4};
        String[] room3Items = {"sofa", "bookshelf"};
        String[] room3ItemDescriptions = {"a comfortable sofa", "a tall bookshelf filled with books"};
        String[] room3Grabbables = {"book"};
        room3.setExitDirections(room3ExitDirections);
        room3.setExitDestinations(room3ExitDestinations);
        room3.setItems(room3Items);
        room3.setItemDescriptions(room3ItemDescriptions);
        room3.setGrabbables(room3Grabbables);

        //Room 4 setup
        String[] room4ExitDirections = {"west", "south"};
        Room[] room4ExitDestinations = {room3, room2};
        String[] room4Items = {"bed", "wardrobe"};
        String[] room4ItemDescriptions = {"a large bed", "a wooden wardrobe"};
        String[] room4Grabbables = {"pillow"};
        room4.setExitDirections(room4ExitDirections);
        room4.setExitDestinations(room4ExitDestinations);
        room4.setItems(room4Items);
        room4.setItemDescriptions(room4ItemDescriptions);
        room4.setGrabbables(room4Grabbables);

        currentRoom = room1; // start in room 1
    }

        
    }

class Room{
        private String name;
        private String[] exitDirections; // north, south, east, west
        private Room[] exitDestinations; 
        private String[] items;
        private String[] itemDescriptions;
        private String[] grabbables;

        //constructor
        public Room(String name){
            this.name = name; // use this to set the name of the room
            
        }

    

    // methods getters and setters 
    public void setExitDirections(String[] exitDirections){
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections(){
        return this.exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations){
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations(){
        return this.exitDestinations;
    }

    public void setItems(String[] items){
        this.items = items;
    }

    public String[] getItems(){
        return this.items;
    }

    public void setItemDescriptions(String[] itemDescriptions){
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions(){
        return this.itemDescriptions;
    }

    public void setGrabbables(String[] grabbables){
        this.grabbables = grabbables;
    }

    public String[] getGrabbables(){
        return this.grabbables;
    }


    public String toString(){
        String result = "\n";
        result += "Location: " + this.name + "\n";
        result += "\nYou see: ";

        for(int i = 0; i < items.length; i++){
            result += items[i] + " ";
        }
        result += "\nExits: ";

        for(String direction : exitDirections){
            result += direction + " ";
        }




        return result += "\n";
    }

}

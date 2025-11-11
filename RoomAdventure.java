/*
 * Room Adventure Game
 * Name: Lane Ingles
 * Github: 1PatentPending1
 * Repo Link: https://github.com/1PatentPending1/Room-Adventure-Java.git
 * Date: 11/7/25
 * Description: A simple text-based adventure game where players navigate through different rooms.
 * Additions: 
 * 
 * 
 * 
 * 
 * AI has been used to make the following quality of life changes to the initial template purely for my own convenience:
 * IMPORTANT: I AM NOT COUNTING ANY OF THESE AS MY ADDITIONS, I ONLY DID THEM FOR CODE AND RUN QUALITY.
 * 1. Combined Room and RoomAdventure into one file with a static nested class.
 * 2. Fixed input parsing to handle 1-word or >2-word entries.
 * 3. Added a 'quit' command to exit the game.
 * 4. Moved Scanner creation outside the game loop to save resources.
 * 5. Added printing of the 'status' message so the player sees feedback.
 * 6. Fixed inventory display to print on a single line.
 * 7. Improved 'handleTake' to remove the item from the room and check for a full inventory.
 * 8. Fixed exit logic in setupGame for rooms 3 and 4.
 * 9. Added null checks in Room.toString() to prevent crashes.
 */

import java.util.Scanner;

public class RoomAdventure {

    private static Room currentRoom;
    private static String[] inventory = {null, null, null, null, null}; // max 5 items
    private static String status = "Welcome to Room Adventure!"; // Initialize status

    final private static String DEFAULT_STATUS = "Sorry, I don't understand. Try 'go [direction]', 'look [item]', 'take [item]', or 'quit'.";

    // main function
    public static void main(String[] args) {
        setupGame();
        
        // Create Scanner once, outside the loop
        Scanner s = new Scanner(System.in);

        while (true) {
            // Print the status from the last command
            System.out.println("\n-------------------------------------------------");
            System.out.println(status);

            // Print current room information
            System.out.println(currentRoom.toString());
            
            // Print inventory on one line
            System.out.print("Inventory: [ ");
            for (int i = 0; i < inventory.length; i++) {
                if (inventory[i] != null) {
                    System.out.print(inventory[i] + " ");
                }
            }
            System.out.println("]");

            System.out.print("\nWhat would you like to do? > ");

            // taking input
            String input = s.nextLine().trim().toLowerCase(); // trim and lowercase
            String[] words = input.split(" ");

            // Reset status for this turn
            status = DEFAULT_STATUS;

            if (words.length == 1) {
                String verb = words[0];
                if (verb.equals("quit")) {
                    System.out.println("Thanks for playing!");
                    break; // Exit loop
                }
                // Handle single-word "look" to look at the room
                if (verb.equals("look")) {
                    status = ""; // Room info will be printed on next loop
                    continue;
                }
                status = "Please use two words, like 'go north' or 'take key'. (Or 'quit' to exit)";
                continue; // Go to top of loop
            }

            if (words.length == 2) {
                String verb = words[0];
                String noun = words[1];

                switch (verb) {
                    case "go":
                        handleGo(noun);
                        break;
                    case "look":
                        handleLook(noun);
                        break;
                    case "take":
                        handleTake(noun);
                        break;
                    // default: status is already set to DEFAULT_STATUS
                }
            } 
            // else: more than 2 words, status is already set to DEFAULT_STATUS
        }
        
        s.close(); // Close the scanner
    }

    private static Boolean hasItem(String itemName){
        // Win condition: player has 'key' and is in Room 4
        boolean hasItem = false;
        for (String item : inventory) {
            if (itemName.equals(item)) {
                hasItem = true;
                break;
                
            }
        }

        return hasItem;

    }


    private static void handleGo(String noun) {
    status = "I don't see an exit in that direction.";
    String[] directions = currentRoom.getExitDirections();
    Room[] destinations = currentRoom.getExitDestinations();

    for (int i = 0; i < directions.length; i++) {
        if (noun.equals(directions[i])) {
            Room destination = destinations[i];

            if (destination != null && destination.getName().equals("door")) {
                if (!hasItem("key")) {
                    status = "The strange door is locked. You need a key to enter.";
                    return;
                } else {
                    System.out.println("\nYou unlock the door with your key...\n The room is dark\n you step inside to find a light, but the door is no longer behind you... \n You are trapped...\n");
                    currentRoom = destination;
                    System.out.println(destination.toString());
                    System.out.println("\nIt seems there is no way out... Your fate is certain...\n");
                    System.out.println(" Program will now exit. Thank you for playing.");
                    System.exit(0);
                }
            }

            currentRoom = destination;
            status = "You go " + noun + ".";
            break;
        }
    }
}

    private static void handleLook(String noun) {
        status = "I don't see that item.";
        String[] items = currentRoom.getItems();
        String[] descriptions = currentRoom.getItemDescriptions();

        for (int i = 0; i < items.length; i++) {
            if (noun.equals(items[i])) {
                status = descriptions[i];
                break;
            }
        }
    }

    private static void handleTake(String noun) {
        status = "I can't grab that item.";
        String[] grabbables = currentRoom.getGrabbables();

        for (int i = 0; i < grabbables.length; i++) {
            // Check if the grabbable at this spot matches the noun
            if (grabbables[i] != null && noun.equals(grabbables[i])) {
                
                // Check for space in inventory
                boolean inventoryFull = true;
                for (int j = 0; j < inventory.length; j++) {
                    if (inventory[j] == null) {
                        // Found a spot! Add to inventory.
                        inventory[j] = noun;
                        status = "You took the " + noun + ".";
                        
                        // Remove item from room by setting it to null
                        grabbables[i] = null; 
                        
                        inventoryFull = false; // Mark that we found a spot
                        break; // Exit inventory loop
                    }
                }
                
                if (inventoryFull) {
                    status = "Your inventory is full.";
                }
                
                // Item was found, so we return to exit the method
                return; 
            }
        }
    }

    public static void setupGame() {

        Room room1 = new Room("Room 1");
        Room room2 = new Room("Room 2");
        Room room3 = new Room("Room 3");
        Room room4 = new Room("Room 4");
        Room room5 = new Room("door"); // Locked room

        // Room 1 setup: Exits North (R2), East (R3)
        String[] room1ExitDirections = {"north", "east"};
        Room[] room1ExitDestinations = {room2, room3};
        String[] room1Items = {"chair", "desk"};
        String[] room1ItemDescriptions = {"A wooden chair.", "A wooden desk. There is a key on the desk."};
        String[] room1Grabbables = {"key"};
        room1.setExitDirections(room1ExitDirections);
        room1.setExitDestinations(room1ExitDestinations);
        room1.setItems(room1Items);
        room1.setItemDescriptions(room1ItemDescriptions);
        room1.setGrabbables(room1Grabbables);

        // Room 2 setup: Exits South (R1), East (R4)
        String[] room2ExitDirections = {"south", "east"};
        Room[] room2ExitDestinations = {room1, room4};
        String[] room2Items = {"table", "lamp"};
        String[] room2ItemDescriptions = {"A small table.", "A bright lamp."};
        String[] room2Grabbables = {"lamp"};
        room2.setExitDirections(room2ExitDirections);
        room2.setExitDestinations(room2ExitDestinations);
        room2.setItems(room2Items);
        room2.setItemDescriptions(room2ItemDescriptions);
        room2.setGrabbables(room2Grabbables);

        // Room 3 setup: Exits West (R1), North (R4)
        String[] room3ExitDirections = {"west", "north", "door"}; // Added "door" as a direction for the locked exit
        Room[] room3ExitDestinations = {room1, room4, room5}; // Added door as a destination for the locked exit
        String[] room3Items = {"sofa", "bookshelf", "door"};
        String[] room3ItemDescriptions = {"A comfortable sofa.", "A tall bookshelf filled with books. One 'book' looks interesting.", "A strange door that you don't remember seeing before."};
        String[] room3Grabbables = {"book"};
        room3.setExitDirections(room3ExitDirections);
        room3.setExitDestinations(room3ExitDestinations);
        room3.setItems(room3Items);
        room3.setItemDescriptions(room3ItemDescriptions);
        room3.setGrabbables(room3Grabbables);

        // Room 4 setup: Exits West (R2), South (R3)
        String[] room4ExitDirections = {"west", "south"};
        Room[] room4ExitDestinations = {room2, room3}; // Fixed: West goes to room2, South to room3
        String[] room4Items = {"bed", "wardrobe"};
        String[] room4ItemDescriptions = {"A large bed.", "A wooden wardrobe."};
        String[] room4Grabbables = {"pillow"};
        room4.setExitDirections(room4ExitDirections);
        room4.setExitDestinations(room4ExitDestinations);
        room4.setItems(room4Items);
        room4.setItemDescriptions(room4ItemDescriptions);
        room4.setGrabbables(room4Grabbables);

        // DOOR room setup: No exits, just a win condition
        String[] room5ExitDirections = {"Void"};
        Room[] room5ExitDestinations = {room5}; // No exits
        String[] room5Items = {"Darkness"};
        String[] room5ItemDescriptions = {"The darkness slips through your fingers... You are overwhelmed with dread."};
        String[] room5Grabbables = {"nothing"};
        room5.setExitDirections(room5ExitDirections);
        room5.setExitDestinations(room5ExitDestinations);
        room5.setItems(room5Items);
        room5.setItemDescriptions(room5ItemDescriptions);
        room5.setGrabbables(room5Grabbables);

        currentRoom = room1; // Start game in Room 1
    }
    

    //
    // --- Static Nested Room Class ---
    //
    // By making this a 'static nested class', it can live inside the
    // RoomAdventure file but acts like a separate class.
    //
    static class Room {
        private String name;
        private String[] exitDirections; // north, south, east, west
        private Room[] exitDestinations;
        private String[] items;
        private String[] itemDescriptions;
        private String[] grabbables;

        //constructor
        public Room(String name) {
            this.name = name; // use this to set the name of the room
        }

        public String getName() {
            return this.name;
        }
        // methods getters and setters 
        public void setExitDirections(String[] exitDirections) {
            this.exitDirections = exitDirections;
        }

        public String[] getExitDirections() {
            return this.exitDirections;
        }

        public void setExitDestinations(Room[] exitDestinations) {
            this.exitDestinations = exitDestinations;
        }

        public Room[] getExitDestinations() {
            return this.exitDestinations;
        }

        public void setItems(String[] items) {
            this.items = items;
        }

        public String[] getItems() {
            return this.items;
        }

        public void setItemDescriptions(String[] itemDescriptions) {
            this.itemDescriptions = itemDescriptions;
        }

        public String[] getItemDescriptions() {
            return this.itemDescriptions;
        }

        public void setGrabbables(String[] grabbables) {
            this.grabbables = grabbables;
        }

        public String[] getGrabbables() {
            return this.grabbables;
        }

        public String toString() {
            String result = "";
            result += "Location: " + this.name + "\n";
            result += "You see: ";

            // Check for null items array
            if (this.items != null) {
                for (int i = 0; i < items.length; i++) {
                    result += items[i] + " ";
                }
            }

            result += "\nExits: ";
            
            // Check for null exitDirections array
            if (this.exitDirections != null) {
                for (String direction : exitDirections) {
                    result += direction + " ";
                }
            }

            return result += "\n";
        }
    }
}
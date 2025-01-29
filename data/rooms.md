# Rooms Data File Documentation

## Overview
The `rooms.json` file defines all rooms in the game world, their descriptions, connections, and contained objects (items and NPCs). This file provides the structure for navigation and interactions within the game environment.

---

## File Structure
The file is a JSON array, where each object represents a **room** with the following fields:

### Fields
| Field          | Type      | Description |
|---------------|-----------|-------------|
| `id`          | `string`  | Unique identifier for the room. |
| `name`        | `string`  | The display name of the room. |
| `longDesc`    | `string`  | A detailed description of the room when the player enters. |
| `shortDesc`   | `string`  | A brief description of the room when the player revisits. |
| `exits`       | `object`  | Defines available movement directions and their corresponding connected rooms. |
| `items`       | `array`   | A list of item IDs available in the room. |
| `npcs`        | `array`   | A list of NPC IDs currently present in the room. |
| `floor`       | `integer` | The floor level the room is on. |

---

## Example Entries

### **Basement Room**
```json
{
    "id": "basement",
    "name": "Damp Basement",
    "longDesc": "A cold, moldy basement with rotting wooden beams and an eerie presence.",
    "shortDesc": "Basement",
    "exits": {
        "up": "floor1_hall"
    },
    "items": [
        "knife",
        "family_photo"
    ],
    "npcs": [
        "bernard"
    ],
    "floor": 1
}
```
- **Connected to**: Floor 1 Hallway (`"up": "floor1_hall"`).
- **Contains Items**: `knife`, `family_photo`.
- **Contains NPC**: `bernard` (who may ask riddles).
- **Floor Level**: Basement (Floor 1).

### **Attic Room**
```json
{
    "id": "attic",
    "name": "Dusty Attic",
    "longDesc": "A shadowy attic with cobweb-covered furniture. A ghost lingers here.",
    "shortDesc": "Attic",
    "exits": {
        "down": "floor2_hall"
    },
    "items": [],
    "npcs": [
        "ghost"
    ],
    "floor": 4
}
```
- **Connected to**: Floor 2 Hallway (`"down": "floor2_hall"`).
- **Contains NPC**: `ghost` (who gives the player a quest).
- **No Items Initially**.
- **Floor Level**: Attic (Floor 4).

---

## Explanation of Key Fields

### `id`
- Must be unique for every room.
- Used in game logic to reference the room.

### `name`
- The room’s display name when the player enters.

### `longDesc`
- Detailed description displayed the first time a player enters the room.

### `shortDesc`
- A brief description shown when the player revisits the room.

### `exits`
- Defines the **available movement directions** and their corresponding room connections.
- Standard directions: `north`, `south`, `east`, `west`, `up`, `down`.

### `items`
- A list of **item IDs** that can be found in the room.
- Items may be **collected** by the player if `isPortable` is `true` in `items.json`.

### `npcs`
- A list of **NPC IDs** present in the room.
- NPCs can interact with the player based on their `behavior` (defined in `npcs.json`).

### `floor`
- The floor level the room is on.
- Important for tracking game mechanics like **house shuffling**.

---

## Common Use Cases

### Moving Between Rooms
**Example:** The player moves **up** from the **basement** to the **floor 1 hallway**.
```json
"exits": {
    "up": "floor1_hall"
}
```
**Game Output:**
> _You climb the creaky stairs to the hallway._

---

### Picking Up an Item
**Example:** The player picks up the **knife** from the **basement**.
```json
"items": [
    "knife"
]
```
**Game Output:**
> _You pick up the knife._

---

### Interacting with an NPC
**Example:** The player meets **Bernard** in the **basement**, who asks a riddle.
```json
"npcs": [
    "bernard"
]
```
**Game Output:**
> _Bernard smirks. "What has to be broken before you can use it?"_

---

## Integration
- **Rooms are referenced in `npcs.json`** to track NPC locations.
- **Items inside rooms are referenced in `items.json`** to determine availability.
- **Player movement updates `GameState`** based on available exits.

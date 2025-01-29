# Verbs Data File Documentation

## Overview
The `verbs.json` file defines all possible actions (commands) that a player can perform in the game. These verbs dictate how players interact with items, NPCs, and the environment. Each verb includes syntax, required conditions, effects, and failure messages.

---

## File Structure
The file is a JSON object where each key represents a **verb** and its associated attributes.

### Fields
| Field          | Type      | Description |
|---------------|-----------|-------------|
| `description` | `string`  | A brief explanation of the verb’s purpose. |
| `syntax`      | `string`  | The expected structure of the command. |
| `requires`    | `object`  | (Optional) Conditions that must be met before execution. |
| `effects`     | `object`  | The action triggered by the verb. |
| `failureMessages` | `object`  | Messages shown when the verb cannot be executed. |

---

## Example Entries

### **Take Command**
```json
{
    "take": {
        "description": "Pick up an item.",
        "syntax": "take {item}",
        "requires": {
            "targetType": "item",
            "isPortable": true
        },
        "effects": {
            "action": "add_to_inventory"
        },
        "failureMessages": {
            "notFound": "You don't see that here.",
            "notPortable": "You can't pick that up."
        }
    }
}
```
- **Requires**: The target must be an item, and it must be portable.
- **Effects**: Adds the item to the player’s inventory.
- **Failure Messages**: Shown when conditions are not met.

---

### **Give Command**
```json
{
    "give": {
        "description": "Offer an item to a character.",
        "syntax": "give {item} to {character}",
        "requires": {
            "targetType": "item",
            "characterPresent": true
        },
        "effects": {
            "action": "trigger_npc_response"
        },
        "failureMessages": {
            "notInInventory": "You don't have that item.",
            "characterNotPresent": "There's no one here to give that to."
        }
    }
}
```
- **Requires**: The item must be in the player’s inventory, and the character must be present.
- **Effects**: The NPC reacts accordingly.
- **Failure Messages**: Displayed if conditions are not met.

---

### **Go Command**
```json
{
    "go": {
        "description": "Move to a connected room.",
        "syntax": "go {direction}",
        "requires": {
            "targetType": "direction",
            "validExit": true
        },
        "effects": {
            "action": "change_room"
        },
        "failureMessages": {
            "noExit": "You can't go that way."
        }
    }
}
```
- **Requires**: The direction must be a valid exit.
- **Effects**: Moves the player to the new room.
- **Failure Messages**: Shown when no valid exit exists.

---

## Explanation of Key Fields

### `description`
- Provides an overview of what the command does.

### `syntax`
- Defines the expected structure of the player’s input.

### `requires`
- Specifies conditions that must be met before executing the verb.
  - `targetType`: Ensures the correct type of object (e.g., item, character, direction).
  - `isPortable`: Checks if an item can be taken.
  - `validExit`: Ensures the player is moving toward a valid room exit.

### `effects`
- Defines the in-game action performed when the verb is executed.
- Common actions include:
  - `add_to_inventory`: Adds an item to the player's inventory.
  - `trigger_npc_response`: Triggers an NPC’s dialogue or reaction.
  - `change_room`: Moves the player to another room.

### `failureMessages`
- Specifies error messages when the command cannot be performed.

---

## Integration
- **Commands are parsed in `GameEngine.java`** to check if the player's input matches any defined verb.
- **Effects are executed based on `effects` data**.
- **Failure messages provide feedback to the player**.

---

## Common Use Cases

### Picking Up an Item
**Example Input:** `take pendant`

**JSON Reference:**
```json
"take": "You pick up the pendant."
```
**Game Output:**
> _You pick up the pendant._

---

### Moving Between Rooms
**Example Input:** `go north`

**JSON Reference:**
```json
"go": "You move north into the dark hallway."
```
**Game Output:**
> _You move north into the dark hallway._

---
# NPCs Data File Documentation

## Overview
The `npcs.json` file defines all **non-player characters (NPCs)** in the game. Each NPC has unique behaviors, interactions, and dialogue. The file allows for dynamic responses based on player actions, such as answering riddles or presenting items.

---

## File Structure
The file is a JSON array, where each object represents an NPC with the following fields:

### Fields
| Field          | Type      | Description |
|---------------|-----------|-------------|
| `id`         | `string`  | Unique identifier for the NPC. |
| `name`       | `string`  | The display name of the NPC. |
| `startRoom`  | `string`  | The initial room where the NPC is located. |
| `behavior`   | `object`  | Defines the NPC’s behavior, including interactions and dialogue. |

---

## Example Entries

### **Bernard – Riddle Master**
```json
{
    "id": "bernard",
    "name": "Bernard",
    "startRoom": "basement",
    "behavior": {
        "type": "riddle",
        "riddles": [
            {
                "question": "What has to be broken before you can use it?",
                "answers": ["egg"],
                "onSuccess": {
                    "action": "give_hint",
                    "message": "Bernard says: 'Upstairs, the answer lies.'"
                },
                "onFail": {
                    "action": "increment_attempt"
                }
            }
        ],
        "maxAttempts": 3,
        "onMaxAttempts": {
            "action": "shuffle_floors",
            "message": "Bernard laughs as the house shifts around you."
        }
    }
}
```
- **Behavior Type**: Riddle-based interaction.
- **Riddles**: Bernard asks a riddle and validates the player's answer.
- **Failure Consequences**: After three incorrect answers, the house shifts.

### **The Ghost – Quest Giver**
```json
{
    "id": "ghost",
    "name": "The Ghost",
    "startRoom": "attic",
    "behavior": {
        "type": "quest",
        "objective": "Find a sentimental item.",
        "dialogue": [
            {
                "trigger": "present_correct_item",
                "message": "Thank you. This item meant everything to her."
            },
            {
                "trigger": "present_wrong_item",
                "message": "This is not what I need."
            }
        ]
    }
}
```
- **Behavior Type**: Quest-related interaction.
- **Objective**: The player must find the ghost’s wife’s pendant.
- **Item Interaction**: If the correct item is given, the ghost provides closure.

---

## Explanation of Key Fields

### `id`
- Must be unique for each NPC.
- Used in game logic to reference interactions.

### `name`
- The NPC’s display name in the game.

### `startRoom`
- Defines the room where the NPC starts.
- NPCs may move dynamically during the game.

### `behavior`
- Defines how an NPC interacts with the player.
- **Types of Behaviors:**
  - `riddle`: NPC asks riddles and tracks attempts.
  - `quest`: NPC provides objectives and responds to item offerings.
  - Future expansion: `merchant`, `guardian`, etc.

### `riddles` (For NPCs like Bernard)
- A list of riddle questions and their acceptable answers.
- **`onSuccess`**: What happens when the player answers correctly.
- **`onFail`**: What happens when the player answers incorrectly.
- **`onMaxAttempts`**: Event triggered if the player fails too many times.

### `dialogue` (For NPCs like The Ghost)
- A set of responses triggered by player actions.
- **`trigger`** defines the action that causes the response (e.g., `present_correct_item`).

---

## Integration
- NPCs are referenced in `rooms.json` to place them in locations.
- The `GameEngine` processes `behavior` to execute interactions dynamically.

---

## Common Use Cases

### Answering a Riddle
```json
{
    "question": "What has keys but can't open locks?",
    "answers": ["piano", "typewriter"],
    "onSuccess": {
        "action": "give_hint"
    },
    "onFail": {
        "action": "increment_attempt"
    }
}
```
**Game Output:**
> Bernard asks: "What has keys but can't open locks?"

Player input: `piano`

> _Correct! Bernard nods approvingly._

---

### Completing a Quest
```json
{
    "trigger": "present_correct_item",
    "message": "Thank you. This pendant meant everything to her."
}
```
**Game Output:**
> The ghost smiles as he fades away…

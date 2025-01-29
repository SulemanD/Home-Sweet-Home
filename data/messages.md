# Messages Data File Documentation

## Overview
The `messages.json` file defines all system messages and responses used in the game. These messages are dynamically referenced to provide feedback to the player during interactions, such as taking items, answering riddles, or triggering events.

---

## File Structure
The file is a JSON object where each key represents a **message identifier**, and the value is the actual message text displayed to the player.

### Example Structure
```json
{
    "take_success": "You pick up the {item}.",
    "take_fail": "You can't take that.",
    "riddle_question": "Bernard asks: \"{question}\"",
    "riddle_correct": "Correct! Bernard nods approvingly.",
    "riddle_incorrect": "Wrong answer! Bernard smirks.",
    "floor_shuffled": "The house groans and shifts... the rooms are no longer the same!"
}
```

---

## Explanation of Key Messages

| Identifier          | Message Example | Description |
|---------------------|----------------|-------------|
| `take_success` | "You pick up the pendant." | Displayed when a player successfully takes an item. `{item}` is replaced dynamically. |
| `take_fail` | "You can't take that." | Shown when the player attempts to take a non-portable item. |
| `riddle_question` | "Bernard asks: \"What has keys but can’t open locks?\"" | Displays a riddle from Bernard. `{question}` is replaced dynamically. |
| `riddle_correct` | "Correct! Bernard nods approvingly." | Displayed when the player answers a riddle correctly. |
| `riddle_incorrect` | "Wrong answer! Bernard smirks." | Displayed when the player answers a riddle incorrectly. |
| `floor_shuffled` | "The house groans and shifts... the rooms are no longer the same!" | Triggered when Bernard's riddle penalty is activated. |

---

## Dynamic Placeholders
Certain messages include **placeholders** that dynamically change based on game context:
- `{item}` → Replaced with the actual item name (e.g., "pendant").
- `{question}` → Replaced with the riddle text presented by Bernard.

Example Message Processing:
```json
{
    "take_success": "You pick up the {item}."
}
```
If the player takes a **knife**, the game replaces `{item}`:
> **Output:** "You pick up the knife."

---

## Integration
- These messages are used in `GameEngine.java` to provide feedback.
- Placeholders are dynamically replaced before displaying the message.

---

## Common Use Cases

### When a Player Takes an Item
**JSON Reference:**
```json
"take_success": "You pick up the {item}."
```
**Game Output:**
> _You pick up the pendant._

### When a Player Answers a Riddle
**JSON Reference:**
```json
"riddle_correct": "Correct! Bernard nods approvingly."
```
**Game Output:**
> _Correct! Bernard nods approvingly._

### When the House Shuffles (Penalty Event)
**JSON Reference:**
```json
"floor_shuffled": "The house groans and shifts... the rooms are no longer the same!"
```
**Game Output:**
> _The house groans and shifts... the rooms are no longer the same!_

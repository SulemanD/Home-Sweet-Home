# Items Data File Documentation

## Overview
The `items.json` file defines all items in the game world, their properties, and their interactions. Each item entry includes details about its description, portability, and effects when used. This file enables a dynamic and scalable structure for item management within the game.

---

### Fields

| Field         | Type      | Description                                                                 |
|---------------|-----------|-----------------------------------------------------------------------------|
| `id`          | `string`  | A unique identifier for the item.                                          |
| `name`        | `string`  | The display name of the item shown to the player.                          |
| `description` | `string`  | A detailed description of the item.                                        |
| `isPortable`  | `boolean` | Specifies if the item can be picked up and added to the player's inventory. |
| `useEffect`   | `object`  | (Optional) Defines the effect triggered when the item is used.             |

---

## Example Entries

### Portable Item with `useEffect`
```json
{
    "id": "pendant",
    "name": "Pendant",
    "description": "A heart-shaped pendant, intricately designed with an engraving that reads 'Forever yours.'",
    "isPortable": true,
    "useEffect": {
        "action": "offer_to_ghost",
        "message": "The ghost's eyes light up as he sees the pendant."
    }
}
```
- **Portable**: Can be picked up by the player.
- **Use Effect**: When offered to the ghost, triggers the action `offer_to_ghost` with a message.

### Non-Portable Item
***Note: We do not have any related items but to show the use case*** 
```json
{
    "id": "grandfather_clock",
    "name": "Grandfather Clock",
    "description": "An old wooden clock, its pendulum swinging slowly.",
    "isPortable": false
}
```
- **Non-Portable**: Cannot be picked up or moved.
- **Use Effect**: Not defined; the item is fixed in place.

---

## Explanation of Key Fields

### `id`
- Must be unique for every item.
- Used to reference the item in other parts of the game (e.g., rooms, NPCs).

### `name`
- The name shown to the player when they interact with the item.

### `description`
- Provides a narrative or contextual detail about the item.
- Displayed when the player examines the item.

### `isPortable`
- **true**: The item can be added to the player's inventory using the `take` command.
- **false**: The item remains fixed in its location.
***Note: We only have portable items used in our game.***

### `useEffect`
- Optional field that defines what happens when the item is used.
- Contains the following subfields:
  - `action`: The specific game logic triggered (e.g., `offer_to_ghost`, `unlock_door`).
  - `target` (optional): The entity or object affected by the action (e.g., a door).
  - `message` (optional): Feedback shown to the player when the effect is triggered.

---

## Common Use Cases

### Unlocking a Door
```json
{
    "id": "rusty_key",
    "name": "Rusty Key",
    "description": "An old iron key covered in rust.",
    "isPortable": true,
    "useEffect": {
        "action": "unlock_door",
        "target": "attic_door",
        "message": "You hear a click as the attic door unlocks."
    }
}
```

### Providing a Hint
```json
{
    "id": "old_journal",
    "name": "Old Journal",
    "description": "A dusty journal with faded handwriting.",
    "isPortable": true,
    "useEffect": {
        "action": "reveal_hint",
        "message": "The journal entry reads: 'She loved roses...'"
    }
}

```

---

## Integration
- Items are referenced in the `rooms.json` file to specify their initial locations.
- The `useEffect` field integrates with game logic in the `GameEngine` to trigger effects dynamically.

---

## Notes
- Ensure all `id` values are unique to avoid conflicts.
- Test items with `useEffect` thoroughly to verify they trigger the correct game actions.


# GitHub Copilot Instructions - Virtual Ecosystem Simulator

You are an expert Java developer specializing in Object-Oriented Design and JavaFX applications. 
You are assisting with a grid-based virtual ecosystem simulation containing Plants (P), Herbivores (H), and Carnivores (C).

## 1. Project Architecture & Core Classes
Always respect the existing class hierarchy and encapsulation rules:
- `Organism` (Abstract): Base model. Tracks `id`, `energy`, `location`, and a reference to `Board`. Contains static counters: `nextId` and `populationCount`.
- `Animal` (Abstract extends `Organism`): Implements movement and eating logic. Has primitive boolean flags: `ifMoveToday`, `ifEatToday`.
- `Herbivore` / `Carnivore` (Concrete extends `Animal`): Only contain constructors setting initial energy and names ('H' and 'C').
- `Plant` (Concrete extends `Organism`): Gains energy daily (`addEnergy(10)`). Name is 'P'.
- `Board`: Manages the simulation state via a 2D array `Organism[][]`. It also acts as a bridge containing the JavaFX `GridPane` and `Cell[][]`.
- `Cell` (Extends JavaFX `Pane`): Represents a single visual cell. Empty cells use `id = -1` as a sentinel value.

## 2. Simulation Logic & Custom Turn Rules
When writing or refactoring simulation logic, strictly adhere to these customized behavioral rules:
- **Daily Cycle (`simulateDay()`):** Iterates through the board grid using a nested loop (`r` and `c`).
    - To prevent animals from acting twice in the same day (due to moving into a cell the loop hasn't scanned yet), always check `if (animal.ifMoveToday || animal.ifEatToday)` at the start of their turn and skip them if true.
- **The Eating Rule (`eat()`):**
    - Every animal gets exactly **one attempt** to eat and move per day.
    - Set `this.ifEatToday = true` immediately at the start of the `eat()` method, regardless of whether it actually finds food or not.
- **The Movement Rule (`move()`):**
    - Animals attempt to move in the same direction as their last move (`previous_direction`). If blocked, they search other directions in a circular sequence using modulo arithmetic (`(startIndex + attempts) % 4`).
    - Set `this.ifMoveToday = true` when a move action is triggered.
- **Organism Removal & Population Safety:**
    - Whenever an organism is removed from the board (either due to being eaten or dying from starvation with `energy <= 0`), always invoke the static method `Organism.decrementPopulation()` exactly once.
    - After decrementing, explicitly set its position in the model grid to `null` (`organism_array[x][y] = null`) to fully release it from memory.

## 3. Coding Standards & Conventions
- **Encapsulation:** Always use explicit getters and setters (`getLocation()`, `setLocation()`, `getEnergy()`, `addEnergy(delta)`). Never access fields directly.
- **State Cleanliness:** Always update the model (`Organism[][]`) first, and then synchronize the View (`Cell[][]`) via the board's helper method `fromBoardToGrid()`.
- **JavaFX Thread Safety & View Updates:**
    - Never allow methods inside model or animal classes (`Organism`, `Animal`, etc.) to directly manipulate JavaFX `Cell` properties or update the `GridPane`.
    - All graphic updates must be driven top-down by the `Board` class via `fromBoardToGrid()` after the day's simulation logic has fully processed.
- **Code Readability:** Always include comments explaining the purpose of complex logic, especially around movement and eating rules. Use meaningful variable names to enhance clarity.

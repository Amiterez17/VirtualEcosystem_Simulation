# 🌍 Virtual Ecosystem Simulation

Developed an interactive ecosystem simulator with a graphical user interface (GUI) using JavaFX, featuring event-driven programming, dynamic board visualization, and user controls for real-time simulation management.

The simulation models synchronized real-time interactions between distinct organisms within a rectangular board environment, separating the model logic from the view layer.

---

## 🚀 Key Features

* **Dynamic Grid Visualization:** A JavaFX-based visual interface tracking organisms day by day.
* **Event-Driven UI Control:** Real-time user input checking to prevent bad data configurations before initialization.
* **Organism Hierarchy & Interactions:**
    * **Plants (P):** Static organisms that generate energy daily.
    * **Herbivores (H):** Mobile animals that search for adjacent plants to consume and gain energy.
    * **Carnivores (C):** Mobile predators that hunt herbivores based on proximity rules.
* **Smart Movement Engine:** Animals navigate the board dynamically using a daily directional logic (defaulting Down, rotating clockwise upon obstacles) while maintaining state synchronization.
* **Robust Core Logic:** Handles manual energy depletion, specialized eating sequences, and proper population management during starvation or predation.

---

## 📂 Project Structure

```text
📦 VirtualEcoSimulator
 ┣ 📂 src               # Clean Java source files (Model-View architecture)
 ┃ ┣ 📄 Animal.java
 ┃ ┣ 📄 Board.java
 ┃ ┣ 📄 Carnivore.java
 ┃ ┣ 📄 Cell.java
 ┃ ┣ 📄 Grid.java
 ┃ ┣ 📄 Herbivore.java
 ┃ ┣ 📄 I_AnimalAction.java
 ┃ ┣ 📄 Main.java
 ┃ ┣ 📄 Organism.java
 ┃ ┣ 📄 Plant.java
 ┃ ┗ 📄 Simulator.java
 ┣ 📂 resources         
 ┃ ┗ 📂 image           # Organism graphics (.jpg / .png icons)
 ┗ 📄 README.md         # Documentation
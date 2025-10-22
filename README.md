# Java Snake Game 

Simple snake game built in Java using Swing and AWT.

Features:
- Real-time snake movement using keyboard and arrow key input
- Apple spawn locations are randomized
- Snake grows longer after each apple
- Speed increases after each apple
- Wall and self-collision detection
- Simple score and tick per second HUD

Concepts:
- Game looping using 'javax.swing.Timer'
- Real-time rendering with 'paintComponent(Graphics g)'
- Keyboard input with 'InputMap' and 'ActionMap'
- Snake body stored as 'List<Point>'

How to Run:
1. Clone repository
   ```bash
   git clone https://github.com/<your-username>/snake-game-java.git
2. Compile and run:
   ```bash
   javac Main.java GamePanel.java
   java Main

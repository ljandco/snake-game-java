import javax.swing.*;      //GUI components (JFrame, JPanel, JLabel, Timer)
import java.awt.*;        // Basic drawing tools and colors (Graphics, Color, Point)
import java.awt.event.*; //Input from keyboard, mouse, etc.

// Entry point for the game
public class Main {

  public static void main(String[] args) {

    // Create the main window (outer frame)
    JFrame window = new JFrame("Snake");

    // This is overridden by getPreferredSize() if you call pack()
    window.setSize(500, 500);

    // Closes the program when you click X
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Creates the actual game panel (inner frame)
    GamePanel area = new GamePanel(); // Where logic and drawing happens

    // Add GamePanel to JFrame's content area
    window.add(area);

    // Resizes JFrame to fit GamePanel's preferred size
    window.pack();

    // Display the window
    window.setVisible(true);

    // Centers the window to the screen
    window.setLocationRelativeTo(null);
  }
}

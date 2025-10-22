/*This is the engine room, the mechanics of the game Snake.
The panel owns the game state: snake body, apple, score, timing and input.
Swing calls paintComponent(...) to draw a frame. Time drives updates.*/

import java.util.*;
import java.util.List;
import javax.swing.*;       // Swing UI (JPanel, Timer, KeyStroke etc.
import java.awt.*;          // Graphics, Color, Point, Dimensions
import java.awt.event.*;    // ActionEvent: input from keyboard, mouse, etc.
import javax.swing.Timer;   // A timer that is GUI friendly (not .util.Timer)

// This is where the game lives. Main creates a JFrame and adds this panel to it.
public class GamePanel extends JPanel {

  // Snake body
  // Store every segment position (grid). Head at index 0.
  List<Point> body = new ArrayList<>();
  int appleX, appleY;       // Declare apple position as a variable
  int cell = 20;            // Size of one grid cell, snake moves one cell per tick
  int dx = 1, dy = 0;       // Direction of movement in steps (dx, dy)
  Timer clock;              // Heartbeat of the game, fires ever 'delayMs' milliseconds

  //HUD and pacing
  int score = 0;            // Declare Apples eaten, start at 0
  int delayMs = 150;        // Tick delay, lower is faster
  final int MIN_DELAY = 70;
  final int SPEED_STEP = 5;

  //Constructor: the setup for everything
  public GamePanel() {
    setBackground(Color.BLACK); // Changes background color
    setFocusable(true);         // Allows key input without clicking the panel
    int sx = 5 * cell;          // Spawn location of snake
    int sy = 5 * cell;

    //Make the snake in 3-segments
    body.clear();
    body.add(new Point(sx, sy));               // Head
    body.add(new Point(sx - cell, sy));     // Body
    body.add(new Point(sx - 2 * cell, sy)); // Tail

    // Place first apple
    placeApple();

    //Game loop: every delayMs advances the game one step, then repaint
    clock = new Timer(delayMs, e -> {
      update();       // Move snake, handle collision, eat apple
      repaint();      // Draw the new panel, grow snake and apple in new location
    });
    clock.start();

    // Direction controls: each direction needs this block repeated (safer than KeyListener)
    // Each key press allows for a new (dx, dy), a new direction.
    // If the direction is the exact opposite, it is rejected, ignore input to prevent 180 turns

    // RIGHT: (1,0)
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
    getActionMap().put("moveRight", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        int newDx = 1, newDy = 0;
        if (!(newDx == -dx && newDy == -dy)) {
          dx = newDx;
          dy = newDy;
        }
      }
    });

    // LEFT: (-1,0)
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
    getActionMap().put("moveLeft", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        int newDx = -1, newDy = 0;
        if (!(newDx == -dx && newDy == -dy)) {
          dx = newDx;
          dy = newDy;
        }
      }
    });

    // UP: (0,-1) in screen coordinates, Y grows downwards, that's why it's -1
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
    getActionMap().put("moveUp", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        int newDx = 0, newDy = -1;
        if (!(newDx == -dx && newDy == -dy)) {
          dx = newDx;
          dy = newDy;
        }
      }
    });

    // DOWN: (0,1)
    getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
    getActionMap().put("moveDown", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        int newDx = 0, newDy = 1;
        if (!(newDx == -dx && newDy == -dy)) {
          dx = newDx;
          dy = newDy;
        }
      }
    });
  }

  // One tick
  void update() {

    // Location of new head after moving one cell
    Point head = body.get(0);
    int nextX = head.x + dx * cell;
    int nextY = head.y + dy * cell;

    // Collision of snake head into a wall, reset game
    int maxX = getWidth() - cell;
    int maxY = getHeight() - cell;

    if (nextX < 0 || nextX >maxX || nextY < 0 || nextY > maxY) {
      resetSnakeBody();
      return;       // Skip ticks after resetting
    }

    // Self collision, reset game if head hits body or tail
    for (Point seg : body) {
      if (seg.x == nextX && seg.y == nextY) {
        resetSnakeBody();
        return;
      }
    }

    // Add new head at front
    body.add(0, new Point(nextX, nextY));

    // Check if apple was eaten. If eaten, grow, speed up, replace apple
    if (nextX == appleX && nextY == appleY) {
      score += 1;
      delayMs = Math.max(MIN_DELAY, delayMs - SPEED_STEP); // Faster with a floor
      clock.setDelay(delayMs);
      placeApple();       // Do not remove tail -> length + 1
    } else {
      body.remove(body.size() - 1);
    }
  }

  // Apple placement
  void placeApple() {
    // Use component size when available; fall back pre-layout
    int w = getWidth();
    int h = getHeight();
    if (w == 0 || h == 0) {       // Matches getPreferredSize()
      w = 500;
      h = 500;
    }
      int cols = w / cell;
      int rows = h / cell;

      // Keeps picking random cells not occupied by the snake
      while (true) {
        int col = (int) (Math.random() * cols);
        int row = (int) (Math.random() * rows);
        int ax = col * cell;
        int ay = row * cell;

        boolean onSnake = false;
        for (Point seg : body) {
          if (seg.x == ax && seg.y == ay) {
            onSnake = true;
            break;
          }
        }

          if (!onSnake) {
            appleX = ax;
            appleY = ay;
            return;
          }
        }
      }

  // Reset snake, HUD, and speed
  void resetSnakeBody() {
    body.clear();
    int snakeX = 5 * cell;
    int snakeY = 5 * cell;
    body.add(new Point(snakeX, snakeY));
    body.add(new Point(snakeX - cell, snakeY));
    body.add(new Point(snakeX - 2 * cell, snakeY));
    dx = 1;                     // Start moving right
    dy = 0;
    delayMs = 150;              // Reset speed
    clock.setDelay(delayMs);    // Apply new delay to timer
    score = 0;                  // Reset score
    placeApple();               // New apple spawn
  }

  // How big we want the frame, lets JFrame.pack() size it correctly
  @Override public Dimension getPreferredSize() {
    return new Dimension(500,500);
  }

  // Stylistic attributes
  @Override public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Snake color for all segments
    g.setColor(Color.RED);
    for (Point p : body) {
      g.fillRect(p.x, p.y, cell, cell);
    }

    // Apple color
    g.setColor(Color.ORANGE);
    g.fillRect(appleX, appleY, cell, cell);

    // HUD color (score and ticks per second)
    g.setColor(Color.WHITE);
    g.drawString("Score: " + score + " Speed " + (1000/delayMs) + " tps", 10, 15);
    g.setColor(Color.RED);
    }
  }
//import application.boardView.GraphicalInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;


/**
 * The main class for the 3D engine. 
 * Implements runnable provides instances are intended to be executed by a thread.
 * For object that wish to execute code while they are active. 
 * 
 * @author LY
 */
public class GameRenderer extends JPanel implements Runnable {

  /**
   * The serializable class needs this.
   */
  private static final long serialVersionUID = 1L;

  private JFrame frame;

  // for showing the row number and the col number for the board
  private static int ROWS = 15;
  private static int COLS = 15;
  // for executing in thread
  private Thread thread;
  // for storing the state of the game 
  private boolean running;
  // the buffered image is the image displayed to user
  private BufferedImage image;
  // pixels represents the pixels in the image 
  private int[] pixels;
  // player representation
  private PlayerView player;
  // canvas for display
  private Canvas canvas;
  // a list stores the pictures
  private ArrayList<Picture> pictures;

  // a game board linked to 2D board
  //private GraphicalInterface gameInterf;

  /** Board of the game, 0 represents corridor, 1 represents wall, 2 represents room.*/
  /**
   * 0 represents room door, 4 represents final door, 5 represents winImage
   */
  private int[][] board = {{1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
          {2, 0, 0, 2, 1, 1, 1, 0, 1, 1, 1, 1, 1, 5, 1},
          {2, 0, 2, 1, 1, 1, 1, 0, 1, 1, 1, 0, 4, 0, 5},
          {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 5, 1},
          {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
          {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 2, 2, 1, 1},
          {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 2, 0, 0, 2, 1},
          {1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 2, 1},
          {1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 2, 0, 0, 2, 1},
          {1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 1, 1},
          {1, 0, 1, 1, 1, 0, 1, 0, 1, 2, 0, 2, 1, 1, 1},
          {2, 0, 2, 1, 1, 0, 1, 0, 0, 0, 0, 2, 1, 1, 1},
          {2, 0, 0, 2, 1, 0, 1, 0, 1, 2, 0, 2, 1, 1, 1},
          {2, 0, 0, 2, 1, 0, 0, 0, 1, 1, 2, 1, 1, 1, 1},
          {1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},};


  /**
   * Constructor of GameRenderer.
   */
  public GameRenderer() {

    this.frame = new JFrame();
    frame.add(this);
    // create new thread
    thread = new Thread(this);
    // set the buffered image with certain size and make its type to be RGB
    image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
    // add pictures
    pictures = new ArrayList<Picture>();
    pictures.add(Picture.corridorWall);
    pictures.add(Picture.roomWall);
    pictures.add(Picture.roomDoor);
    pictures.add(Picture.finalDoor);
    pictures.add(Picture.winImage);
    //setup player
    player = new PlayerView(1.5, 7.5, 1, 0, 0, -0.67);
    addKeyListener(player);
    // setup canvas
    canvas = new Canvas(board, pictures, ROWS, COLS, 640, 480);

    // connecting image and pixels 
    pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    // setting up the Frame
    frame.setTitle("3D Engine");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(640, 480);
    frame.setBackground(Color.black);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    startGame();
  }

  /**
   * The main method of GameRenderer, for creating new instance.
   *
   * @param args
   */
  public static void main(String[] args) {
    GameRenderer game = new GameRenderer();
  }

  /**
   * Start the render, automatically call the run method.
   * called by constructor
   */
  private synchronized void startGame() {
    running = true;
    thread.start();
  }

  /**
   * Stop the render.
   */
  private synchronized void stopGame() {
    running = false;
    try {
      //wait this thread until it complete
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * The run method handles the program updated.
   * this run method need to be implemented from Runnable interface.
   */
  @Override
  public void run() {

    double lastTime = System.nanoTime();
    // update 60 times per second
    final double frequency = 1000000000.0 / 60.0;
    double timeGap = 0;
    requestFocus();
    // this while loop is for updating screen
    while (running) {
      double nowTime = System.nanoTime();
      // update only happen when time reach to 1/60 second
      timeGap += (nowTime - lastTime) / frequency;
      lastTime = nowTime;
      while (timeGap >= 1) {
        canvas.updateCanvas(player, pixels);
        player.updatePlayerView(board);

        //this.gameInterf.getGame().getPlayer().setPos((int)player.getX(), (int)player.getY());

        timeGap--;
        // call paintComponent method to repaint the updated screen
      }
      repaint();
    }
  }

  /*Methods that can be used to link with the game board.*/

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
  }

  /**
   * return player object to game world class, for getting player's position
   * and player's direction.
   *
   * @return PlayerView
   */
  public PlayerView getPlayer() {
    return this.player;
  }

  /**
   * for updating rendered board.
   *
   * @param row
   * @param col
   * @param value
   */
  public void setBoard(int row, int col, int value) {
    this.board[row][col] = value;
  }

  /**
   * return the screen show to the user.
   *
   * @return bufferedImage
   */
  public BufferedImage getScreen() {
    return this.image;
  }

  //public void setGameInterf(GraphicalInterface gi) {
  //  this.gameInterf = gi;
  //}
  
}

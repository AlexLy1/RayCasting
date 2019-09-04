import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class represents the first player view in the game.
 * It keeps tracking player's location and does some updates
 * @author LY
 */
public class PlayerView implements KeyListener {

  // speed for player moving
  private final double speed = 0.07;
  // rotating speed for player rotation
  private final double rotate = 0.06;
  // player's position on the 2D board
  // using double value here is for smoothly move
  // integer positions on board is just for wall detection
  private double posX;
  private double posY;
  /**
   * x,y directions and x, y planes define the player's view.
   **/
  // x and y directions of player's facing direction
  private double dirX;
  private double dirY;
  // x and y planes which are perpendicular to x and y directions
  private double planeX;
  private double planeY;
  // boolean fields are used to track key pressed
  private boolean left;
  private boolean right;
  private boolean forward;
  private boolean backward;

  /**
   * Constructor of class PlayerView.
   *
   * @param px
   * @param py
   * @param dx
   * @param dy
   * @param plx
   * @param ply
   */
  public PlayerView(double px, double py, double dx, double dy, double plx, double ply) {
    this.posX = px;
    this.posY = py;
    this.dirX = dx;
    this.dirY = dy;
    this.planeX = plx;
    this.planeY = ply;
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyChar() == 'w') {
      this.forward = true;
    } else if (e.getKeyChar() == 's') {
      this.backward = true;
    } else if (e.getKeyChar() == 'a') {
      this.left = true;
    } else if (e.getKeyChar() == 'd') {
      this.right = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyChar() == 'w') {
      this.forward = false;
    } else if (e.getKeyChar() == 's') {
      this.backward = false;
    } else if (e.getKeyChar() == 'a') {
      this.left = false;
    } else if (e.getKeyChar() == 'd') {
      this.right = false;
    }
  }

  /**
   * This update method updates the player's position according to key pressed by user.
   * The method is called by run method in GameRenderer class and this method can detect
   * whether the move is available or not.
   *
   * @param board
   */
  public void updatePlayerView(int[][] board) {
    // if user presses forward button

    if (-0.9994 <= dirX && dirX <= -0.6878
            && -0.4244 <= dirY && dirY <= 0.4034) {
      //System.out.println("North!");
    }
    if (-0.51928 <= dirX && dirX <= 0.65484
            && -0.99854 <= dirY && dirY <= -0.56857) {
      //System.out.println("West!");
    }
    if (0.86623 <= dirX && dirX <= 0.99705
            && -0.254001 <= dirY && dirY <= 0.279415) {
      //System.out.println("South!");
    }
    if (-0.326402 <= dirX && dirX <= 0.99705
            && 0.806617 <= dirY && dirY <= 0.999475) {
      //System.out.println("East!");
    }

    if (this.forward) {
      if (board[(int) (posX + dirX * speed)][(int) posY] == 0) {
        // if the x direction movement is available
        posX += dirX * speed;
      }
      if (board[(int) posX][(int) (posY + dirY * speed)] == 0) {
        // if the y direction movement is available
        posY += dirY * speed;
      }
    }
    if (this.backward) {
      if (board[(int) (posX - dirX * speed)][(int) posY] == 0) {
        posX -= dirX * speed;
      }
      if (board[(int) posX][(int) (posY - dirY * speed)] == 0) {
        posY -= dirY * speed;
      }
    }

    /** forward and backward above change the position of the player **/
    /** right and left below change the direction of player's view by using rotate **/

    if (this.left) {
      double preDirX = dirX;
      // rotation matrix multiplication for direction vectors and plane vectors
      dirX = dirX * Math.cos(rotate) - dirY * Math.sin(rotate);
      dirY = preDirX * Math.sin(rotate) + dirY * Math.cos(rotate);
      double prePlaneX = planeX;
      planeX = planeX * Math.cos(rotate) - planeY * Math.sin(rotate);
      planeY = prePlaneX * Math.sin(rotate) + planeY * Math.cos(rotate);
    }
    if (this.right) {
      double preDirX = dirX;
      dirX = dirX * Math.cos(-rotate) - dirY * Math.sin(-rotate);
      dirY = preDirX * Math.sin(-rotate) + dirY * Math.cos(-rotate);
      double prePlaneX = planeX;
      planeX = planeX * Math.cos(-rotate) - planeY * Math.sin(-rotate);
      planeY = prePlaneX * Math.sin(-rotate) + planeY * Math.cos(-rotate);

    }
    //System.out.println("x: " + dirX + "y: " + dirY);

  }

  /**
   * return posX, which is row.
   *
   * @return double
   */
  public double getX() {
    return this.posX;
  }

  /**
   * return posY, which is column.
   *
   * @return double
   */
  public double getY() {
    return this.posY;
  }

  /**
   * return dirX.
   *
   * @return double
   */
  public double getDirX() {
    return this.dirX;
  }


  /**
   * return dirY.
   *
   * @return double
   */
  public double getDirY() {
    return this.dirY;
  }

  /**
   * return planeX.
   *
   * @return double
   */
  public double getPlaneX() {
    return this.planeX;
  }

  /**
   * return planeY.
   *
   * @return double
   */
  public double getPlaneY() {
    return this.planeY;
  }

  public void setPos(double x, double y) {
    this.posX = x;
    this.posY = y;
  }

  public void setDir(double dx, double dy) {
    this.dirX = dx;
    this.dirY = dy;
  }

  /**
   * Get facing direction.
   *
   * @return
   */
  public String getFacingDir() {
    if (-0.9994 <= dirX && dirX <= -0.6878
            && -0.4244 <= dirY && dirY <= 0.4034) {
      return "north";
    }
    if (-0.51928 <= dirX && dirX <= 0.65484
            && -0.99854 <= dirY && dirY <= -0.56857) {
      return "west";
    }
    if (0.86623 <= dirX && dirX <= 0.99705
            && -0.254001 <= dirY && dirY <= 0.279415) {
      return "south";
    }
    if (-0.326402 <= dirX && dirX <= 0.99705
            && 0.806617 <= dirY && dirY <= 0.999475) {
      return "east";
    }
    return null;
  }

  // no use method
  @Override
  public void keyTyped(KeyEvent e) {
  }
  
}

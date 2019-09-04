import java.awt.*;
import java.util.ArrayList;

/**
 * The Canvas class represents the rendered screen showed to user.
 * @author LY
 */
public class Canvas {
    // the game board.
    // for calculating the distance between objects and player's current position
    private int[][] board;
    private int width;
    private int height;
    private int boardRows;
    private int boardCols;
    private ArrayList<Picture> pictures;

    /**
     * The constructor of class Canvas.
     *
     * @param board
     * @param pics
     * @param br
     * @param bc
     * @param w
     * @param h
     */
    public Canvas(int[][] board, ArrayList<Picture> pics, int br, int bc, int w, int h) {
        this.board = board;
        this.pictures = pics;
        this.boardRows = br;
        this.boardCols = bc;
        this.width = w;
        this.height = h;
    }

    /**
     * This method update the canvas showing to user, it will calculate how does the screen look like
     * based on player's current position and return an updated array of pixels to Game.
     *
     * @param view
     * @param pixels
     * @return integer[]
     */
    public int[] updateCanvas(PlayerView view, int[] pixels) {
        // clear the canvas with two different color
        // to respectively represent the ceiling and the ground.
        for (int i = 0; i < pixels.length / 2; i++) {
            if (pixels[i] != Color.DARK_GRAY.getRGB()) {
                pixels[i] = Color.DARK_GRAY.getRGB();
            }
        }
        for (int i = pixels.length / 2; i < pixels.length; i++) {
            if (pixels[i] != Color.GRAY.getRGB()) {
                pixels[i] = Color.GRAY.getRGB();
            }
        }

        /**The calculations below are all for building up the 3D wall view of the game.**/
        /**Some of the calculations I cannot understand well. **/

        // loops through every vertical bar on screen
        // casts a ray to figure out the corresponding wall at this bar
        for (int x = 0; x < width; x++) {
            double viewX = 2 * x / (double) width - 1;
            double rayX = view.getDirX() + view.getPlaneX() * viewX;
            double rayY = view.getDirY() + view.getPlaneY() * viewX;
            // player's position on board
            int boardX = (int) view.getX();
            int boardY = (int) view.getY();
            // length of ray from current position to next x grid or y grid
            double sideDistX;
            double sideDistY;
            //length of ray from one side to next on board
            double deltaDistX = Math.sqrt(1 + (rayY * rayY) / (rayX * rayX));
            double deltaDistY = Math.sqrt(1 + (rayX * rayX) / (rayY * rayY));
            // distance from the player to the first wall this ray collides with
            double firCollDist;
            // direction to go in x and y
            int stepX;
            int stepY;
            // detect whether there is a wall hit by the ray
            boolean wallHit = false;
            // detect whether the hit is on vertical wall or horizontal wall
            int hitSide = 0;

            // figure out the step direction
            // initial distance to a side
            if (rayX < 0) {
                stepX = -1;
                sideDistX = (view.getX() - boardX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (boardX + 1.0 - view.getX()) * deltaDistX;
            }
            if (rayY < 0) {
                stepY = -1;
                sideDistY = (view.getY() - boardY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (boardY + 1.0 - view.getY()) * deltaDistY;
            }

            // figure out where the ray collides with a wall
            // loops through to check if there is a wall hit
            // if not, moves to the next possible collision point
            while (!wallHit) {
                // skip to next grid
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    if (boardX < boardRows - 1) {
                        boardX += stepX;
                    }
                    hitSide = 0;
                } else {
                    sideDistY += deltaDistY;
                    if (boardY < boardCols - 1) {
                        boardY += stepY;
                    }
                    hitSide = 1;
                }
                // check if the ray has hit a wall
                if (board[boardX][boardY] > 0) {
                    wallHit = true;
                }
            }

            // figure out how the wall should look in the vertical stripe
            // calculate the distance to the wall,
            // use the distance to figure out how tall the wall should appear in the vertical strip
            // translate the height in terms of pixels on canvas
            if (hitSide == 0) {
                firCollDist = Math.abs((boardX - view.getX() + (1 - stepX) / 2) / rayX);
            } else {
                firCollDist = Math.abs((boardY - view.getY() + (1 - stepY) / 2) / rayY);
            }
            // get the wall height based on the distance
            int wallHeight;
            if (firCollDist > 0) {
                wallHeight = Math.abs((int) (height / firCollDist));
            } else {
                wallHeight = height;
            }
            // calculate lowest and highest pixel
            int start = -wallHeight / 2 + height / 2;
            if (start < 0) {
                start = 0;
            }
            int end = wallHeight / 2 + height / 2;
            if (end >= height) {
                end = height - 1;
            }

            // add pictures to wall
            // figure out what picture is associated with the hit wall
            // figure the x coordinate on the picture of the pixels that will appear to the user
            int index = board[boardX][boardY] - 1;
            // the exact position of the wall hit
            double wallH;
            if (hitSide == 1) {
                // hit at y side wall
                wallH = (view.getX() + ((boardY - view.getY() + (1 - stepY) / 2) / rayY) * rayX);
            } else {
                // hit at x side wall
                wallH = (view.getY() + ((boardX - view.getX() + (1 - stepX) / 2) / rayX) * rayY);
            }
            wallH -= Math.floor(wallH);
            // x coordinate on the picture
            int picX = (int) (wallH * (pictures.get(index).getSize()));
            if (hitSide == 0 && rayX > 0) {
                picX = pictures.get(index).getSize() - picX - 1;
            }
            if (hitSide == 1 && rayY < 0) {
                picX = pictures.get(index).getSize() - picX - 1;
            }

            // calculate y coordinate on picture
            for (int y = start; y < end; y++) {
                int picY = (((y * 2 - height + wallHeight) << 6) / wallHeight) / 2;
                int colour;
                if (hitSide == 0) {
                    colour = pictures.get(index).getPixels()[picX + (picY * pictures.get(index).getSize())];
                } else {
                    // make Y sides darker
                    colour = (pictures.get(index).getPixels()
                            [picX + (picY * pictures.get(index).getSize())] >> 1) & 8355711;
                }
                pixels[x + y * (width)] = colour;
            }
        }
        return pixels;
    }
}

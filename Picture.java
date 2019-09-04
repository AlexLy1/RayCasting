import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The picture class is for holding the pictures for wall. 
 * @author LY
 */
public class Picture {

    /*Some pictures can be defined over here*/
    /*As public static field be used by the main render class*/

    /**
     * corridor wall picture.
     */
    public static Picture corridorWall = new Picture("src/corridorWall.png", 64);
    /**
     * room wall picture.
     */
    public static Picture roomWall = new Picture("src/roomWall.png", 64);
    /**
     * room door picture.
     */
    public static Picture roomDoor = new Picture("src/roomDoor.png", 64);
    /**
     * final door picture.
     */
    public static Picture finalDoor = new Picture("src/finalDoor.png", 64);
    /**
     * win image for the final room
     */
    public static Picture winImage = new Picture("src/winImage.png", 64);
    // SIZE represents the length of one side of a square picture
    private final int size;
    // pixels array holds all pixel data for the picture
    private int[] pixels;
    // picture path holds the path of this picture file
    private String picturePath;

    /**
     * Constructor of Picture class.
     *
     * @param path
     * @param size
     */
    public Picture(String path, int size) {
        this.picturePath = path;
        this.size = size;
        pixels = new int[this.size * this.size];
        loadPicture();
    }

    /**
     * return size of the picture.
     *
     * @return integer
     */
    public int getSize() {
        return this.size;
    }

    /**
     * return the array of pixels.
     *
     * @return integer[]
     */
    public int[] getPixels() {
        return this.pixels;
    }

    /**
     * Load picture file.
     */
    private void loadPicture() {
        try {
            BufferedImage image = ImageIO.read(new File(this.picturePath));
            int width = image.getWidth();
            int height = image.getHeight();
            // every pixel is taken from the buffered image and stored in pixels array
            image.getRGB(0, 0, width, height, this.pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package sabrewulf.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

/**
 * This class is responsible for loading and storing textures (images) in
 * memory. The texture can be anything: spike, platform, character, background.
 */

public class Texture {
    private int id;
    private int width;
    private int height;

    private final int BITS_PER_PIXEL = 4;

    /**
     * Create a texture. The constructor also loads the texture into memory
     * and applies the necessary pre processing so the texture is in a format
     * that OpenGL can understand.
     *
     * @param fileName file name of the texture (with path from project root)
     */
    public Texture(String fileName) {
        BufferedImage bufferedImage;

        try {
            // load image file via ImageIO and store its width and height
            bufferedImage = ImageIO.read(new File(fileName));
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            // load the raw pixels into memory. Will need to manipulate them for LWJGL
            int[] rawPixels = new int[width * height * BITS_PER_PIXEL];
            rawPixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);

            // create a byte buffer to store the pixels in a format that LWJGL can use
            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * BITS_PER_PIXEL);

            // LWJGL requires a byte array of pixels, whereas ImageIO.read gives us an int array
            // therefore transformation is needed to get the image in a format LWJGL can use
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int currentPixel = rawPixels[y * width + x];
                    pixels.put((byte) ((currentPixel >> 16) & 0xFF)); // red value
                    pixels.put((byte) ((currentPixel >> 8) & 0xFF)); // green value
                    pixels.put((byte) (currentPixel & 0xFF)); // blue value
                    pixels.put((byte) ((currentPixel >> 24) & 0xFF)); // transparency value
                }
            }

            pixels.flip(); // LWJGL requires the buffer to come in flipped

            // the image is now stored in a format LWJGL can use.
            // generate a unique id so we can refer to the texture in the future
            id = glGenTextures();

            // bind the texture, and set a linear filter on it for scaling
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            // send image data to OpenGL
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA8,
                    width,
                    height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    pixels);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a texture. Technically, This method only retrieves the texture
     * from {@link TextureMapper}.
     *
     * @param textureName the name of the texture in {@link TextureMapper}
     * @return the retrieved texture
     */
    public static Texture create(String textureName) {
        return TextureMapper.getTexture(textureName);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

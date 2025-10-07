package got.client.utils;
/*
 * Copyright (c) 2022 DupliCAT
 * GNU Lesser General Public License v3.0
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;

public class ColorHelper {



    public static void color(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        GL11.glColor4f(red, green, blue, alpha);
    }



    public static Color intColorToRGB(int color) {
        float alpha = (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;
        return new Color(red, green, blue, alpha);
    }




}
package com.example.helpers;

import com.example.modules.Themes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;
import thunder.hack.gui.font.FontRenderer;
import thunder.hack.gui.font.FontRenderers;
import thunder.hack.modules.client.HudEditor;
import thunder.hack.utility.render.GaussianFilter;
import thunder.hack.utility.render.Render2DEngine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static thunder.hack.utility.render.Render2DEngine.*;

public class Render {
    public MinecraftClient mc = MinecraftClient.getInstance();
    public static ShaderUtility glow = new ShaderUtility("glow");
    public static FontRenderer[] bold = new FontRenderer[23];
    public static FontRenderer[] medium = new FontRenderer[23];
    static {
        try {
        for (int i = 6; i < 23; i++) {
            bold[i] = FontRenderers.create(i, "sf_bold");
            medium[i] = FontRenderers.create(i, "sf_medium");
            }
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void drawRoundedGradientBlurredRect(MatrixStack ms, double x, double y, double width, double height, double roundR, float blurR, Color topRight, Color bottomRight, Color topLeft, Color bottomLeft) {
        drawSetup();


        float left = (float) (x - blurR);
        float top = (float) (y - blurR);
        float right = (float) (width + blurR * 2) + left;
        float bottom = (float) (height + blurR * 2) + top;

        float left1 = (float) (x);
        float top1 = (float) (y);
        float right1 = (float) (width) + left1;
        float bottom1 = (float) (height) + top1;

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        drawRoundedCorner(ms, bufferBuilder, right1, top1, roundR, topRight, blurR, 270);
        drawRoundedCorner(ms, bufferBuilder, right1, bottom1, roundR, bottomRight, blurR, 0);
        drawRoundedCorner(ms, bufferBuilder, left1, bottom1, roundR, bottomLeft, blurR, 90);
        drawRoundedCorner(ms, bufferBuilder, left1, top1, roundR, topLeft, blurR, 180);

        bufferBuilder.vertex(ms.peek().getPositionMatrix(), right1, (float) (top1 - roundR), 0.0F).color(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha()).next();
        bufferBuilder.vertex(ms.peek().getPositionMatrix(), (float) (right - roundR - 2f), (float) (top - roundR), 0.0F).color(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), 0).next();

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        drawFinish();
    }

    private static void drawRoundedCorner(MatrixStack ms, BufferBuilder bufferBuilder, float cx, float cy, double radius, Color color, float blurR, int startAngle) {
        int segments = 5;
        double angleStep = Math.PI / 2 / segments;
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle * Math.PI / 180 + i * angleStep;
            float x1 = (float) (cx + Math.cos(angle) * radius);
            float y1 = (float) (cy + Math.sin(angle) * radius);
            float x2 = (float) (cx + Math.cos(angle) * (radius + blurR));
            float y2 = (float) (cy + Math.sin(angle) * (radius + blurR));
            bufferBuilder.vertex(ms.peek().getPositionMatrix(), x1, y1, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
            bufferBuilder.vertex(ms.peek().getPositionMatrix(), x2, y2, 0.0F).color(color.getRed(), color.getGreen(), color.getBlue(), 0).next();
        }
    }
    public static void drawSetup() {
        Render2DEngine.setupRender();
    }

    public static void drawFinish() {
        Render2DEngine.endRender();
    }
    public static void clientRect(MatrixStack ms, float x, float y, float w, float h) {

        switch (Themes.rectMode.getValue()) {
            case Astana: {
                drawRoundedGradientBlurredRect(ms, x + 2, y + 2, w - 4, h - 4, 3, 5,
                        applyOpacity(getColor(270), 200 / 255f),
                        applyOpacity(getColor(0), 200 / 255f),
                        applyOpacity(getColor(180), 200 / 255f),
                        applyOpacity(getColor(90), 200 / 255f));

                Render2DEngine.drawGradientRound(ms, x - 1, y - 1, w + 2, h + 2, 4,
                        getColor(270),
                        getColor(0),
                        getColor(180),
                        getColor(90));
                Render2DEngine.drawGradientRound(ms, -1000, -1000, 10, 10, 4,
                        getColor(270),
                        getColor(0),
                        getColor(180),
                        getColor(90));
                Render2DEngine.drawGradientRound(ms, x, y, w, h, 3,
                        getColor(270).darker(),
                        getColor(0).darker(),
                        getColor(180).darker(),
                        getColor(90).darker());
                break;
            }
            case New: {
                drawRoundedGradientBlurredRect(ms, x - 0.5f - 2.5f + 5, y - 0.5f - 2.5f + 5, w + 1 + 5 - 10, h + 1 + 5 - 10, 2, 5,
                        getColor(270).darker().darker().darker().darker(),
                        getColor(0).darker().darker().darker().darker(),
                        getColor(180).darker().darker().darker().darker(),
                        getColor(90).darker().darker().darker().darker());
                Render2DEngine.drawGradientRound(ms, x - 0.5f, y - 0.5f, w + 1, h + 1, 0,
                        getColor(270).darker().darker().darker().darker(),
                        getColor(0).darker().darker().darker().darker(),
                        getColor(180).darker().darker().darker().darker(),
                        getColor(90).darker().darker().darker().darker());


//                drawRoundedGradientBlurredRect(x - 1.5f - 0.5f, (float) (y - 1) - 0.5f, w + 3 + 1, (float) 0.5f + 1, 0, 5, getColor(1), getColor(1), getColor(50), getColor(50));
                Render2DEngine.drawGradientRound(ms, x - 1.5f - 0.5f, (float) (y - 1) - 0.5f, w + 3 + 1, (float) 0.5f + 1, 0, getColor(1), getColor(1), getColor(50), getColor(50));
                break;
            }
        }
    }
    public static Color astolfo(int speed, int offset, float saturation, float brightness, float alpha) {
        float hue = (float) calculateHueDegrees(speed, offset);
        hue = (float) ((double) hue % 360.0);
        float hueNormalized;
        return new Color(Render2DEngine.applyOpacity(
                Color.HSBtoRGB((double) ((hueNormalized = hue % 360.0F) / 360.0F) < 0.5 ? -(hueNormalized / 360.0F) : hueNormalized / 360.0F, saturation, brightness),
                Math.max(0, Math.min(255, (int) (alpha * 255.0F)))));
    }
    public static Color rainbow(int speed, int index, float saturation, float brightness, float opacity) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        float hue = angle / 360f;
        Color color = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int) (opacity * 255))));
    }
    private static int calculateHueDegrees(int divisor, int offset) {
        long currentTime = System.currentTimeMillis();
        long calculatedValue = (currentTime / divisor + offset) % 360L;
        return (int) calculatedValue;
    }
    public static Color twoColorEffect(int cl1, int cl2, double speed, int index) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return interpolateColorC(new Color(cl1), new Color(cl2), angle / 360f);
    }
    public static Color getColor(int index) {
        switch (Themes.theme.getValue()) {
            case Astolfo -> {
                return astolfo(15, index, 0.6f, 1, 1);
            }
            case Rainbow -> {
                return rainbow(15, index, 0.6f, 1, 1);
            }
            case Custom -> {
                return twoColorEffect(Themes.color1.getValue().getColor(), Themes.color2.getValue().getColor(), 7, index);
            }
        }
        return Color.magenta;
    }
}

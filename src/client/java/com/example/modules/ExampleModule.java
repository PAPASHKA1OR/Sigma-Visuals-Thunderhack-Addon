package com.example.modules;

import com.example.helpers.Render;
import com.example.helpers.compactAnims.CompactAnimation;
import com.example.helpers.compactAnims.Easing;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import thunder.hack.modules.Module;
import thunder.hack.utility.render.Render2DEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExampleModule extends Module  {
    public ExampleModule() {//BESTCLIENTSUKATOCHNONEKRASHITOTSVICHKIBLYAAA
        super("Jump Circles 2", Category.getCategory("Sigma Visuals"));
    }

    public static List<Circle>circles = new ArrayList<>();

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        super.onRender3D(matrixStack);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glLineWidth(3);

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        circles.removeIf(circle -> circle.toRemove);

        for (Circle c : circles) {
            c.toRemove = System.currentTimeMillis() - c.time - 2500 >= 1400;
            c.anim.run(System.currentTimeMillis() - c.time >= 2500 ? 0 : 1);
            c.anim.setEasing(System.currentTimeMillis() - c.time >= 2500 ? Easing.EASE_IN_BACK : Easing.EASE_OUT_BACK);
            c.alpha.run(System.currentTimeMillis() - c.time >= 2500 ? 0 : 1);

            float shadowSize = 40;
            float radius = (float) (c.anim.getValue() + Math.cos((System.currentTimeMillis() - c.time) / 500f) * 0.2f);
            float alpha = (float) c.alpha.getValue();
            double x = c.pos.x - mc.gameRenderer.getCamera().getPos().x;
            double y = c.pos.y - mc.gameRenderer.getCamera().getPos().y + 0.01;
            double z = c.pos.z - mc.gameRenderer.getCamera().getPos().z;
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

            for (float i = 0; i <= 30; ++i) {
                double sin = Math.sin(Math.toRadians(i * 12));
                double cos = Math.cos(Math.toRadians(i * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * (radius - (radius / 100 * shadowSize))), (float) y, (float) (z + cos * (radius - (radius / 100 * shadowSize)))).color(0, 0, 0, 0).next();

                Color color = Render.getColor((int) ((i + circles.indexOf(c) * 2) * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y, (float) (z + cos * radius)).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha * 0.8f).next();
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

            for (int i = 0; i <= 30; ++i) {
                double sin = Math.sin(Math.toRadians(i * 12));
                double cos = Math.cos(Math.toRadians(i * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * (radius + (radius / 100 * shadowSize))), (float) y, (float) (z + cos * (radius + (radius / 100 * shadowSize)))).color(0, 0, 0, 0).next();
                Color color = Render.getColor((int) ((i + circles.indexOf(c) * 2) * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y, (float) (z + cos * radius)).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha * 0.8f).next();
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            shadowSize = 10;
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
            for (float i = 0; i <= 30; ++i) {
                double sin = Math.sin(Math.toRadians(i * 12));
                double cos = Math.cos(Math.toRadians(i * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * (radius - (radius / 100 * shadowSize))), (float) y, (float) (z + cos * (radius - (radius / 100 * shadowSize)))).color(0, 0, 0, 0).next();

                Color color = Render.getColor((int) ((i + circles.indexOf(c) * 2) * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y, (float) (z + cos * radius)).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha).next();
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());

            RenderSystem.setShader(GameRenderer::getPositionColorProgram);
            buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

            for (int i = 0; i <= 30; ++i) {
                double sin = Math.sin(Math.toRadians(i * 12));
                double cos = Math.cos(Math.toRadians(i * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y + 0.5f, (float) (z + cos * radius)).color(0, 0, 0, 0).next();
                Color color = Render.getColor((int) ((i + circles.indexOf(c) * 2) * 12));
                buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y, (float) (z + cos * radius)).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha * 0.5f).next();
            }
            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static class Circle {
        public Vec3d pos;
        public long time = System.currentTimeMillis();
        public CompactAnimation alpha = new CompactAnimation(Easing.EASE_IN_OUT_QUAD, 700);
        public CompactAnimation anim = new CompactAnimation(Easing.EASE_OUT_BACK, 800);
        public boolean toRemove = false;
        public Circle(Vec3d po) {
            pos = po;
        }
    }
}

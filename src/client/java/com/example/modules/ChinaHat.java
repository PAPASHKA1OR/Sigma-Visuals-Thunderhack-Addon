package com.example.modules;

import com.example.helpers.compactAnims.CompactAnimation;
import com.example.helpers.compactAnims.Easing;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import thunder.hack.modules.Module;
import thunder.hack.setting.Setting;
import thunder.hack.utility.render.Render2DEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChinaHat extends Module  {
    public ChinaHat() {
        super("China Hat", Category.getCategory("Sigma Visuals"));
    }
//    public Setting<Mode> mode = new Setting<>("Mode", Mode.PASHOLNAHUIIIII);
    public static int astolfo(int speed, int offset, float saturation, float brightness, float alpha) {
        float hue = (float) calculateHueDegrees(speed, offset);
        hue = (float) ((double) hue % 360.0);
        float hueNormalized;
        return Render2DEngine.applyOpacity(
                Color.HSBtoRGB((double) ((hueNormalized = hue % 360.0F) / 360.0F) < 0.5 ? -(hueNormalized / 360.0F) : hueNormalized / 360.0F, saturation, brightness),
                Math.max(0, Math.min(255, (int) (alpha * 255.0F)))
        );
    }

    private static int calculateHueDegrees(int divisor, int offset) {
        long currentTime = System.currentTimeMillis();
        long calculatedValue = (currentTime / divisor + offset) % 360L;
        return (int) calculatedValue;
    }
    public int getColor(int index) {
        return astolfo(15, index, 0.6f, 1, 1);
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        super.onRender3D(matrixStack);
        if (mc.options.getPerspective() == Perspective.FIRST_PERSON) return;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glLineWidth(3);
        float radius = 0.6f;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        double x = mc.player.lastRenderX + (mc.player.getPos().x - mc.player.lastRenderX) * mc.getTickDelta() - mc.gameRenderer.getCamera().getPos().x;
        double y = mc.player.lastRenderY + (mc.player.getPos().y - mc.player.lastRenderY) * mc.getTickDelta() + mc.player.getEyeHeight(mc.player.getPose()) + 0.1f - mc.gameRenderer.getCamera().getPos().y + 0.2;
        double z = mc.player.lastRenderZ + (mc.player.getPos().z - mc.player.lastRenderZ) * mc.getTickDelta() - mc.gameRenderer.getCamera().getPos().z;


//        matrixStack.push();
//        matrixStack.translate(x, y - 0.125f, z);
//        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
//        matrixStack.translate(-x, -y + 0.125f, -z);
//        matrixStack.push();
//        matrixStack.translate(x, y - 0.125f, z);
//        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
//        matrixStack.translate(-x, -y + 0.125f, -z);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        for (float i = 0; i <= 360; ++i) {
            double sin = Math.sin(Math.toRadians(i));
            double cos = Math.cos(Math.toRadians(i));

            Color color = new Color(getColor((int) i));
            buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) (x - sin * radius), (float) y, (float) (z + cos * radius)).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.8f).next();
            buffer.vertex(matrixStack.peek().getPositionMatrix(), (float) x, (float) y + 0.25f, (float) z).color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 0.5f).next();
        }
        BufferRenderer.drawWithGlobalProgram(buffer.end());
//        matrixStack.pop();
//        matrixStack.pop();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
//    public enum Mode {
//        Static, Dynamic, PASHOLNAHUIIIII
//    }
}

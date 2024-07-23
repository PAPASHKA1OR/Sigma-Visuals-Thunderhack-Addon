package com.example.hud;

import com.example.helpers.Render;
import com.example.helpers.compactAnims.CompactAnimation;
import com.example.helpers.compactAnims.Easing;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.mixin.transfer.ItemMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL40C;
import thunder.hack.gui.font.FontRenderers;
import thunder.hack.gui.hud.HudEditorGui;
import thunder.hack.gui.hud.HudElement;
import thunder.hack.modules.combat.Aura;
import thunder.hack.modules.combat.AutoAnchor;
import thunder.hack.modules.combat.AutoCrystal;
import thunder.hack.utility.math.MathUtility;
import thunder.hack.utility.render.Render2DEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TargetHudikElement extends HudElement {
    public TargetHudikElement() {
        super("Sigma THud", 1, 1);
    }

    private Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE;
    private Color gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;

    private static LivingEntity curTarget = null;
    private double scale = 0;
    private CompactAnimation anim = new CompactAnimation(Easing.EASE_OUT_BACK, 350);
    private CompactAnimation hpAn = new CompactAnimation(Easing.EASE_IN_OUT_CUBIC, 250);
    float hp, ar;
    int posX;
    int posY;
    public boolean direction;


    public void onRender2D(DrawContext context) {
        super.onRender2D(context);
//        if (Killaura.targetEntity == null) {
//            if (mc.player != null && mc.currentScreen instanceof ChatScreen) {
//                curTarget = mc.player;
//                scale = AnimationMath.animation((float) scale, (float) 1, (float) (3 * AnimationMath.deltaTime()));
//            } else {
//                scale = AnimationMath.animation((float) scale, (float) 0, (float) (3 * AnimationMath.deltaTime()));
//            }
//        } else {
//            curTarget = Killaura.targetEntity;
//            scale = AnimationMath.animation((float) scale, (float) 1, (float) (3 * AnimationMath.deltaTime()));
//        }
        anim.run(direction ? 1 : 0);
        anim.setEasing(direction ? Easing.EASE_OUT_BACK : Easing.EASE_IN_BACK);
        scale = anim.getValue();
        if (AutoCrystal.target != null) {
            curTarget = AutoCrystal.target;
            direction = true;
            if (AutoCrystal.target.isDead()) {
                AutoCrystal.target = null;
                curTarget = null;
                direction = false;
            }
        } else if (Aura.target != null) {
            if (Aura.target instanceof LivingEntity) {
                curTarget = (LivingEntity) Aura.target;
                direction = true;
            } else {
                curTarget = null;
                direction = false;
            }
        } else if (AutoAnchor.target != null) {
            curTarget = AutoAnchor.target;
            direction = true;
            if (AutoAnchor.target.isDead()) {
                AutoAnchor.target = null;
                curTarget = null;
            }
        } else if (mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof HudEditorGui) {
            curTarget = mc.player;
            direction = true;
        } else {
            direction = false;
            if (scale < 0.02)
                curTarget = null;
        }
        if (curTarget == null || !(curTarget instanceof PlayerEntity)) {
            return;
        }
        hpAn.run(((int) curTarget.getHealth()) / curTarget.getMaxHealth());
        String name = curTarget.getName().getString();
        {
            float width = 90 + Render.medium[12].getStringWidth(name);
            float height = 30;
            setBounds(getPosX(), getPosY(), width, height);
            posX = (int) getPosX();
            posY = (int) getPosY();
            hp = (float) hpAn.getValue();

            String healthText = (int) (hp * 100) + "%";
            MatrixStack ms = new MatrixStack();
            ms.push();
            ms.translate(posX + width / 2, posY + 43 / 2f, 0);
            ms.scale((float) scale, (float) scale, 1);
            ms.translate(-posX - width / 2, -posY - 43 / 2f, 0);
            Render.clientRect(ms, posX, posY, width, height);
            float realHealthWidth = width - 44;
            float realHealthHeight = 3;
            int healthWidth = (int) ((width - 40f) * hp);
//            Render.medium[14].drawString(ms, healthText, posX + width - 2 - Render.medium[14].getStringWidth(healthText), posY + 14, -1);//tenacity16

            Render.medium[15].drawString(ms, name, posX + 34, posY + 4, -1);//moonb22
            for (int i = 0; i <= curTarget.getMaxHealth(); i++) {
                float maxH = curTarget.getMaxHealth() * 2 * (Render.bold[12].getFontHeight("") + 4);
                float add = (1-(hp)) * (Render.bold[12].getFontHeight("") + 4) * 20;
                float x = posX + width - 2 + 3 - 20;
                float y = posY - 0.5f + 8 - 10 + height / 2 - 1;
                float w = 80 + x;
                float h = (Render.bold[12].getFontHeight("") + 4) + y;
                Window sr = MinecraftClient.getInstance().getWindow();
                float factor = (float) sr.getScaleFactor();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                GL11.glScissor((int) (x * (float) factor), (int) (((float) sr.getScaledHeight() - h) * (float) factor), (int) ((w - x) * (float) factor), (int) ((h - y) * (float) factor));

                Render.medium[12].drawCenteredString(ms, i + "", posX + width + 5 - 10, posY + 14 + add - maxH / 2 + i * (Render.bold[12].getFontHeight("") + 4), -1);

                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }

            Render2DEngine.drawRound(ms, posX + 35 - 0.5f, posY + 21f - 0.5f, (width - 40f), 3.5f + 1, 2, new Color(80, 79, 79, 123));
            Render2DEngine.drawGradientRound(ms, posX + 35 - 0.5f, posY + 21f - 0.5f, (width - 40f) * hp, 3.5f + 1, 2,
                    Render.getColor(270),
                    Render.getColor(0),
                    Render.getColor(180),
                    Render.getColor(90));
            if (curTarget instanceof PlayerEntity) {
                RenderSystem.setShaderTexture(0, ((AbstractClientPlayerEntity) curTarget).getSkinTextures().texture());
            } else {
                RenderSystem.setShaderTexture(0, mc.getEntityRenderDispatcher().getRenderer(curTarget).getTexture(curTarget));
            }

            context.getMatrices().push();
            context.getMatrices().translate(posX + width / 2, posY + 43 / 2f, 0);
            context.getMatrices().scale((float) scale, (float) scale, 1);
            context.getMatrices().translate(-posX - width / 2, -posY - 43 / 2f, 0);

            RenderSystem.enableBlend();
            RenderSystem.colorMask(false, false, false, true);
            RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            RenderSystem.clear(GL40C.GL_COLOR_BUFFER_BIT, false);
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.setShader(GameRenderer::getPositionColorProgram);

            float animationFactor = (float) MathUtility.clamp(scale, 0, 1);
            Render2DEngine.renderRoundedQuadInternal(context.getMatrices().peek().getPositionMatrix(), 1, 1, 1, 1,
                    posX + 4, posY + 2, 26 + posX + 4, 26 + posY + 2, 3, 10);
            RenderSystem.blendFunc(GL40C.GL_DST_ALPHA, GL40C.GL_ONE_MINUS_DST_ALPHA);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            Render2DEngine.renderTexture(context.getMatrices(), posX + 4, posY + 2, 26, 26, 8, 8, 8, 8, 64, 64);
            Render2DEngine.renderTexture(context.getMatrices(), posX + 4, posY + 2, 26, 26, 40, 8, 8, 8, 64, 64);
            RenderSystem.defaultBlendFunc();
            context.getMatrices().pop();
            List<ItemStack> list = new ArrayList<>();
            list.add(mc.player.getStackInHand(Hand.MAIN_HAND));
            for (ItemStack is : curTarget.getArmorItems()) {
                list.add(is);
            }
            int off = 8;
            for (ItemStack itemStack : list) {
                if (itemStack.isEmpty())continue;
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                GL11.glClear(256);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                context.getMatrices().push();
                context.getMatrices().translate(posX + width / 2, posY + 43 / 2f, 0);
                context.getMatrices().scale((float) scale, (float) scale, 1);
                context.getMatrices().translate(-posX - width / 2, -posY - 43 / 2f, 0);

                context.getMatrices().translate(posX + 25 + off, posY + 9, 0);
                context.getMatrices().scale(0.73f, 0.73f, 1);
                context.getMatrices().translate(-posX - 25 - off, -posY - 9, 0);
                context.getMatrices().translate(0, 0, -150);
                DiffuseLighting.enableGuiDepthLighting();
                context.drawItem(itemStack, (int) posX + 25 + off, posY + 9);
                DiffuseLighting.disableGuiDepthLighting();
                context.getMatrices().pop();
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                off += 11;
            }
            if (!curTarget.getOffHandStack().isEmpty()) {
                Render.clientRect(ms, posX + width + 7, posY + 5, 18, 20);



                float pox = posX + width + 9;

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                GL11.glClear(256);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                context.getMatrices().push();
                context.getMatrices().translate(posX + width / 2, posY + 43 / 2f, 0);
                context.getMatrices().scale((float) scale, (float) scale, 1);
                context.getMatrices().translate(-posX - width / 2, -posY - 43 / 2f, 0);
                context.getMatrices().translate(0, 0, -150);
                DiffuseLighting.enableGuiDepthLighting();
                ItemStack offHandItem = curTarget.getStackInHand(Hand.OFF_HAND);
                context.drawItem(offHandItem, (int) pox, posY + 6);
                context.drawItemInSlot(mc.textRenderer, offHandItem, (int) pox, posY + 6);
                DiffuseLighting.disableGuiDepthLighting();
                context.getMatrices().pop();
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }
            ms.pop();
        }
    }
}

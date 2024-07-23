package com.example.hud;

import com.example.ExampleMod;
import com.example.helpers.Render;
import com.example.modules.Themes;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import thunder.hack.core.impl.ServerManager;
import thunder.hack.gui.font.FontRenderers;
import thunder.hack.gui.hud.HudElement;
import thunder.hack.gui.hud.impl.PingHud;
import thunder.hack.setting.Setting;
import thunder.hack.utility.player.PlayerUtility;
import thunder.hack.utility.render.Render2DEngine;

public class WaterkaElement extends HudElement {
    public WaterkaElement() {
        super("Sigma Watermark", 100, 10);
    }
    float s1, s2;
    private Identifier ping = new Identifier("thunderhack", "textures/hud/icons/ping.png");
    private Identifier logo = new Identifier("thunderhack", "textures/hud/icons/mini_logo.png");

    public void onRender2D(DrawContext context) {
        super.onRender2D(context);


        String text = "Sigma |      " + ServerManager.getPing() + "ms" + " | " + ExampleMod.version;

        float pX = getPosX() + FontRenderers.getModulesRenderer().getStringWidth(text) + 14 > mc.getWindow().getScaledWidth() ? mc.getWindow().getScaledWidth() - (FontRenderers.getModulesRenderer().getStringWidth(text) + 14) : getPosX();

        Render.clientRect(context.getMatrices(), pX, getPosY(), FontRenderers.getModulesRenderer().getStringWidth(text) + 10 + 4, 13f);
        Render2DEngine.setupRender();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.setShaderTexture(0, logo);
        Render2DEngine.renderGradientTexture(context.getMatrices(), pX + 1, getPosY() + 1, 10, 10, 0, 0, 512, 512, 512, 512,
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject());
        RenderSystem.setShaderTexture(0, ping);
        Render2DEngine.renderGradientTexture(context.getMatrices(), pX + 11 + FontRenderers.getModulesRenderer().getStringWidth("Sigma | "), getPosY() + 1, 10, 10, 0, 0, 512, 512, 512, 512,
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject(),
                Themes.miscColor.getValue().getColorObject());
        Render2DEngine.endRender();


        FontRenderers.getModulesRenderer().drawString(context.getMatrices(), text, pX + 12, getPosY() + 5, -1);
        setBounds(pX, getPosY(), FontRenderers.getModulesRenderer().getStringWidth(text) + 10 + 4, 13f);
    }
}

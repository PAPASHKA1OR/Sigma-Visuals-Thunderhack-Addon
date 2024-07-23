package com.example.modules;

import com.example.helpers.EnumThemes;
import thunder.hack.ThunderHack;
import thunder.hack.gui.hud.HudEditorGui;
import thunder.hack.modules.Module;
import thunder.hack.modules.client.HudEditor;
import thunder.hack.modules.client.Notifications;
import thunder.hack.setting.Setting;
import thunder.hack.setting.impl.ColorSetting;

import java.awt.*;

public class Themes extends Module {
    public Themes() {
        super("Sigma Options", Category.getCategory("Sigma Visuals"));
    }
    public static Setting<EnumThemes> theme = new Setting<>("Theme", EnumThemes.Astolfo);
    public static Setting<ColorSetting> color1 = new Setting<>("Color 1", new ColorSetting(new Color(31, 76, 255).getRGB()), v -> theme.is(EnumThemes.Custom));
    public static Setting<ColorSetting> color2 = new Setting<>("Color 2", new ColorSetting(new Color(0, 187, 255).getRGB()), v -> theme.is(EnumThemes.Custom));
    public static Setting<ColorSetting> miscColor = new Setting<>("Misc Color", new ColorSetting(new Color(150, 150, 150).getRGB()));
    public static Setting<RectEnum> rectMode = new Setting<>("Rect Mode", RectEnum.New);
    public enum RectEnum {
        Astana, New;
    }
    @Override
    public void onEnable() {
        disable();
    }
}

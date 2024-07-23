package com.example;

import com.example.hud.CoordsElement;
import com.example.hud.TargetHudikElement;
import com.example.hud.WaterkaElement;
import com.example.modules.ChinaHat;
import com.example.modules.ExampleModule;
import com.example.modules.Themes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thunder.hack.api.IAddon;
import thunder.hack.cmd.Command;
import thunder.hack.gui.hud.HudElement;
import thunder.hack.modules.Module;

import java.util.Arrays;
import java.util.List;

public class ExampleMod implements IAddon {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static String version = "0.3";
	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}

	@Override
	public List<Module> getModules() {
		// Return a list of your modules here
		return Arrays.asList(
				new ExampleModule(),
				new ChinaHat(),
				new Themes());
	}

	@Override
	public List<Command> getCommands() {
		return Arrays.asList();
	}

	@Override
	public List<HudElement> getHudElements() {
		return Arrays.asList(
				new CoordsElement(),
				new TargetHudikElement(),
				new WaterkaElement());
	}

	@Override
	public String getPackage() {
		return "com.example";
	}

	@Override
	public String getName() {
		return "SigmaVisuals";
	}

	@Override
	public String getAuthor() {
		return "PAPASHKA";
	}

	@Override
	public String getRepo() {
		return "https://github.com/PAPASHKA1OR/Sigma-Visuals-Thunderhack-Addon/";
	}
}

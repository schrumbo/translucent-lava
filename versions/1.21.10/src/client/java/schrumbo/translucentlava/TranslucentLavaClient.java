package schrumbo.translucentlava;

import net.fabricmc.api.ClientModInitializer;
import schrumbo.translucentlava.commands.Commands;
import schrumbo.translucentlava.config.Config;
import schrumbo.translucentlava.config.ConfigManager;
import schrumbo.translucentlava.features.TransLava;

public class TranslucentLavaClient implements ClientModInitializer {
	public static Config config;
	public static ConfigManager configManager;
	@Override
	public void onInitializeClient() {
		configManager = new ConfigManager();
		config = ConfigManager.load();
		Commands.register();
		TransLava.init();
		TransLava.update(config.getEnabled());
	}
}
package zefir.mangelogs;


import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zefir.mangelogs.config.ConfigManager;;

public class MangeLogs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("mange-logs");
	@Override
	public void onInitialize() {
		LOGGER.info("Initialization of the MangeLogs");
		ConfigManager.registerConfigs();

		Events.RegisterEvents();

		Commands.registerCommands();
	}
}
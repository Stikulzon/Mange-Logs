package zefir.mangelogs;


import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zabi.minecraft.nbttooltip.NBTTooltip;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.NBTTooltipAccessor;;

public class MangeLogs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("mange-logs");
	public static NBTTooltipAccessor toolTip;
	@Override
	public void onInitialize() {
		LOGGER.info("Initialization of the MangeLogs");
		toolTip = (NBTTooltipAccessor) new NBTTooltip();
		ConfigManager.registerConfigs();

		Events.RegisterEvents();

		Commands.registerCommands();
	}
}
package zefir.mangelogs;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.*;

public class MangeLogs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("mange-logs");
	@Override
	public void onInitialize() {
		LOGGER.info("Initialization of the MangeLogs");
		SessionManager sessionMgr = new SessionManager();
		sessionMgr.CreateSessionId();
		LogWriter.WriteToLog("SessionID", sessionMgr.GetSessionId());

		Events.RegisterEvent();

		//register the reload command
		String CommandName = "reloadMangeLogs";

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal(CommandName)
				.requires(source -> source.hasPermissionLevel(2))
				.executes(context -> {
					Events.RegisterEvent();
					context.getSource().sendFeedback(() -> Text.literal("The config was reload"), false);
					return 1;})));
	}
}
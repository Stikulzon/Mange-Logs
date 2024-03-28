package zefir.mangelogs;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reloadMangeLogs")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    Events.RegisterEvents();
                    context.getSource().sendFeedback(() -> Text.literal("The config was reload"), false);
                    return 1;})));
    }
}

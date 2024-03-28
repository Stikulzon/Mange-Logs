package zefir.mangelogs;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import zefir.mangelogs.config.ConfigManager;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.literal;

public class Commands {
    public static void registerCommands(){
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("reloadMangeLogs")
                .requires(Permissions.require(Objects.requireNonNullElse(ConfigManager.getConfigReloadPermission(), "servercosmetics.reload")))
                .executes(context -> {
                    Events.RegisterEvents();
                    context.getSource().sendFeedback(() -> Text.literal("The config was reload"), false);
                    return 1;})));
    }
}

package zefir.mangelogs.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;

@Mixin(CommandManager.class)
public class CommandManagerMixin {

    @Inject(method = "execute", at = @At("HEAD"))
    private void onCommandExecute(ParseResults<ServerCommandSource> parseResults, String command, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("CommandExecution")) {
            if (parseResults.getContext().getSource().getEntity() instanceof ServerPlayerEntity player) {
                BlockPos pos = player.getBlockPos();
                String eventInfo = String.format(
                        "Player: %s | Command: %s | Location: World: %s Dimension: %s X: %d Y: %d Z: %d",
                        player.getName().getString(),
                        command,
                        parseResults.getContext().getSource().getWorld().getRegistryKey().getValue(),
                        parseResults.getContext().getSource().getWorld().getDimension(),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                );
                LogWriter.logToFile("CommandExecution", eventInfo);
            }
        }
    }
}

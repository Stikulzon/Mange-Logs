package zefir.mangelogs.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;

@Mixin(Block.class)
public class BlockBreakMixin {

    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBlockBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        if (ConfigManager.isLogEventEnabled("BlockBreak")) {
            if (player != null) {
                String eventInfo = String.format(
                        "Player: %s | Location: World: %s Dimension: %s X: %d Y: %d Z: %d",
                        player.getName().getString(),
                        world.getRegistryKey().getValue(),
                        world.getDimension(),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                );
                LogWriter.logToFile("BlockBreak", eventInfo);
            }
        }
    }
}

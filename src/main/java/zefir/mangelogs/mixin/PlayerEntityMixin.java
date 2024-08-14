package zefir.mangelogs.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.MangeLogs;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
    private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        if (ConfigManager.isLogEventEnabled("ItemDropped")) {
            if (cir.getReturnValue() != null) {
                PlayerEntity player = (PlayerEntity) (Object) this;

                NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(stack, player.getRegistryManager().getOps(NbtOps.INSTANCE));
                String nbtString = nbt != null ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Item: %s | NBT: %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        stack.getItem().getName().getString(),
                        nbtString
                );
                LogWriter.logToFile("ItemDropped", eventInfo);
            }
        }
    }
}

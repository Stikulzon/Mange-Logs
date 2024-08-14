package zefir.mangelogs.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zabi.minecraft.nbttooltip.NBTTooltip;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.MangeLogs;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.NBTTooltipAccessor;
import zefir.mangelogs.utils.Utils;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "triggerItemPickedUpByEntityCriteria", at = @At("HEAD"))
    private void onItemPickup(ItemEntity item, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("ItemPickup")) {
            if ((LivingEntity) (Object) this instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) (Object) this;
                ItemStack pickedStack = item.getStack();

                // Use encodeStack from NBTTooltip to get the NBT data as a compound
                NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(pickedStack, player.getRegistryManager().getOps(NbtOps.INSTANCE));
                String nbtString = !nbt.isEmpty() ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Item: %s | NBT: %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        pickedStack.getItem().getName().getString(),
                        nbtString
                );
                LogWriter.logToFile("ItemPickup", eventInfo);
            }
        }
    }
}

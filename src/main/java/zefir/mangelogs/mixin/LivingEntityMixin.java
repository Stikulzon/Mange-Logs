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
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

import static zefir.mangelogs.MangeLogs.getItemStackNbt;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "triggerItemPickedUpByEntityCriteria", at = @At("HEAD"))
    private void onItemPickup(ItemEntity item, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("ItemPickup")) {
            if ((LivingEntity) (Object) this instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) (Object) this;
                ItemStack pickedStack = item.getStack();

                // Use getItemStackNbt from NBTTooltip to get the NBT data as a compound
                NbtCompound nbt = getItemStackNbt(pickedStack, player.getRegistryManager().getOps(NbtOps.INSTANCE));
                String nbtString = !nbt.isEmpty() ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | PlayerUuid: %s | Location: %s | Item: %s | Nbt: %s",
                        player.getName().getString(),
                        player.getUuid().toString(),
                        Utils.formatPlayerLocation(player),
                        pickedStack.getItem().getName().getString()
                        ,
                        nbtString
                );
                LogWriter.logToFile("ItemPickup", eventInfo);
            }
        }
    }
}

package zefir.mangelogs.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "loot", at = @At("HEAD"))
    private void onLoot(ItemEntity item, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("MobPickupItem")) {
            MobEntity mobEntity = (MobEntity) (Object) this;
            ItemStack itemStack = item.getStack();
            NbtCompound nbt = itemStack.getNbt();
            String nbtString = nbt != null ? nbt.toString() : "No NBT";

            String eventInfo = String.format(
                    "Mob: %s | Location: %s | Item: %s | NBT: %s",
                    mobEntity.getName().getString(),
                    Utils.formatEntityLocation(mobEntity),
                    itemStack.getItem().getName().getString(),
                    nbtString
            );
            LogWriter.logToFile("MobPickupItem", eventInfo);
        }
    }
}
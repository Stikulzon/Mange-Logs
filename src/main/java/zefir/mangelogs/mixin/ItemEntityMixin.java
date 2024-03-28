package zefir.mangelogs.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Final @Shadow private static int DESPAWN_AGE;
    @Shadow private int health;
    @Inject(method = "tick", at = @At("HEAD"))
    private void onItemTick(CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("ItemDespawn")) {
            ItemEntity itemEntity = (ItemEntity) (Object) this;
            World world = itemEntity.getWorld();
            ItemStack itemStack = itemEntity.getStack();
            if (itemEntity.age >= DESPAWN_AGE) { // Check if item is about to despawn
                NbtCompound nbt = itemStack.getNbt();
                String nbtString = nbt != null ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Item: %s | Location: World: %s Dimension: %s X: %d Y: %d Z: %d | NBT: %s",
                        itemStack.getItem().getName().getString(),
                        world.getRegistryKey().getValue(),
                        world.getDimensionKey(),
                        itemEntity.getBlockPos().getX(),
                        itemEntity.getBlockPos().getY(),
                        itemEntity.getBlockPos().getZ(),
                        nbtString
                );
                LogWriter.logToFile("ItemDespawn", eventInfo);
            }
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void onItemDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        World world = itemEntity.getWorld();
        ItemStack itemStack = itemEntity.getStack();

        if (health - amount <= 0) { // Check if health will be zero or less
            NbtCompound nbt = itemStack.getNbt();
            String nbtString = nbt != null ? nbt.toString() : "No NBT";

            String eventInfo = String.format(
                    "Item: %s | Location: World: %s Dimension: %s X: %d Y: %d Z: %d | NBT: %s | Damage Source: %s",
                    itemStack.getItem().getName().getString(),
                    world.getRegistryKey().getValue(),
                    world.getDimensionKey(),
                    itemEntity.getBlockPos().getX(),
                    itemEntity.getBlockPos().getY(),
                    itemEntity.getBlockPos().getZ(),
                    nbtString,
                    source.getName()
            );
            LogWriter.logToFile("ItemDestroyed", eventInfo);
        }
    }
}

package zefir.mangelogs.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.MangeLogs;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "damage(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private void onItemDamage(int amount, ServerWorld world, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("ItemBreak")) {
            ItemStack is = (ItemStack) (Object) this;
            if (player != null && is.getDamage() + amount >= is.getMaxDamage()) {
                NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(is, player.getRegistryManager().getOps(NbtOps.INSTANCE));
                String nbtString = nbt != null ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Item: %s | NBT: %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        is.getItem().getName().getString(),
                        nbtString
                );
                LogWriter.logToFile("ItemBreak", eventInfo);
            }
        }
    }
}
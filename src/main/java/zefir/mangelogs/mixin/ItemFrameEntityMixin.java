package zefir.mangelogs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.MangeLogs;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @Inject(method = "interact", at = @At("RETURN"))
    private void onItemFrameInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemPlacedInFrame(player, hand, cir);
    }
    @Unique
    private void ItemPlacedInFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if (ConfigManager.isLogEventEnabled("ItemPlacedInFrame")) {
            ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;
            ItemStack heldItem = player.getStackInHand(hand);

            if (!heldItem.isEmpty()) {
                ItemStack itemInFrame = itemFrame.getHeldItemStack();
//                NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(itemInFrame, itemFrame.getRegistryManager().getOps(NbtOps.INSTANCE));
//                String nbtString = nbt != null ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Item: %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        itemInFrame.getItem().getName().getString()
//                        ,
//                        nbtString
                );
                LogWriter.logToFile("ItemPlacedInFrame", eventInfo);
            }
        }
    }
    @Inject(method = "dropHeldStack", at = @At("HEAD"))
    private void logItemDrop(Entity entity, boolean alwaysDrop, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("ItemFrameItemRemoved")) {
            ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;

            ItemStack itemInFrame = itemFrame.getHeldItemStack();
//            NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(itemInFrame, itemFrame.getRegistryManager().getOps(NbtOps.INSTANCE));
//            String nbtString = nbt != null ? nbt.toString() : "No NBT";

            String eventInfo = String.format(
                    "Location: %s | Item: %s",
                    Utils.formatEntityLocation(itemFrame),
                    itemInFrame.getItem().getName().getString()
//                    ,
//                    nbtString
            );
            LogWriter.logToFile("ItemFrameItemRemoved", eventInfo);
        }
    }
}
package zefir.mangelogs.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import zefir.mangelogs.utils.Utils;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @Inject(method = "interact", at = @At("RETURN"))
    private void onItemFrameInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemPlacedInFrame(player, hand, cir);
//        ItemTakenOutOfFrame(player, hand, cir);
    }
    @Unique
    private void ItemPlacedInFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;
        ItemStack heldItem = player.getStackInHand(hand);

        if (!heldItem.isEmpty()) {
            ItemStack itemInFrame = itemFrame.getHeldItemStack();
            NbtCompound nbt = itemInFrame.getNbt();
            String nbtString = nbt != null ? nbt.toString() : "No NBT";

            String eventInfo = String.format(
                    "Player: %s | Location: %s | Item: %s | NBT: %s",
                    player.getName().getString(),
                    Utils.formatPlayerLocation(player),
                    itemInFrame.getItem().getName().getString(),
                    nbtString
            );
            LogWriter.logToFile("ItemPlacedInFrame", eventInfo);
        }
    }
    @Inject(method = "dropHeldStack", at = @At("HEAD"))
    private void logItemDrop(Entity entity, boolean alwaysDrop, CallbackInfo ci) {
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;

        ItemStack itemInFrame = itemFrame.getHeldItemStack();
        NbtCompound nbt = itemInFrame.getNbt();
        String nbtString = nbt != null ? nbt.toString() : "No NBT";

        String eventInfo = String.format(
                "Location: %s | Item: %s | NBT: %s",
                Utils.formatEntityLocation(itemFrame),
                itemInFrame.getItem().getName().getString(),
                nbtString
        );
        LogWriter.logToFile("ItemFrameItemRemoved", eventInfo);
    }
}
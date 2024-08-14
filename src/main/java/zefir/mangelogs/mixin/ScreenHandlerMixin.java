package zefir.mangelogs.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.MangeLogs;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Inject(method = "onSlotClick", at = @At("HEAD"))
    private void onUIClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (ConfigManager.isLogEventEnabled("UIClick")) {
            if (player != null && slotIndex >= 0) {
                Slot slot = ((ScreenHandler) (Object) this).getSlot(slotIndex);
                ItemStack clickedStack = slot.getStack();

                NbtCompound nbt = MangeLogs.toolTip.mangelogs$encodeStack(clickedStack, player.getRegistryManager().getOps(NbtOps.INSTANCE));
                String nbtString = nbt != null ? nbt.toString() : "No NBT";

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Slot: %d | Button: %d | Action: %s | Item: %s | NBT: %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        slotIndex,
                        button,
                        actionType,
                        clickedStack.getItem().getName().getString(),
                        nbtString
                );
                LogWriter.logToFile("UIClick", eventInfo);
            }
        }
    }
}

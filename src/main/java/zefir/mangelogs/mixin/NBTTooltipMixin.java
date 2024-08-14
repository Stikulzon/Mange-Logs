package zefir.mangelogs.mixin;

import com.mojang.serialization.DynamicOps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import zabi.minecraft.nbttooltip.NBTTooltip;
import zefir.mangelogs.utils.NBTTooltipAccessor;

@Mixin(NBTTooltip.class)
public class NBTTooltipMixin implements NBTTooltipAccessor {
    @Shadow
    private static NbtCompound encodeStack(ItemStack stack, DynamicOps<NbtElement> ops) {
        throw new AssertionError();
    }

    @Override
    public NbtCompound mangelogs$encodeStack(ItemStack stack, DynamicOps<NbtElement> ops){
        return encodeStack(stack, ops);
    }
}

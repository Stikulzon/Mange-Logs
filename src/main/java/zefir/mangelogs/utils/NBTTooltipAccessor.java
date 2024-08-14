package zefir.mangelogs.utils;

import com.mojang.serialization.DynamicOps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public interface NBTTooltipAccessor {
    NbtCompound mangelogs$encodeStack(ItemStack stack, DynamicOps<NbtElement> ops);
}

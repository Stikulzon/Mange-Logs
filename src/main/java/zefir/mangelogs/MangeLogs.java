package zefir.mangelogs;


import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.fabricmc.api.ModInitializer;

import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zefir.mangelogs.config.ConfigManager;

public class MangeLogs implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("mange-logs");
	@Override
	public void onInitialize() {
		LOGGER.info("Initialization of the MangeLogs");
		ConfigManager.registerConfigs();

		Events.RegisterEvents();

		Commands.registerCommands();
	}

	public static NbtCompound getItemStackNbt(ItemStack stack, DynamicOps<NbtElement> ops) {
		DataResult<NbtElement> result = ComponentChanges.CODEC.encodeStart(ops, stack.getComponentChanges());
		result.ifError(e->{});
		NbtElement nbtElement = result.getOrThrow();
		return (NbtCompound) nbtElement;
	}
}
package zefir.mangelogs.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utils {
    public static String formatPlayerLocation(PlayerEntity player) {
        return String.format(
                "World: %s X: %d Y: %d Z: %d",
                player.getWorld().getRegistryKey().getValue(),
                player.getBlockPos().getX(),
                player.getBlockPos().getY(),
                player.getBlockPos().getZ()
        );
    }

    public static String formatEntityLocation(Entity entity) {
        return String.format(
                "World: %s X: %d Y: %d Z: %d",
                entity.getWorld().getRegistryKey().getValue(),
                entity.getBlockPos().getX(),
                entity.getBlockPos().getY(),
                entity.getBlockPos().getZ()
        );
    }

    public static String EntityType(Entity entity) {
        String EntityName = entity.getType().toString();
        String[] name = EntityName.split("\\.");
        return name[name.length-1];
    }
    public static String getItemNameInHand(PlayerEntity player, Hand hand) {
        ItemStack heldItemStack = player.getStackInHand(hand);
        Item heldItem = heldItemStack.getItem();
        return getItemName(heldItem);
    }
    public static String getItemName(Item item) {
        String ItemName = item.getTranslationKey();
        String[] name = ItemName.split("\\.");
        return name[name.length-1];
    }
    public static String Block(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block.getTranslationKey();
    }
    public static String getEntityPos(Entity entity) {
        return "{x=" + entity.getPos().getX() + ", y=" + entity.getPos().getY() + ", z=" + entity.getPos().getZ() + "}";
    }
}

package zefir.mangelogs.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class getName {
    public static String Entity(Entity entity) {
            String EntityName = entity.getName().getString();
            String[] name = EntityName.split("\\.");
            return name[name.length-1];
    }
    public static String Item(Item item) {
        String ItemName = item.getTranslationKey();
        String[] name = ItemName.split("\\.");
        return name[name.length-1];
    }
    public static String Block(World world, BlockPos blockPos) {
        Block block = world.getBlockState(blockPos).getBlock();
        return block.getTranslationKey();
    }
}


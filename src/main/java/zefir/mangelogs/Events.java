package zefir.mangelogs;

import zefir.mangelogs.utils.getName;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents.Load;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class Events {
    public static void RegisterEvent() {
        // Called when left-clicking (“attacking”) a block.
        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
            MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] hits [" + world.getBlockState(blockPos).getBlock() + "] at pos" + getName.BlockPos(blockPos));
            return ActionResult.PASS;
        });

        // Called when left-clicking (“attacking”) an entity.
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
        if (entity instanceof PlayerEntity) {
            MangeLogs.LOGGER.info("The player [" + player.getName().getString() + " ]hit player " + entity.getName().getString());
        } else if (entity.hasCustomName()){
            MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] hit entity " + getName.EntityType(entity) + " with name " + entity.getName().getString());
        } else {
            MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] hit entity " + entity.getName().getString());
        }
            return ActionResult.PASS;
        });

        // Called when right-clicking (“using”) an item
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack heldItemStack = player.getStackInHand(hand);
            Item heldItem = heldItemStack.getItem();

            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " used " + getName.Item(heldItem));
            return TypedActionResult.pass(ItemStack.EMPTY);
        });

        // Called when right-clicking (“using”) a block
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " use " + getName.Block(world, hitResult.getBlockPos()));
            return ActionResult.PASS;
        });
        // Called when right-clicking (“using”) an entity.
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(hitResult != null) {
                if (entity instanceof PlayerEntity) {
                    MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] right-clicking player " + entity.getName().getString());
                } else if (entity.hasCustomName()){
                    MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] right-clicking entity " + getName.EntityType(entity) + " with name " + entity.getName().getString());
                } else {
                    MangeLogs.LOGGER.info("The player [" + player.getName().getString() + "] right-clicking entity " + entity.getName().getString());
                }
            }
            return ActionResult.PASS;
        });





        // Called when lighting spawns
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " use " + getName.Block(world, hitResult.getBlockPos()));
            return ActionResult.PASS;
        });
    }
}

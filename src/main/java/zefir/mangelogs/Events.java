package zefir.mangelogs;

import zefir.mangelogs.utils.getName;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class Events {
    public static void RegisterEvent() {
        // Player break a block
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " to break " + getName.Block(world,pos));
            return ActionResult.PASS;
        });

        // Called when left-clicking (“attacking”) an entity.
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
        if (entity instanceof PlayerEntity) {
            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " hit player " + getName.Entity(entity));
        } else {
            MangeLogs.LOGGER.info("The player " + player.getName().getString() + " hit entity " + getName.Entity(entity));
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
                MangeLogs.LOGGER.info("The player " + player.getName().getString() + " use " + getName.Entity(hitResult.getEntity()));
            }
            return ActionResult.PASS;
        });
    }
}

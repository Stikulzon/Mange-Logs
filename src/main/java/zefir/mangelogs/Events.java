package zefir.mangelogs;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public class Events {
    public static void RegisterEvent() {

        // Called when left-clicking (“attacking”) a block.
        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
            String info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " hits [" + world.getBlockState(blockPos).getBlock() + "] at " + Utils.BlockPos(blockPos);
            MangeLogs.LOGGER.info(info);
            return ActionResult.PASS;
        });

        // Called when left-clicking (“attacking”) an entity.
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            String info;
            if (entity instanceof PlayerEntity) {
                info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " hits player " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
            } else if (entity.hasCustomName()) {
                info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " hits entity " + Utils.EntityType(entity) + " with name " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
            } else {
                info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " hits entity " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
            }
            MangeLogs.LOGGER.info(info);
            return ActionResult.PASS;
        });

        // Called when right-clicking (“using”) an item
        UseItemCallback.EVENT.register((player, world, hand) -> {
            String info = "The player " + player.getName().getString() + " at pos " + Utils.getPlayerPos(player) + " used " + Utils.getItemNameInHand(player, hand);
            MangeLogs.LOGGER.info(info);
            return TypedActionResult.pass(ItemStack.EMPTY);
        });

        // Called when right-clicking (“using”) a block
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            String info = "The player " + player.getName().getString() + " at pos " + Utils.getPlayerPos(player) + " use " + Utils.Block(world, hitResult.getBlockPos()) + " with " + Utils.getItemNameInHand(player, hand);
            MangeLogs.LOGGER.info(info);
            return ActionResult.PASS;
        });

        // Called when right-clicking (“using”) an entity.
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (hitResult != null) {
                String info;
                if (entity instanceof PlayerEntity) {
                    info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " right-clicking player " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
                } else if (entity.hasCustomName()) {
                    info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " right-clicking entity " + Utils.EntityType(entity) + " with name " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
                } else {
                    info = "The player [" + player.getName().getString() + "] at pos " + Utils.getPlayerPos(player) + " right-clicking entity " + entity.getName().getString() + " at pos " + Utils.getEntityPos(entity);
                }
                MangeLogs.LOGGER.info(info);
            }
            return ActionResult.PASS;
        });

        // Called when lighting spawns
        ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> {
            if (entity instanceof LightningEntity) {
                String info = "The lighting hit at coordinates " + Utils.getEntityPos(entity);
                MangeLogs.LOGGER.info(info);
                LogWriter.WriteToLog("Lighting", info);
            }
        });

//        ServerLivingEntityEvents.ALLOW_DEATH
    }
}

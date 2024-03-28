package zefir.mangelogs;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import zefir.mangelogs.config.ConfigManager;
import zefir.mangelogs.utils.Utils;

public class Events {
    public static void RegisterEvents() {

        // Attack Block Event
        if (ConfigManager.isLogEventEnabled("AttackBlock")) {
            AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> {
                String eventInfo = String.format(
                        "Player: %s | Location: %s | Action: Attacked block %s at %s",
                        player.getName().getString(),
                        zefir.mangelogs.utils.Utils.formatPlayerLocation(player),
                        world.getBlockState(blockPos).getBlock(),
                        blockPos
                );
                LogWriter.logToFile("AttackBlock", eventInfo);
                return ActionResult.PASS;
            });
        }

        // Attack Entity Event
        if (ConfigManager.isLogEventEnabled("AttackEntity")) {
            AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                String entityInfo = entity instanceof PlayerEntity
                        ? "player " + entity.getName().getString()
                        : entity.hasCustomName()
                        ? "entity " + Utils.EntityType(entity) + " with name " + entity.getName().getString()
                        : "entity " + entity.getName().getString();

                String eventInfo = String.format(
                        "Player: %s | Location: %s | Action: Attacked %s at %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        entityInfo,
                        Utils.getEntityPos(entity)
                );
                LogWriter.logToFile("AttackEntity", eventInfo);
                return ActionResult.PASS;
            });
        }

        // Attack Item Frame Event
        if (ConfigManager.isLogEventEnabled("ItemFrameItemRemoved")) {
            AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                if (entity instanceof ItemFrameEntity) {
                    String eventInfo = String.format(
                            "Player: %s | Location: %s | Action: Attacked Item Frame at %s",
                            player.getName().getString(),
                            Utils.formatPlayerLocation(player),
                            Utils.getEntityPos(entity)
                    );
                    LogWriter.logToFile("ItemFrameItemRemoved", eventInfo);
                }
                return ActionResult.PASS;
            });
        }

        // Use Item Event
        if (ConfigManager.isLogEventEnabled("UseItem")) {
            UseItemCallback.EVENT.register((player, world, hand) -> {
                String eventInfo = String.format(
                        "Player: %s | Location: %s | Action: Used item %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        Utils.getItemNameInHand(player, hand)
                );
                LogWriter.logToFile("UseItem", eventInfo);
                return TypedActionResult.pass(ItemStack.EMPTY);
            });
        }

        // Use Item Event
        if (ConfigManager.isLogEventEnabled("EggsThrown")) {
            UseItemCallback.EVENT.register((player, world, hand) -> {
                if (player.getStackInHand(hand).getItem() == Items.EGG) {
                    String eventInfo = String.format(
                            "Player: %s | Location: %s | Action: Eggs thrown %s",
                            player.getName().getString(),
                            Utils.formatPlayerLocation(player),
                            Utils.getItemNameInHand(player, hand)
                    );
                    LogWriter.logToFile("EggsThrown", eventInfo);
                }
                return TypedActionResult.pass(ItemStack.EMPTY);
            });
        }

        // Use Block Event
        if (ConfigManager.isLogEventEnabled("UseBlock")) {
            UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
                String eventInfo = String.format(
                        "Player: %s | Location: %s | Action: Used block %s with item %s",
                        player.getName().getString(),
                        Utils.formatPlayerLocation(player),
                        Utils.Block(world, hitResult.getBlockPos()),
                        Utils.getItemNameInHand(player, hand)
                );
                LogWriter.logToFile("UseBlock", eventInfo);
                return ActionResult.PASS;
            });
        }

        // Use Entity Event
        if (ConfigManager.isLogEventEnabled("UseEntity")) {
            UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
                if (hitResult != null) {
                    String entityInfo = entity instanceof PlayerEntity
                            ? "player " + entity.getName().getString()
                            : entity.hasCustomName()
                            ? "entity " + Utils.EntityType(entity) + " with name " + entity.getName().getString()
                            : "entity " + entity.getName().getString();

                    String eventInfo = String.format(
                            "Player: %s | Location: %s | Action: Right-clicked %s at %s",
                            player.getName().getString(),
                            Utils.formatPlayerLocation(player),
                            entityInfo,
                            Utils.getEntityPos(entity)
                    );
                    LogWriter.logToFile("UseEntity", eventInfo);
                }
                return ActionResult.PASS;
            });
        }

        // Lightning Event
        if (ConfigManager.isLogEventEnabled("Lightning")) {
            ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerWorld world) -> {
                if (entity instanceof LightningEntity) {
                    String eventInfo = String.format(
                            "Lightning struck at coordinates %s",
                            Utils.getEntityPos(entity)
                    );
                    LogWriter.logToFile("Lightning", eventInfo);
                }
            });
        }
    }
}

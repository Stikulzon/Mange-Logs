package zefir.mangelogs.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.config.ConfigManager;

@Mixin(VehicleEntity.class)
public class VehicleEntityMixin {
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/VehicleEntity;killAndDropSelf(Lnet/minecraft/entity/damage/DamageSource;)V", shift = At.Shift.AFTER))
    private void onMinecartDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.isLogEventEnabled("MinecartDestroyed")) {
            VehicleEntity minecart = (VehicleEntity) (Object) this;
            World world = minecart.getWorld();
            String eventInfo = String.format(
                    "Minecart Type: %s | Location: World: %s Dimension: %s X: %d Y: %d Z: %d | Damage Source: %s",
                    minecart.getType().getName().getString(),
                    world.getRegistryKey().getValue(),
                    world.getDimension(),
                    minecart.getBlockPos().getX(),
                    minecart.getBlockPos().getY(),
                    minecart.getBlockPos().getZ(),
                    source.getName()
            );
            LogWriter.logToFile("MinecartDestroyed", eventInfo);
        }
    }
}

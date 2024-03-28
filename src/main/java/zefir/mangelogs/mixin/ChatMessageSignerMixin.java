package zefir.mangelogs.mixin;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zefir.mangelogs.LogWriter;
import zefir.mangelogs.utils.Utils;

@Mixin(SentMessage.Chat.class)
public class ChatMessageSignerMixin {
    @Final
    @Shadow
    private SignedMessage message;

    @Inject(method = "send", at = @At("HEAD"))
    private void onChatMessage(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params, CallbackInfo ci) {
        String eventInfo = String.format(
                "Player: %s | Location: %s | Message: %s",
                sender.getName().getString(),
                Utils.formatPlayerLocation(sender),
                message.getContent().getString()
        );
        LogWriter.logToFile("ChatMessage", eventInfo);
    }
}
package io.github.boogiemonster1o1.mcnotify.mixin;

import io.github.boogiemonster1o1.mcnotify.MCNotify;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
	@Inject(method = "onChatMessage", at = @At("HEAD"))
	public void onChatMessageReceived(SignedMessage message, MessageType.Parameters params, CallbackInfo ci) {
		String content = params.applyChatDecoration(message.getContent()).getString();
		if (content.toLowerCase().contains(MinecraftClient.getInstance().getSession().getUsername().toLowerCase())) {
			MCNotify.queueMessage(content);
		}
	}
}

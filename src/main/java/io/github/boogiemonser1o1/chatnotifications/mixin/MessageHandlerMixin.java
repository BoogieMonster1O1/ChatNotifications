package io.github.boogiemonser1o1.chatnotifications.mixin;

import io.github.boogiemonser1o1.chatnotifications.ChatNotifications;
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
	public void onChatMessageRecieved(SignedMessage message, MessageType.Parameters params, CallbackInfo ci) {
		String content = params.applyChatDecoration(message.getContent()).getString();
		if (content.toLowerCase().contains("@" + MinecraftClient.getInstance().getSession().getUsername().toLowerCase())) {
			ChatNotifications.queueMessage(content);
		}
	}
}

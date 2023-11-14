package io.github.boogiemonster1o1.mcnotify.mixin;

import com.mojang.authlib.GameProfile;
import io.github.boogiemonster1o1.mcnotify.MCNotify;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {
	@Inject(method = "onChatMessage", at = @At("HEAD"))
	public void onChatMessageReceived(SignedMessage message, GameProfile sender, MessageType.Parameters params, CallbackInfo ci) {
		String content = params.applyChatDecoration(message.getContent()).getString();
		if (content.toLowerCase().contains(MinecraftClient.getInstance().getSession().getUsername().toLowerCase())) {
			MCNotify.queueMessage(content);
		}
	}
}

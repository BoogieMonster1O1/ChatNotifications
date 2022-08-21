package io.github.boogiemonser1o1.chatnotifications;

import io.github.boogiemonser1o1.chatnotifications.provider.MacOSProvider;
import io.github.boogiemonser1o1.chatnotifications.provider.NotificationProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatNotifications implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ChatNotifications");
	public static final ConfigHolder<ModConfig> CONFIG = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
	private static NotificationProvider PROVIDER = NotificationProvider.DUMMY;
	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing ChatNotifications");
		Util.OperatingSystem os = Util.getOperatingSystem();
		if (os == Util.OperatingSystem.OSX) {
			PROVIDER = new MacOSProvider();
		}
		PROVIDER.init();
		queueMessage("Lol");
	}

	public static void queueMessage(String message) {
		PROVIDER.queueMessage(message);
	}
}

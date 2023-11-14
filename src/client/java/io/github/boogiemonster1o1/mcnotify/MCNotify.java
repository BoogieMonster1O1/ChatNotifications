package io.github.boogiemonster1o1.mcnotify;

import io.github.boogiemonster1o1.mcnotify.provider.MacOSProvider;
import io.github.boogiemonster1o1.mcnotify.provider.NotificationProvider;
import io.github.boogiemonster1o1.mcnotify.provider.WindowsProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MCNotify implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("MCNotify");
	public static final ConfigHolder<ModConfig> CONFIG = AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
	private static NotificationProvider PROVIDER = NotificationProvider.DUMMY;
	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing MCNotify");
		Util.OperatingSystem os = Util.getOperatingSystem();
		PROVIDER = switch(Util.getOperatingSystem())
		{
			case OSX -> new MacOSProvider();
			case WINDOWS -> new WindowsProvider();
			default -> NotificationProvider.DUMMY;
		};

		PROVIDER.init();

		ClientLifecycleEvents.CLIENT_STOPPING.register((client -> PROVIDER.shutdown()));
	}

	public static void queueMessage(String message) {
		PROVIDER.queueMessage(message);
	}
}

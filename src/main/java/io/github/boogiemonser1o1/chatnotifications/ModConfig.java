package io.github.boogiemonser1o1.chatnotifications;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "Chat Notifications")
public class ModConfig implements ConfigData {
	public boolean enabled = true;
}

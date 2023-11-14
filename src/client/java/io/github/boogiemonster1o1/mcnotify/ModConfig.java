package io.github.boogiemonster1o1.mcnotify;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "MCNotify")
public class ModConfig implements ConfigData {
	public boolean enabled = true;
}

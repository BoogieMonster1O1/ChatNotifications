package io.github.boogiemonster1o1.mcnotify.provider;

import java.io.IOException;

public class MacOSProvider implements NotificationProvider {
	@Override
	public void queueMessage(String message) {
		try {
			String[] args = {"osascript", "-e",
					"""
					display notification "%s" with title "Minecraft" subtitle "New Chat Mention" sound name "Purr"
					""".formatted(message)
			};
			Runtime.getRuntime().exec(args);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void shutdown() {

	}
}

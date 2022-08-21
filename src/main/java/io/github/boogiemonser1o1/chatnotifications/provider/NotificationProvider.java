package io.github.boogiemonser1o1.chatnotifications.provider;

import io.github.boogiemonser1o1.chatnotifications.ChatNotifications;

import net.minecraft.text.Text;

public interface NotificationProvider {
	void queueMessage(String message);

	void init();

	NotificationProvider DUMMY = new NotificationProvider() {
		@Override
		public void queueMessage(String message) {
		}

		@Override
		public void init() {
			ChatNotifications.LOGGER.warn("Notifications are not supported on this platform");
		}
	};
}

package io.github.boogiemonster1o1.mcnotify.provider;

import io.github.boogiemonster1o1.mcnotify.MCNotify;

public interface NotificationProvider {
	void queueMessage(String message);

	void init();

	void shutdown();

	NotificationProvider DUMMY = new NotificationProvider() {
		@Override
		public void queueMessage(String message) {
		}

		@Override
		public void init() {
			MCNotify.LOGGER.warn("Notifications are not supported on this platform");
		}

		@Override
		public void shutdown() {
		}
	};
}

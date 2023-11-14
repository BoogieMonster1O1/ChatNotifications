package io.github.boogiemonster1o1.mcnotify.provider;

import io.github.boogiemonster1o1.mcnotify.MCNotify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class WindowsProvider implements NotificationProvider {
    boolean isInitialized;

    @Override
    public void init() {
        InputStream fileStream = this.getClass().getResourceAsStream("/native/win32-x86-64/MCNotifyNative.dll");
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + "MCNotifyNative.dll");
        tmpFile.delete();
        try {
            if (fileStream == null) throw new FileNotFoundException("Library not found in JAR");
            Files.copy(fileStream, tmpFile.toPath().toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.load(tmpFile.getAbsolutePath());

        try {
            Native.init();
            isInitialized = true;
        } catch (Exception ex) {
            MCNotify.LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public void queueMessage(String message) {
        if (!isInitialized) return;

        try {
            Native.sendNotification(message);
        } catch (Exception ex) {
            MCNotify.LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public void shutdown() {
        if (!isInitialized) return;

        Native.shutdown();
    }

    // JNI for now so that i can use an explicit path for library
    class Native {
        static native boolean init();

        static native void sendNotification(String content);

        static native void shutdown();
    }
}

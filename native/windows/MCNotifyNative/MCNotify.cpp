#include "pch.h"
#include "MCNotify.h"

using namespace winrt::Microsoft::Windows::AppNotifications;

JNIEXPORT jboolean JNICALL Java_io_github_boogiemonster1o1_mcnotify_provider_WindowsProvider_00024Native_init(JNIEnv* env, jclass)
{
    try
    {
        winrt::init_apartment();
        HRESULT hr{ MddBootstrapInitialize2(WINDOWSAPPSDK_RELEASE_MAJORMINOR, WINDOWSAPPSDK_RELEASE_FORMATTED_VERSION_TAG_W, { WINDOWSAPPSDK_RUNTIME_VERSION_UINT64 }, MddBootstrapInitializeOptions_None) };
        if (FAILED(hr))
        {
            auto outputString = std::vformat("Failed to bootstrap WASDK: {#x}", std::make_format_args((uint32_t)hr));
            OutputDebugStringA(outputString.c_str());
            jclass cls = env->FindClass("java/lang/Exception");
            env->ThrowNew(cls, outputString.c_str());
            return JNI_FALSE;
        }

        AppNotificationManager::Default().NotificationInvoked([](const auto&, AppNotificationActivatedEventArgs  const& notificationActivatedEventArgs)
            {
                return;
            });
        AppNotificationManager::Default().Register();
    }
    catch (winrt::hresult_error const& ex)
    {
        auto outputString = std::vformat("Failed to initialize MCNotifyNative: {} {}", std::make_format_args((uint32_t)ex.code(), winrt::to_string(ex.message())));
        OutputDebugStringA(outputString.c_str());
        jclass cls = env->FindClass("java/lang/Exception");
        env->ThrowNew(cls, outputString.c_str());
    }

    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_io_github_boogiemonster1o1_mcnotify_provider_WindowsProvider_00024Native_sendNotification(JNIEnv* env, jclass, jstring content)
{
    if (!content) return;

    jboolean isCopy;
    const char* convertedValue = (env)->GetStringUTFChars(content, &isCopy);
    std::string str = convertedValue;
#ifdef DEBUG
    OutputDebugStringA(str.c_str());
#endif // DEBUG

    try
    {
        winrt::hstring payload = winrt::to_hstring(str);
        auto toast{ AppNotification(payload) };
        AppNotificationManager::Default().Show(toast);
    }
    catch (winrt::hresult_error const& ex)
    {
        auto outputString = std::vformat("Failed to display notification: {} {}", std::make_format_args((uint32_t)ex.code(), winrt::to_string(ex.message())));
        OutputDebugStringA(outputString.c_str());
        jclass cls = env->FindClass("java/lang/Exception");
        env->ThrowNew(cls, outputString.c_str());
    }
}

JNIEXPORT void JNICALL Java_io_github_boogiemonster1o1_mcnotify_provider_WindowsProvider_00024Native_shutdown(JNIEnv*, jclass)
{
    // we don't really care about errors here since we're shutting down anyways
    RemoveNotifications();
    AppNotificationManager::Default().Unregister();
    winrt::uninit_apartment();
    MddBootstrapShutdown();
}

winrt::fire_and_forget RemoveNotifications()
{
    co_await AppNotificationManager::Default().RemoveAllAsync();
}

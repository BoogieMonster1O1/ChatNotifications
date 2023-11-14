#include "pch.h"
#include "MCNotify.h"
#include "Notifications.h"

JNIEXPORT jboolean JNICALL Java_io_github_boogiemonster1o1_mcnotify_provider_WindowsProvider_00024Native_init(JNIEnv* env, jclass)
{
    winrt::init_apartment();
    Notifications::Init(L"Default", L"Minecraft", L"");

    return JNI_TRUE;
}

JNIEXPORT void JNICALL Java_io_github_boogiemonster1o1_mcnotify_provider_WindowsProvider_00024Native_sendNotification(JNIEnv* env, jclass, jstring content)
{
    if (!content) return;

    auto strBytes = (env)->GetStringChars(content, NULL);
    std::wstring str((const wchar_t*)strBytes);
    (env)->ReleaseStringChars(content, strBytes);

    try
    {
        Notifications::Send(L"", str);
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
    Notifications::Shutdown();
}

#pragma once

#define WIN32_LEAN_AND_MEAN             // Exclude rarely-used stuff from Windows headers
// Windows Header Files
#include <windows.h>
#include <wil/cppwinrt.h>
#include <wil/registry.h>
#include <winrt/Windows.Foundation.h>
#include <winrt/Windows.Data.Xml.Dom.h>
#include <winrt/Windows.UI.Notifications.h>
#include <NotificationActivationCallback.h>

// STL header files
#include <string>
#include <format>

// JNI
#include <jni.h>
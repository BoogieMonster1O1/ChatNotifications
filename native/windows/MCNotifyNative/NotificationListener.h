#pragma once

struct NotificationListener : winrt::implements<NotificationListener, INotificationActivationCallback>
{
	HRESULT __stdcall Activate(LPCWSTR appUserModelId, LPCWSTR invokedArgs, const NOTIFICATION_USER_INPUT_DATA* data, ULONG count) override;
};


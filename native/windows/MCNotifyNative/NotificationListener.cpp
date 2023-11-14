#include "pch.h"
#include "NotificationListener.h"

HRESULT __stdcall NotificationListener::Activate(LPCWSTR appUserModelId, LPCWSTR invokedArgs, const NOTIFICATION_USER_INPUT_DATA* data, ULONG count)
{
	return S_OK;
}

#pragma once

namespace Notifications
{
	void Init(std::wstring aumidSuffix, std::wstring name, std::wstring iconPath);
	void Send(std::wstring name, std::wstring content);
	void Shutdown();
}


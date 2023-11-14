#include <string>
#include <iostream>
#include "../MCNotifyNative/Notifications.h"

int main()
{
	Notifications::Init(L"MyTestAumid", L"Test", L"C:\\Users\\Nik\\Desktop\\copilot.png");
	Notifications::Send(L"", L"<e> beanz");
	std::cin.get();
	Notifications::Shutdown();
}
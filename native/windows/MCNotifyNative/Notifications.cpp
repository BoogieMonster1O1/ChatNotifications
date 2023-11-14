#include "pch.h"
#include "Notifications.h"
#include "NotificationListener.h"

using namespace winrt::Windows::UI::Notifications;

struct ComServer : winrt::implements<ComServer, IClassFactory>
{
	HRESULT __stdcall CreateInstance(IUnknown* pUnkOuter, REFIID riid, void** ppvObject) override
	{
		*ppvObject = nullptr;

		if (pUnkOuter)
		{
			return CLASS_E_NOAGGREGATION;
		}

		return winrt::make<NotificationListener>()->QueryInterface(riid, ppvObject);
	}

	HRESULT __stdcall LockServer(BOOL fLock) override
	{
		return S_OK;
	}
};

std::wstring aumid;
std::wstring name;
std::wstring iconPath;

GUID classGuid;
DWORD registration;

void Notifications::Init(std::wstring aumidSuffix, std::wstring name, std::wstring iconPath)
{
	winrt::init_apartment();

	::aumid = std::vformat(L"MCNotify.{}", std::make_wformat_args(aumidSuffix));
	::name = name;
	::iconPath = iconPath;
	 
	CoCreateGuid(&classGuid);

	const auto regKey = wil::reg::create_unique_key(
		HKEY_CURRENT_USER,
		std::vformat(L"Software\\Classes\\AppUserModelId\\{}", std::make_wformat_args(aumid)).c_str(),
		wil::reg::key_access::readwrite
	);

	wil::reg::set_value_expanded_string(regKey.get(), L"DisplayName", name.c_str());
	wil::reg::set_value_expanded_string(regKey.get(), L"IconUri", iconPath.c_str());
	wil::reg::set_value_string(regKey.get(), L"IconBackgroundColor", L"00000000");

	wil::unique_cotaskmem_string guidString;
	StringFromCLSID(classGuid, guidString.put());
	wil::reg::set_value_string(regKey.get(), L"CustomActivator", guidString.get());

	winrt::check_hresult(CoRegisterClassObject(classGuid, winrt::make<ComServer>().get(), CLSCTX_LOCAL_SERVER, REGCLS_SINGLEUSE, &registration));
}

void Notifications::Send(std::wstring name, std::wstring content)
{
	winrt::Windows::Data::Xml::Dom::XmlDocument doc;
	doc.LoadXml(LR"(<toast>
 <visual>
   <binding template="ToastGeneric">
	 <text>New notification</text>
	 <text></text>
   </binding>
 </visual>
 <actions>
	   <input id="textBox" type="text" placeHolderContent="Reply"/>
	   <action
		 content="Send"
		 hint-inputId="textBox"
		 activationType="background"
		 arguments="action=reply"/>
   </actions>
</toast>)");
	doc.SelectSingleNode(L"/toast/visual/binding/text[1]").InnerText();
	doc.SelectSingleNode(L"/toast/visual/binding/text[2]").InnerText(content);

	auto toastNotifier = ToastNotificationManager::CreateToastNotifier(aumid);
	toastNotifier.Show(ToastNotification(doc));
}

void Notifications::Shutdown()
{
	ToastNotificationManager::History().Clear(aumid);

	CoRevokeClassObject(registration);

	const auto regKey = wil::reg::open_unique_key(
		HKEY_CURRENT_USER,
		L"Software\\Classes\\AppUserModelId",
		wil::reg::key_access::readwrite
	);

	RegDeleteKeyW(regKey.get(), aumid.c_str());
}

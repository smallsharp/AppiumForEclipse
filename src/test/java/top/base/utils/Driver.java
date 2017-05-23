package top.base.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.events.EventFiringWebDriverFactory;

/**
 * 初始化driver
 * @author lee
 *
 */
public class Driver {

	private static AndroidDriver<WebElement> mdriver;
	
	public Driver() {
		// TODO Auto-generated constructor stub
	}

	public static AndroidDriver<WebElement> newInstance() {

		if (mdriver == null) {
			PropertyUtil pro = new PropertyUtil("/app.properties"); // 这里需要加个/表示类的根目录,从配置中取数据
			String udid1 = pro.getValue("meizu");
			String appPackage = pro.getValue("appPackage");
			String appActivity = pro.getValue("appActivity");
			initAndroidDriver(udid1,udid1,"4723", appPackage, appActivity);
		}
		return mdriver;
	}
	
	public static AndroidDriver<WebElement> getMdriver() {
		return mdriver;
	}

	public static void setMdriver(AndroidDriver<WebElement> mdriver) {
		Driver.mdriver = mdriver;
	}

	private static void initAndroidDriver(String deviceName,String udid,String port, String appPackage, String appActivity) {

		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "apps");
		File app = new File(appDir, "play_debug.apk");		// 指定app的存放目录

		DesiredCapabilities dc1 = new DesiredCapabilities();
		dc1.setCapability("unicodeKeyboard", "True");		// 支持中文输入
		dc1.setCapability("resetKeyboard", "True");		// 重置输入法
		// dc.setCapability("noReset", true);		// 不需要再次安装
		dc1.setCapability("noSign", "True");
		dc1.setCapability("platformName", "Android");
		dc1.setCapability("deviceName", deviceName);
		dc1.setCapability("udid", udid);
		dc1.setCapability("platformVersion", "5.0");
		dc1.setCapability("appPackage", appPackage);
		dc1.setCapability("appActivity", appActivity);
		dc1.setCapability("app", app.getAbsolutePath());

		try {

			mdriver = new AndroidDriver<WebElement>(new URL("http://192.168.101.201:"+port+"/wd/hub"), dc1);
			// 注册MyDriverListener监听事件
			mdriver = EventFiringWebDriverFactory.getEventFiringWebDriver(mdriver, new MyElementEventListener());

		} catch (MalformedURLException e) {
			Reporter.log("========== 测试环境启动失败，请重试 ==========", true);
			e.printStackTrace();
		}
		// 全局等待20秒
		mdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		Reporter.log("========== 环境准备完毕，测试即将开始 ==========", true);
	}

}

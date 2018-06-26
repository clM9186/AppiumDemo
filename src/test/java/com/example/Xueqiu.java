package com.example;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Xueqiu {

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "192.168.56.101:5555");
        desiredCapabilities.setCapability("platformVersion", "6.0");
        desiredCapabilities.setCapability("appPackage", "com.xueqiu.android");
        desiredCapabilities.setCapability("appActivity", ".view.WelcomeActivityAlias");
        desiredCapabilities.setCapability("noReset", true);
        desiredCapabilities.setCapability("newCommandTimeout", "300");
        desiredCapabilities.setCapability("showChromedriverLog", true);
        desiredCapabilities.setCapability("recreateChromeDriverSessions", "True");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver(remoteUrl, desiredCapabilities);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void testWebview() throws InterruptedException {

        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"交易\")").click();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        driver.findElementById("com.xueqiu.android:id/page_type_fund").click(); //选中基金
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        System.out.println(driver.getContextHandles());//打印当前所有页面
        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {
            System.out.println(contextName);

            if (contextName.contains("WEBVIEW")) {
                driver.context(contextName);

                Thread.sleep(500);

                Set<String> windowsHandles = driver.getWindowHandles();
                for (String windowsHandle : windowsHandles) {
                    System.out.println("windowsHandle名: " + windowsHandle);
                    driver.switchTo().window(windowsHandle);
                }

            }
            driver.context("WEBVIEW_com.xueqiu.android");
            System.out.println(driver.getPageSource());
            Thread.sleep(500);
            driver.findElementByXPath("//*[@id=\"my-money\"]/div[2]/div[2]/button[2]").click();
            driver.context("NATIVE_APP");//切换到原生
            Thread.sleep(5000);
            driver.context("WEBVIEW_com.xueqiu.android");//再切换到webview

            System.out.println(driver.getContext()); //
            String aa = driver.findElementByXPath("/html/body/div[5]/form/div[5]/a").getText();
            System.out.println("登录页的pageSource：" + aa);
            driver.findElementByXPath("//*[@id='telno']").sendKeys("11122223333");
            driver.findElementByXPath("//*[@id='code']").sendKeys("1234");
            driver.findElementByXPath("//*[@id='next']").click();

        }

    }

}

package sainsburys.scrape.app.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public enum WebDriverManager {
	INSTANCE;
	
	private WebDriver webDriver;
	
	public WebDriver getWebDriver() {
		if(webDriver == null)
			initialWebDriver();
		
		return webDriver;
	}

	public void initialWebDriver() {
		webDriver = new ChromeDriver();
	}
	
	public void close() {
		if(webDriver == null)
			return;
		
		webDriver.close();
	}
}

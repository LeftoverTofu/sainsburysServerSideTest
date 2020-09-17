package sainsburys.scrape.app.scrape;

import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SainsburyScraperTest {

	private String url;
	private WebDriver webDriver;
	
	@Before
	public void startUp() {
		url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
		
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--headless");
		webDriver = new ChromeDriver(chromeOptions);
	}
	
	@After
	public void tearDown() {
		webDriver.close();
	}
	
	@Test
	public void testScrapeForAllProducts() {
		double expectedGross = 32.25;
		double expectedVAT = 6.45; 
		JSONObject actualJSONObject = SainsburyScraper.scrapeForAllProducts(url, webDriver);
		
		JSONObject totalObject = actualJSONObject.getJSONObject("total");
	
		assertTrue("expected gross is not matching actual gross", expectedGross == totalObject.getDouble("gross"));
		assertTrue("expected vat is not matching actual vat", expectedVAT == totalObject.getDouble("vat"));
	}

}

package sainsburys.scrape.app.scrape;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class SainsburyScraperTest {

	private String url;
	private WebDriver webDriver;
	
	@Test
	public void testScapeForAllProducts() {
		JSONArray expectedJSONArray = new JSONArray();
		JSONArray actualJSONArray = SainsburyScraper.scapeForAllProducts(url, webDriver);
		
		assertTrue("expected JSONArray does not match actualJSONArray", expectedJSONArray.equals(actualJSONArray));
	}

}

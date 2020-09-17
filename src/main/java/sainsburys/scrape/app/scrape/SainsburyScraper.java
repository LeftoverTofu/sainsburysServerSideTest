package sainsburys.scrape.app.scrape;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SainsburyScraper {

	private static final By PRODUCT_XPATH = By.xpath("//div[@class='product ']");
	private static final String ANCHOR_XPATH_STRING = "(//div[@class='product '])[%d]//a";
	
	private static final String GROSS_STRING = "gross", HREF_STRING = "href", 
			UNIT_PRICE = "unit_price", KAL_PER_100G_STRING = "kcal_per_100g", 
			TITLE_STRING = "title", DESCRIPTION_STRING = "description",
			RESULT_STRING = "result", TOTAL_STRING = "total", VAT_STRING = "vat";
	
	public static JSONObject scrapeForAllProducts(String url, WebDriver webDriver) {
		webDriver.get(url);
		
		List<WebElement> productElements = webDriver.findElements(PRODUCT_XPATH);
		
		JSONObject returnObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject totalObject = new JSONObject();
		totalObject.put(GROSS_STRING, 0.0);
		
		for(int i = 0; i < productElements.size(); i++) {
			WebElement productAnchorElement = webDriver.findElement(By.xpath(String.format(ANCHOR_XPATH_STRING, i + 1)));

			String href = productAnchorElement.getAttribute(HREF_STRING);
			
			webDriver.get(href);
			
			JSONObject extractedInformationObject = extractInformation(webDriver);
			jsonArray.put(extractedInformationObject);
			
			totalObject.put(GROSS_STRING, totalObject.getFloat(GROSS_STRING) + extractedInformationObject.getFloat(UNIT_PRICE));
			
			webDriver.get(url);
		}
		
		returnObject.put(RESULT_STRING, jsonArray);
		totalObject.put(VAT_STRING, totalObject.getFloat(GROSS_STRING) * 0.2);		
		returnObject.put(TOTAL_STRING, totalObject);
		
		return returnObject;
	}
	
	private static JSONObject extractInformation(WebDriver webDriver) {
		JSONObject jsonObject = new JSONObject();
				
		extractTitle(jsonObject, webDriver);
		extractKcalPer100kg(jsonObject, webDriver);
		extractDescription(jsonObject, webDriver);
		extractUnitPrice(jsonObject, webDriver);
		
		return jsonObject;
	}

	private static final By TITLE_XPATH = By.xpath("//div[@class='productTitleDescriptionContainer']/h1");

	private static void extractTitle(JSONObject jsonObject, WebDriver webDriver) {
		WebElement headerElement = webDriver.findElement(TITLE_XPATH);
		jsonObject.put(TITLE_STRING, headerElement.getText());
	}
	
	private static final By KCAL_PER_100KG_XPATH = By.xpath("//td[@class='nutritionLevel1' and contains(text(), 'kcal')]");
	private static final Pattern KCAL_PATTERN = Pattern.compile("([0-9]*)kcal");
	
	private static void extractKcalPer100kg(JSONObject jsonObject, WebDriver webDriver) {
		try {
			WebElement kcalElement = webDriver.findElement(KCAL_PER_100KG_XPATH);
			
			String kcal = kcalElement.getText();
			
			Matcher matcher = KCAL_PATTERN.matcher(kcal);
			
			if(matcher.find())
				jsonObject.put(KAL_PER_100G_STRING, Integer.parseInt(matcher.group(1)));
			
		} catch(NoSuchElementException e) {
		}
	}

	private static final By DESCRIPTION_XPATH = By.xpath("//h3[text()='Description']/following-sibling::div");
	
	private static void extractDescription(JSONObject jsonObject, WebDriver webDriver) {
		WebElement descriptionElement = webDriver.findElement(DESCRIPTION_XPATH);
		jsonObject.put(DESCRIPTION_STRING, descriptionElement.getText());
	}
	
	private static final By PRICE_PER_UNIT_XPATH = By.xpath("//p[@class='pricePerUnit']");
	private static final Pattern UNIT_PRICE_PATTERN = Pattern.compile("£([0-9].*)/");
	
	private static void extractUnitPrice(JSONObject jsonObject, WebDriver webDriver) {
		WebElement pricePerUnitElement = webDriver.findElement(PRICE_PER_UNIT_XPATH);
		
		String price = pricePerUnitElement.getText();
		
		Matcher matcher = UNIT_PRICE_PATTERN.matcher(price);
		
		if(matcher.find())
			jsonObject.put(UNIT_PRICE, Double.parseDouble(matcher.group(1)));
	}
}

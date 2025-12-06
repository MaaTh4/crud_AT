package pages;

import driver.DriverFactory;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {

    protected WebDriver driver = DriverFactory.getDriver();

    public void open(String url) {
        driver.get(url);
    }
}
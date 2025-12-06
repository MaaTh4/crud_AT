package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FuncionarioFormPage extends BasePage {

    public FuncionarioFormPage(WebDriver driver) {
        super();
    }

    public void fillForm(String nomeVal, String cpfVal) {

        driver.findElement(By.id("nome")).clear();
        driver.findElement(By.id("nome")).sendKeys(nomeVal);

        driver.findElement(By.id("cpf")).clear();
        driver.findElement(By.id("cpf")).sendKeys(cpfVal);

        driver.findElement(By.id("idade")).clear();
        driver.findElement(By.id("idade")).sendKeys("25");

        driver.findElement(By.id("salario")).clear();
        driver.findElement(By.id("salario")).sendKeys("2000");
    }

    public void submit() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
}

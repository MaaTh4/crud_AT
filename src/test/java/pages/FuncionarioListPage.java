package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FuncionarioListPage extends BasePage {

    public FuncionarioListPage(WebDriver driver) {
        super();
    }

    public void open(String baseUrl) {
        open(baseUrl + "/index.html");
    }

    public void clickNovo() {
        driver.findElement(By.id("btn-novo")).click();
    }

    public boolean containsMessage(String texto) {
        return driver.getPageSource().contains(texto);
    }

    public boolean listarNomes(String nome) {
        return driver.getPageSource().contains(nome);
    }
}

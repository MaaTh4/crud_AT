package tests;

import driver.DriverFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.FuncionarioListPage;
import pages.FuncionarioFormPage;

import java.time.Duration;
import java.util.Random;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class) // garante ordem exata de execução
public class FuncionarioE2ETest {

    private WebDriver driver;
    private FuncionarioListPage listPage;
    private FuncionarioFormPage formPage;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:8080";

    @BeforeEach
    public void setup() {
        driver = DriverFactory.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        listPage = new FuncionarioListPage(driver);
        formPage = new FuncionarioFormPage(driver);
    }

    @AfterEach
    public void teardown() {
        DriverFactory.quitDriver();
    }

    // ==========================
    // HELPER: Gera CPF único
    // ==========================
    private String gerarCpfUnico() {
        long numero = 10000000000L + new Random().nextLong(90000000000L);
        String raw = String.valueOf(numero);
        return raw.substring(0, 3) + "." +
                raw.substring(3, 6) + "." +
                raw.substring(6, 9) + "-" +
                raw.substring(9, 11);
    }

    // ======================================================
    // GRUPO 1 — FLUXO BÁSICO DO SISTEMA
    // ======================================================

    @Test
    @Order(1)
    @DisplayName("1. Deve cadastrar um funcionário com sucesso")
    public void testeCadastroSucesso() {
        listPage.open(baseUrl);
        listPage.clickNovo();

        String cpf = gerarCpfUnico();
        formPage.fillForm("Teste Sucesso", cpf);
        formPage.submit();

        wait.until(ExpectedConditions.urlContains("index.html"));

        Assertions.assertTrue(
                listPage.listarNomes("Teste Sucesso"),
                "O funcionário cadastrado deve aparecer na lista."
        );
    }

    @Test
    @Order(2)
    @DisplayName("2. Deve navegar corretamente para a página de Novo Funcionário")
    public void testeNavegacaoBotaoNovo() {
        listPage.open(baseUrl);
        listPage.clickNovo();

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("form.html"),
                "Deveria estar na página de formulário."
        );
    }

    @Test
    @Order(3)
    @DisplayName("3. O botão Voltar deve retornar para a Listagem")
    public void testeBotaoVoltar() {
        listPage.open(baseUrl);
        listPage.clickNovo();

        driver.findElement(By.id("btn-voltar")).click();

        Assertions.assertTrue(
                driver.getCurrentUrl().contains("index.html"),
                "Botão Voltar deveria voltar para a lista."
        );
    }

    // ======================================================
    // GRUPO 2 — REGRAS DE NEGÓCIO E VALIDAÇÕES
    // ======================================================

    @Test
    @Order(4)
    @DisplayName("4. Não deve permitir salário menor que o mínimo (R$ 1300)")
    public void testeSalarioBaixo() {
        listPage.open(baseUrl);
        listPage.clickNovo();

        formPage.fillForm("Estagiario Mal Pago", gerarCpfUnico());
        driver.findElement(By.id("salario")).clear();
        driver.findElement(By.id("salario")).sendKeys("500");
        formPage.submit();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-salario")));

        Assertions.assertTrue(
                msg.getText().toLowerCase().contains("salári") ||
                        msg.getText().contains("1300"),
                "Deve exibir erro sobre salário mínimo."
        );
    }

    @Test
    @Order(5)
    @DisplayName("5. Não deve permitir CPF duplicado")
    public void testeCpfDuplicado() {
        String cpf = gerarCpfUnico();

        // Cadastro 1
        listPage.open(baseUrl);
        listPage.clickNovo();
        formPage.fillForm("Original", cpf);
        formPage.submit();

        wait.until(ExpectedConditions.urlContains("index.html"));

        // Cadastro 2 com mesmo CPF
        listPage.clickNovo();
        formPage.fillForm("Clone", cpf);
        formPage.submit();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-cpf")));

        Assertions.assertTrue(
                msg.getText().toLowerCase().contains("cadastrado"),
                "Deveria informar CPF duplicado."
        );
    }

    // ======================================================
    // GRUPO 3 — INTERFACE E USABILIDADE
    // ======================================================

    @Test
    @Order(6)
    @DisplayName("6. A máscara de CPF deve formatar automaticamente")
    public void testeMascaraCpf() {

        listPage.open(baseUrl);
        listPage.clickNovo();

        WebElement inputCpf = driver.findElement(By.id("cpf"));
        inputCpf.sendKeys("12345678900");

        String value = inputCpf.getAttribute("value");

        Assertions.assertEquals(
                "123.456.789-00",
                value,
                "A máscara deveria formatar o input."
        );
    }

    @Test
    @Order(7)
    @DisplayName("7. A página deve ter o título correto")
    public void testeTituloPagina() {

        listPage.open(baseUrl);
        Assertions.assertTrue(
                driver.getTitle().contains("Listagem") || driver.getTitle().contains("Funcionários"),
                "Título da Home incorreto."
        );

        listPage.clickNovo();
        Assertions.assertTrue(
                driver.getTitle().contains("Cadastro") ||
                        driver.getTitle().contains("Formulário"),
                "Título do Form incorreto."
        );
    }

    @Test
    @Order(8)
    @DisplayName("8. HTML5: Não deve enviar formulário com campo Nome vazio")
    public void testeCamposObrigatorios() {

        listPage.open(baseUrl);
        listPage.clickNovo();

        driver.findElement(By.id("nome")).clear();
        formPage.submit();

        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-nome")));

        Assertions.assertTrue(
                msg.getText().length() > 0,
                "Deveria exibir erro de nome obrigatório."
        );
    }

    @Test
    @Order(9)
    @DisplayName("9. O sistema deve sanitizar scripts maliciosos (XSS)")
    public void testeSegurancaXSS() {

        listPage.open(baseUrl);
        listPage.clickNovo();

        String script = "<script>alert('HACKED')</script>";

        formPage.fillForm(script, gerarCpfUnico());
        formPage.submit();

        wait.until(ExpectedConditions.urlContains("index.html"));

        Assertions.assertTrue(
                driver.getPageSource().contains("&lt;script&gt;"),
                "O script deve ser sanitizado e exibido como texto."
        );
    }

    @Test
    @Order(10)
    @DisplayName("10. Deve aceitar salário com centavos")
    public void testeSalarioQuebrado() {

        listPage.open(baseUrl);
        listPage.clickNovo();

        formPage.fillForm("Centavos Test", gerarCpfUnico());
        driver.findElement(By.id("salario")).clear();
        driver.findElement(By.id("salario")).sendKeys("1500.99");
        formPage.submit();

        wait.until(ExpectedConditions.urlContains("index.html"));

        Assertions.assertTrue(
                driver.getPageSource().contains("1.500,99") ||
                        driver.getPageSource().contains("1500.99"),
                "Deveria exibir o salário com centavos."
        );
    }

    // ======================================================
    // GRUPO 5 — OPERAÇÕES DESTRUTIVAS
    // ======================================================

    @Test
    @Order(11)
    @DisplayName("11. Deve excluir um funcionário")
    public void testeExclusao() {

        listPage.open(baseUrl);

        // Cadastro temporário
        listPage.clickNovo();
        formPage.fillForm("Para Deletar " + System.currentTimeMillis(), gerarCpfUnico());
        formPage.submit();

        wait.until(ExpectedConditions.urlContains("index.html"));

        // Clica excluir: botão vermelho
        WebElement btnExcluir = driver.findElement(By.cssSelector("button.btn-danger"));
        btnExcluir.click();

        // Confirmação nativa
        driver.switchTo().alert().accept();

        wait.until(ExpectedConditions.urlContains("index.html"));

        Assertions.assertFalse(
                driver.getPageSource().contains("Para Deletar"),
                "O funcionário deveria ter sido removido."
        );
    }
}

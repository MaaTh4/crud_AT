# CRUD de Funcionários – AT

Projeto de CRUD de funcionários com:

- Backend em Java + Spring (API REST em `/funcionarios`)
- Frontend em HTML/CSS/JS simples (`index.html` e `form.html`)
- Testes automatizados (unitários, fuzz, resiliência e E2E com Selenium)
- Pipeline CI/CD no GitHub Actions

---

## Como rodar o backend

Requisitos:

- Java 19+
- Maven

### Pela IDE (IntelliJ)

1. Abra o projeto.
2. Localize a classe `Main` (no pacote `org.example`).
3. Clique em **Run** para iniciar a aplicação.

### Pela linha de comando

Na raiz do projeto:

```bash
mvn spring-boot:run
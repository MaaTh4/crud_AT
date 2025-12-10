package org.example.domain.model;

public class Funcionario {

    private int id;
    private String nome;
    private String cpf;
    private Integer idade;   // pode ser nulo
    private double salario;


    public Funcionario() {
    }

    // Construtor completo
    public Funcionario(int id, String nome, String cpf, Integer idade, double salario) {
        setId(id);
        setNome(nome);
        setCpf(cpf);
        setIdade(idade);
        setSalario(salario);
    }

    public Funcionario(int id, String nome, String cpf, double salario) {
        this(id, nome, cpf, null, salario);
    }

    // === GETTERS / SETTERS COM VALIDAÇÃO ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        validarId(id);
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = sanitizarXss(nome);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        validarCpf(cpf);
        this.cpf = cpf;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        if (idade != null) {
            validarIdade(idade);
        }
        this.idade = idade;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        validarSalario(salario);
        this.salario = salario;
    }

    // === VALIDAÇÕES ===

    private void validarId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID deve ser positivo.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        String semEspacos = nome.replace(" ", "");
        if (semEspacos.length() < 3 || semEspacos.length() > 30) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 30 caracteres.");
        }
    }

    private void validarCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new IllegalArgumentException("CPF inválido (esperado: 000.000.000-00).");
        }
    }

    private void validarIdade(int idade) {
        if (idade < 16 || idade > 100) {
            throw new IllegalArgumentException("Idade deve estar entre 16 e 100 anos.");
        }
    }

    private void validarSalario(double salario) {
        if (salario < 1300.0) {
            throw new IllegalArgumentException("Salário mínimo é 1300.00.");
        }
    }

    // === Proteção simples contra XSS no nome ===
    private String sanitizarXss(String texto) {
        if (texto == null) return null;
        return texto
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}

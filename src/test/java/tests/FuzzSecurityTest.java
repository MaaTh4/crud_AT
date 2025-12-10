package tests;

import net.jqwik.api.*;
import org.example.domain.model.Funcionario;

import static org.junit.jupiter.api.Assertions.*;

public class FuzzSecurityTest {

    // ============================
    // NOMES VÁLIDOS (3 a 30 chars)
    // incluindo caracteres "suspeitos"
    // ============================
    @Provide
    Arbitrary<String> nomesValidosPossivelmenteMaliciosos() {
        return Arbitraries.strings()
                .withChars('a', 'z')
                .withChars('A', 'Z')
                .withChars('0', '9')
                .withChars(' ', '<', '>', '/', '\\', '"', '&', '\'')
                .ofMinLength(1)
                .ofMaxLength(50)
                // só aceita nomes que, DEPOIS de remover espaços, tenham entre 3 e 30 chars
                .filter(n -> {
                    String semEspacos = n.replace(" ", "");
                    int len = semEspacos.length();
                    return len >= 3 && len <= 30;
                });
    }

    @Property
    @Label("Fuzz: nomes válidos (3-30, desconsiderando espaços) com caracteres perigosos não quebram o sistema e são sanitizados")
    void nomesValidosNaoQuebramENaoPermitemTags(@ForAll("nomesValidosPossivelmenteMaliciosos") String nome) {
        Funcionario f = new Funcionario();
        f.setId(1);
        f.setCpf("123.456.789-10");
        f.setSalario(2000.0);

        // Agora SÓ entram nomes que respeitam a regra -> não deve lançar
        assertDoesNotThrow(() -> f.setNome(nome));

        String tratado = f.getNome();
        // Depois de sanitizar, não deve ter tags HTML
        assertFalse(tratado.contains("<") || tratado.contains(">"),
                "Nome sanitizado não deveria conter tags HTML.");
    }

    // ============================
    // NOMES INVÁLIDOS (tamanho)
    // ============================
    @Provide
    Arbitrary<String> nomesInvalidosPorTamanho() {
        Arbitrary<String> muitoCurtos = Arbitraries.strings()
                .ofMinLength(0)
                .ofMaxLength(2);

        Arbitrary<String> muitoLongos = Arbitraries.strings()
                .ofMinLength(31)
                .ofMaxLength(60);

        return Arbitraries.oneOf(muitoCurtos, muitoLongos);
    }

    @Property
    @Label("Fuzz: nomes muito curtos ou muito longos devem lançar IllegalArgumentException")
    void nomesForaDoLimiteDevemFalhar(@ForAll("nomesInvalidosPorTamanho") String nomeInvalido) {
        Funcionario f = new Funcionario();
        f.setId(1);
        f.setCpf("123.456.789-10");
        f.setSalario(2000.0);

        assertThrows(IllegalArgumentException.class, () -> f.setNome(nomeInvalido));
    }

    // ============================
    // CPF INVÁLIDO ALEATÓRIO
    // ============================
    @Provide
    Arbitrary<String> cpfsInvalidos() {
        return Arbitraries.strings()
                .ofMinLength(1)
                .ofMaxLength(14)
                .filter(cpf -> !cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")); // garante que é inválido
    }

    @Property
    @Label("Fuzz: CPFs aleatórios fora do padrão devem lançar IllegalArgumentException")
    void fuzzCpfInvalido(@ForAll("cpfsInvalidos") String cpf) {
        Funcionario f = new Funcionario();
        f.setId(1);
        f.setNome("Teste válido");
        f.setSalario(2000.0);

        assertThrows(IllegalArgumentException.class, () -> f.setCpf(cpf));
    }

    // ============================
    // SALÁRIO INVÁLIDO ALEATÓRIO
    // ============================
    @Property
    @Label("Fuzz: salários aleatórios abaixo do mínimo devem lançar IllegalArgumentException")
    void fuzzSalariosInvalidos(@ForAll int salario) {
        // Considera só casos realmente inválidos
        Assume.that(salario < 1300);

        Funcionario f = new Funcionario();
        f.setId(1);
        f.setNome("Teste");
        f.setCpf("123.456.789-10");

        assertThrows(IllegalArgumentException.class, () -> f.setSalario(salario));
    }
}

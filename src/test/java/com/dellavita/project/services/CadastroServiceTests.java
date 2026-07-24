package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dellavita.project.dto.ClienteCadastroDTO;
import com.dellavita.project.dto.FuncionarioCadastroDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class CadastroServiceTests {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ClienteService clienteService;
    private FuncionarioService funcionarioService;
    private Validator validator;

    @BeforeEach
    void configurar() {
        clienteService = new ClienteService(clienteRepository, funcionarioRepository, passwordEncoder);
        funcionarioService = new FuncionarioService(funcionarioRepository, clienteRepository, passwordEncoder);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void deveRejeitarCpfCamposEmBrancoESenhaCurta() {
        ClienteCadastroDTO dados = clienteValido();
        dados.setCpf("123");
        dados.setNome("   ");
        dados.setSenha("1");
        dados.setEndereco("   ");

        Set<ConstraintViolation<ClienteCadastroDTO>> violacoes = validator.validate(dados);

        assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("Informe um CPF válido.")));
        assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O nome é obrigatório.")));
        assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("A senha deve ter pelo menos 6 caracteres.")));
        assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().equals("O endereço de entrega é obrigatório.")));
    }

    @Test
    void deveCriarClienteNormalizadoComSenhaCodificada() {
        ClienteCadastroDTO dados = clienteValido();
        dados.setNome("  Cliente Teste  ");
        dados.setLogin("  CLIENTE@TESTE.DEV  ");
        when(passwordEncoder.encode("123456")).thenReturn("senha-codificada");
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Cliente criado = clienteService.criar(dados);

        assertEquals("Cliente Teste", criado.getNome());
        assertEquals("CLIENTE@TESTE.DEV", criado.getLogin());
        assertEquals("senha-codificada", criado.getSenha());
        assertEquals("Rua Teste", criado.getEndereco());
    }

    @Test
    void deveCriarFuncionarioPendenteEImpedirCadastroComoGerente() {
        FuncionarioCadastroDTO dados = funcionarioValido();
        when(passwordEncoder.encode("123456")).thenReturn("senha-codificada");
        when(funcionarioRepository.save(any(Funcionario.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        Funcionario criado = funcionarioService.criar(dados);

        assertEquals(Status.EM_VALIDAÇÃO, criado.getStatus());
        assertEquals(Funcao.ATENDENTE, criado.getFuncao());
        assertEquals("Atendimento", criado.getSetor());

        dados.setFuncao(Funcao.GERENTE);
        assertThrows(IllegalArgumentException.class, () -> funcionarioService.criar(dados));
    }

    private ClienteCadastroDTO clienteValido() {
        ClienteCadastroDTO dados = new ClienteCadastroDTO();
        dados.setCpf("17178567996");
        dados.setNome("Cliente Teste");
        dados.setLogin("cliente@teste.dev");
        dados.setSenha("123456");
        dados.setGenero("Outro");
        dados.setTelefone("99999-0000");
        dados.setEndereco("Rua Teste");
        return dados;
    }

    private FuncionarioCadastroDTO funcionarioValido() {
        FuncionarioCadastroDTO dados = new FuncionarioCadastroDTO();
        dados.setCpf("46608755554");
        dados.setNome("Funcionário Teste");
        dados.setLogin("funcionario@teste.dev");
        dados.setSenha("123456");
        dados.setGenero("Outro");
        dados.setTelefone("99999-0000");
        dados.setFuncao(Funcao.ATENDENTE);
        dados.setRg("1234567890");
        dados.setDataNascimento(LocalDate.of(1990, 1, 1));
        dados.setSetor("Atendimento");
        return dados;
    }
}

package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dellavita.project.dto.LoginRequestDTO;
import com.dellavita.project.dto.UsuarioResponseDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.services.exceptions.ContaNaoAprovadaException;
import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void configurar() {
        authService = new AuthService(clienteRepository, funcionarioRepository, passwordEncoder);
    }

    @Test
    void deveIdentificarClienteAutomaticamente() {
        Cliente cliente = new Cliente();
        cliente.setLogin("cliente@teste.dev");
        cliente.setSenha("hash-cliente");
        when(clienteRepository.findByLoginIgnoreCase("cliente@teste.dev"))
                .thenReturn(Optional.of(cliente));
        when(passwordEncoder.matches("123456", "hash-cliente")).thenReturn(true);

        UsuarioResponseDTO resposta = authService.login(login("cliente@teste.dev", "123456"));

        assertEquals("cliente", resposta.getTipo());
    }

    @Test
    void deveIdentificarFuncionarioEGerenteAutomaticamente() {
        Funcionario gerente = new Funcionario();
        gerente.setLogin("gerente@teste.dev");
        gerente.setSenha("hash-gerente");
        gerente.setFuncao(Funcao.GERENTE);
        gerente.setStatus(Status.ATIVO);
        when(clienteRepository.findByLoginIgnoreCase("gerente@teste.dev"))
                .thenReturn(Optional.empty());
        when(funcionarioRepository.findByLoginIgnoreCase("gerente@teste.dev"))
                .thenReturn(Optional.of(gerente));
        when(passwordEncoder.matches("123456", "hash-gerente")).thenReturn(true);

        UsuarioResponseDTO resposta = authService.login(login("gerente@teste.dev", "123456"));

        assertEquals("funcionario", resposta.getTipo());
        assertEquals("GERENTE", resposta.getFuncao());
    }

    @Test
    void deveBloquearFuncionarioPendenteERejeitarCredenciaisInvalidas() {
        Funcionario pendente = new Funcionario();
        pendente.setLogin("pendente@teste.dev");
        pendente.setSenha("hash-pendente");
        pendente.setStatus(Status.EM_VALIDAÇÃO);
        when(clienteRepository.findByLoginIgnoreCase("pendente@teste.dev"))
                .thenReturn(Optional.empty());
        when(funcionarioRepository.findByLoginIgnoreCase("pendente@teste.dev"))
                .thenReturn(Optional.of(pendente));
        when(passwordEncoder.matches("123456", "hash-pendente")).thenReturn(true);

        assertThrows(
                ContaNaoAprovadaException.class,
                () -> authService.login(login("pendente@teste.dev", "123456")));
        assertThrows(
                CredenciaisInvalidasException.class,
                () -> authService.login(login("", "")));
    }

    private LoginRequestDTO login(String email, String senha) {
        LoginRequestDTO dados = new LoginRequestDTO();
        dados.setLogin(email);
        dados.setSenha(senha);
        return dados;
    }
}

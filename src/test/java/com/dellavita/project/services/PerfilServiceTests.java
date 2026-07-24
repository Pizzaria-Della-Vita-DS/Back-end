package com.dellavita.project.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dellavita.project.dto.PerfilUpdateDTO;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTests {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FuncionarioRepository funcionarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ClienteService clienteService;
    private FuncionarioService funcionarioService;

    @BeforeEach
    void configurar() {
        clienteService = new ClienteService(clienteRepository, funcionarioRepository, passwordEncoder);
        funcionarioService = new FuncionarioService(funcionarioRepository, clienteRepository, passwordEncoder);
    }

    @Test
    void deveEditarClienteComPedidosSemAlterarCpfGeneroOuSenha() {
        Cliente cliente = new Cliente(
                "52998224725",
                "Nome Antigo",
                "antigo@teste.dev",
                "hash-atual",
                "Outro",
                "1111",
                "Rua Antiga");
        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Cliente atualizado = clienteService.atualizar(
                cliente.getCpf(),
                perfil("Nome Novo", "novo@teste.dev", "2222", "Rua Nova", null));

        assertEquals("52998224725", atualizado.getCpf());
        assertEquals("Outro", atualizado.getGenero());
        assertEquals("hash-atual", atualizado.getSenha());
        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals("novo@teste.dev", atualizado.getLogin());
        assertEquals("Rua Nova", atualizado.getEndereco());
    }

    @Test
    void deveEditarGerenteSemPermitirAlteracaoDeFuncaoStatusOuDadosAdministrativos() {
        Funcionario gerente = new Funcionario(
                "93541134780",
                "Gerente Antigo",
                "gerente-antigo@teste.dev",
                "hash-atual",
                "Outro",
                "1111",
                Funcao.GERENTE,
                Status.ATIVO,
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Gerência");
        when(funcionarioRepository.findById(gerente.getCpf())).thenReturn(Optional.of(gerente));
        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Funcionario atualizado = funcionarioService.atualizar(
                gerente.getCpf(),
                perfil("Gerente Novo", "gerente-novo@teste.dev", "2222", null, null));

        assertSame(Funcao.GERENTE, atualizado.getFuncao());
        assertSame(Status.ATIVO, atualizado.getStatus());
        assertEquals("Gerência", atualizado.getSetor());
        assertEquals("123456789", atualizado.getRg());
        assertEquals(LocalDate.of(1990, 1, 1), atualizado.getData_nascimento());
        assertEquals("hash-atual", atualizado.getSenha());
    }

    @Test
    void deveCodificarNovaSenhaEExcluirOsDoisTiposDePerfil() {
        Cliente cliente = new Cliente();
        cliente.setCpf("52998224725");
        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("11144477735");
        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(funcionarioRepository.findById(funcionario.getCpf())).thenReturn(Optional.of(funcionario));
        when(passwordEncoder.encode("nova123")).thenReturn("novo-hash");
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        PerfilUpdateDTO dados = perfil("Cliente", "cliente@teste.dev", "9999", null, "nova123");
        Cliente atualizado = clienteService.atualizar(cliente.getCpf(), dados);
        assertEquals("novo-hash", atualizado.getSenha());

        clienteService.excluir(cliente.getCpf());
        funcionarioService.excluir(funcionario.getCpf());

        verify(clienteRepository).delete(cliente);
        verify(funcionarioRepository).delete(funcionario);
    }

    private PerfilUpdateDTO perfil(
            String nome,
            String login,
            String telefone,
            String endereco,
            String senha) {
        PerfilUpdateDTO dados = new PerfilUpdateDTO();
        dados.setNome(nome);
        dados.setLogin(login);
        dados.setTelefone(telefone);
        dados.setEndereco(endereco);
        dados.setSenha(senha);
        return dados;
    }
}

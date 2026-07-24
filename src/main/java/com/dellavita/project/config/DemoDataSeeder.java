package com.dellavita.project.config;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dellavita.project.dto.PedidoRequestDTO;
import com.dellavita.project.dto.PedidoRequestDTO.PizzaRequestDTO;
import com.dellavita.project.dto.PedidoResponseDTO;
import com.dellavita.project.entities.Borda;
import com.dellavita.project.entities.Cliente;
import com.dellavita.project.entities.Funcionario;
import com.dellavita.project.entities.Ingrediente;
import com.dellavita.project.entities.Pedido;
import com.dellavita.project.entities.Sabor;
import com.dellavita.project.enums.Estado;
import com.dellavita.project.enums.FormaPagamento;
import com.dellavita.project.enums.Funcao;
import com.dellavita.project.enums.Status;
import com.dellavita.project.repositories.BordaRepository;
import com.dellavita.project.repositories.ClienteRepository;
import com.dellavita.project.repositories.FuncionarioRepository;
import com.dellavita.project.repositories.IngredienteRepository;
import com.dellavita.project.repositories.PedidoRepository;
import com.dellavita.project.repositories.SaborRepository;
import com.dellavita.project.services.PedidoService;

@Component
@ConditionalOnProperty(name = "demo.data.enabled", havingValue = "true")
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataSeeder.class);
    private static final String SENHA_DEMO = "123456";
    private static final String CPF_CLIENTE = "52998224725";
    private static final String CPF_FUNCIONARIO = "11144477735";
    private static final String CPF_GERENTE = "93541134780";
    private static final String ENDERECO_PEDIDO_ATIVO = "[DEMO] Rua das Pizzas, 100";
    private static final String ENDERECO_PEDIDO_FINALIZADO = "[DEMO] Retirada no balcão";

    private final IngredienteRepository ingredienteRepository;
    private final SaborRepository saborRepository;
    private final BordaRepository bordaRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;
    private final PasswordEncoder passwordEncoder;

    public DemoDataSeeder(
            IngredienteRepository ingredienteRepository,
            SaborRepository saborRepository,
            BordaRepository bordaRepository,
            ClienteRepository clienteRepository,
            FuncionarioRepository funcionarioRepository,
            PedidoRepository pedidoRepository,
            PedidoService pedidoService,
            PasswordEncoder passwordEncoder) {
        this.ingredienteRepository = ingredienteRepository;
        this.saborRepository = saborRepository;
        this.bordaRepository = bordaRepository;
        this.clienteRepository = clienteRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Ingrediente mussarela = criarIngrediente("Queijo mussarela", true);
        Ingrediente molho = criarIngrediente("Molho de tomate", true);
        Ingrediente calabresa = criarIngrediente("Calabresa", true);
        Ingrediente cebola = criarIngrediente("Cebola", true);
        Ingrediente tomate = criarIngrediente("Tomate", true);
        Ingrediente manjericao = criarIngrediente("Manjericão", true);
        Ingrediente frango = criarIngrediente("Frango desfiado", true);
        Ingrediente catupiry = criarIngrediente("Catupiry", true);
        Ingrediente cheddar = criarIngrediente("Cheddar", true);
        Ingrediente camarao = criarIngrediente("Camarão", false);

        Sabor saborCalabresa = criarSabor(
                "Calabresa",
                39.90,
                List.of(mussarela, molho, calabresa, cebola));
        Sabor saborMargherita = criarSabor(
                "Margherita",
                37.90,
                List.of(mussarela, molho, tomate, manjericao));
        criarSabor(
                "Frango com Catupiry",
                42.90,
                List.of(mussarela, molho, frango, catupiry));
        criarSabor(
                "Camarão",
                49.90,
                List.of(mussarela, molho, camarao));

        Borda bordaCatupiry = criarBorda("Catupiry", 7.00, List.of(catupiry));
        criarBorda("Cheddar", 7.00, List.of(cheddar));
        criarBorda("Sem borda recheada", 0.00, List.of());

        Cliente cliente = criarCliente();
        criarFuncionario(
                CPF_FUNCIONARIO,
                "Funcionário Demonstração",
                "funcionario@dellavita.dev",
                Funcao.ATENDENTE,
                "Atendimento");
        criarFuncionario(
                CPF_GERENTE,
                "Gerente Demonstração",
                "gerente@dellavita.dev",
                Funcao.GERENTE,
                "Gerência");

        criarPedidoSeAusente(
                cliente,
                ENDERECO_PEDIDO_ATIVO,
                saborCalabresa,
                bordaCatupiry,
                66.85,
                null);
        criarPedidoSeAusente(
                cliente,
                ENDERECO_PEDIDO_FINALIZADO,
                saborMargherita,
                null,
                56.85,
                Estado.FINALIZADO);

        LOGGER.info("Dados de demonstração disponíveis.");
        LOGGER.info("Cliente: cliente@dellavita.dev / {}", SENHA_DEMO);
        LOGGER.info("Funcionário: funcionario@dellavita.dev / {}", SENHA_DEMO);
        LOGGER.info("Gerente: gerente@dellavita.dev / {}", SENHA_DEMO);
    }

    private Ingrediente criarIngrediente(String nome, boolean disponivel) {
        Ingrediente ingrediente = ingredienteRepository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> new Ingrediente(null, nome, disponivel));
        ingrediente.setDisponivel(disponivel);
        return ingredienteRepository.save(ingrediente);
    }

    private Sabor criarSabor(String nome, double preco, List<Ingrediente> ingredientes) {
        Sabor sabor = saborRepository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> new Sabor(null, nome, ingredientes, preco));
        if (sabor.getIngredientes() == null || sabor.getIngredientes().isEmpty()) {
            sabor.setIngredientes(ingredientes);
        }
        return saborRepository.save(sabor);
    }

    private Borda criarBorda(String nome, double preco, List<Ingrediente> ingredientes) {
        Borda borda = bordaRepository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> new Borda(null, nome, preco, ingredientes));
        if (borda.getIngredientes() == null || borda.getIngredientes().isEmpty()) {
            borda.setIngredientes(ingredientes);
        }
        return bordaRepository.save(borda);
    }

    private Cliente criarCliente() {
        return clienteRepository.findById(CPF_CLIENTE)
                .orElseGet(() -> clienteRepository.save(new Cliente(
                        CPF_CLIENTE,
                        "Cliente Demonstração",
                        "cliente@dellavita.dev",
                        passwordEncoder.encode(SENHA_DEMO),
                        "Outro",
                        "(53) 99999-0001",
                        "Rua das Pizzas, 100")));
    }

    private Funcionario criarFuncionario(
            String cpf,
            String nome,
            String login,
            Funcao funcao,
            String setor) {
        return funcionarioRepository.findById(cpf)
                .orElseGet(() -> funcionarioRepository.save(new Funcionario(
                        cpf,
                        nome,
                        login,
                        passwordEncoder.encode(SENHA_DEMO),
                        "Outro",
                        "(53) 99999-0002",
                        funcao,
                        Status.ATIVO,
                        "1234567890",
                        LocalDate.of(1990, 1, 1),
                        setor)));
    }

    private void criarPedidoSeAusente(
            Cliente cliente,
            String endereco,
            Sabor sabor,
            Borda borda,
            double total,
            Estado estadoFinal) {
        List<Pedido> pedidosDoCliente = pedidoRepository.listarPorCliente(cliente.getCpf());
        boolean jaExiste = pedidosDoCliente.stream()
                .anyMatch(pedido -> endereco.equals(pedido.getEndereco_entrega()));
        if (jaExiste) {
            return;
        }

        PizzaRequestDTO pizza = new PizzaRequestDTO();
        pizza.setTamanho("GRANDE");
        pizza.setPreco(total);
        pizza.setSaboresIds(List.of(sabor.getId()));
        pizza.setBordaId(borda == null ? null : borda.getId());

        PedidoRequestDTO pedido = new PedidoRequestDTO();
        pedido.setClienteCpf(cliente.getCpf());
        pedido.setFormaPagamento(FormaPagamento.PIX);
        pedido.setEnderecoEntrega(endereco);
        pedido.setPrecoTotal(total);
        pedido.setPizzas(List.of(pizza));

        PedidoResponseDTO criado = pedidoService.criar(pedido);
        if (estadoFinal != null) {
            pedidoService.atualizarEstado(criado.getId(), estadoFinal);
        }
    }
}

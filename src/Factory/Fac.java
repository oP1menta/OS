package Factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import Classe.*;
import Exception.InvalidArgumentException;
import dominio.enums.StatusOrdemServico;

public class Fac {

    private static Fac instanciaUnica;

    private Fac() {}

    public static Fac getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new Fac();
        }
        return instanciaUnica;
    }

    // ======================
    // USUÁRIO
    // ======================
    public Usuario criarUsuario(String login, String senha) {
        return new Usuario(login, senha);
    }

    public Usuario criarUsuarioComHash(String login, String senhaHash) {
        return new Usuario(login, senhaHash, true);
    }

    // ======================
    // CLIENTE
    // ======================
    public Cliente criarCliente(
            String nome,
            String telefone,
            String email,
            String documento
    ) throws InvalidArgumentException {

        String docLimpo = documento.replaceAll("\\D", "");

        if (docLimpo.length() == 11) {
            return new ClienteFisica(nome, telefone, email, docLimpo);
        } else if (docLimpo.length() == 14) {
            return new ClienteJuridica(nome, telefone, email, docLimpo);
        } else {
            throw new InvalidArgumentException(
                    "Documento inválido: deve ter 11 (CPF) ou 14 (CNPJ) dígitos."
            );
        }
    }

    // ======================
    // EQUIPAMENTO
    // ======================
    public Equipamento criarEquipamento(
            String nome,
            String modelo,
            String documentoCliente
    ) {

        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome obrigatório");

        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("Modelo obrigatório");

        if (documentoCliente == null || documentoCliente.isBlank())
            throw new IllegalArgumentException("Documento do cliente obrigatório");

        return new Equipamento(nome, modelo, documentoCliente);
    }

    // REIDRATAÇÃO DE EQUIPAMENTO (DAO)
    public Equipamento reidratarEquipamento(
            int id,
            String nome,
            String modelo,
            String documentoCliente,
            boolean ativo
    ) {
        return new Equipamento(id, nome, modelo, documentoCliente, ativo);
    }

    // ======================
    // ORDEM DE SERVIÇO
    // ======================
    // CRIAÇÃO
    public OrdemDeServico criarOrdemDeServico(
            Equipamento equipamento,
            String descricaoProblema,
            LocalDateTime dataFechamentoPrevista,
            long id
    ) {
        return new OrdemDeServico(
                id, equipamento,
                descricaoProblema,
                dataFechamentoPrevista, dataFechamentoPrevista, dataFechamentoPrevista, null
        );
    }

    
    public OrdemDeServico reidratarOrdemDeServico(
            Long id,
            Equipamento equipamento,
            String descricaoProblema,
            LocalDateTime dataAbertura,
            LocalDateTime dataFechamentoPrevisto,
            LocalDateTime dataConclusao,
            StatusOrdemServico status
    ) {

        OrdemDeServico os = new OrdemDeServico(
                id, equipamento,
                descricaoProblema,
                dataFechamentoPrevisto, dataConclusao, dataConclusao, status
        );

        os.setId(id);
        os.setDataAbertura(dataAbertura);
        os.setDataFechamcentoReal(dataConclusao);
        os.setStatus(status);

        return os;
    }

    // ======================
    // ORÇAMENTO
    // ======================
    public Orçamento criarOrcamento(
            String peca,
            BigDecimal valor,
            String tipoPagamento,
            Tecnico tecnico
    ) {
        return new Orçamento(peca, valor, tipoPagamento, tecnico);
    }
}
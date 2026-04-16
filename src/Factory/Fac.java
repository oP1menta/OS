package Factory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public Usuario criarUsuario(String login, String senha) {
        return new Usuario(login, senha);
    }

    public Usuario criarUsuarioComHash(String login, String senhaHash) {
        return new Usuario(login, senhaHash, true);
    }

    public Cliente criarCliente(
            String nome, String telefone, String email,
            String Cidade, String CEP, String documento
    ) throws InvalidArgumentException {

        String docLimpo = documento.replaceAll("\\D", "");

        if (docLimpo.length() == 11) {
            return new ClienteFisica(nome, telefone, email, Cidade, CEP, docLimpo);
        } else if (docLimpo.length() == 14) {
            return new ClienteJuridica(nome, telefone, email, Cidade, CEP, docLimpo);
        } else {
            throw new InvalidArgumentException(
                    "Documento inválido: deve ter 11 (CPF) ou 14 (CNPJ) dígitos.");
        }
    }

    public Equipamento criarEquipamento(String nome, String modelo, String documentoCliente) {
        return new Equipamento(nome, modelo, documentoCliente);
    }

    public Equipamento reidratarEquipamento(
            int id, String nome, String modelo, String documentoCliente, boolean ativo) {
        return new Equipamento(id, nome, modelo, documentoCliente, ativo);
    }

    public OrdemDeServico criarOrdemDeServico(
            Equipamento equipamento, String descricaoProblema,
            LocalDateTime dataFechamentoPrevista, long id) {
        return new OrdemDeServico(
                id, equipamento, descricaoProblema,
                dataFechamentoPrevista, dataFechamentoPrevista, dataFechamentoPrevista, null);
    }

    public OrdemDeServico reidratarOrdemDeServico(
            Long id, Equipamento equipamento, String descricaoProblema,
            LocalDateTime dataAbertura, LocalDateTime dataFechamentoPrevisto,
            LocalDateTime dataConclusao, StatusOrdemServico status) {

        OrdemDeServico os = new OrdemDeServico(
                id, equipamento, descricaoProblema,
                dataFechamentoPrevisto, dataConclusao, dataConclusao, status);

        os.setId(id);
        os.setDataAbertura(dataAbertura);
        os.setDataFechamcentoReal(dataConclusao);
        os.setStatus(status);
        return os;
    }

    public Tecnico criarTecnico(String nome, String documento, LocalDate dataAssociacao) {
        return new Tecnico(nome, documento, dataAssociacao);
    }

    public Orçamento criarOrcamento(
            List<ItemOrcamento> itens,
            String tipoPagamento,
            Tecnico tecnico,
            BigDecimal maodeobra) {
        return new Orçamento(itens, tipoPagamento, tecnico, maodeobra);
    }
}
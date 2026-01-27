package Factory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import Classe.*;
import Exception.InvalidArgumentException;

public class Fac {

    private static Fac instanciaUnica;

    private Fac() {}

    public static Fac getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new Fac();
        }
        return instanciaUnica;
    }

    // --- USUÁRIO ---
    public Usuario criarUsuario(String login, String senha) {
        return new Usuario(login, senha);
    }

    public Usuario criarUsuarioComHash(String login, String senhaHash) {
        return new Usuario(login, senhaHash, true);
    }

    //  CLIENTE 
    public Cliente criarCliente(String nome, String telefone, String email, String documento)
            throws InvalidArgumentException {

        String docLimpo = documento.replaceAll("\\D", "");

        if (docLimpo.length() == 11) {
            return new ClienteFisica(nome, telefone, email, docLimpo);
        } else if (docLimpo.length() == 14) {
            return new ClienteJuridica(nome, telefone, email, docLimpo);
        } else {
            throw new InvalidArgumentException( "Documento inválido: deve ter 11 (CPF) ou 14 (CNPJ) dígitos.");
        }
    }

    //  EQUIPAMENTO 
    public Equipamento criarEquipamento(String nome, String modelo, String cliente) {

        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome obrigatório");

        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("Modelo obrigatório");

        if (cliente == null)
            throw new IllegalArgumentException("Cliente obrigatório");

        return new Equipamento(nome, modelo, cliente);
    }

    // ORDEM DE SERVIÇO
    public OrdemDeServico criarOrdemDeServico(
            Equipamento equipamento,
            String descricaoProblema,
            LocalDateTime dataFechamentoPrevista) {

        return new OrdemDeServico(equipamento, descricaoProblema, dataFechamentoPrevista);
    }

    // --- ORÇAMENTO ---
    public Orçamento criarOrcamento(
            String peca,
            BigDecimal valor,
            String tipoPagamento,
            OrdemDeServico os) {

        return new Orçamento(peca, valor, tipoPagamento, os);
    }
}
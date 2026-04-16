package PDF;

import Classe.Cliente;
import Classe.ItemOrcamento;
import Classe.OrdemDeServico;
import Classe.Orçamento;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeradorPDFOS {

    public static void gerarOS(Orçamento orc, OrdemDeServico os, Cliente cliente) {

        if (os == null)
            throw new IllegalArgumentException("Ordem de serviço inválida");
        if (orc == null)
            throw new IllegalArgumentException("A OS não possui orçamento vinculado para gerar o PDF");
        if (cliente == null)
            throw new IllegalArgumentException("Cliente inválido para gerar o PDF");

        Document document = new Document();

        try {
            PdfWriter.getInstance(
                document,
                new FileOutputStream("OrdemDeServico_" + os.getId() + "_" + System.currentTimeMillis() + ".pdf")
            );
            document.open();

            Font bold     = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normal   = new Font(Font.HELVETICA, 9);
            Font vermelho = new Font(Font.HELVETICA, 10, Font.BOLD);
            vermelho.setColor(136, 8, 8);

            Font email = new Font(Font.HELVETICA, 10);
            email.setColor(0, 127, 255);

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // ===== CABEÇALHO =====
            PdfPTable cabecalho = new PdfPTable(2);
            cabecalho.getDefaultCell();
//            .setBorderColor(Color.RED);
            cabecalho.setWidthPercentage(100);
            cabecalho.setWidths(new float[]{3, 1});

            PdfPCell titulo = new PdfPCell(new Phrase("CONTRIMAQ", vermelho));
//            titulo.setBorderColor(new java.awt.Color(255, 0, 0));
            titulo.setColspan(2);
            titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            titulo.setPadding(10);
            titulo.setBorderWidth(1.5f);
            cabecalho.addCell(titulo);

            PdfPCell esquerda = new PdfPCell(new Phrase("SERVIÇO DE MANUTENÇÃO / ORDEM DE SERVIÇO", bold));
//            esquerda.setBorderColor(new java.awt.Color(255, 0, 0));
            esquerda.setHorizontalAlignment(Element.ALIGN_CENTER);
            esquerda.setPadding(6);
            esquerda.setBorderWidth(1.5f);

            PdfPCell direita = new PdfPCell(new Phrase("OS Nº " + os.getId(), bold));
//            direita.setBorderColor(new java.awt.Color(255, 0, 0));

            direita.setHorizontalAlignment(Element.ALIGN_CENTER);
            direita.setPadding(6);
            direita.setBorderWidth(1.5f);

            cabecalho.addCell(esquerda);
            cabecalho.addCell(direita);

            // ===== GRID DO CLIENTE =====
            PdfPTable clienteGrid = new PdfPTable(4);
            clienteGrid.getDefaultCell();
//            .setBorderColor(Color.RED);
            clienteGrid.setWidthPercentage(100);
            clienteGrid.setWidths(new float[]{1, 2, 1, 2});

            clienteGrid.addCell(new Phrase("Cliente", bold));
            clienteGrid.addCell(new Phrase(cliente.getNome() != null ? cliente.getNome() : "-", normal));
            clienteGrid.addCell(new Phrase("Telefones", bold));
            clienteGrid.addCell(new Phrase(cliente.getTelefone() != null ? cliente.getTelefone() : "-", normal));

            clienteGrid.addCell(new Phrase("CEP", bold));
            clienteGrid.addCell(new Phrase(cliente.getCEP() != null ? cliente.getCEP() : "-", normal));
            clienteGrid.addCell(new Phrase("Cidade", bold));
            clienteGrid.addCell(new Phrase(cliente.getCidade() != null ? cliente.getCidade() : "-", normal));

            clienteGrid.addCell(new Phrase("Email", bold));
            PdfPCell emailCell = new PdfPCell(
                new Phrase(cliente.getEmail() != null ? cliente.getEmail() : "-", email)
            );
//            emailCell.setBorderColor(new java.awt.Color(255, 0, 0));

            emailCell.setColspan(3);
            clienteGrid.addCell(emailCell);

            clienteGrid.addCell(new Phrase("CPF / CNPJ", bold));
            clienteGrid.addCell(new Phrase(cliente.getDocumento() != null ? cliente.getDocumento() : "-", normal));
            PdfPCell vazio2 = new PdfPCell(new Phrase(""));
//            vazio2.setBorderColor(new java.awt.Color(255, 0, 0));
            vazio2.setColspan(2);
            clienteGrid.addCell(vazio2);

            // ===== DADOS DO ORÇAMENTO =====
            PdfPTable dadosOrc = new PdfPTable(2);
            dadosOrc.getDefaultCell();
//            .setBorderColor(Color.RED);
            dadosOrc.setWidthPercentage(100);

            dadosOrc.addCell(new Phrase("Orçamento ID:", bold));
            dadosOrc.addCell(new Phrase(String.valueOf(orc.getId()), normal));

            dadosOrc.addCell(new Phrase("Técnico Responsável:", bold));
            dadosOrc.addCell(new Phrase(
                orc.getTecnicoResponsavel() != null ? orc.getTecnicoResponsavel().getNome() : "-", normal));

            dadosOrc.addCell(new Phrase("Tipo de Pagamento:", bold));
            dadosOrc.addCell(new Phrase(orc.getTipoPagamento() != null ? orc.getTipoPagamento() : "-", normal));

            dadosOrc.addCell(new Phrase("Status do Orçamento:", bold));
            dadosOrc.addCell(new Phrase(orc.getStatus() != null ? orc.getStatus().name() : "-", normal));

            // ===== TÍTULO DESCRIÇÃO =====
            PdfPTable tituloObs = new PdfPTable(1);
            tituloObs.getDefaultCell();
//            .setBorderColor(Color.RED);
            tituloObs.setWidthPercentage(100);
            
            PdfPCell tituloObsCell = new PdfPCell(new Phrase("Descrição", bold));
//            tituloObsCell.setBorderColor(new java.awt.Color(255, 0, 0));
            tituloObsCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tituloObsCell.setPadding(6);
            tituloObsCell.setBorderWidth(1.5f);
            tituloObs.addCell(tituloObsCell);

            // ===== DESCRIÇÃO DO PROBLEMA =====
            PdfPTable obs = new PdfPTable(1);
            obs.getDefaultCell();
//            .setBorderColor(Color.RED);
            obs.setWidthPercentage(100);
            PdfPCell obsCell = new PdfPCell(
                new Phrase(os.getDescricaoProblema() != null ? os.getDescricaoProblema() : "", normal)
            );
//            obsCell.setBorderColor(new java.awt.Color(255, 0, 0));
            obsCell.setFixedHeight(80);
            obs.addCell(obsCell);

            // ===== DATAS =====
            PdfPTable dadosDatas = new PdfPTable(2);
            dadosDatas.getDefaultCell();
//            .setBorderColor(Color.RED);
            dadosDatas.setWidthPercentage(100);

            String textoCaixa1 =
                "Data de Abertura da OS: " + (os.getDataAbertura() != null ? os.getDataAbertura().format(dtf) : "-") + "\n" +
                "Data Prevista: " + (os.getDataFechamentoPrevisto() != null ? os.getDataFechamentoPrevisto().format(dtf) : "-") + "\n" +
                "Data de Início da OS: " + (os.getDataInicio() != null ? os.getDataInicio().format(dtf) : "-") + "\n" +
                "Data de Fechamento da OS: " + (os.getDataFechamentoReal() != null ? os.getDataFechamentoReal().format(dtf) : "-");

            String textoCaixa2 =
                "Data de Criação do Orçamento: " + (orc.getDataCriacao() != null ? orc.getDataCriacao().format(dtf) : "-") + "\n" +
                "Data de Decisão: " + (orc.getDataDecisao() != null ? orc.getDataDecisao().format(dtf) : "-") + "\n";

            PdfPCell caixa1 = new PdfPCell(new Phrase(textoCaixa1, normal));
//            caixa1.setBorderColor(new java.awt.Color(255, 0, 0));
            caixa1.setPadding(8f);
            caixa1.setBorderWidth(1.5f);

            PdfPCell caixa2 = new PdfPCell(new Phrase(textoCaixa2, normal));
//            caixa2.setBorderColor(new java.awt.Color(255, 0, 0));
            caixa2.setPadding(8f);
            caixa2.setBorderWidth(1.5f);

            dadosDatas.addCell(caixa1);
            dadosDatas.addCell(caixa2);

            // ===== TABELA DE PEÇAS (uma linha por item) =====
            PdfPTable tabelaItens = new PdfPTable(2);
            tabelaItens.getDefaultCell();
//            .setBorderColor(Color.RED);
            tabelaItens.setWidthPercentage(100);
            tabelaItens.setSpacingBefore(6);

            PdfPCell cabPeca = new PdfPCell(new Phrase("Peça / Serviço", bold));
//            cabPeca.setBorderColor(new java.awt.Color(255, 0, 0));
            cabPeca.setBorderWidth(1.5f);
            tabelaItens.addCell(cabPeca);

            PdfPCell cabValorItem = new PdfPCell(new Phrase("Valor", bold));
//            cabValorItem.setBorderColor(new java.awt.Color(255, 0, 0));
            cabValorItem.setBorderWidth(1.5f);
            cabValorItem.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabelaItens.addCell(cabValorItem);

            for (ItemOrcamento item : orc.getItens()) {
                tabelaItens.addCell(new Phrase(item.getDescricao(), normal));
                PdfPCell cellVlr = new PdfPCell(new Phrase(nf.format(item.getValor()), normal));
//                cellVlr.setBorderColor(new java.awt.Color(255, 0, 0));
                cellVlr.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaItens.addCell(cellVlr);
            }

            // ===== RODAPÉ FINANCEIRO =====
            BigDecimal maoDeObra  = orc.getMão_de_obra() != null ? orc.getMão_de_obra() : BigDecimal.ZERO;
            BigDecimal totalPecas = orc.getValorTotalPecas();
            BigDecimal total      = totalPecas.add(maoDeObra);

            PdfPTable financeiro = new PdfPTable(3);
            financeiro.getDefaultCell();
//            .setBorderColor(Color.RED);
            financeiro.setWidthPercentage(100);

            PdfPCell cellMO = new PdfPCell(new Phrase("Mão de Obra", bold));
//            cellMO.setBorderColor(new java.awt.Color(255, 0, 0));
            cellMO.setBorderWidth(1.5f);
            financeiro.addCell(cellMO);

            PdfPCell cellPS = new PdfPCell(new Phrase("Peças / Serviço", bold));
//            cellPS.setBorderColor(new java.awt.Color(255, 0, 0));
            cellPS.setBorderWidth(1.5f);
            financeiro.addCell(cellPS);

            PdfPCell cellTtl = new PdfPCell(new Phrase("Total", bold));
//            cellTtl.setBorderColor(new java.awt.Color(255, 0, 0));
            cellTtl.setBorderWidth(1.5f);
            financeiro.addCell(cellTtl);

            PdfPCell cellMao = new PdfPCell(new Phrase(nf.format(maoDeObra), normal));
//            cellMao.setBorderColor(new java.awt.Color(255, 0, 0));
            cellMao.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellMao.setBorderWidth(1.5f);
            financeiro.addCell(cellMao);

            PdfPCell cellPecas = new PdfPCell(new Phrase(nf.format(totalPecas), normal));
//            cellPecas.setBorderColor(new java.awt.Color(255, 0, 0));
            cellPecas.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellPecas.setBorderWidth(1.5f);
            financeiro.addCell(cellPecas);

            PdfPCell cellTotal = new PdfPCell(new Phrase(nf.format(total), normal));
//            cellTotal.setBorderColor(new java.awt.Color(255, 0, 0));
            cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTotal.setBorderWidth(1.5f);
            financeiro.addCell(cellTotal);

            // ===== ASSINATURAS =====
            PdfPTable assinaturas = new PdfPTable(2);
            assinaturas.setWidthPercentage(100);
            assinaturas.setSpacingBefore(80);

            PdfPCell clienteCell = new PdfPCell(new Phrase("Assinatura do Cliente: ____________________________", normal));
            clienteCell.setBorder(Rectangle.NO_BORDER);
            clienteCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            clienteCell.setFixedHeight(50);
            assinaturas.addCell(clienteCell);

            PdfPCell empresaCell = new PdfPCell(new Phrase("Assinatura da Empresa: ____________________________", normal));
            empresaCell.setBorder(Rectangle.NO_BORDER);
            empresaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            empresaCell.setFixedHeight(50);
            assinaturas.addCell(empresaCell);

            // ===== MONTAGEM DO DOCUMENTO =====
            document.add(cabecalho);
            document.add(clienteGrid);
            document.add(dadosOrc);
            document.add(tituloObs);
            document.add(obs);
            document.add(new Paragraph("\n"));
            document.add(tabelaItens);
            document.add(new Paragraph("\n"));
            document.add(dadosDatas);
            document.add(financeiro);
            document.add(assinaturas);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF da ordem de serviço", e);
        } finally {
            if (document.isOpen()) document.close();
        }
    }
}
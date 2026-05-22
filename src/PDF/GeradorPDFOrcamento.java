package PDF;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import Classe.ItemOrcamento;
import Classe.Orçamento;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GeradorPDFOrcamento {

    public static void gerarOrcamento(Orçamento orc, File destino) {
        try {
            if (orc == null) {
                throw new IllegalArgumentException("Orçamento inválido para gerar PDF");
            }

            if (destino == null) {
                throw new IllegalArgumentException("Destino inválido para salvar o PDF");
            }

            File pastaDestino = destino.getParentFile();

            if (pastaDestino != null && !pastaDestino.exists()) {
                pastaDestino.mkdirs();
            }

            Document document = new Document();

            PdfWriter.getInstance(
                document,
                new FileOutputStream(destino)
            );

            document.open();

            Font bold = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 9);

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // ===== TÍTULO =====
            PdfPTable titulo = new PdfPTable(1);
            titulo.getDefaultCell();
            titulo.setWidthPercentage(100);

            PdfPCell cellTitulo = new PdfPCell(new Phrase("ORÇAMENTO", bold));
            cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTitulo.setPadding(8);
            titulo.addCell(cellTitulo);

            // ===== DADOS DO ORÇAMENTO =====
            PdfPTable dados = new PdfPTable(2);
            dados.getDefaultCell();
            dados.setWidthPercentage(100);

            dados.addCell(new Phrase("ID:", bold));
            dados.addCell(new Phrase(String.valueOf(orc.getId()), normal));

            dados.addCell(new Phrase("Técnico Responsável:", bold));
            dados.addCell(new Phrase(
                orc.getTecnicoResponsavel() != null
                    ? orc.getTecnicoResponsavel().getNome()
                    : "-",
                normal
            ));

            dados.addCell(new Phrase("Tipo de Pagamento:", bold));
            dados.addCell(new Phrase(
                orc.getTipoPagamento() != null
                    ? orc.getTipoPagamento()
                    : "-",
                normal
            ));

            dados.addCell(new Phrase("Status:", bold));
            dados.addCell(new Phrase(
                orc.getStatus() != null
                    ? formatarStatus(orc.getStatus())
                    : "-",
                normal
            ));

            dados.addCell(new Phrase("Data de Criação:", bold));
            dados.addCell(new Phrase(
                orc.getDataCriacao() != null
                    ? orc.getDataCriacao().format(dtf)
                    : "-",
                normal
            ));

            if (orc.getDataDecisao() != null) {
                dados.addCell(new Phrase("Data da Decisão:", bold));
                dados.addCell(new Phrase(orc.getDataDecisao().format(dtf), normal));
            }

            // ===== TABELA DE PEÇAS =====
            PdfPTable tabelaItens = new PdfPTable(2);
            tabelaItens.getDefaultCell();
            tabelaItens.setWidthPercentage(100);
            tabelaItens.setSpacingBefore(8);

            PdfPCell cabPeca = new PdfPCell(new Phrase("Peça / Serviço", bold));

            PdfPCell cabValor = new PdfPCell(new Phrase("Valor", bold));
            cabValor.setHorizontalAlignment(Element.ALIGN_CENTER);

            tabelaItens.addCell(cabPeca);
            tabelaItens.addCell(cabValor);

            if (orc.getItens() != null) {
                for (ItemOrcamento item : orc.getItens()) {
                    tabelaItens.addCell(new Phrase(
                        item.getDescricao() != null
                            ? item.getDescricao()
                            : "-",
                        normal
                    ));

                    PdfPCell cellValorItem = new PdfPCell(new Phrase(
                        nf.format(item.getValor() != null ? item.getValor() : BigDecimal.ZERO),
                        normal
                    ));

                    cellValorItem.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaItens.addCell(cellValorItem);
                }
            }

            // ===== RODAPÉ FINANCEIRO =====
            BigDecimal maoDeObra = orc.getMão_de_obra() != null
                ? orc.getMão_de_obra()
                : BigDecimal.ZERO;

            BigDecimal totalPecas = orc.getValorTotalPecas() != null
                ? orc.getValorTotalPecas()
                : BigDecimal.ZERO;

            BigDecimal total = totalPecas.add(maoDeObra);

            PdfPTable rodape = new PdfPTable(3);
            rodape.getDefaultCell();
            rodape.setWidthPercentage(100);
            rodape.setSpacingBefore(4);

            rodape.addCell(new Phrase("Mão de Obra", bold));
            rodape.addCell(new Phrase("Peças (total)", bold));
            rodape.addCell(new Phrase("Total", bold));

            PdfPCell cellMao = new PdfPCell(new Phrase(nf.format(maoDeObra), normal));
            cellMao.setHorizontalAlignment(Element.ALIGN_CENTER);
            rodape.addCell(cellMao);

            PdfPCell cellPecas = new PdfPCell(new Phrase(nf.format(totalPecas), normal));
            cellPecas.setHorizontalAlignment(Element.ALIGN_CENTER);
            rodape.addCell(cellPecas);

            PdfPCell cellTotal = new PdfPCell(new Phrase(nf.format(total), normal));
            cellTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            rodape.addCell(cellTotal);

            // ===== ASSINATURAS =====
            PdfPTable assinaturas = new PdfPTable(2);
            assinaturas.setWidthPercentage(100);
            assinaturas.setSpacingBefore(80);

            PdfPCell tecnicoCell = new PdfPCell(new Phrase("Assinatura: ____________________________", normal));
            tecnicoCell.setBorder(Rectangle.NO_BORDER);
            tecnicoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tecnicoCell.setFixedHeight(50);
            assinaturas.addCell(tecnicoCell);

            PdfPCell empresaCell = new PdfPCell(new Phrase("Assinatura: ____________________________", normal));
            empresaCell.setBorder(Rectangle.NO_BORDER);
            empresaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            empresaCell.setFixedHeight(50);
            assinaturas.addCell(empresaCell);

            document.add(titulo);
            document.add(dados);
            document.add(tabelaItens);
            document.add(rodape);
            document.add(assinaturas);

            document.close();

            System.out.println("PDF de orçamento salvo em: " + destino.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF de orçamento: " + e.getMessage(), e);
        }
    }

    private static String formatarStatus(Orçamento.Status status) {
        switch (status) {
            case PENDENTE:
                return "Pendente";
            case APROVADO:
                return "Aprovado";
            case REPROVADO:
                return "Reprovado";
            case EXPIRADO:
                return "Expirado";
            default:
                return status.toString();
        }
    }
}
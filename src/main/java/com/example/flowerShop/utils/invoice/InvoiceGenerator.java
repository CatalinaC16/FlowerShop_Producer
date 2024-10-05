package com.example.flowerShop.utils.invoice;

import com.example.flowerShop.entity.Order;

import com.example.flowerShop.entity.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;


import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class InvoiceGenerator {

    public static byte[] generateInvoicePDF(Order order, List<OrderItem> items) throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font fontTitlu = FontFactory.getFont(FontFactory.TIMES_BOLD, 24, BaseColor.PINK);
            Font fontAntet = FontFactory.getFont(FontFactory.TIMES_BOLD, 16, BaseColor.BLACK);
            Font fontText = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
            Font fontMesaj = FontFactory.getFont(FontFactory.TIMES_ITALIC, 12, BaseColor.GRAY);

            Paragraph titlu = new Paragraph("Factura comanda", fontTitlu);
            titlu.setAlignment(Element.ALIGN_CENTER);
            titlu.setSpacingAfter(20);
            document.add(titlu);

            document.add(createParagraf("ID Comanda: " + order.getId(), fontAntet));
            document.add(createParagraf("Data Comenzii: " + order.getOrderDate(), fontText));
            document.add(createParagraf("Metoda de Plata: " + order.getPay(), fontText));
            document.add(createParagraf("Adresa de Livrare: " + order.getAddress(), fontText));
            document.add(new Paragraph("Produse comandate:", fontAntet));

            PdfPTable table = getPdfTable(items, fontAntet);

            document.add(table);

            document.add(createParagraf("Pret Total: " + order.getTotalPrice(), fontAntet));

            document.add(createParagraf("Va multumim ca ati ales floraria noastra!", fontMesaj));

        } finally {
            if (document != null) {
                document.close();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static PdfPTable getPdfTable(List<OrderItem> items, Font fontAntet) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 2, 4});

        PdfPCell cell1 = new PdfPCell(new Phrase("Nr. crt.", fontAntet));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase("Nume produs", fontAntet));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Phrase("Cantitate", fontAntet));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Phrase("Pret", fontAntet));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell4);

        int index = 1;
        for (OrderItem orderItem : items) {
            table.addCell(String.valueOf(index++));
            table.addCell(orderItem.getProduct().getName());
            table.addCell(String.valueOf(orderItem.getQuantity()));
            table.addCell(String.valueOf(orderItem.getQuantity() * orderItem.getProduct().getPrice()));
        }
        return table;
    }

    private static Paragraph createParagraf(String content, Font font) {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setSpacingAfter(10);
        return paragraph;
    }
}

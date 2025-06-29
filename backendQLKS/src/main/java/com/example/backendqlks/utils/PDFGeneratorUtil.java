package com.example.backendqlks.utils;

import com.example.backendqlks.dto.invoice.ResponseInvoiceDto;
import com.example.backendqlks.service.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PDFGeneratorUtil {
    private final InvoiceDetailService invoiceDetailService;
    private final RentalFormService rentalFormService;
    private final RentalFormDetailService rentalFormDetailService;
    private final GuestService guestService;
    private final RoomService roomService;
    private final RentalExtensionFormService rentalExtensionFormService;

    public PDFGeneratorUtil(InvoiceDetailService invoiceDetailService,
                            RentalFormService rentalFormService,
                            RentalFormDetailService rentalFormDetailService,
                            GuestService guestService,
                            RoomService roomService, RentalExtensionFormService rentalExtensionFormService) {
        this.invoiceDetailService = invoiceDetailService;
        this.rentalFormService = rentalFormService;
        this.rentalFormDetailService = rentalFormDetailService;
        this.guestService = guestService;
        this.roomService = roomService;
        this.rentalExtensionFormService = rentalExtensionFormService;
    }

    public byte[] generateInvoicePdf(ResponseInvoiceDto invoiceDto) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(new Rectangle(400, 842), 36, 36, 36, 36);
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            // Font hỗ trợ tiếng Việt
            URL fontUrl = getClass().getClassLoader().getResource("fonts/times.ttf");
            if (fontUrl == null) {
                throw new RuntimeException("Không tìm thấy file font: fonts/times.ttf");
            }
            BaseFont bf = BaseFont.createFont(
                    fontUrl.toString(),
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );

            // Đánh số trang
//            writer.setPageEvent(new PdfPageEventHelper() {
//                public void onEndPage(PdfWriter writer, Document document) {
//                    PdfContentByte cb = writer.getDirectContent();
//                    Phrase footer = new Phrase("Trang " + writer.getPageNumber(), new Font(bf, 10));
//                    ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
//                            footer, (document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
//                }
//            });

            document.open();

            // Chèn logo vào đầu trang
            String logoPath = "images/roomify-logo2.png";
            InputStream logoStream = getClass().getClassLoader().getResourceAsStream(logoPath);
            if (logoStream != null) {
                Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
                logo.scaleToFit(150, 150);
                logo.setAlignment(Image.ALIGN_CENTER); // CĂN GIỮA
                document.add(logo);
                document.add(Chunk.NEWLINE); // xuống dòng
            } else {
                throw new RuntimeException("Không tìm thấy logo: " + logoPath);
            }

            Font headerFont = new Font(bf, 16, Font.BOLD);
            Font middleFont = new Font(bf, 14, Font.BOLD);
            Font normalFont = new Font(bf, 12);

            // Trang đầu
            document.add(new Paragraph("HÓA ĐƠN THANH TOÁN", headerFont));
            document.add(new Paragraph(" ", normalFont));
            document.add(new Paragraph("Mã hóa đơn: " + invoiceDto.getId(), normalFont));
            document.add(new Paragraph("Ngày tạo: " + invoiceDto.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normalFont));
            document.add(new Paragraph("Khách hàng: " + invoiceDto.getPayingGuestName() + " (ID: " + invoiceDto.getPayingGuestId() + ")", normalFont));
            document.add(new Paragraph("Nhân viên lập hóa đơn: " + invoiceDto.getStaffName() + " (ID: " + invoiceDto.getStaffId() + ")", normalFont));
            document.add(new Paragraph("Tổng chi phí: " + String.format("%,.0f VND", invoiceDto.getTotalReservationCost()), normalFont));
            document.add(Chunk.NEWLINE);

            // Chi tiết hóa đơn
            document.add(new Paragraph("CHI TIẾT", headerFont));
            document.add(new Paragraph("-----------", normalFont));
            document.add(new Paragraph(" ", normalFont));

            List<Integer> rentalFormIds=invoiceDto.getRentalFormIds();
            for (var id : rentalFormIds) {
                var rentalForm = rentalFormService.getRentalFormById(id);
                var room=roomService.getRoomById(rentalForm.getRoomId());
                document.add(new Paragraph("Phiếu thuê ID: "+rentalForm.getId(), headerFont));
                document.add(new Paragraph(" ", normalFont));

                document.add(new Paragraph("Phòng ID: " + rentalForm.getRoomId(), normalFont));
                document.add(new Paragraph("Tên phòng: " + rentalForm.getRoomName(), normalFont));
                document.add(new Paragraph("Loại phòng: " + room.getRoomTypeName(), normalFont));
                document.add(new Paragraph("Ngày thuê: " + rentalForm.getRentalDate(), normalFont));
                document.add(new Paragraph("Số ngày thuê: " + rentalForm.getNumberOfRentalDays(), normalFont));
                document.add(new Paragraph("Ghi chú: " + rentalForm.getNote(), normalFont));

                document.add(new Paragraph(" ", normalFont));
                document.add(new Paragraph("DANH SÁCH GIA HẠN", middleFont));

                List<Integer> rentalExtensionFormIds=rentalForm.getRentalExtensionFormIds();
                if (rentalExtensionFormIds != null && !rentalExtensionFormIds.isEmpty()) {
                    for (var extensionId: rentalExtensionFormIds) {
                        var rentalExtensionForm=rentalExtensionFormService.getRentalExtensionFormById(extensionId);
                        document.add(new Paragraph("Số ngày gia hạn: " + rentalExtensionForm.getNumberOfRentalDays(), normalFont));
                    }
                }
                else document.add(new Paragraph("Trống", normalFont));
            }

            //Khach hang thanh toan
            document.add(new Paragraph(" ", normalFont));
            document.add(new Paragraph("-----------", normalFont));
            document.add(new Paragraph("Khách hàng thanh toán:", headerFont));
            var guest = guestService.get(invoiceDto.getPayingGuestId());
            document.add(new Paragraph("  Họ tên: " + guest.getName(), normalFont));
            document.add(new Paragraph("  SĐT: " + guest.getPhoneNumber(), normalFont));
            document.add(new Paragraph("  CCCD: " + guest.getIdentificationNumber(), normalFont));
            document.add(new Paragraph("  Email: " + guest.getEmail(), normalFont));

            document.add(new Paragraph(" ", normalFont));
            Paragraph line = new Paragraph("-----------", normalFont);
            line.setAlignment(Element.ALIGN_CENTER);
            document.add(line);
            Font italicFont = new Font(bf, 12, Font.ITALIC);
            Paragraph thankYou = new Paragraph("Cảm ơn và hẹn gặp lại quý khách!", italicFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF", e);
        }
    }
}

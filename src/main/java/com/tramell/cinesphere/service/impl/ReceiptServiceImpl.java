package com.tramell.cinesphere.service.impl;

import com.tramell.cinesphere.entity.Booking;
import com.tramell.cinesphere.entity.ShowSeat;
import com.tramell.cinesphere.exception.ResourceNotFoundException;
import com.tramell.cinesphere.repository.BookingRepository;
import com.tramell.cinesphere.service.ReceiptService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public String generateReceiptHtml(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        String movieTitle = escapeHtml(booking.getShow().getMovie().getTitle());
        String theatreName = escapeHtml(booking.getShow().getTheatre().getName());
        String totalAmount = booking.getTotalAmount().toString();
        String bookedSeats = booking.getBookedSeats().stream()
                .map(ShowSeat::getSeatNumber)
                .map(this::escapeHtml)
                .collect(Collectors.joining(", "));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");
        String generatedAt = booking.getBookingDate().format(formatter);

        return String.format(
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Cinesphere Receipt</title>\n" +
            "    <style>\n" +
            "        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap');\n" +
            "        \n" +
            "        body {\n" +
            "            background-color: #050a11;\n" +
            "            color: #e2e8f0;\n" +
            "            font-family: 'Inter', sans-serif;\n" +
            "            display: flex;\n" +
            "            flex-direction: column;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "            min-height: 100vh;\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "\n" +
            "        .receipt-card {\n" +
            "            background: rgba(255, 255, 255, 0.03);\n" +
            "            border: 1px solid rgba(255, 255, 255, 0.1);\n" +
            "            backdrop-filter: blur(10px);\n" +
            "            border-radius: 12px;\n" +
            "            padding: 40px;\n" +
            "            width: 100%%;\n" +
            "            max-width: 400px;\n" +
            "            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);\n" +
            "            position: relative;\n" +
            "            overflow: hidden;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .receipt-card::before {\n" +
            "            content: '';\n" +
            "            position: absolute;\n" +
            "            top: 0;\n" +
            "            left: 0;\n" +
            "            width: 100%%;\n" +
            "            height: 4px;\n" +
            "            background: linear-gradient(90deg, #3b82f6, #8b5cf6);\n" +
            "        }\n" +
            "\n" +
            "        h1 {\n" +
            "            font-weight: 300;\n" +
            "            letter-spacing: 2px;\n" +
            "            font-size: 24px;\n" +
            "            margin-top: 0;\n" +
            "            text-align: center;\n" +
            "            text-transform: uppercase;\n" +
            "            color: #ffffff;\n" +
            "        }\n" +
            "\n" +
            "        .divider {\n" +
            "            height: 1px;\n" +
            "            background: rgba(255, 255, 255, 0.1);\n" +
            "            margin: 20px 0;\n" +
            "        }\n" +
            "\n" +
            "        .detail-row {\n" +
            "            display: flex;\n" +
            "            justify-content: space-between;\n" +
            "            margin-bottom: 12px;\n" +
            "            font-size: 14px;\n" +
            "        }\n" +
            "\n" +
            "        .detail-label {\n" +
            "            color: #94a3b8;\n" +
            "        }\n" +
            "\n" +
            "        .detail-value {\n" +
            "            font-weight: 600;\n" +
            "            color: #ffffff;\n" +
            "            text-align: right;\n" +
            "        }\n" +
            "\n" +
            "        .total-row {\n" +
            "            font-size: 18px;\n" +
            "            margin-top: 20px;\n" +
            "            color: #60a5fa;\n" +
            "        }\n" +
            "\n" +
            "        .signature {\n" +
            "            margin-top: 40px;\n" +
            "            text-align: center;\n" +
            "            font-family: 'Inter', sans-serif;\n" +
            "            font-weight: 300;\n" +
            "            letter-spacing: 4px;\n" +
            "            text-transform: uppercase;\n" +
            "            color: #64748b;\n" +
            "            font-size: 12px;\n" +
            "        }\n" +
            "\n" +
            "        .download-btn {\n" +
            "            background: linear-gradient(90deg, #3b82f6, #8b5cf6);\n" +
            "            border: none;\n" +
            "            border-radius: 6px;\n" +
            "            color: #ffffff;\n" +
            "            padding: 12px 24px;\n" +
            "            font-size: 14px;\n" +
            "            font-family: 'Inter', sans-serif;\n" +
            "            font-weight: 600;\n" +
            "            cursor: pointer;\n" +
            "            transition: opacity 0.2s;\n" +
            "            text-transform: uppercase;\n" +
            "            letter-spacing: 1px;\n" +
            "            box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);\n" +
            "        }\n" +
            "\n" +
            "        .download-btn:hover {\n" +
            "            opacity: 0.9;\n" +
            "        }\n" +
            "\n" +
            "        @media print {\n" +
            "            body {\n" +
            "                background-color: white;\n" +
            "            }\n" +
            "            .receipt-card {\n" +
            "                box-shadow: none;\n" +
            "                border: 1px solid #ddd;\n" +
            "                background: #fff;\n" +
            "            }\n" +
            "            h1, .detail-value, .total-row {\n" +
            "                color: #000;\n" +
            "            }\n" +
            "            .detail-label {\n" +
            "                color: #555;\n" +
            "            }\n" +
            "            .download-btn {\n" +
            "                display: none;\n" +
            "            }\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "    <div class=\"receipt-card\">\n" +
            "        <h1>Booking Confirmed</h1>\n" +
            "        \n" +
            "        <div class=\"divider\"></div>\n" +
            "\n" +
            "        <div class=\"detail-row\">\n" +
            "            <span class=\"detail-label\">Receipt ID</span>\n" +
            "            <span class=\"detail-value\">rcpt_%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"detail-row\">\n" +
            "            <span class=\"detail-label\">Date</span>\n" +
            "            <span class=\"detail-value\">%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"divider\"></div>\n" +
            "\n" +
            "        <div class=\"detail-row\">\n" +
            "            <span class=\"detail-label\">Movie</span>\n" +
            "            <span class=\"detail-value\">%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"detail-row\">\n" +
            "            <span class=\"detail-label\">Theatre</span>\n" +
            "            <span class=\"detail-value\">%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"detail-row\">\n" +
            "            <span class=\"detail-label\">Seats</span>\n" +
            "            <span class=\"detail-value\">%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"divider\"></div>\n" +
            "\n" +
            "        <div class=\"detail-row total-row\">\n" +
            "            <span class=\"detail-label\">Amount Paid</span>\n" +
            "            <span class=\"detail-value\">&#8377;%s</span>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"signature\">Cinesphere Reserved</div>\n" +
            "    </div>\n" +
            "\n" +
            "    <button class=\"download-btn\" onclick=\"window.print()\">Download PDF</button>\n" +
            "\n" +
            "</body>\n" +
            "</html>",
            bookingId, generatedAt, movieTitle, theatreName, bookedSeats, totalAmount
        );
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] generateReceiptPdf(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));

        String movieTitle = booking.getShow().getMovie().getTitle();
        String theatreName = booking.getShow().getTheatre().getName();
        String totalAmount = booking.getTotalAmount().toString();
        String bookedSeats = booking.getBookedSeats().stream()
                .map(ShowSeat::getSeatNumber)
                .collect(Collectors.joining(", "));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy hh:mm a");
        String generatedAt = booking.getBookingDate().format(formatter);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 24, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            document.add(new Paragraph("CINESPHERE BOOKING RECEIPT", titleFont));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Receipt ID: rcpt_" + bookingId, normalFont));
            document.add(new Paragraph("Date: " + generatedAt, normalFont));
            document.add(new Paragraph("Movie: " + movieTitle, normalFont));
            document.add(new Paragraph("Theatre: " + theatreName, normalFont));
            document.add(new Paragraph("Seats: " + bookedSeats, normalFont));
            document.add(new Paragraph("Amount Paid: $" + totalAmount, normalFont));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public ReceiptServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}

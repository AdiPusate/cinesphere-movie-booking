package com.tramell.cinesphere.controller;

import com.tramell.cinesphere.service.ReceiptService;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tramell/cinesphere/receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping(value = "/{bookingId}", produces = MediaType.TEXT_HTML_VALUE)
    @PreAuthorize("@bookingService.canAccessBooking(#bookingId, authentication)")
    public ResponseEntity<String> getReceiptHtml(@PathVariable Long bookingId) {
        String html = receiptService.generateReceiptHtml(bookingId);
        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/{bookingId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("@bookingService.canAccessBooking(#bookingId, authentication)")
    public ResponseEntity<byte[]> getReceiptPdf(@PathVariable Long bookingId) {
        byte[] pdf = receiptService.generateReceiptPdf(bookingId);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"receipt_" + bookingId + ".pdf\"")
                .body(pdf);
    }

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }
}

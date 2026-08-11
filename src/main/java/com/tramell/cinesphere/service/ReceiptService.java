package com.tramell.cinesphere.service;

public interface ReceiptService {
    String generateReceiptHtml(Long bookingId);
    byte[] generateReceiptPdf(Long bookingId);
}

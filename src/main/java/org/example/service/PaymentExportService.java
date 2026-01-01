package org.example.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class PaymentExportService {

    public static void appendPaymentLine(
            String filePath,
            String companyName,
            String employeeName,
            String buildingName,
            long apartmentId,
            double amount,
            LocalDate paymentDate
    ) {
        String dateStr = (paymentDate == null) ? "" : paymentDate.toString();

        String line = String.join(",",
                companyName,
                employeeName,
                buildingName,
                String.valueOf(apartmentId),
                String.valueOf(amount),
                dateStr
        );

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot write payments file: " + filePath, e);
        }
    }
}

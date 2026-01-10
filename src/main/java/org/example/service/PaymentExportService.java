package org.example.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentExportService {

    public static void appendPaymentLine(
            String file_path,
            String company_name,
            String employee_name,
            String building_name,
            long apartment_id,
            BigDecimal amount,
            LocalDate payment_date
    ) {
        String dateStr = (payment_date == null) ? "" : payment_date.toString();

        String line = String.join(",",
                company_name,
                employee_name,
                building_name,
                String.valueOf(apartment_id),
                String.valueOf(amount),
                dateStr
        );

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file_path, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot write payments file: " + file_path, e);
        }
    }
}

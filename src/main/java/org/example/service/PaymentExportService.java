package org.example.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
// записва плащане в csv файл
public class PaymentExportService {
    // параметри
    public static void appendPaymentLine(
            String file_path,
            String company_name,
            String employee_name,
            String building_name,
            long apartment_id,
            BigDecimal amount,
            LocalDate payment_date
    ) {
        // датата се преобразува в стринг или е празно поле
        String dateStr = (payment_date == null) ? "" : payment_date.toString();
        // всичко се събира на един ред със запетаи
        String line = String.join(",",
                company_name,
                employee_name,
                building_name,
                String.valueOf(apartment_id),
                String.valueOf(amount),
                dateStr
        );
        // отваря файл и добавя информация в края, а не презаписва
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file_path, true))) {
            // пише ред инфо и следва нов ред
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot write payments file: " + file_path, e);
        }
    }
}

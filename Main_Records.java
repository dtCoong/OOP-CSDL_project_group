package Vaccination_Records_Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main_Records {
    public static void main(String[] args) {
        VaccinationRecords record = new VaccinationRecords(
                101,
                5,
                1,
                "Comirnaty (Pfizer-BioNTech)",
                2,
                LocalDate.of(2025, 10, 8),
                LocalDate.of(2026, 4, 8),
                "PF1234567",
                "Da tiêm",
                "Không có phan ung phu.",
                LocalDateTime.now()
        );

        System.out.println("===== Vaccination Records ===== \n");
        System.out.println(record);
    }
}


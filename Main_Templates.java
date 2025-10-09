package Vaccine_Templates_Table;

import java.time.LocalDateTime;

public class Main_Templates {
    public static void main(String[] args) {
        VaccineTemplates vaccine = new VaccineTemplates(
                1,
                "Comirnaty (Pfizer-BioNTech)",
                "Phong ngua COVID-19 do virus SARS-CoV-2 gây ra",
                180,   
                3650,  
                21,    
                2,  
                "Tiêm bap tay. Theo doi 30 phút sau tiêm.",
                LocalDateTime.now()
        );

        System.out.println("===== Vaccine Information ===== \n");
        System.out.println(vaccine);
        System.out.println();
        System.out.println("Vaccine name: " + vaccine.getVaccineName());
        System.out.println("Total doses: " + vaccine.getTotalDoses());
        System.out.println("Interval between doses: " + vaccine.getIntervalDays() + " days");
        System.out.println("Record creation date: " + vaccine.getCreatedAt());
    }
}


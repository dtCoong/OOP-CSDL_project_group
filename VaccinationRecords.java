package Vaccination_Records_Table;

import java.time.LocalDateTime;
import java.time.LocalDate;


public class VaccinationRecords {
    private int vaccinationId;        
    private int memberId;             
    private Integer templateId;       
    private String vaccineName;       
    private int doseNumber;           
    private LocalDate vaccinationDate;
    private LocalDate nextDueDate;    
    private String batchNumber;       
    private String status;            
    private String notes;             
    private LocalDateTime createdAt;  
    
    public VaccinationRecords() {}

    public VaccinationRecords(int vaccinationId, int memberId, Integer templateId, String vaccineName,
                              int doseNumber, LocalDate vaccinationDate, LocalDate nextDueDate,
                              String batchNumber, String status, String notes, LocalDateTime createdAt) {
        this.vaccinationId = vaccinationId;
        this.memberId = memberId;
        this.templateId = templateId;
        this.vaccineName = vaccineName;
        this.doseNumber = doseNumber;
        this.vaccinationDate = vaccinationDate;
        this.nextDueDate = nextDueDate;
        this.batchNumber = batchNumber;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }


    public int getVaccinationId(){ 
        return vaccinationId; 
    }
    public void setVaccinationId(int vaccinationId){ 
        this.vaccinationId = vaccinationId; 
    }

    public int getMemberId() { 
        return memberId; 
    }
    public void setMemberId(int memberId) { 
        this.memberId = memberId; 
    }

    public Integer getTemplateId() { 
        return templateId; 
    }
    public void setTemplateId(Integer templateId) { 
        this.templateId = templateId; 
    }

    public String getVaccineName() { 
        return vaccineName; 
    }
    public void setVaccineName(String vaccineName) { 
        this.vaccineName = vaccineName; 
    }

    public int getDoseNumber() { 
        return doseNumber; 
    }
    public void setDoseNumber(int doseNumber) { 
        this.doseNumber = doseNumber; 
    }

    public LocalDate getVaccinationDate() { 
        return vaccinationDate; 
    }
    public void setVaccinationDate(LocalDate vaccinationDate) { 
        this.vaccinationDate = vaccinationDate; 
    }

    public LocalDate getNextDueDate() { 
        return nextDueDate; 
    }
    public void setNextDueDate(LocalDate nextDueDate) { 
        this.nextDueDate = nextDueDate; 
    }

    public String getBatchNumber() { 
        return batchNumber; 
    }
    public void setBatchNumber(String batchNumber) { 
        this.batchNumber = batchNumber; 
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getNotes() { 
        return notes; 
    }
    public void setNotes(String notes) { 
        this.notes = notes; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }


    public String toString() {
        return  "vaccinationId: " + vaccinationId + "\n" +
                "memberId: " + memberId + "\n" +
                "templateId: " + templateId + "\n" +
                "vaccineName: " + vaccineName + '\n' +
                "doseNumber: " + doseNumber + "\n" +
                "vaccinationDate: " + vaccinationDate + "\n" +
                "nextDueDate: " + nextDueDate + "\n" +
                "batchNumber: " + batchNumber + "\n" +
                "status: " + status + "\n" +
                "notes: " + notes + "\n" +
                "createdAt: " + createdAt;
    }
}


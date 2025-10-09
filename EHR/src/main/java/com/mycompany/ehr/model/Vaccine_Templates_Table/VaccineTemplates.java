package EHR.src.main.java.com.mycompany.ehr.model.Vaccine_Templates_Table;
import java.time.LocalDateTime;

public class VaccineTemplates {
    private int vaccineTemplateId;   
    private String vaccineName;      
    private String description;      
    private int ageFromDays;         
    private int ageToDays;           
    private int intervalDays;        
    private int totalDoses;          
    private String notes;            
    private LocalDateTime createdAt; 


    public VaccineTemplates(){}

    public VaccineTemplates(int vaccineTemplateId, String vaccineName, String description,
                           int ageFromDays, int ageToDays, int intervalDays,
                           int totalDoses, String notes, LocalDateTime createdAt){
        this.vaccineTemplateId = vaccineTemplateId;
        this.vaccineName = vaccineName;
        this.description = description;
        this.ageFromDays = ageFromDays;
        this.ageToDays = ageToDays;
        this.intervalDays = intervalDays;
        this.totalDoses = totalDoses;
        this.notes = notes;
        this.createdAt = createdAt;
    }


    public int getVaccineTemplateId() {
        return vaccineTemplateId;
    }

    public void setVaccineTemplateId(int vaccineTemplateId) {
        this.vaccineTemplateId = vaccineTemplateId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAgeFromDays() {
        return ageFromDays;
    }

    public void setAgeFromDays(int ageFromDays) {
        this.ageFromDays = ageFromDays;
    }

    public int getAgeToDays() {
        return ageToDays;
    }

    public void setAgeToDays(int ageToDays) {
        this.ageToDays = ageToDays;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public int getTotalDoses() {
        return totalDoses;
    }

    public void setTotalDoses(int totalDoses) {
        this.totalDoses = totalDoses;
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
        return  "vaccineTemplateId: " + vaccineTemplateId + "\n" +
                "vaccineName: " + vaccineName + '\'' + "\n" +
                "description: " + description + '\'' + "\n" +
                "ageFromDays: " + ageFromDays + "\n" +
                "ageToDays: " + ageToDays + "\n" +
                "intervalDays: " + intervalDays + "\n" +
                "totalDoses: " + totalDoses + "\n" +
                "notes: " + notes + "\n" +
                "createdAt: " + createdAt + "\n" ;
    }
}

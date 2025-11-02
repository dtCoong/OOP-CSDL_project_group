package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class VaccineTemplates {
    private int template_id;
    private String vaccine_name;
    private String description;
    private int age_from_days;
    private Integer age_to_days; 
    private Integer interval_days; 
    private int total_doses;
    private String notes;
    private LocalDateTime created_at;

    
    public VaccineTemplates() {}

    public VaccineTemplates(int template_id, String vaccine_name, String description, int age_from_days, Integer age_to_days, Integer interval_days, int total_doses, String notes, LocalDateTime created_at) {
        this.template_id = template_id;
        this.vaccine_name = vaccine_name;
        this.description = description;
        this.age_from_days = age_from_days;
        this.age_to_days = age_to_days;
        this.interval_days = interval_days;
        this.total_doses = total_doses;
        this.notes = notes;
        this.created_at = created_at;
    }
    

    public int getTemplate_id() { return template_id; }
    public void setTemplate_id(int template_id) { this.template_id = template_id; }

    public String getVaccine_name() { return vaccine_name; }
    public void setVaccine_name(String vaccine_name) { this.vaccine_name = vaccine_name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAge_from_days() { return age_from_days; }
    public void setAge_from_days(int age_from_days) { this.age_from_days = age_from_days; }

    public Integer getAge_to_days() { return age_to_days; }
    public void setAge_to_days(Integer age_to_days) { this.age_to_days = age_to_days; }

    public Integer getInterval_days() { return interval_days; }
    public void setInterval_days(Integer interval_days) { this.interval_days = interval_days; }

    public int getTotal_doses() { return total_doses; }
    public void setTotal_doses(int total_doses) { this.total_doses = total_doses; }


    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
}
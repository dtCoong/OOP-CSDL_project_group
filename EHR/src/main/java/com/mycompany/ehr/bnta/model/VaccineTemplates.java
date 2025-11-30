package com.mycompany.ehr.bnta.model;

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
        setTemplate_id(template_id);
        setVaccine_name(vaccine_name);
        setDescription(description);
        setAge_from_days(age_from_days);
        setAge_to_days(age_to_days);
        setInterval_days(interval_days);
        setTotal_doses(total_doses);
        setNotes(notes);
        setCreated_at(created_at);
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        if(template_id > 0)
            this.template_id = template_id;
    }


    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        if(vaccine_name != null && !vaccine_name.trim().isEmpty())
            this.vaccine_name = vaccine_name.trim();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description == null || description.trim().isEmpty()) {
            this.description = "";
        } else {
            this.description = description.trim();
        }
    }


    public int getAge_from_days() {
        return age_from_days;
    }

    public void setAge_from_days(int age_from_days) {
        if(age_from_days >= 0)
            this.age_from_days = age_from_days;
    }


    public Integer getAge_to_days() {
        return age_to_days;
    }

    public void setAge_to_days(Integer age_to_days) {
        if(age_to_days != null && age_to_days > 0) {
            this.age_to_days = age_to_days;
        } else if (age_to_days == null) {
            this.age_to_days = null;
        }
    }


    public Integer getInterval_days() {
        return interval_days;
    }

    public void setInterval_days(Integer interval_days) {
        if(interval_days != null && interval_days >= 0) {
            this.interval_days = interval_days;
        } else if (interval_days == null) {
            this.interval_days = null;
        }
    }


    public int getTotal_doses() {
        return total_doses;
    }

    public void setTotal_doses(int total_doses) {
        if(total_doses > 0)
            this.total_doses = total_doses;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if(notes == null || notes.trim().isEmpty()) {
            this.notes = "";
        } else {
            this.notes = notes.trim();
        }
    }


    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        if(created_at == null) {
            this.created_at = LocalDateTime.now();
        } else {
            this.created_at = created_at;
        }
    }
}

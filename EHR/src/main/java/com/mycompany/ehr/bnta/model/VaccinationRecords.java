package com.mycompany.ehr.bnta.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VaccinationRecords {
    private int vaccination_id;
    private int member_id;
    private Integer template_id;
    private String vaccine_name;
    private int dose_number;
    private LocalDate vaccination_date;
    private LocalDate next_due_date;
    private String batch_number;
    private String status;
    private String side_effects;
    private String notes;
    private LocalDateTime created_at;

    public VaccinationRecords() {}

    public VaccinationRecords(int vaccination_id, int member_id, Integer template_id, String vaccine_name, int dose_number, LocalDate vaccination_date, LocalDate next_due_date, String batch_number, String status, String side_effects, String notes, LocalDateTime created_at) {
        setVaccination_id(vaccination_id);
        setMember_id(member_id);
        setTemplate_id(template_id);
        setVaccine_name(vaccine_name);
        setDose_number(dose_number);
        setVaccination_date(vaccination_date);
        setNext_due_date(next_due_date);
        setBatch_number(batch_number);
        setStatus(status);
        setSide_effects(side_effects);
        setNotes(notes);
        setCreated_at(created_at);
    }

    public int getVaccination_id() {
        return vaccination_id;
    }

    public void setVaccination_id(int vaccination_id) {
        if(vaccination_id > 0)
            this.vaccination_id = vaccination_id;
    }


    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        if(member_id > 0)
            this.member_id = member_id;
    }


    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        if(template_id != null && template_id > 0)
            this.template_id = template_id;
    }


    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        if(vaccine_name != null && !vaccine_name.trim().isEmpty())
            this.vaccine_name = vaccine_name.trim();
    }


    public int getDose_number() {
        return dose_number;
    }

    public void setDose_number(int dose_number) {
        if(dose_number >= 0)
            this.dose_number = dose_number;
    }


    public LocalDate getVaccination_date() {
        return vaccination_date;
    }

    public void setVaccination_date(LocalDate vaccination_date) {
        if(vaccination_date != null)
            this.vaccination_date = vaccination_date;
    }


    public LocalDate getNext_due_date() {
        return next_due_date;
    }

    public void setNext_due_date(LocalDate next_due_date) {
            this.next_due_date = next_due_date;
    }


    public String getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(String batch_number) {
        if(batch_number != null && !batch_number.trim().isEmpty()) {
            this.batch_number = batch_number.trim();
        } else {
            this.batch_number = "";
        }
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
             this.status = "Chưa tiêm"; 
        } else {
             this.status = status.trim();
        }
    }


    public String getSide_effects() {
        return side_effects;
    }

    public void setSide_effects(String side_effects) {
        if (side_effects == null || side_effects.trim().isEmpty()) {
            this.side_effects = "Không có";
        } else {
            this.side_effects = side_effects.trim();
        }
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (notes == null || notes.trim().isEmpty()) {
            this.notes = "";
        } else {
            this.notes = notes.trim();
        }
    }


    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        if (created_at == null) {
            this.created_at = LocalDateTime.now();
        } else {
            this.created_at = created_at;
        }
    }
}

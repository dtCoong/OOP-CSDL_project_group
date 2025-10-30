package com.mycompany.ehr.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class MedicationSchedule {


    private int schedule_id;
    private int detail_id;
    private LocalTime scheduled_time;
    private String daily_dosage;
    private boolean is_active;
    private LocalDateTime created_at;

    public MedicationSchedule() {
        this.created_at = LocalDateTime.now();
        this.is_active = true; // Default value
    }

    public MedicationSchedule(int detail_id, LocalTime scheduled_time, String daily_dosage, boolean is_active) {
        this(); // Call default constructor
        this.detail_id = detail_id;
        this.scheduled_time = scheduled_time;
        this.daily_dosage = daily_dosage;
        this.is_active = is_active;
    }

    public MedicationSchedule(int schedule_id, int detail_id, LocalTime scheduled_time,
                              String daily_dosage, boolean is_active, LocalDateTime created_at) {
        this.schedule_id = schedule_id;
        this.detail_id = detail_id;
        this.scheduled_time = scheduled_time;
        this.daily_dosage = daily_dosage;
        this.is_active = is_active;
        this.created_at = created_at;
    }

    public int getScheduleId() { return schedule_id; }
    public void setScheduleId(int schedule_id) { this.schedule_id = schedule_id; }

    public int getDetailId() { return detail_id; }
    public void setDetailId(int detail_id) { this.detail_id = detail_id; }

    public LocalTime getScheduledTime() { return scheduled_time; }
    public void setScheduledTime(LocalTime scheduled_time) { this.scheduled_time = scheduled_time; }

    public String getDailyDosage() { return daily_dosage; }
    public void setDailyDosage(String daily_dosage) { this.daily_dosage = daily_dosage; }

    public boolean isActive() { return is_active; }
    public void setActive(boolean is_active) { this.is_active = is_active; }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "MedicationSchedule{" +
                "schedule_id=" + schedule_id +
                ", detail_id=" + detail_id +
                ", scheduled_time=" + scheduled_time +
                ", daily_dosage='" + daily_dosage + '\'' +
                ", is_active=" + is_active +
                ", created_at=" + created_at +
                '}';
    }
}
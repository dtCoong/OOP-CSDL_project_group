package com.mycompany.ehr;
import java.util.*;
import java.time.*;
public class MedicationSchedule {
    private int scheduleId;
    private int detailId;
    private String scheduledTime;
    private String dailyDosage;
    private boolean isActive;
    private LocalDateTime createdAt;

    public MedicationSchedule() {}

    public MedicationSchedule(int scheduleId, int detailId, String scheduledTime,
                              String dailyDosage, boolean isActive, LocalDateTime createdAt) {
        this.scheduleId = scheduleId;
        this.detailId = detailId;
        this.scheduledTime = scheduledTime;
        this.dailyDosage = dailyDosage;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public String getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(String scheduledTime) { this.scheduledTime = scheduledTime; }

    public String getDailyDosage() { return dailyDosage; }
    public void setDailyDosage(String dailyDosage) { this.dailyDosage = dailyDosage; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
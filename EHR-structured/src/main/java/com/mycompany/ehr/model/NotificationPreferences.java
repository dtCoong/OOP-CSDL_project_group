package com.mycompany.ehr.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Lưu cài đặt thông báo của từng người dùng
 */
public class NotificationPreferences {
    private Integer userId;
    private Set<NotificationType> enabledTypes = new HashSet<>();
    private Boolean enableSound = true;
    private Boolean enablePopup = true;
    private Boolean enableBadge = true;
    private Integer reminderDaysBefore = 1; // Nhắc trước bao nhiêu ngày
    
    public NotificationPreferences() {
        // Mặc định bật tất cả loại thông báo
        for (NotificationType type : NotificationType.values()) {
            enabledTypes.add(type);
        }
    }
    
    public NotificationPreferences(Integer userId) {
        this();
        this.userId = userId;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Set<NotificationType> getEnabledTypes() { return enabledTypes; }
    public void setEnabledTypes(Set<NotificationType> enabledTypes) { this.enabledTypes = enabledTypes; }
    
    public Boolean getEnableSound() { return enableSound; }
    public void setEnableSound(Boolean enableSound) { this.enableSound = enableSound; }
    
    public Boolean getEnablePopup() { return enablePopup; }
    public void setEnablePopup(Boolean enablePopup) { this.enablePopup = enablePopup; }
    
    public Boolean getEnableBadge() { return enableBadge; }
    public void setEnableBadge(Boolean enableBadge) { this.enableBadge = enableBadge; }
    
    public Integer getReminderDaysBefore() { return reminderDaysBefore; }
    public void setReminderDaysBefore(Integer reminderDaysBefore) { this.reminderDaysBefore = reminderDaysBefore; }
    
    public boolean isTypeEnabled(NotificationType type) {
        return enabledTypes.contains(type);
    }
    
    public void enableType(NotificationType type) {
        enabledTypes.add(type);
    }
    
    public void disableType(NotificationType type) {
        enabledTypes.remove(type);
    }
}

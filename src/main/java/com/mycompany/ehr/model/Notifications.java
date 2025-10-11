package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class Notifications {
    private Integer notificationId;
    private Integer recipientUserId;
    private String title;
    private String message;
    private NotificationType type = NotificationType.Khac;
    private Integer relatedDocumentId;
    private Integer relatedAppointmentId;
    private Boolean isRead = false;
    private LocalDateTime createdAt;

    public Integer getNotificationId() { return notificationId; }
    public void setNotificationId(Integer notificationId) { this.notificationId = notificationId; }
    public Integer getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Integer recipientUserId) { this.recipientUserId = recipientUserId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    public Integer getRelatedDocumentId() { return relatedDocumentId; }
    public void setRelatedDocumentId(Integer relatedDocumentId) { this.relatedDocumentId = relatedDocumentId; }
    public Integer getRelatedAppointmentId() { return relatedAppointmentId; }
    public void setRelatedAppointmentId(Integer relatedAppointmentId) { this.relatedAppointmentId = relatedAppointmentId; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean read) { isRead = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override public String toString() {
        return "Notifications{id=" + notificationId + ", title='" + title + "'}";
    }
}

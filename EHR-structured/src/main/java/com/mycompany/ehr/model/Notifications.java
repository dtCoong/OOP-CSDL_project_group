package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class Notifications {
    private Integer notificationId;
    private Integer recipientUserId;
    private String recipientName; // Tên người nhận để hiển thị
    private String title;
    private String message;
    private NotificationType type = NotificationType.ThongTinChung;
    private NotificationPriority priority = NotificationPriority.NORMAL;
    private Integer relatedDocumentId;
    private Integer relatedAppointmentId;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private String actionUrl; // URL hoặc action để navigate khi click
    private Boolean requiresResponse = false; // Có cần phản hồi không
    private String responseText; // Phản hồi từ người dùng
    private LocalDateTime respondedAt;

    // Constructors
    public Notifications() {}
    
    public Notifications(Integer recipientUserId, String title, String message, NotificationType type) {
        this.recipientUserId = recipientUserId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getNotificationId() { return notificationId; }
    public void setNotificationId(Integer notificationId) { this.notificationId = notificationId; }
    
    public Integer getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Integer recipientUserId) { this.recipientUserId = recipientUserId; }
    
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }
    
    public NotificationPriority getPriority() { return priority; }
    public void setPriority(NotificationPriority priority) { this.priority = priority; }
    
    public Integer getRelatedDocumentId() { return relatedDocumentId; }
    public void setRelatedDocumentId(Integer relatedDocumentId) { this.relatedDocumentId = relatedDocumentId; }
    
    public Integer getRelatedAppointmentId() { return relatedAppointmentId; }
    public void setRelatedAppointmentId(Integer relatedAppointmentId) { this.relatedAppointmentId = relatedAppointmentId; }
    
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean read) { isRead = read; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
    
    public String getActionUrl() { return actionUrl; }
    public void setActionUrl(String actionUrl) { this.actionUrl = actionUrl; }
    
    public Boolean getRequiresResponse() { return requiresResponse; }
    public void setRequiresResponse(Boolean requiresResponse) { this.requiresResponse = requiresResponse; }
    
    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }
    
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }

    @Override 
    public String toString() {
        return "Notifications{id=" + notificationId + ", title='" + title + "', priority=" + priority + ", isRead=" + isRead + "}";
    }
}

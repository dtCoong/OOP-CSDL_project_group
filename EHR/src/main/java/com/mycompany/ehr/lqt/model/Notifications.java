package com.mycompany.ehr.lqt.model;

import java.time.LocalDateTime;

public class Notifications {
    private Integer notificationId;
    private Integer userId;              // user_id
    private Integer memberId;            // member_id
    private Integer recipientUserId;     // Giữ lại để tương thích
    private String recipientName;        // Tên người nhận để hiển thị
    private String title;
    private String message;
    private NotificationType type = NotificationType.ThongTinChung;
    private NotificationPriority priority = NotificationPriority.NORMAL;
    private Integer referenceId;         // reference_id
    private String referenceType;        // reference_type (vaccination, medication, appointment)
    private LocalDateTime scheduledTime; // scheduled_time
    private Integer relatedDocumentId;   // Giữ lại để tương thích
    private Integer relatedAppointmentId;// Giữ lại để tương thích
    private Boolean isRead = false;
    private Boolean isSent = false;      // is_sent
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime readAt;
    private String actionUrl;
    private Boolean requiresResponse = false;
    private String responseText;
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
    
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    
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
    
    public Integer getReferenceId() { return referenceId; }
    public void setReferenceId(Integer referenceId) { this.referenceId = referenceId; }
    
    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public Integer getRelatedDocumentId() { return relatedDocumentId; }
    public void setRelatedDocumentId(Integer relatedDocumentId) { this.relatedDocumentId = relatedDocumentId; }
    
    public Integer getRelatedAppointmentId() { return relatedAppointmentId; }
    public void setRelatedAppointmentId(Integer relatedAppointmentId) { this.relatedAppointmentId = relatedAppointmentId; }
    
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean read) { isRead = read; }
    
    public Boolean getIsSent() { return isSent; }
    public void setIsSent(Boolean sent) { isSent = sent; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
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

package com.mycompany.ehr.lqt.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Documents {
    private Integer id;
    private Integer memberId;
    private Integer appointmentId;
    private DocumentType type;
    private String title;
    private String filePath;
    private String description;
    private LocalDate documentDate;
    private Integer uploadedByUserId;
    private LocalDateTime uploadedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public DocumentType getType() { return type; }
    public void setType(DocumentType type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDocumentDate() { return documentDate; }
    public void setDocumentDate(LocalDate documentDate) { this.documentDate = documentDate; }
    public Integer getUploadedByUserId() { return uploadedByUserId; }
    public void setUploadedByUserId(Integer uploadedByUserId) { this.uploadedByUserId = uploadedByUserId; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}

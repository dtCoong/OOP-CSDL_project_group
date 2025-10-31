package com.mycompany.ehr.model;

public enum DocumentType {
    XRay("X-quang"),
    CTScan("Chụp CT"),
    MRI("Chụp MRI"),
    LabResult("Xét nghiệm"),
    Prescription("Đơn thuốc"),
    Report("Báo cáo"),
    Other("Khác");
    
    private final String displayName;
    
    DocumentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

package com.mycompany.ehr.lqt.model;

public enum DocumentType {
    KET_QUA_XET_NGHIEM("Kết quả xét nghiệm"),
    CHAN_DOAN_HINH_ANH("Chẩn đoán hình ảnh"),
    DON_THUOC("Đơn thuốc"),
    BENH_AN("Bệnh án"),
    KHAC("Khác");
    
    private final String displayName;
    
    DocumentType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public static DocumentType fromDisplayName(String displayName) {
        if (displayName == null) return null;
        for (DocumentType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}


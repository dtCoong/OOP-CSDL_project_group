package com.mycompany.ehr.model;

public enum NotificationType {
    TaiLieuMoi("Tài liệu mới", "📄"),
    KetQuaXetNghiem("Kết quả xét nghiệm", "🔬"),
    LichHenSapToi("Lịch hẹn sắp tới", "📅"),
    NhacNhoKhamBenh("Nhắc nhở khám bệnh", "⏰"),
    DonThuoc("Đơn thuốc", "💊"),
    HetHanTaiLieu("Hết hạn tài liệu", "⚠️"),
    CanhBaoKhan("Cảnh báo khẩn", "🚨"),
    ThongTinChung("Thông tin chung", "ℹ️"),
    XacNhanBacSi("Xác nhận bác sĩ", "✓"),
    YeuCauBoSung("Yêu cầu bổ sung", "📝"),
    Khac("Khác", "📌");
    
    private final String displayName;
    private final String icon;
    
    NotificationType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    public static NotificationType fromDb(String s) {
        return s == null ? null : NotificationType.valueOf(s);
    }
}

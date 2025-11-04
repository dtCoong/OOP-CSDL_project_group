package com.mycompany.ehr.lqt.model;

public enum NotificationType {
    // Các loại thông báo chính từ database
    LichTiemChung("Lịch tiêm chủng", "💉"),
    LichUongThuoc("Lịch uống thuốc", "💊"),
    LichKhamBenh("Lịch khám bệnh", "📅"),
    NhacNhoKhac("Nhắc nhở khác", "🔔"),
    // Giữ lại các loại cũ để tương thích
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
    
    // Chuyển đổi từ tên database ENUM thành Java enum
    public static NotificationType fromDisplayName(String displayName) {
        if (displayName == null) return null;
        for (NotificationType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return Khac; // Mặc định
    }
    
    public static NotificationType fromDb(String s) {
        return s == null ? null : NotificationType.valueOf(s);
    }
}

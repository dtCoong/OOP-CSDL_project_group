-- =====================================================
-- SCHEMA CHO NOTIFICATIONS - CHỈ PHẦN THÔNG BÁO
-- =====================================================
USE ehr;

-- =====================================================
-- BẢNG NOTIFICATIONS - Quản lý thông báo
-- =====================================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    recipient_user_id INT NULL COMMENT 'ID người nhận thông báo',
    recipient_name VARCHAR(255) NULL COMMENT 'Tên người nhận',
    title VARCHAR(500) NOT NULL COMMENT 'Tiêu đề thông báo',
    message TEXT NULL COMMENT 'Nội dung thông báo',
    `type` VARCHAR(50) NOT NULL DEFAULT 'ThongTinChung' COMMENT 'Loại thông báo',
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT 'Mức độ ưu tiên: LOW, NORMAL, HIGH, URGENT',
    related_document_id INT NULL COMMENT 'ID tài liệu liên quan',
    related_appointment_id INT NULL COMMENT 'ID cuộc hẹn liên quan',
    is_read BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Đã đọc chưa',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    read_at DATETIME NULL COMMENT 'Thời gian đọc',
    action_url VARCHAR(500) NULL COMMENT 'URL để navigate khi click',
    requires_response BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Có cần phản hồi không',
    response_text TEXT NULL COMMENT 'Phản hồi từ người dùng',
    responded_at DATETIME NULL COMMENT 'Thời gian phản hồi',
    
    INDEX idx_recipient (recipient_user_id),
    INDEX idx_type (`type`),
    INDEX idx_priority (priority),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at),
    INDEX idx_related_document (related_document_id),
    INDEX idx_related_appointment (related_appointment_id),
    
    FOREIGN KEY (related_document_id) REFERENCES documents(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Bảng quản lý thông báo';

-- =====================================================
-- DỮ LIỆU MẪU CHO BẢNG NOTIFICATIONS (50 RECORDS)
-- =====================================================

-- Thông báo tài liệu mới
INSERT INTO notifications (recipient_user_id, recipient_name, title, message, type, priority, related_document_id, is_read) VALUES
(1, 'Bác sĩ Nguyễn Văn A', 'Tài liệu X-quang mới', 'X-quang phổi chính diện đã được upload cho bệnh nhân #1', 'TaiLieuMoi', 'NORMAL', 1, false),
(2, 'Bác sĩ Trần Thị B', 'Kết quả xét nghiệm máu', 'Kết quả xét nghiệm máu tổng quát đã có cho bệnh nhân #1', 'KetQuaXetNghiem', 'HIGH', 21, false),
(3, 'Y tá Lê Văn C', 'X-quang bụng mới', 'X-quang bụng thẳng đã được upload', 'TaiLieuMoi', 'NORMAL', 2, true),
(1, 'Bác sĩ Nguyễn Văn A', 'Cảnh báo: Kết quả bất thường', 'Phát hiện thoái hóa đốt sống L4-L5 ở bệnh nhân #3', 'CanhBaoKhan', 'URGENT', 3, false),
(4, 'Bệnh nhân Phạm Thị D', 'Lịch hẹn khám sắp tới', 'Bạn có lịch khám ngày 2025-11-05 lúc 09:00', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Kết quả sinh hóa máu', 'Kết quả xét nghiệm sinh hóa máu đã sẵn sàng', 'KetQuaXetNghiem', 'NORMAL', 22, true),
(2, 'Bác sĩ Trần Thị B', 'Yêu cầu xác nhận', 'Vui lòng xác nhận đã xem X-quang vai phải', 'XacNhanBacSi', 'HIGH', 4, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Đơn thuốc mới', 'Đơn thuốc điều trị viêm khớp đã được kê', 'DonThuoc', 'HIGH', 45, false),
(1, 'Bác sĩ Nguyễn Văn A', 'X-quang đầu gối', 'X-quang đầu gối trái - thoái hóa khớp độ 2', 'TaiLieuMoi', 'NORMAL', 5, true),
(3, 'Y tá Lê Văn C', 'Nhắc nhở khám định kỳ', 'Bệnh nhân #6 cần khám định kỳ vào tuần tới', 'NhacNhoKhamBenh', 'NORMAL', NULL, false),

-- Thông báo xét nghiệm
(1, 'Bác sĩ Nguyễn Văn A', 'Xét nghiệm nước tiểu', 'Protein niệu (+), cần theo dõi chức năng thận', 'KetQuaXetNghiem', 'HIGH', 23, false),
(2, 'Bác sĩ Trần Thị B', 'Kết quả chức năng gan', 'Các chỉ số gan bình thường', 'KetQuaXetNghiem', 'NORMAL', 24, true),
(6, 'Bệnh nhân Vũ Thị F', 'Lịch hẹn xét nghiệm', 'Lịch xét nghiệm máu vào ngày mai 08:00', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'HIV test', 'Kết quả HIV âm tính', 'KetQuaXetNghiem', 'NORMAL', 25, true),
(3, 'Y tá Lê Văn C', 'Đường huyết', 'Đường huyết cao - 180 mg/dL, cần điều chỉnh thuốc', 'CanhBaoKhan', 'URGENT', 26, false),

-- Thông báo cảnh báo
(1, 'Bác sĩ Nguyễn Văn A', 'KHẨN: Chức năng thận suy giảm', 'Creatinine 2.5 mg/dL - cần can thiệp ngay', 'CanhBaoKhan', 'URGENT', 27, false),
(2, 'Bác sĩ Trần Thị B', 'Huyết áp cao', 'Huyết áp 180/110 mmHg - nguy cơ cao', 'CanhBaoKhan', 'URGENT', NULL, false),
(4, 'Bệnh nhân Phạm Thị D', 'Nhắc uống thuốc', 'Đã đến giờ uống thuốc huyết áp', 'NhacNhoKhamBenh', 'NORMAL', NULL, true),
(7, 'Bệnh nhân Ngô Văn G', 'Hết hạn đơn thuốc', 'Đơn thuốc sắp hết hạn, cần tái khám', 'HetHanTaiLieu', 'HIGH', 46, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Kết quả X-quang', 'X-quang ngực nghiêng cho thấy tim to', 'TaiLieuMoi', 'HIGH', 6, false),

-- Thông báo CT Scan và MRI
(1, 'Bác sĩ Nguyễn Văn A', 'CT Scan não mới', 'CT Scan não đã được upload', 'TaiLieuMoi', 'NORMAL', 51, false),
(2, 'Bác sĩ Trần Thị B', 'MRI cột sống', 'MRI cột sống thắt lưng có sẵn', 'TaiLieuMoi', 'NORMAL', 71, true),
(8, 'Bệnh nhân Đặng Thị H', 'Lịch chụp MRI', 'Lịch chụp MRI não ngày 2025-11-10', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'CT Scan ngực', 'Phát hiện tổn thương phổi', 'CanhBaoKhan', 'URGENT', 52, false),
(3, 'Y tá Lê Văn C', 'MRI đầu gối', 'MRI đầu gối - tổn thương sụn khớp', 'TaiLieuMoi', 'HIGH', 72, false),

-- Thông báo đơn thuốc
(4, 'Bệnh nhân Phạm Thị D', 'Đơn thuốc kháng sinh', 'Đơn thuốc Amoxicillin 500mg', 'DonThuoc', 'HIGH', 91, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Đơn thuốc tim mạch', 'Đơn thuốc điều trị huyết áp', 'DonThuoc', 'HIGH', 92, true),
(6, 'Bệnh nhân Vũ Thị F', 'Đơn thuốc tiểu đường', 'Đơn thuốc Metformin 850mg', 'DonThuoc', 'HIGH', 93, false),
(9, 'Bệnh nhân Bùi Văn I', 'Nhắc uống thuốc', 'Đã đến giờ uống thuốc huyết áp buổi tối', 'NhacNhoKhamBenh', 'NORMAL', NULL, false),
(7, 'Bệnh nhân Ngô Văn G', 'Đơn thuốc kháng viêm', 'Đơn thuốc Ibuprofen 400mg', 'DonThuoc', 'NORMAL', 94, true),

-- Thông báo yêu cầu bổ sung
(1, 'Bác sĩ Nguyễn Văn A', 'Yêu cầu X-quang thêm', 'Cần chụp X-quang thêm góc nghiêng', 'YeuCauBoSung', 'HIGH', 1, false),
(2, 'Bác sĩ Trần Thị B', 'Yêu cầu xét nghiệm bổ sung', 'Cần làm thêm xét nghiệm chức năng thận', 'YeuCauBoSung', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Cần thông tin thêm', 'Vui lòng cung cấp tiền sử bệnh', 'YeuCauBoSung', 'NORMAL', NULL, true),
(3, 'Y tá Lê Văn C', 'Cập nhật hồ sơ', 'Cần cập nhật thông tin liên lạc', 'ThongTinChung', 'LOW', NULL, false),
(10, 'Bệnh nhân Lý Thị K', 'Xác nhận lịch hẹn', 'Vui lòng xác nhận lịch khám ngày 2025-11-15', 'LichHenSapToi', 'NORMAL', NULL, false),

-- Thông báo báo cáo
(1, 'Bác sĩ Nguyễn Văn A', 'Báo cáo tổng kết', 'Báo cáo tổng kết quý 4/2025 đã sẵn sàng', 'ThongTinChung', 'NORMAL', 111, true),
(2, 'Bác sĩ Trần Thị B', 'Báo cáo ca bệnh', 'Báo cáo ca bệnh tim mạch', 'ThongTinChung', 'NORMAL', 112, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Kết quả siêu âm', 'Siêu âm tim - van tim có vấn đề', 'KetQuaXetNghiem', 'HIGH', 113, false),
(3, 'Y tá Lê Văn C', 'Báo cáo theo dõi', 'Báo cáo theo dõi bệnh nhân nội trú', 'ThongTinChung', 'LOW', 114, true),
(2, 'Bác sĩ Trần Thị B', 'Xác nhận đã xem', 'Vui lòng xác nhận đã xem báo cáo', 'XacNhanBacSi', 'NORMAL', 115, false),

-- Thông báo hệ thống
(NULL, 'Tất cả người dùng', 'Bảo trì hệ thống', 'Hệ thống sẽ bảo trì vào 02:00 sáng ngày 2025-11-01', 'ThongTinChung', 'NORMAL', NULL, false),
(NULL, 'Tất cả người dùng', 'Cập nhật phần mềm', 'Phiên bản mới 2.0 đã được cài đặt', 'ThongTinChung', 'LOW', NULL, true),
(NULL, 'Tất cả bác sĩ', 'Họp khoa', 'Họp khoa nội vào 14:00 chiều nay', 'ThongTinChung', 'NORMAL', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Báo cáo tuần', 'Báo cáo tuần cần nộp trước 17:00', 'ThongTinChung', 'NORMAL', NULL, false),
(2, 'Bác sĩ Trần Thị B', 'Đào tạo mới', 'Khóa đào tạo về COVID-19 vào thứ 6', 'ThongTinChung', 'LOW', NULL, false);

-- =====================================================
-- BẢNG NOTIFICATION_PREFERENCES - Cài đặt thông báo người dùng
-- =====================================================
CREATE TABLE IF NOT EXISTS notification_preferences (
    user_id INT PRIMARY KEY COMMENT 'ID người dùng',
    enabled_types TEXT NULL COMMENT 'Các loại thông báo được bật (JSON array)',
    enable_sound BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật âm thanh',
    enable_popup BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật popup',
    enable_badge BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật badge số',
    reminder_days_before INT NOT NULL DEFAULT 1 COMMENT 'Nhắc trước bao nhiêu ngày',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Cài đặt thông báo của người dùng';

-- Dữ liệu mẫu preferences
INSERT INTO notification_preferences (user_id, enabled_types, enable_sound, enable_popup, enable_badge, reminder_days_before) VALUES
(1, '["TaiLieuMoi","KetQuaXetNghiem","CanhBaoKhan","XacNhanBacSi"]', true, true, true, 1),
(2, '["TaiLieuMoi","KetQuaXetNghiem","CanhBaoKhan"]', true, true, true, 2),
(3, '["TaiLieuMoi","NhacNhoKhamBenh","ThongTinChung"]', false, true, true, 1),
(4, '["LichHenSapToi","DonThuoc","NhacNhoKhamBenh"]', true, true, true, 3),
(5, '["LichHenSapToi","DonThuoc","HetHanTaiLieu"]', true, false, true, 1);

SELECT 'Notifications tables created successfully!' AS status;
SELECT COUNT(*) AS total_notifications FROM notifications;
SELECT `type`, COUNT(*) AS count FROM notifications GROUP BY `type`;
SELECT priority, COUNT(*) AS count FROM notifications GROUP BY priority;
SELECT is_read, COUNT(*) AS count FROM notifications GROUP BY is_read;

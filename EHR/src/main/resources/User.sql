-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bảng Users 
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
INSERT INTO Users (username, password_hash, email, phone, full_name, is_active, last_login)
VALUES
(
    'nguyenvanan', 'hashed_password_1', 'an.nguyen@example.com', 
    '0901234567', 'Nguyễn Văn An', TRUE, '2025-10-30 09:15:00'
),
(
    'lethimai', 'hashed_password_2', 'mai.le@example.com', 
    '0912345678', 'Lê Thị Mai', TRUE, '2025-10-31 14:20:00'
),
(
    'tranvanhung', 'hashed_password_3', 'hung.tran@example.com', 
    '0923456789', 'Trần Văn Hùng', TRUE, '2025-10-25 11:30:00'
),
(
    'phamthibich', 'hashed_password_4', 'bich.pham@example.com', 
    '0934567890', 'Phạm Thị Bích', FALSE, '2025-05-10 11:00:00'
),
(
    'hoangvandung', 'hashed_password_5', 'dung.hoang@example.com', 
    '0945678901', 'Hoàng Văn Dũng', TRUE, '2025-10-29 17:45:00'
),
(
    'dangthuha', 'hashed_password_6', 'ha.dang@example.com', 
    '0956789012', 'Đặng Thu Hà', TRUE, '2025-11-01 08:00:00'
),
(
    'vuminhtuan', 'hashed_password_7', 'tuan.vu@example.com', 
    '0967890123', 'Vũ Minh Tuấn', TRUE, '2025-09-01 18:10:00'
),
(
    'buithilan', 'hashed_password_8', 'lan.bui@example.com', 
    '0978901234', 'Bùi Thị Lan', FALSE, '2025-03-15 10:05:00'
),
(
    'ngogiahuy', 'hashed_password_9', 'huy.ngo@example.com', 
    '0989012345', 'Ngô Gia Huy', TRUE, '2025-06-15 20:30:00'
),
(
    'phanthanh_tam', 'hashed_password_10', 'tam.phan@example.com', 
    '0990123456', 'Phan Thanh Tâm', TRUE, '2025-10-28 16:10:00'
);
-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;

-- Bảng Medication_Schedule - lịch uống thuốc
CREATE TABLE Medication_Schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    detail_id INT NOT NULL,
    scheduled_time TIME NOT NULL,
    daily_dosage VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detail_id) REFERENCES Prescription_Details(detail_id) ON DELETE CASCADE
);
INSERT INTO Medication_Schedule (detail_id, scheduled_time, daily_dosage, is_active)
VALUES
(
    -- Đơn 1 (detail_id=1) - 1 lần/ngày. Đã hoàn thành.
    1, '20:00:00', '1 viên (20mg)', FALSE
),
(
    -- Đơn 2 (detail_id=3) - 2 lần/ngày. Đã hoàn thành.
    3, '07:00:00', '1 viên (500mg)', FALSE
),
(
    -- Đơn 2 (detail_id=3) - 2 lần/ngày. Đã hoàn thành.
    3, '19:00:00', '1 viên (500mg)', FALSE
),
(
    -- Đơn 4 (detail_id=7) - 1 lần/ngày. Đang dùng.
    7, '08:00:00', '1 viên', TRUE
),
(
    -- Đơn 6 (detail_id=8) - 1 lần/ngày. Đang dùng.
    8, '20:30:00', '1 viên (500mg)', TRUE
),
(
    -- Đơn 9 (detail_id=9) - 3 lần/ngày. Bắt đầu hôm nay (1/11).
    9, '07:30:00', '5 ml', TRUE
),
(
    -- Đơn 9 (detail_id=9) - 3 lần/ngày. Bắt đầu hôm nay (1/11).
    9, '12:00:00', '5 ml', TRUE
),
(
    -- Đơn 9 (detail_id=9) - 3 lần/ngày. Bắt đầu hôm nay (1/11).
    9, '18:30:00', '5 ml', TRUE
),
(
    -- Đơn 5 (detail_id=10) - 1 lần/ngày. Chưa bắt đầu (start 20/11).
    10, '20:00:00', '1 viên (10mg)', FALSE
),
(
    -- Đơn 1 (detail_id=2) - 1 lần/ngày. Đã hoàn thành.
    2, '07:30:00', '1 viên (81mg)', FALSE
);
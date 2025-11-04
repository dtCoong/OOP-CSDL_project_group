-- ===================================
-- NOTIFICATIONS SYSTEM SETUP
-- Hệ thống thông báo cho lịch tiêm, uống thuốc, khám bệnh
-- ===================================

USE personal_health_management;

-- Bảng Notifications - Thông báo
CREATE TABLE IF NOT EXISTS Notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    member_id INT,
    notification_type ENUM('Lịch tiêm chủng', 'Lịch uống thuốc', 'Lịch khám bệnh', 'Nhắc nhở khác') NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    reference_id INT,  -- ID tham chiếu đến vaccination_record_id, schedule_id, hoặc appointment_id
    reference_type ENUM('vaccination', 'medication', 'appointment', 'other'),
    scheduled_time DATETIME NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    is_sent BOOLEAN DEFAULT FALSE,
    priority ENUM('Thấp', 'Trung bình', 'Cao', 'Khẩn cấp') DEFAULT 'Trung bình',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    INDEX idx_user_time (user_id, scheduled_time),
    INDEX idx_type (notification_type),
    INDEX idx_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Notification_Preferences - Cài đặt thông báo
CREATE TABLE IF NOT EXISTS Notification_Preferences (
    preference_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    notification_type ENUM('Lịch tiêm chủng', 'Lịch uống thuốc', 'Lịch khám bệnh', 'Nhắc nhở khác') NOT NULL,
    is_enabled BOOLEAN DEFAULT TRUE,
    advance_notice_hours INT DEFAULT 24,  -- Thông báo trước bao nhiêu giờ
    reminder_frequency ENUM('Một lần', 'Hàng ngày', 'Hàng tuần') DEFAULT 'Một lần',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_type (user_id, notification_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ===================================
-- INSERT SAMPLE DATA
-- ===================================

-- 1. Cài đặt thông báo mặc định cho user
INSERT INTO Notification_Preferences (user_id, notification_type, is_enabled, advance_notice_hours, reminder_frequency)
VALUES 
    (1, 'Lịch tiêm chủng', TRUE, 48, 'Một lần'),
    (1, 'Lịch uống thuốc', TRUE, 1, 'Hàng ngày'),
    (1, 'Lịch khám bệnh', TRUE, 24, 'Một lần'),
    (1, 'Nhắc nhở khác', TRUE, 12, 'Một lần');

-- 2. Dữ liệu mẫu Vaccination_Records
INSERT INTO Vaccination_Records (
    member_id, vaccine_name, vaccination_date, next_due_date,
    batch_number, notes
)
VALUES
    -- Member 1 (Nguyễn Văn An - Bản thân)
    (1, 'Vaccine COVID-19 (Pfizer) - Mũi 3', '2024-12-10', NULL, 'PF-2024-12-001', 'Đã hoàn thành 3 mũi vaccine COVID-19'),
    (1, 'Vaccine Cúm mùa 2025', '2025-09-15', '2026-09-15', 'FLU-2025-09-456', 'Cần tiêm lại mỗi năm'),
    -- Member 2 (Nguyễn Văn B - Con mới sinh)
    (2, 'Vaccine BCG (Lao)', '2025-01-02', NULL, 'BCG-2025-01-123', 'Tiêm khi sơ sinh'),
    (2, 'Vaccine Viêm gan B - Mũi 1', '2025-01-02', '2025-02-02', 'HBV-2025-01-234', 'Cần tiêm mũi 2 sau 1 tháng'),
    (2, 'Vaccine Viêm gan B - Mũi 2', '2025-02-05', '2025-08-05', 'HBV-2025-02-345', 'Đã tiêm mũi 2, cần tiêm mũi 3 sau 6 tháng'),
    (2, 'Vaccine Bại liệt (Polio) - Mũi 1', '2025-03-10', '2025-05-10', 'POLIO-2025-03-567', 'Cần tiêm mũi 2 sau 2 tháng'),
    (2, 'Vaccine 5 trong 1 (Quinvaxem) - Mũi 1', '2025-03-10', '2025-05-10', 'QUIN-2025-03-678', 'Phòng bạch hầu, ho gà, uốn ván, viêm gan B, Hib'),
    (2, 'Vaccine Phế cầu (PCV) - Mũi 1', '2025-04-15', '2025-06-15', 'PCV-2025-04-789', 'Vaccine tự nguyện, phòng viêm phổi'),
    -- Member 3 (Nguyễn Văn C - Vợ/Chồng)
    (3, 'Vaccine HPV (Phòng ung thư cổ tử cung) - Mũi 1', '2025-03-18', '2025-09-18', 'HPV-2025-03-111', 'Cần tiêm 3 mũi trong 6 tháng'),
    (3, 'Vaccine Viêm gan B - Mũi nhắc lại', '2024-11-20', NULL, 'HBV-2024-11-222', 'Tiêm nhắc lại sau 10 năm'),
    -- Member 4 (Nguyễn Văn D - Cha mẹ)
    (4, 'Vaccine Cúm mùa 2025', '2025-09-20', '2026-09-20', 'FLU-2025-09-333', 'Người cao tuổi nên tiêm hàng năm'),
    (4, 'Vaccine Phế cầu (PCV13)', '2025-08-15', NULL, 'PCV13-2025-08-444', 'Phòng viêm phổi cho người cao tuổi');

-- 3. Dữ liệu mẫu Appointments (Lịch khám)
INSERT INTO Appointments (member_id, doctor_id, hospital_id, department_id, appointment_date, appointment_slot, type, status, chief_complaint, room_number, queue_number, cost, payment_status)
VALUES
    -- Lịch khám sắp tới cho member 1
    (1, NULL, 1, 101, '2025-11-05 09:00:00', '09:00 - 09:30', 'Tái khám', 'Đã đặt', 'Khám định kỳ tim mạch', 'P301', 5, 500000, 'Chưa thanh toán'),
    (1, NULL, 1, 102, '2025-11-10 14:30:00', '14:30 - 15:00', 'Khám tổng quát', 'Đã đặt', 'Kiểm tra sức khỏe định kỳ', 'P205', 12, 800000, 'Chưa thanh toán'),
    -- Lịch khám cho member 2 (Con)
    (2, NULL, 1, 103, '2025-11-06 10:00:00', '10:00 - 10:30', 'Khám tổng quát', 'Đã đặt', 'Khám sức khỏe định kỳ 10 tháng', 'P101', 8, 300000, 'Chưa thanh toán'),
    (2, NULL, 1, 103, '2025-11-15 08:30:00', '08:30 - 09:00', 'Tái khám', 'Đã đặt', 'Tiêm vaccine 5 trong 1 - Mũi 2', 'P102', 3, 250000, 'Chưa thanh toán'),
    (2, NULL, 1, 103, '2025-12-10 09:00:00', '09:00 - 09:30', 'Khám tổng quát', 'Đã đặt', 'Khám sức khỏe 11 tháng', 'P101', 5, 300000, 'Chưa thanh toán'),
    -- Lịch khám cho member 3
    (3, NULL, 1, 106, '2025-11-08 15:00:00', '15:00 - 15:30', 'Khám tổng quát', 'Đã đặt', 'Khám phụ khoa định kỳ', 'P401', 15, 600000, 'Chưa thanh toán'),
    (3, NULL, 1, 106, '2025-11-20 10:30:00', '10:30 - 11:00', 'Tái khám', 'Đã đặt', 'Tiêm vaccine HPV - Mũi 2', 'P402', 7, 1500000, 'Chưa thanh toán'),
    -- Lịch khám cho member 4 (Cha mẹ)
    (4, NULL, 2, 120, '2025-11-04 08:00:00', '08:00 - 08:30', 'Tái khám', 'Đã đặt', 'Tái khám đái tháo đường', 'P501', 2, 400000, 'Bảo hiểm'),
    (4, NULL, 2, 111, '2025-11-12 14:00:00', '14:00 - 14:30', 'Khám tổng quát', 'Đã đặt', 'Khám tim mạch định kỳ', 'P302', 10, 500000, 'Bảo hiểm'),
    (4, NULL, 2, 120, '2025-11-25 09:30:00', '09:30 - 10:00', 'Tái khám', 'Đã đặt', 'Kiểm tra đường huyết và HbA1c', 'P502', 6, 350000, 'Bảo hiểm');

-- 4. Dữ liệu mẫu Medications (Danh mục thuốc)
INSERT INTO Medications (name, generic_name, description, unit, requires_prescription, manufacturer)
VALUES
    ('Metformin 500mg', 'Metformin Hydrochloride', 'Thuốc điều trị đái tháo đường type 2', 'Viên', TRUE, 'Sanofi'),
    ('Glimepiride 2mg', 'Glimepiride', 'Thuốc hạ đường huyết', 'Viên', TRUE, 'Berlin-Chemie'),
    ('Amlodipine 5mg', 'Amlodipine Besylate', 'Thuốc hạ huyết áp', 'Viên', TRUE, 'Pfizer'),
    ('Losartan 50mg', 'Losartan Potassium', 'Thuốc điều trị tăng huyết áp', 'Viên', TRUE, 'Merck'),
    ('Vitamin D3 1000IU', 'Cholecalciferol', 'Bổ sung vitamin D', 'Viên', FALSE, 'Nature Made'),
    ('Calcium 500mg', 'Calcium Carbonate', 'Bổ sung canxi', 'Viên', FALSE, 'Vitacare'),
    ('Paracetamol 500mg', 'Paracetamol', 'Thuốc hạ sốt, giảm đau', 'Viên', FALSE, 'Pymepharco'),
    ('Amoxicillin 500mg', 'Amoxicillin', 'Kháng sinh nhóm Penicillin', 'Viên', TRUE, 'DHG Pharma');

-- 5. Dữ liệu mẫu Prescriptions (Đơn thuốc)
INSERT INTO Prescriptions (member_id, appointment_id, doctor_id, prescription_date, diagnosis, notes, total_cost, status)
VALUES
    -- Đơn thuốc cho member 1
    (1, NULL, NULL, '2025-10-15', 'Khỏe mạnh - Bổ sung vitamin', 'Uống sau bữa ăn', 200000, 'Đang sử dụng'),
    -- Đơn thuốc cho member 3
    (3, NULL, NULL, '2025-09-20', 'Bổ sung canxi và vitamin D', 'Uống vào buổi sáng', 350000, 'Đang sử dụng'),
    -- Đơn thuốc cho member 4 (Cha mẹ - Đái tháo đường)
    (4, NULL, NULL, '2025-09-18', 'Đái tháo đường type 2', 'Theo dõi đường huyết định kỳ', 450000, 'Đang sử dụng'),
    (4, NULL, NULL, '2025-09-25', 'Tăng huyết áp', 'Uống đều đặn mỗi ngày', 380000, 'Đang sử dụng');

-- 6. Dữ liệu mẫu Prescription_Details (Chi tiết đơn thuốc)
INSERT INTO Prescription_Details (prescription_id, medication_id, dosage, frequency, duration_days, total_quantity, instructions, start_date, end_date, status)
VALUES
    -- Đơn thuốc 1 (member 1) - Vitamin D3
    (1, 5, '1 viên', '1 lần/ngày', 90, 90, 'Uống vào buổi sáng sau ăn', '2025-10-15', '2026-01-13', 'Đang dùng'),
    -- Đơn thuốc 2 (member 3) - Canxi + Vitamin D
    (2, 6, '1 viên', '1 lần/ngày', 90, 90, 'Uống vào buổi sáng', '2025-09-20', '2025-12-19', 'Đang dùng'),
    (2, 5, '1 viên', '1 lần/ngày', 90, 90, 'Uống cùng với canxi', '2025-09-20', '2025-12-19', 'Đang dùng'),
    -- Đơn thuốc 3 (member 4) - Điều trị đái tháo đường
    (3, 1, '1 viên', '2 lần/ngày', 90, 180, 'Uống sau bữa sáng và tối', '2025-09-18', '2025-12-17', 'Đang dùng'),
    (3, 2, '1 viên', '1 lần/ngày', 90, 90, 'Uống trước bữa sáng', '2025-09-18', '2025-12-17', 'Đang dùng'),
    -- Đơn thuốc 4 (member 4) - Điều trị tăng huyết áp
    (4, 3, '1 viên', '1 lần/ngày', 90, 90, 'Uống vào buổi sáng', '2025-09-25', '2025-12-24', 'Đang dùng'),
    (4, 4, '1 viên', '1 lần/ngày', 90, 90, 'Uống vào buổi tối', '2025-09-25', '2025-12-24', 'Đang dùng');

-- 7. Dữ liệu mẫu Medication_Schedule (Lịch uống thuốc)
INSERT INTO Medication_Schedule (detail_id, scheduled_time, daily_dosage, is_active)
VALUES
    -- Lịch cho Vitamin D3 (member 1)
    (1, '08:00:00', '1 viên', TRUE),
    -- Lịch cho Canxi + Vitamin D (member 3)
    (2, '07:30:00', '1 viên', TRUE),
    (3, '07:30:00', '1 viên', TRUE),
    -- Lịch cho thuốc đái tháo đường (member 4)
    (4, '07:00:00', '1 viên', TRUE),  -- Metformin sáng
    (4, '19:00:00', '1 viên', TRUE),  -- Metformin tối
    (5, '06:30:00', '1 viên', TRUE),  -- Glimepiride trước bữa sáng
    -- Lịch cho thuốc huyết áp (member 4)
    (6, '07:00:00', '1 viên', TRUE),  -- Amlodipine sáng
    (7, '20:00:00', '1 viên', TRUE);  -- Losartan tối

-- ===================================
-- 8. TẠO THÔNG BÁO TỰ ĐỘNG
-- ===================================

-- Thông báo lịch TIÊM CHỦNG (dựa vào next_dose_date trong Vaccination_Records)INSERT INTO Notifications (
INSERT INTO Notifications (
    user_id, member_id, notification_type, title, message,
    reference_id, reference_type, scheduled_time, priority
)
SELECT 
    1 AS user_id,
    vr.member_id,
    'Lịch tiêm chủng' AS notification_type,
    CONCAT('Nhắc nhở tiêm vaccine: ', vr.vaccine_name) AS title,
    CONCAT(
        'Đến hạn tiêm mũi tiếp theo cho ', fm.name,
        '. Vaccine: ', vr.vaccine_name,
        '. Ngày hẹn: ', DATE_FORMAT(vr.next_due_date, '%d/%m/%Y'),
        '. Địa điểm: Chưa xác định'
    ) AS message,
    vr.vaccination_id AS reference_id,
    'vaccination' AS reference_type,
    DATE_SUB(CONCAT(vr.next_due_date, ' 09:00:00'), INTERVAL 48 HOUR) AS scheduled_time,
    'Cao' AS priority
FROM Vaccination_Records vr
JOIN Family_Members fm ON vr.member_id = fm.member_id
WHERE vr.next_due_date IS NOT NULL
  AND vr.next_due_date > CURDATE()
  AND vr.next_due_date <= DATE_ADD(CURDATE(), INTERVAL 90 DAY);


-- Thông báo lịch KHÁM BỆNH (dựa vào Appointments)
INSERT INTO Notifications (
    user_id, member_id, notification_type, title, message,
    reference_id, reference_type, scheduled_time, priority
)
SELECT 
    1 AS user_id,
    a.member_id,
    'Lịch khám bệnh' AS notification_type,
    CONCAT('Lịch khám: ', DATE_FORMAT(a.appointment_date, '%d/%m/%Y %H:%i')) AS title,
    COALESCE(
        CONCAT(
            'Bạn có lịch khám cho ', fm.name, '. ',
            'Thời gian: ', DATE_FORMAT(a.appointment_date, '%d/%m/%Y lúc %H:%i'), '. ',
            'Loại: ', COALESCE(a.type, 'Không rõ'), '. ',
            'Lý do: ', COALESCE(a.chief_complaint, 'Chưa ghi rõ'), '. ',
            'Phòng: ', COALESCE(a.room_number, 'Chưa xác định'), '. ',
            'Chi phí dự kiến: ', COALESCE(FORMAT(a.cost, 0), '0'), ' VNĐ.'
        ),
        'Thông báo lịch khám'
    ) AS message,
    a.appointment_id AS reference_id,
    'appointment' AS reference_type,
    DATE_SUB(a.appointment_date, INTERVAL 24 HOUR) AS scheduled_time,
    CASE 
        WHEN a.type = 'Khẩn cấp' THEN 'Khẩn cấp'
        WHEN a.type = 'Tái khám' THEN 'Cao'
        ELSE 'Trung bình'
    END AS priority
FROM Appointments a
JOIN Family_Members fm ON a.member_id = fm.member_id
WHERE a.status = 'Đã đặt'
  AND a.appointment_date > NOW()
  AND a.appointment_date <= DATE_ADD(NOW(), INTERVAL 30 DAY);


-- Thông báo lịch UỐNG THUỐC (dựa vào Medication_Schedule)
INSERT INTO Notifications (user_id, member_id, notification_type, title, message, reference_id, reference_type, scheduled_time, priority)
SELECT DISTINCT
    1 as user_id,
    p.member_id,
    'Lịch uống thuốc' as notification_type,
    CONCAT('Nhắc uống thuốc: ', DATE_FORMAT(NOW(), '%d/%m/%Y')) as title,
    CONCAT('Nhắc nhở uống thuốc cho ', fm.name, '. ',
           'Hôm nay bạn cần uống thuốc theo lịch đã đặt. ',
           'Đơn thuốc: ', COALESCE(p.diagnosis, 'Theo chỉ định bác sĩ')) as message,
    ms.schedule_id as reference_id,
    'medication' as reference_type,
    CONCAT(DATE_FORMAT(NOW(), '%Y-%m-%d'), ' ', ms.scheduled_time) as scheduled_time,
    'Trung bình' as priority
FROM Medication_Schedule ms
JOIN Prescription_Details pd ON ms.detail_id = pd.detail_id
JOIN Prescriptions p ON pd.prescription_id = p.prescription_id
JOIN Family_Members fm ON p.member_id = fm.member_id
WHERE ms.is_active = TRUE
  AND pd.status = 'Đang dùng'
  AND p.status = 'Đang sử dụng'
  AND pd.end_date >= CURDATE();

-- Thông báo nhắc uống thuốc cho ngày mai
INSERT INTO Notifications (user_id, member_id, notification_type, title, message, reference_id, reference_type, scheduled_time, priority)
SELECT DISTINCT
    1 as user_id,
    p.member_id,
    'Lịch uống thuốc' as notification_type,
    CONCAT('Nhắc uống thuốc: ', DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%d/%m/%Y')) as title,
    CONCAT('Nhắc nhở uống thuốc cho ', fm.name, '. ',
           'Ngày mai bạn cần uống thuốc theo lịch đã đặt. ',
           'Đơn thuốc: ', COALESCE(p.diagnosis, 'Theo chỉ định bác sĩ')) as message,
    ms.schedule_id as reference_id,
    'medication' as reference_type,
    CONCAT(DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 1 DAY), '%Y-%m-%d'), ' ', ms.scheduled_time) as scheduled_time,
    'Trung bình' as priority
FROM Medication_Schedule ms
JOIN Prescription_Details pd ON ms.detail_id = pd.detail_id
JOIN Prescriptions p ON pd.prescription_id = p.prescription_id
JOIN Family_Members fm ON p.member_id = fm.member_id
WHERE ms.is_active = TRUE
  AND pd.status = 'Đang dùng'
  AND p.status = 'Đang sử dụng'
  AND pd.end_date >= DATE_ADD(CURDATE(), INTERVAL 1 DAY);

-- ===================================
-- STATISTICS & VERIFICATION QUERIES
-- ===================================

-- Thống kê thông báo theo loại
SELECT 
    notification_type as 'Loại thông báo',
    COUNT(*) as 'Tổng số',
    SUM(CASE WHEN is_read = TRUE THEN 1 ELSE 0 END) as 'Đã đọc',
    SUM(CASE WHEN is_read = FALSE THEN 1 ELSE 0 END) as 'Chưa đọc'
FROM Notifications
GROUP BY notification_type;

-- Thống kê thông báo theo mức độ ưu tiên
SELECT 
    priority as 'Mức độ',
    COUNT(*) as 'Số lượng'
FROM Notifications
GROUP BY priority
ORDER BY FIELD(priority, 'Khẩn cấp', 'Cao', 'Trung bình', 'Thấp');

-- Thông báo sắp tới trong 7 ngày
SELECT 
    n.notification_type as 'Loại',
    fm.name as 'Thành viên',
    n.title as 'Tiêu đề',
    DATE_FORMAT(n.scheduled_time, '%d/%m/%Y %H:%i') as 'Thời gian',
    n.priority as 'Ưu tiên'
FROM Notifications n
JOIN Family_Members fm ON n.member_id = fm.member_id
WHERE n.scheduled_time BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)
  AND n.is_read = FALSE
ORDER BY n.scheduled_time ASC;

-- Kiểm tra lịch uống thuốc hôm nay
SELECT 
    fm.name as 'Thành viên',
    m.name as 'Tên thuốc',
    pd.dosage as 'Liều lượng',
    TIME_FORMAT(ms.scheduled_time, '%H:%i') as 'Giờ uống',
    ms.daily_dosage as 'Số viên/lần'
FROM Medication_Schedule ms
JOIN Prescription_Details pd ON ms.detail_id = pd.detail_id
JOIN Medications m ON pd.medication_id = m.medication_id
JOIN Prescriptions p ON pd.prescription_id = p.prescription_id
JOIN Family_Members fm ON p.member_id = fm.member_id
WHERE ms.is_active = TRUE
  AND pd.status = 'Đang dùng'
  AND pd.end_date >= CURDATE()
ORDER BY ms.scheduled_time;

-- ===================================
-- COMPLETED SUCCESSFULLY
-- ===================================
SELECT 'Notifications system setup completed!' as Status,
       (SELECT COUNT(*) FROM Notifications) as 'Total Notifications',
       (SELECT COUNT(*) FROM Vaccination_Records) as 'Vaccination Records',
       (SELECT COUNT(*) FROM Appointments WHERE status = "Đã đặt") as 'Upcoming Appointments',
       (SELECT COUNT(*) FROM Medication_Schedule WHERE is_active = TRUE) as 'Active Medication Schedules';

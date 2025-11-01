-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bảng Appointments 
CREATE TABLE Appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    doctor_id INT,
    hospital_id INT,
    department_id INT,
    appointment_date DATETIME NOT NULL,
    type ENUM('Khám tổng quát', 'Tái khám', 'Khẩn cấp') DEFAULT 'Khám tổng quát',
    status ENUM('Đã đặt', 'Hoàn thành', 'Hủy bỏ', 'Không đến') DEFAULT 'Đã đặt',
    chief_complaint TEXT,
    diagnosis TEXT,
    treatment_notes TEXT,
    follow_up_date DATE,
    cost DECIMAL(10,2),
    payment_status ENUM('Chưa thanh toán', 'Đã thanh toán', 'Bảo hiểm') DEFAULT 'Chưa thanh toán',
    room_number VARCHAR(20),
    queue_number INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id),
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id),
    FOREIGN KEY (department_id) REFERENCES Departments(department_id)
);
INSERT INTO Appointments (
    member_id, doctor_id, hospital_id, department_id, 
    appointment_date, type, status, chief_complaint, 
    diagnosis, treatment_notes, follow_up_date, 
    cost, payment_status, room_number, queue_number
)
VALUES
(
    -- 1. Hoàn thành (Tim mạch)
    1, 1, 1, 1, '2025-10-20 08:00:00', 'Khám tổng quát', 'Hoàn thành', 
    'Đau ngực, khó thở khi leo cầu thang.', 
    'Hẹp động mạch vành 70%.', 
    'Kê đơn thuốc, thay đổi lối sống, theo dõi huyết áp.', 
    '2025-11-20', 500000.00, 'Đã thanh toán', 'P301-A5', 3
),
(
    -- 2. Hoàn thành (Tiêu hóa - Nội soi)
    2, 6, 1, 4, '2025-10-22 09:30:00', 'Khám tổng quát', 'Hoàn thành', 
    'Đau thượng vị, ợ chua kéo dài.', 
    'Viêm loét dạ dày (H. pylori dương tính).', 
    'Nội soi dạ dày, kê phác đồ kháng sinh 14 ngày.', 
    '2025-11-05', 1200000.00, 'Bảo hiểm', 'P402-A5 (Nội soi)', 5
),
(
    -- 3. Hoàn thành (Nhi)
    3, 9, 1, 7, '2025-10-28 15:00:00', 'Khẩn cấp', 'Hoàn thành', 
    'Bé sốt cao 39.5 độ, co giật nhẹ.', 
    'Sốt virus, co giật do sốt cao.', 
    'Hạ sốt, bù điện giải, theo dõi tại nhà.', 
    '2025-10-30', 350000.00, 'Đã thanh toán', 'P205-C1', 1
),
(
    -- 4. Hoàn thành (Sản)
    5, 8, 1, 6, '2025-10-25 10:00:00', 'Tái khám', 'Hoàn thành', 
    'Khám thai định kỳ tuần 12.', 
    'Thai 12 tuần phát triển bình thường, độ mờ da gáy tốt.', 
    'Bổ sung sắt, canxi. Hẹn siêu âm 4D tuần 16.', 
    '2025-11-22', 400000.00, 'Đã thanh toán', 'P303-B2', 8
),
(
    -- 5. Đã đặt (Tái khám Tim mạch)
    1, 1, 1, 1, '2025-11-20 08:30:00', 'Tái khám', 'Đã đặt', 
    'Tái khám theo lịch hẹn của bác sĩ Mạnh.', 
    'Chưa chẩn đoán', 'Chưa có ghi chú điều trị', 
    '2025-11-20', 150000.00, 'Chưa thanh toán', 'P301-A5', 5
),
(
    -- 6. Đã đặt (Nội tổng hợp)
    6, 3, 1, 2, '2025-11-05 10:00:00', 'Khám tổng quát', 'Đã đặt', 
    'Kiểm tra sức khỏe tổng quát, xét nghiệm đường huyết.', 
    'Chưa chẩn đoán', 'Chưa có ghi chú điều trị', 
    '2025-11-05', 300000.00, 'Chưa thanh toán', 'P210-A1', 12
),
(
    -- 7. Đã đặt (Nhi - Tiêm chủng)
    7, 9, 1, 7, '2025-11-03 09:00:00', 'Khám tổng quát', 'Đã đặt', 
    'Tiêm vắc-xin cúm mùa.', 
    'Tiêm chủng định kỳ', 'Thực hiện tiêm chủng', 
    '2025-11-03', 250000.00, 'Chưa thanh toán', 'P208-C1 (Tiêm chủng)', 20
),
(
    -- 8. Hủy bỏ (Chấn thương)
    8, 4, 1, 3, '2025-10-30 14:00:00', 'Tái khám', 'Hủy bỏ', 
    'Tái khám sau 2 tuần bó bột tay trái.', 
    'Đã hủy', 'Bệnh nhân báo bận, dời lịch', 
    '2025-10-30', 0.00, 'Chưa thanh toán', 'P105-A8', 15
),
(
    -- 9. Không đến (Nội tổng hợp)
    4, 3, 1, 2, '2025-10-29 11:00:00', 'Khám tổng quát', 'Không đến', 
    'Đau mỏi vai gáy.', 
    'Không đến khám', 'Bệnh nhân không có mặt', 
    '2025-10-29', 0.00, 'Chưa thanh toán', 'P210-A1', 10
),
(
    -- 10. Đã đặt (Cấp cứu)
    10, 10, 1, 10, '2025-11-02 07:00:00', 'Khẩn cấp', 'Đã đặt', 
    'Đau bụng dữ dội vùng hố chậu phải, nghi ruột thừa.', 
    'Theo dõi viêm ruột thừa cấp', 'Chờ chỉ định siêu âm và xét nghiệm', 
    '2025-11-02', 100000.00, 'Chưa thanh toán', 'P101-C3 (Cấp cứu)', 1
);
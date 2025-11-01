-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bảng Prescriptions - đơn thuốc
CREATE TABLE Prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    appointment_id INT,
    doctor_id INT,
    prescription_date DATE NOT NULL,
    diagnosis TEXT,
    notes TEXT,
    total_cost DECIMAL(10,2),
    status ENUM('Đang sử dụng', 'Đã hoàn thành', 'Đã dừng', 'Hết hạn') DEFAULT 'Đang sử dụng',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id)
);
INSERT INTO Prescriptions (
    member_id, appointment_id, doctor_id, prescription_date, 
    diagnosis, notes, total_cost, status
)
VALUES
(
    -- 1. Đơn thuốc từ Lịch hẹn 1 (Tim mạch)
    1, 1, 1, '2025-10-20', 
    'Hẹp động mạch vành 70%.', 
    'Uống thuốc sau ăn no. Theo dõi huyết áp hàng ngày.', 
    750000.00, 'Đã hoàn thành'
),
(
    -- 2. Đơn thuốc từ Lịch hẹn 2 (Tiêu hóa)
    2, 2, 6, '2025-10-22', 
    'Viêm loét dạ dày (H. pylori dương tính).', 
    'Uống kháng sinh đủ 14 ngày. Tái khám sau khi hết thuốc.', 
    1120000.00, 'Đã hoàn thành'
),
(
    -- 3. Đơn thuốc từ Lịch hẹn 3 (Nhi)
    3, 3, 9, '2025-10-28', 
    'Sốt virus.', 
    'Uống hạ sốt (Paracetamol) khi sốt trên 38.5 độ. Bù Oresol.', 
    85000.00, 'Đã hoàn thành'
),
(
    -- 4. Đơn thuốc từ Lịch hẹn 4 (Sản)
    5, 4, 8, '2025-10-25', 
    'Thai 12 tuần - Khám định kỳ.', 
    'Bổ sung 1 viên Sắt và 1 viên Canxi mỗi ngày sau bữa sáng.', 
    450000.00, 'Đang sử dụng'
),
(
    -- 5. Đơn thuốc tái khám (Tim mạch, không cần appointment_id)
    1, NULL, 1, '2025-11-20', 
    'Tái khám Hẹp động mạch vành.', 
    'Tiếp tục dùng đơn cũ. Giảm liều Atorvastatin xuống 10mg.', 
    680000.00, 'Đang sử dụng'
),
(
    -- 6. Đơn thuốc mới (Nội tổng hợp - Tiểu đường)
    6, NULL, 3, '2025-11-01', 
    'Đái tháo đường Tuýp 2 (phát hiện mới).', 
    'Bắt đầu dùng Metformin 500mg, 1 viên/ngày sau bữa tối.', 
    150000.00, 'Đang sử dụng'
),
(
    -- 7. Đơn thuốc cũ (Đã dừng)
    1, NULL, 5, '2025-06-15', 
    'Đau cơ xương khớp.', 
    'Bệnh nhân báo cáo bị đau dạ dày sau khi dùng thuốc.', 
    300000.00, 'Đã dừng'
),
(
    -- 8. Đơn thuốc cũ (Hết hạn - vd: kháng sinh dùng 7 ngày)
    8, NULL, 4, '2025-09-10', 
    'Viêm xoang cấp.', 
    'Đơn thuốc Amoxicillin dùng trong 7 ngày.', 
    220000.00, 'Hết hạn'
),
(
    -- 9. Đơn thuốc (Nhi - Ho)
    7, NULL, 9, '2025-11-01', 
    'Viêm họng cấp.', 
    'Siro ho (Prospan) 5ml x 3 lần/ngày.', 
    110000.00, 'Đang sử dụng'
),
(
    -- 10. Đơn thuốc (Chấn thương - Tái khám)
    8, NULL, 4, '2025-11-02', 
    'Tái khám gãy xương tay trái (sau khi tháo bột).', 
    'Bổ sung Canxi, Vitamin D3. Bắt đầu vật lý trị liệu.', 
    350000.00, 'Đang sử dụng'
);
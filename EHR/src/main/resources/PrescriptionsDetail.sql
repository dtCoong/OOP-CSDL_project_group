
-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bảng Prescription_Details - chi tiết đơn thuốc
CREATE TABLE Prescription_Details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,
    medication_id INT NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration_days INT NOT NULL,
    total_quantity INT NOT NULL,
    instructions TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Chưa bắt đầu', 'Đang dùng', 'Đã hoàn thành', 'Đã dừng') DEFAULT 'Chưa bắt đầu',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES Prescriptions(prescription_id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) REFERENCES Medications(medication_id)
);
INSERT INTO Prescription_Details (
    prescription_id, medication_id, dosage, frequency, 
    duration_days, total_quantity, instructions, 
    start_date, end_date, status
)
VALUES
(
    -- Đơn 1 (Tim mạch) - Thuốc 1
    1, 3, '1 viên (20mg)', '1 lần/ngày (sau bữa tối)', 
    30, 30, 'Uống đều đặn vào 8h tối.', 
    '2025-10-20', '2025-11-18', 'Đã hoàn thành'
),
(
    -- Đơn 1 (Tim mạch) - Thuốc 2 (vd: Aspirin 81mg - dùng ID 1 Paracetamol làm placeholder)
    1, 1, '1 viên (81mg)', '1 lần/ngày (sau bữa sáng)', 
    30, 30, 'Uống sau khi ăn no để tránh kích ứng dạ dày.', 
    '2025-10-20', '2025-11-18', 'Đã hoàn thành'
),
(
    -- Đơn 2 (Tiêu hóa - H. pylori) - Thuốc 1
    2, 2, '2 viên (1000mg)', '2 lần/ngày (sáng, tối)', 
    14, 28, 'Uống trước ăn 30 phút. Phải uống đủ 14 ngày.', 
    '2025-10-22', '2025-11-04', 'Đã hoàn thành'
),
(
    -- Đơn 2 (Tiêu hóa - H. pylori) - Thuốc 2
    2, 8, '1 viên (20mg)', '2 lần/ngày (sáng, tối)', 
    14, 28, 'Uống trước ăn 30 phút.', 
    '2025-10-22', '2025-11-04', 'Đã hoàn thành'
),
(
    -- Đơn 3 (Nhi - Sốt) - Thuốc 1
    3, 1, '1 gói (150mg)', '3-4 lần/ngày (khi sốt > 38.5 độ)', 
    3, 12, 'Pha với nước. Chỉ uống khi sốt, cách nhau 4-6 tiếng.', 
    '2025-10-28', '2025-10-30', 'Đã hoàn thành'
),
(
    -- Đơn 3 (Nhi - Sốt) - Thuốc 2
    3, 4, '1 gói', '2-3 lần/ngày', 
    3, 9, 'Pha 1 gói với 200ml nước đun sôi để nguội, uống thay nước.', 
    '2025-10-28', '2025-10-30', 'Đã hoàn thành'
),
(
    -- Đơn 4 (Sản) - (Dùng ID 10 Berberin làm placeholder cho Sắt)
    4, 10, '1 viên', '1 lần/ngày (sau bữa sáng)', 
    30, 30, 'Bổ sung Sắt.', 
    '2025-10-25', '2025-11-23', 'Đang dùng'
),
(
    -- Đơn 6 (Tiểu đường)
    6, 6, '1 viên (500mg)', '1 lần/ngày (sau bữa tối)', 
    60, 60, 'Theo dõi đường huyết lúc đói vào sáng hôm sau.', 
    '2025-11-01', '2025-12-30', 'Đang dùng'
),
(
    -- Đơn 9 (Nhi - Ho) (Dùng ID 5 Salbutamol "Chai" làm placeholder cho Siro ho)
    9, 5, '5 ml', '3 lần/ngày (sáng, trưa, tối)', 
    5, 1, 'Uống sau khi ăn. Lắc kỹ chai trước khi dùng.', 
    '2025-11-01', '2025-11-05', 'Chưa bắt đầu'
),
(
    -- Đơn 5 (Tái khám Tim mạch) - Giảm liều
    5, 3, '1 viên (10mg)', '1 lần/ngày (sau bữa tối)', 
    30, 30, 'Giảm liều so với đơn cũ. Uống đều đặn vào 8h tối.', 
    '2025-11-20', '2025-12-19', 'Chưa bắt đầu'
);
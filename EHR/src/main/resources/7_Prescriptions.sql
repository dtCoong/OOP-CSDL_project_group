USE personal_health_management;
INSERT INTO Prescriptions 
    (member_id, appointment_id, doctor_id, prescription_date, diagnosis, notes, total_cost, status)
VALUES
(
    1, 1, (SELECT doctor_id FROM Doctors WHERE department_id = 101 LIMIT 1 OFFSET 0), 
    '2025-11-10', 'Tăng huyết áp', 'Đơn thuốc 30 ngày. Tái khám sau 1 tháng.', 150000.00, 'Đang sử dụng'
),
(
    1, 1, (SELECT doctor_id FROM Doctors WHERE department_id = 101 LIMIT 1 OFFSET 0), 
    '2025-11-10', 'Tăng huyết áp', 'Thuốc lợi tiểu (Hydrochlorothiazide) 25mg.', 60000.00, 'Đang sử dụng'
),
(
    1, 2, (SELECT doctor_id FROM Doctors WHERE department_id = 140 LIMIT 1 OFFSET 0), 
    '2025-10-15', 'Tiểu đường Type 2 (Kiểm soát tốt)', 'Tiếp tục Metformin 1000mg/ngày.', 120000.00, 'Đã hoàn thành'
),
(
    1, 2, (SELECT doctor_id FROM Doctors WHERE department_id = 140 LIMIT 1 OFFSET 0), 
    '2025-10-15', 'Tiểu đường Type 2 (Kiểm soát tốt)', 'Bổ sung Insulin pen. Tự theo dõi đường huyết.', 450000.00, 'Đang sử dụng'
),
(
    1, 2, (SELECT doctor_id FROM Doctors WHERE department_id = 140 LIMIT 1 OFFSET 0), 
    '2025-10-15', 'Rối loạn mỡ máu (Kèm tiểu đường)', 'Atorvastatin 20mg (Giảm mỡ máu).', 220000.00, 'Đang sử dụng'
),
(
    1, 3, (SELECT doctor_id FROM Doctors WHERE department_id = 122 LIMIT 1 OFFSET 0), 
    '2025-09-05', 'Viêm dạ dày mạn tính (HP âm tính)', 'Đơn Omeprazol 4 tuần.', 250000.00, 'Đã hoàn thành'
),
(
    1, 3, (SELECT doctor_id FROM Doctors WHERE department_id = 122 LIMIT 1 OFFSET 0), 
    '2025-09-05', 'Viêm dạ dày mạn tính', 'Thuốc trung hòa acid (dạng gói). Uống khi đau.', 90000.00, 'Đã hoàn thành'
),
(
    1, 5, (SELECT doctor_id FROM Doctors WHERE department_id = 117 LIMIT 1 OFFSET 0), 
    '2025-11-20', 'Viêm họng cấp', 'Kháng sinh, giảm ho, long đờm 5 ngày.', 180000.00, 'Đang sử dụng'
),
(
    1, 5, (SELECT doctor_id FROM Doctors WHERE department_id = 117 LIMIT 1 OFFSET 0), 
    '2025-11-20', 'Viêm họng cấp (kèm sốt)', 'Paracetamol 500mg. Uống khi sốt > 38.5 độ.', 25000.00, 'Đang sử dụng'
),


(
    2, 6, (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), 
    '2025-10-28', 'Sốt virus', 'Hạ sốt Hapacol 150mg khi cần.', 45000.00, 'Đã hoàn thành'
),
(
    2, 6, (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), 
    '2025-10-28', 'Sốt virus', 'Bù dịch Oresol. Uống rải rác.', 20000.00, 'Đã hoàn thành'
),
(
    2, 6, (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), 
    '2025-10-28', 'Sốt virus (nghi ngờ bội nhiễm)', 'Kháng sinh Amoxicillin 250mg 5 ngày.', 90000.00, 'Đã hoàn thành'
),
(
    2, 7, (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), 
    '2025-10-30', 'Hồi phục sau sốt virus', 'Kê thêm siro ho thảo dược.', 95000.00, 'Đã hoàn thành'
),
(
    2, 8, (SELECT doctor_id FROM Doctors WHERE department_id = 113 LIMIT 1 OFFSET 1), 
    '2025-08-01', 'Theo dõi sau tiêm chủng', 'Dự phòng hạ sốt (Paracetamol 80mg) nếu bé sốt > 38.5 độ.', 30000.00, 'Đã hoàn thành'
),
(
    2, 9, (SELECT doctor_id FROM Doctors WHERE department_id = 133 LIMIT 1 OFFSET 0), 
    '2025-11-15', 'Chậm tăng cân', 'Bổ sung men vi sinh và kẽm. Thay đổi chế độ ăn.', 210000.00, 'Đang sử dụng'
),
(
    2, 9, (SELECT doctor_id FROM Doctors WHERE department_id = 133 LIMIT 1 OFFSET 0), 
    '2025-11-15', 'Chậm tăng cân', 'Siro ăn ngon (Lysine).', 120000.00, 'Đang sử dụng'
),
-- Đơn cho Member 3 (Nguyễn Văn C)
(
    3, 11, (SELECT doctor_id FROM Doctors WHERE department_id = 106 LIMIT 1 OFFSET 0), 
    '2025-10-01', 'Viêm lộ tuyến cổ tử cung (Nhẹ)', 'Thuốc đặt 10 ngày.', 150000.00, 'Đã hoàn thành'
),
(
    3, 11, (SELECT doctor_id FROM Doctors WHERE department_id = 106 LIMIT 1 OFFSET 0), 
    '2025-10-01', 'Viêm lộ tuyến (Nhẹ)', 'Dung dịch vệ sinh phụ nữ.', 110000.00, 'Đã hoàn thành'
),
(
    3, 13, (SELECT doctor_id FROM Doctors WHERE department_id = 112 LIMIT 1 OFFSET 1), 
    '2025-06-15', 'Rối loạn tiêu hóa', 'Thuốc giảm co thắt, men tiêu hóa.', 110000.00, 'Đã hoàn thành'
),
(
    3, 13, (SELECT doctor_id FROM Doctors WHERE department_id = 112 LIMIT 1 OFFSET 1), 
    '2025-06-15', 'Rối loạn tiêu hóa', 'Berberin 5 ngày.', 40000.00, 'Đã hoàn thành'
),
(
    3, 14, (SELECT doctor_id FROM Doctors WHERE department_id = 138 LIMIT 1 OFFSET 0), 
    '2025-11-25', 'Cận thị (tiến triển)', 'Thuốc nhỏ mắt chống mỏi. Đo kính mới.', 90000.00, 'Đang sử dụng'
),
(
    3, 14, (SELECT doctor_id FROM Doctors WHERE department_id = 138 LIMIT 1 OFFSET 0), 
    '2025-11-25', 'Mỏi mắt điều tiết', 'Vitamin A-D bổ sung.', 75000.00, 'Đang sử dụng'
),
(
    3, 15, (SELECT doctor_id FROM Doctors WHERE department_id = 110 LIMIT 1 OFFSET 0), 
    '2025-05-10', 'Bình thường - Tầm soát', 'Kê vitamin tổng hợp theo yêu cầu.', 130000.00, 'Đã hoàn thành'
),
(
    1, 3, (SELECT doctor_id FROM Doctors WHERE department_id = 122 LIMIT 1 OFFSET 0), 
    '2025-10-05', 'Viêm dạ dày (Tái khám)', 'Tiếp tục Omeprazol 2 tuần. Thêm men tiêu hóa.', 180000.00, 'Đã hoàn thành'
),
(
    1, 1, (SELECT doctor_id FROM Doctors WHERE department_id = 101 LIMIT 1 OFFSET 0), 
    '2025-11-10', 'Tăng huyết áp (Dự phòng)', 'Aspirin 81mg (Chống kết tập tiểu cầu).', 50000.00, 'Đang sử dụng'
);
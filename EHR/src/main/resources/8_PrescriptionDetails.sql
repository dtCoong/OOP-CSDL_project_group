USE personal_health_management;
INSERT INTO Prescription_Details
    (prescription_id, medication_id, dosage, frequency, duration_days, total_quantity, instructions, start_date, end_date, status)
VALUES
(1, 3, '1 viên 20mg', '1 lần/ngày (tối)', 30, 30, 'Uống sau ăn tối. Kiểm soát mỡ máu.', '2025-11-10', '2025-12-09', 'Đang dùng'),
(2, 1, '1 viên 500mg', 'Khi đau', 30, 10, 'Thuốc lợi tiểu. Dùng khi bị phù.', '2025-11-10', '2025-12-09', 'Đang dùng'),
(3, 6, '1 viên 1000mg', '1 lần/ngày (sáng)', 30, 30, 'Uống sau bữa ăn sáng.', '2025-10-15', '2025-11-13', 'Đã hoàn thành'),
(4, 9, '10 IU', '1 lần/ngày (trước ăn sáng)', 30, 1, 'Tiêm dưới da bụng 15 phút trước ăn.', '2025-10-15', '2025-11-13', 'Đang dùng'),
(5, 3, '1 viên 20mg', '1 lần/ngày (tối)', 30, 30, 'Uống sau ăn tối.', '2025-10-15', '2025-11-13', 'Đang dùng'),
(6, 8, '1 viên 20mg', '1 lần/ngày (sáng)', 28, 28, 'Uống trước ăn sáng 30 phút.', '2025-09-05', '2025-10-02', 'Đã hoàn thành'),
(7, 10, '2 viên', '2 lần/ngày', 5, 20, 'Uống sau ăn. Thuốc hỗ trợ dạ dày.', '2025-09-05', '2025-09-09', 'Đã hoàn thành'),
(8, 2, '1 viên 500mg', '3 lần/ngày', 5, 15, 'Uống sau ăn no, cách 8 tiếng.', '2025-11-20', '2025-11-24', 'Đang dùng'),
(9, 1, '1 viên 500mg', 'Khi sốt > 38.5 độ', 3, 10, 'Không quá 4 viên/ngày. Cách 4-6 tiếng.', '2025-11-20', '2025-11-22', 'Đang dùng'),

(10, 1, '1 gói 150mg', 'Khi sốt > 38.5 độ', 3, 10, 'Cách 4-6 tiếng nếu còn sốt. Pha với nước.', '2025-10-28', '2025-10-30', 'Đã hoàn thành'),
(11, 4, '1 gói pha 200ml', 'Uống rải rác', 3, 6, 'Uống thay nước lọc. Pha đúng tỷ lệ.', '2025-10-28', '2025-10-30', 'Đã hoàn thành'),
(12, 2, '1 gói 250mg', '2 lần/ngày (sáng, tối)', 5, 10, 'Uống sau ăn no. Đủ 5 ngày.', '2025-10-28', '2025-11-01', 'Đã hoàn thành'),
(13, 10, '1 thìa 5ml', '3 lần/ngày', 5, 1, 'Siro ho thảo dược. Uống sau ăn.', '2025-10-30', '2025-11-03', 'Đã hoàn thành'),
(14, 1, '1 viên 80mg', 'Khi sốt > 38.5 độ', 2, 4, 'Dùng dự phòng khi sốt sau tiêm. Tối đa 4 lần/ngày.', '2025-08-01', '2025-08-02', 'Đã hoàn thành'),
(15, 10, '1 gói (Men vi sinh)', '2 lần/ngày', 14, 28, 'Pha với nước nguội, uống sau ăn.', '2025-11-15', '2025-11-28', 'Đang dùng'),
(16, 4, '10ml (Siro)', '2 lần/ngày', 15, 1, 'Siro ăn ngon. Uống trước bữa ăn 30 phút.', '2025-11-15', '2025-11-29', 'Đang dùng'),

(17, 7, '1 viên đặt', '1 lần/ngày (tối)', 10, 10, 'Đặt âm đạo trước khi đi ngủ.', '2025-10-01', '2025-10-10', 'Đã hoàn thành'),
(18, 7, '1 tuýp (Kem)', '1 lần/ngày', 10, 1, 'Dung dịch vệ sinh (Dùng ngoài).', '2025-10-01', '2025-10-10', 'Đã hoàn thành'),
(19, 10, '2 viên', '3 lần/ngày', 3, 18, 'Uống khi đau bụng.', '2025-06-15', '2025-06-17', 'Đã hoàn thành'),
(20, 10, '2 viên', '3 lần/ngày', 5, 30, 'Berberin. Uống sau ăn.', '2025-06-15', '2025-06-19', 'Đã hoàn thành'),
(21, 4, '1 giọt', '4 lần/ngày (2 mắt)', 15, 1, 'Nước mắt nhân tạo (Oresol - giả định). Nhỏ mắt chống khô.', '2025-11-25', '2025-12-09', 'Đang dùng'),
(22, 4, '1 viên', '1 lần/ngày (sáng)', 30, 30, 'Bổ sung Vitamin A-D.', '2025-11-25', '2025-12-24', 'Đang dùng'),
(23, 4, '1 viên', '1 lần/ngày (sáng)', 30, 30, 'Vitamin tổng hợp (Oresol - giả định).', '2025-05-10', '2025-06-08', 'Đã hoàn thành'),

(24, 8, '1 viên 20mg', '1 lần/ngày (sáng)', 14, 14, 'Tiếp tục uống trước ăn 30 phút.', '2025-10-05', '2025-10-18', 'Đã hoàn thành'),
(25, 3, '1 viên 10mg', '1 lần/ngày', 30, 30, 'Dự phòng tim mạch (Atorvastatin - Giả định).', '2025-11-10', '2025-12-09', 'Đang dùng');
-- 1. Dữ liệu Users (10 người dùng)
INSERT INTO Users (username, password_hash, email, phone, full_name, is_active, last_login) VALUES
('nguyenvannam', '$2y$10$example1hash', 'nam.nguyen@email.com', '0901234567', 'Nguyễn Văn Nam', TRUE, '2024-01-15 10:30:00'),
('tranthimai', '$2y$10$example2hash', 'mai.tran@email.com', '0912345678', 'Trần Thị Mai', TRUE, '2024-01-14 14:20:00'),
('lehoanganh', '$2y$10$example3hash', 'anh.le@email.com', '0923456789', 'Lê Hoàng Anh', TRUE, '2024-01-13 09:15:00'),
('phamthiha', '$2y$10$example4hash', 'ha.pham@email.com', '0934567890', 'Phạm Thị Hà', TRUE, '2024-01-12 16:45:00'),
('vovanminh', '$2y$10$example5hash', 'minh.vo@email.com', '0945678901', 'Võ Văn Minh', TRUE, '2024-01-11 11:30:00'),
('ngothihuong', '$2y$10$example6hash', 'huong.ngo@email.com', '0956789012', 'Ngô Thị Hương', TRUE, '2024-01-10 13:20:00'),
('buiquangduc', '$2y$10$example7hash', 'duc.bui@email.com', '0967890123', 'Bùi Quang Đức', TRUE, '2024-01-09 08:45:00'),
('hoangthilanb', '$2y$10$example8hash', 'lan.hoang@email.com', '0978901234', 'Hoàng Thị Lan', TRUE, '2024-01-08 15:30:00'),
('dovankhoa', '$2y$10$example9hash', 'khoa.do@email.com', '0989012345', 'Đỗ Văn Khoa', TRUE, '2024-01-07 12:15:00'),
('lythinga', '$2y$10$example10hash', 'nga.ly@email.com', '0990123456', 'Lý Thị Nga', TRUE, '2024-01-06 17:00:00');

-- 2. Dữ liệu Hospitals (5 bệnh viện)
INSERT INTO Hospitals (name, address, hotline, website) VALUES
('Bệnh viện Bạch Mai', '78 Giải Phóng, Đống Đa, Hà Nội', '19001234', 'www.bachmai.gov.vn'),
('Bệnh viện Việt Đức', '40 Tràng Thi, Hoàn Kiếm, Hà Nội', '19001235', 'www.vietduc.org.vn'),
('Bệnh viện Chợ Rẫy', '201B Nguyễn Chí Thanh, Quận 5, TP.HCM', '19001236', 'www.choray.vn'),
('Bệnh viện FV', '6 Nguyễn Lương Bằng, Quận 7, TP.HCM', '19001237', 'www.fvhospital.com'),
('Bệnh viện Vinmec Times City', '458 Minh Khai, Hai Bà Trưng, Hà Nội', '19001238', 'www.vinmec.com');

-- 3. Dữ liệu Departments (10 khoa)
INSERT INTO Departments (hospital_id, name, description) VALUES
(1, 'Khoa Nội Tim mạch', 'Chuyên khám và điều trị các bệnh về tim mạch'),
(1, 'Khoa Nhi', 'Chuyên khám và điều trị cho trẻ em từ 0-16 tuổi'),
(2, 'Khoa Ngoại tổng hợp', 'Chuyên phẫu thuật các bệnh ngoại khoa'),
(2, 'Khoa Sản phụ khoa', 'Chuyên khám và điều trị cho phụ nữ'),
(3, 'Khoa Cấp cứu', 'Khoa cấp cứu 24/7'),
(3, 'Khoa Thần kinh', 'Chuyên khám và điều trị các bệnh thần kinh'),
(4, 'Khoa Nội tổng quát', 'Khám sức khỏe tổng quát và nội khoa'),
(4, 'Khoa Tai mũi họng', 'Chuyên khám và điều trị TMH'),
(5, 'Khoa Da liễu', 'Chuyên khám và điều trị các bệnh về da'),
(5, 'Khoa Mắt', 'Chuyên khám và điều trị các bệnh về mắt');

-- 4. Dữ liệu Family_Members (20 thành viên gia đình)
INSERT INTO Family_Members (user_id, name, dob, gender, relationship, blood_type, insurance_number, phone, notes) VALUES
(1, 'Nguyễn Văn Nam', '1985-03-15', 'Nam', 'Bản thân', 'O+', 'BH001234567', '0901234567', 'Không có ghi chú đặc biệt'),
(1, 'Lê Thị Hoa', '1987-07-20', 'Nữ', 'Vợ/Chồng', 'A+', 'BH001234568', '0901234568', 'Đang mang thai tháng thứ 6'),
(1, 'Nguyễn Minh Tuấn', '2010-12-05', 'Nam', 'Con', 'O+', 'BH001234569', NULL, 'Con trai đầu lòng'),
(2, 'Trần Thị Mai', '1990-05-10', 'Nữ', 'Bản thân', 'B+', 'BH002345678', '0912345678', 'Có tiền sử dị ứng thuốc kháng sinh'),
(2, 'Trần Văn Phúc', '2015-09-18', 'Nam', 'Con', 'AB+', 'BH002345679', NULL, 'Đang theo học mẫu giáo'),
(3, 'Lê Hoàng Anh', '1988-11-25', 'Nam', 'Bản thân', 'A-', 'BH003456789', '0923456789', 'Có bệnh tiểu đường type 2'),
(3, 'Nguyễn Thị Lan', '1992-02-14', 'Nữ', 'Vợ/Chồng', 'O-', 'BH003456790', '0923456790', 'Nhóm máu hiếm'),
(4, 'Phạm Thị Hà', '1995-08-30', 'Nữ', 'Bản thân', 'AB-', 'BH004567890', '0934567890', 'Mới sinh con 6 tháng'),
(4, 'Phạm Minh Khoa', '2023-02-28', 'Nam', 'Con', 'A+', 'BH004567891', NULL, 'Em bé 10 tháng tuổi'),
(5, 'Võ Văn Minh', '1982-06-12', 'Nam', 'Bản thân', 'B-', 'BH005678901', '0945678901', 'Có tiền sử bệnh tim'),
(5, 'Trần Thị Thu', '1984-10-08', 'Nữ', 'Vợ/Chồng', 'O+', 'BH005678902', '0945678902', 'Đang điều trị cao huyết áp'),
(6, 'Ngô Thị Hương', '1993-01-22', 'Nữ', 'Bản thân', 'A+', 'BH006789012', '0956789012', 'Công việc nhiều stress'),
(6, 'Ngô Văn Hùng', '2018-04-15', 'Nam', 'Con', 'B+', 'BH006789013', NULL, 'Con trai 5 tuổi, khỏe mạnh'),
(7, 'Bùi Quang Đức', '1979-12-01', 'Nam', 'Bản thân', 'O+', 'BH007890123', '0967890123', 'Làm việc ca đêm thường xuyên'),
(7, 'Lý Thị Mai', '1981-03-17', 'Nữ', 'Vợ/Chồng', 'A-', 'BH007890124', '0967890124', 'Có bệnh viêm khớp dạng thấp'),
(8, 'Hoàng Thị Lan', '1991-09-05', 'Nữ', 'Bản thân', 'AB+', 'BH008901234', '0978901234', 'Thường xuyên đau đầu mãn tính'),
(8, 'Hoàng Văn Tú', '2020-07-10', 'Nam', 'Con', 'B+', 'BH008901235', NULL, 'Bé trai 3 tuổi'),
(9, 'Đỗ Văn Khoa', '1986-04-28', 'Nam', 'Bản thân', 'B+', 'BH009012345', '0989012345', 'Có tiền sử phẫu thuật ruột thừa'),
(9, 'Phạm Thị Linh', '1989-11-13', 'Nữ', 'Vợ/Chồng', 'O+', 'BH009012346', '0989012346', 'Đang điều trị vô sinh'),
(10, 'Lý Thị Nga', '1994-08-19', 'Nữ', 'Bản thân', 'A+', 'BH010123456', '0990123456', 'Mới chuyển công tác từ TP.HCM ra Hà Nội');

-- 5. Dữ liệu Doctors (10 bác sĩ)
INSERT INTO Doctors (user_id, department_id, doctor_code, specialization, degree, experience_years) VALUES
(1, 1, 'BS001', 'Tim mạch can thiệp', 'Tiến sỹ', 15),
(2, 2, 'BS002', 'Nhi khoa tổng quát', 'Thạc sỹ', 8),
(3, 3, 'BS003', 'Phẫu thuật gan mật', 'Giáo sư', 20),
(4, 4, 'BS004', 'Sản khoa', 'Bác sỹ', 12),
(5, 5, 'BS005', 'Cấp cứu nội khoa', 'Thạc sỹ', 10),
(6, 6, 'BS006', 'Thần kinh học', 'Tiến sỹ', 18),
(7, 7, 'BS007', 'Nội tiết', 'Bác sỹ', 6),
(8, 8, 'BS008', 'Tai mũi họng', 'Thạc sỹ', 14),
(9, 9, 'BS009', 'Da liễu thẩm mỹ', 'Bác sỹ', 9),
(10, 10, 'BS010', 'Nhãn khoa', 'Tiến sỹ', 16);

-- 6. Dữ liệu Medical_History (15 bệnh án)
INSERT INTO Medical_History (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) VALUES
(3, 'Tiểu đường type 2', '2020-05-15', TRUE, 'Trung bình', 'Kiểm soát được', 'Đang dùng Metformin 500mg'),
(6, 'Cao huyết áp', '2019-03-20', TRUE, 'Nhẹ', 'Kiểm soát được', 'Đang theo dõi huyết áp tại nhà'),
(10, 'Bệnh tim mạch vành', '2021-08-10', TRUE, 'Nặng', 'Đang điều trị', 'Đã đặt stent 2 vị trí'),
(11, 'Cao huyết áp', '2022-01-15', TRUE, 'Trung bình', 'Đang điều trị', 'Tăng huyết áp khi mang thai'),
(15, 'Viêm khớp dạng thấp', '2020-11-30', TRUE, 'Trung bình', 'Đang điều trị', 'Đau nhức khớp vào mùa lạnh'),
(16, 'Đau đầu mãn tính', '2021-06-25', TRUE, 'Nhẹ', 'Đang điều trị', 'Căng thẳng công việc gây ra'),
(18, 'Viêm ruột thừa cấp', '2018-04-12', FALSE, 'Nặng', 'Đã khỏi', 'Đã phẫu thuật cắt ruột thừa'),
(2, 'Dị ứng kháng sinh Penicillin', '2015-07-08', TRUE, 'Trung bình', 'Kiểm soát được', 'Tránh nhóm thuốc Beta-lactam'),
(4, 'Thiếu máu sau sinh', '2023-03-15', FALSE, 'Nhẹ', 'Đã khỏi', 'Đã bổ sung sắt và folate'),
(19, 'Vô sinh thứ phát', '2023-01-20', FALSE, 'Trung bình', 'Đang điều trị', 'Đang thụ tinh trong ống nghiệm'),
(1, 'Loét dạ dày', '2022-09-10', FALSE, 'Nhẹ', 'Đã khỏi', 'Đã điều trị H.Pylori thành công'),
(12, 'Lo âu trầm cảm', '2023-05-18', TRUE, 'Nhẹ', 'Đang điều trị', 'Stress do công việc và nuôi con nhỏ'),
(14, 'Thoát vị đĩa đệm L4-L5', '2021-12-05', TRUE, 'Trung bình', 'Kiểm soát được', 'Tập vật lý trị liệu đều đặn'),
(7, 'Thiếu máu thiếu sắt', '2023-02-28', FALSE, 'Nhẹ', 'Đang điều trị', 'Do thiếu dinh dưỡng trong thai kỳ'),
(20, 'Hội chứng ruột kích thích', '2022-11-22', TRUE, 'Nhẹ', 'Kiểm soát được', 'Kiêng thức ăn cay nóng');

-- 7. Dữ liệu Allergies (12 dị ứng)
INSERT INTO Allergies (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes) VALUES
(2, 'Penicillin', 'Thuốc', 'Nặng', 'Nổi mề đay, khó thở, sưng môi', '2015-07-08', 'Phản ứng sau tiêm kháng sinh'),
(4, 'Tôm cua', 'Thức ăn', 'Trung bình', 'Nổi mẩn đỏ, ngứa', '2020-12-20', 'Xuất hiện sau khi ăn hải sản'),
(6, 'Phấn hoa', 'Môi trường', 'Nhẹ', 'Hắt hơi, chảy nước mũi', '2019-04-15', 'Thường xuất hiện vào mùa xuân'),
(8, 'Sữa bò', 'Thức ăn', 'Nhẹ', 'Đau bụng, tiêu chảy', '2023-03-10', 'Không dung nạp lactose'),
(10, 'Aspirin', 'Thuốc', 'Trung bình', 'Nổi mề đay, đau bụng', '2021-09-15', 'Phát hiện khi dùng thuốc giảm đau'),
(12, 'Lông mèo', 'Môi trường', 'Nhẹ', 'Hắt hơi, ngứa mắt', '2020-06-30', 'Không thể nuôi thú cưng có lông'),
(15, 'Iodine', 'Thuốc', 'Nặng', 'Sưng họng, khó thở', '2019-11-12', 'Phản ứng khi chụp CT có thuốc cản quang'),
(1, 'Đậu phộng', 'Thức ăn', 'Nguy hiểm', 'Sốc phản vệ, khó thở nặng', '2010-05-20', 'Luôn mang theo EpiPen'),
(7, 'Bụi nhà', 'Môi trường', 'Trung bình', 'Ho, nghẹt mũi, khó thở', '2021-01-08', 'Cần vệ sinh nhà cửa thường xuyên'),
(14, 'Sulfonamide', 'Thuốc', 'Trung bình', 'Phát ban, sốt', '2020-08-25', 'Tránh nhóm thuốc kháng sinh này'),
(18, 'Trứng gà', 'Thức ăn', 'Nhẹ', 'Nổi mẩn đỏ, ngứa', '2022-04-18', 'Có thể ăn được trứng đã nấu chín kỹ'),
(3, 'Latex', 'Môi trường', 'Trung bình', 'Da đỏ, ngứa tại chỗ tiếp xúc', '2018-10-30', 'Cần dùng găng tay không latex khi khám bệnh');

-- 8. Dữ liệu Medications (15 loại thuốc)
INSERT INTO Medications (name, generic_name, description, side_effects, manufacturer, unit, requires_prescription, barcode) VALUES
('Glucophage 500mg', 'Metformin HCl', 'Thuốc điều trị tiểu đường type 2', 'Buồn nôn, tiêu chảy, đau bụng', 'Merck', 'Viên', TRUE, '8901030891234'),
('Norvasc 5mg', 'Amlodipine', 'Thuốc hạ huyết áp nhóm chẹn kênh Canxi', 'Phù chân, đau đầu, mệt mỏi', 'Pfizer', 'Viên', TRUE, '8901030891235'),
('Plavix 75mg', 'Clopidogrel', 'Thuốc chống đông máu', 'Chảy máu, bầm tím', 'Sanofi', 'Viên', TRUE, '8901030891236'),
('Paracetamol 500mg', 'Paracetamol', 'Thuốc giảm đau, hạ sốt', 'Hiếm gặp khi dùng đúng liều', 'Teva', 'Viên', FALSE, '8901030891237'),
('Omeprazole 20mg', 'Omeprazole', 'Thuốc ức chế bơm proton điều trị loét dạ dày', 'Đau đầu, buồn nôn, tiêu chảy', 'AstraZeneca', 'Viên', TRUE, '8901030891238'),
('Ventolin HFA', 'Salbutamol', 'Thuốc xịt điều trị hen suyễn', 'Run tay, tim đập nhanh', 'GSK', 'Chai', TRUE, '8901030891239'),
('Lipitor 20mg', 'Atorvastatin', 'Thuốc hạ mỡ máu', 'Đau cơ, đau đầu, rối loạn tiêu hóa', 'Pfizer', 'Viên', TRUE, '8901030891240'),
('Aspirin 100mg', 'Acid acetylsalicylic', 'Thuốc chống đông máu liều thấp', 'Đau dạ dày, chảy máu', 'Bayer', 'Viên', FALSE, '8901030891241'),
('Prednisolone 5mg', 'Prednisolone', 'Thuốc corticosteroid chống viêm', 'Tăng cân, loãng xương, tăng đường huyết', 'Pfizer', 'Viên', TRUE, '8901030891242'),
('Amoxicillin 500mg', 'Amoxicillin', 'Thuốc kháng sinh beta-lactam', 'Tiêu chảy, nôn, phát ban', 'Sandoz', 'Viên', TRUE, '8901030891243'),
('Ferrous sulfate 200mg', 'Iron sulfate', 'Thuốc bổ sung sắt điều trị thiếu máu', 'Táo bón, đau bụng, nôn', 'Abbott', 'Viên', FALSE, '8901030891244'),
('Loratadine 10mg', 'Loratadine', 'Thuốc chống dị ứng H1', 'Buồn ngủ, khô miệng, đau đầu', 'Schering-Plough', 'Viên', FALSE, '8901030891245'),
('Insulin Lantus', 'Insulin glargine', 'Insulin tác dụng dài', 'Hạ đường huyết, tăng cân', 'Sanofi', 'Ống tiêm', TRUE, '8901030891246'),
('Folic acid 5mg', 'Folic acid', 'Vitamin B9 bổ sung folate', 'Hiếm gặp tác dụng phụ', 'Generic', 'Viên', FALSE, '8901030891247'),
('Diazepam 5mg', 'Diazepam', 'Thuốc an thần, chống lo âu', 'Buồn ngủ, chóng mặt, phụ thuộc', 'Roche', 'Viên', TRUE, '8901030891248');

-- 9. Dữ liệu Appointments (15 cuộc hẹn)
INSERT INTO Appointments (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, cost, payment_status, room_number) VALUES
(3, 1, 1, 1, '2024-01-20 09:00:00', 'Tái khám', 'Hoàn thành', 'Khám kiểm tra đường huyết', 'Tiểu đường type 2 ổn định', 'Tiếp tục dùng Metformin, hẹn khám sau 3 tháng', 200000, 'Bảo hiểm', 'P101'),
(5, 2, 1, 2, '2024-01-22 14:30:00', 'Khám tổng quát', 'Hoàn thành', 'Khám sức khỏe định kỳ cho bé', 'Trẻ phát triển bình thường', 'Tiêm vaccine DPT, hẹn khám sau 6 tháng', 150000, 'Đã thanh toán', 'P205'),
(10, 3, 2, 3, '2024-01-25 08:00:00', 'Tái khám', 'Hoàn thành', 'Khám lại sau phẫu thuật', 'Phẫu thuật stent thành công', 'Vết mổ lành tốt, tiếp tục thuốc chống đông', 300000, 'Bảo hiểm', 'P302'),
(8, 4, 2, 4, '2024-02-01 10:15:00', 'Khám tổng quát', 'Hoàn thành', 'Khám thai định kỳ', 'Thai 28 tuần phát triển bình thường', 'Bổ sung sắt và canxi, hẹn khám sau 2 tuần', 180000, 'Bảo hiểm', 'P108'),
(2, 5, 3, 5, '2024-02-05 16:45:00', 'Khẩn cấp', 'Hoàn thành', 'Phản ứng dị ứng sau dùng thuốc', 'Dị ứng thuốc Amoxicillin', 'Đã xử lý phản ứng dị ứng, kê thuốc thay thế', 250000, 'Đã thanh toán', 'CC01'),
(15, 6, 3, 6, '2024-02-08 11:20:00', 'Tái khám', 'Hoàn thành', 'Đau khớp tăng nặng', 'Viêm khớp dạng thấp bùng phát', 'Tăng liều Prednisolone, vật lý trị liệu', 220000, 'Bảo hiểm', 'P405'),
(16, 7, 4, 7, '2024-02-10 09:30:00', 'Khám tổng quát', 'Hoàn thành', 'Đau đầu thường xuyên', 'Đau đầu căng thẳng', 'Kê thuốc giảm đau, tư vấn giảm stress', 160000, 'Đã thanh toán', 'P201'),
(1, 8, 4, 8, '2024-02-12 15:00:00', 'Khám tổng quát', 'Hoàn thành', 'Nghẹt mũi, chảy nước mũi', 'Viêm mũi dị ứng', 'Kê thuốc xịt mũi và chống histamine', 140000, 'Đã thanh toán', 'P304'),
(12, 9, 5, 9, '2024-02-15 13:45:00', 'Khám tổng quát', 'Hoàn thành', 'Nổi mẩn đỏ trên da', 'Viêm da tiếp xúc', 'Kê kem bôi và thuốc chống dị ứng', 120000, 'Đã thanh toán', 'P203'),
(20, 10, 5, 10, '2024-02-18 10:00:00', 'Khám tổng quát', 'Hoàn thành', 'Mắt mờ, khó nhìn xa', 'Cận thị 2.5 độ', 'Kê đơn kính cận, hẹn khám lại sau 6 tháng', 100000, 'Đã thanh toán', 'P105'),
(7, 1, 1, 1, '2024-02-20 14:00:00', 'Khám tổng quát', 'Đã đặt', 'Khám tim thai định kỳ', NULL, NULL, NULL, 'Chưa thanh toán', 'P101'),
(9, 2, 1, 2, '2024-02-22 09:15:00', 'Khám tổng quát', 'Đã đặt', 'Khám sức khỏe cho bé 10 tháng', NULL, NULL, NULL, 'Chưa thanh toán', 'P205'),
(11, 4, 2, 4, '2024-02-25 11:30:00', 'Tái khám', 'Đã đặt', 'Kiểm tra huyết áp thai kỳ', NULL, NULL, NULL, 'Chưa thanh toán', 'P108'),
(14, 6, 3, 6, '2024-02-28 08:45:00', 'Tái khám', 'Đã đặt', 'Đau lưng tái phát', NULL, NULL, NULL, 'Chưa thanh toán', 'P405'),
(19, 4, 2, 4, '2024-03-02 10:20:00', 'Khám tổng quát', 'Đã đặt', 'Tư vấn thụ tinh nhân tạo', NULL, NULL, NULL, 'Chưa thanh toán', 'P108');

-- 10. Dữ liệu Prescriptions (10 đơn thuốc)
INSERT INTO Prescriptions (member_id, appointment_id, doctor_id, prescription_date, diagnosis, notes, total_cost, status) VALUES
(3, 1, 1, '2024-01-20', 'Tiểu đường type 2', 'Dùng thuốc đều đặn, kiểm tra đường huyết hàng tuần', 150000, 'Đang sử dụng'),
(10, 3, 3, '2024-01-25', 'Sau phẫu thuật tim', 'Dùng thuốc chống đông, tránh va đập mạnh', 280000, 'Đang sử dụng'),
(8, 4, 4, '2024-02-01', 'Thai kỳ 28 tuần', 'Bổ sung dinh dưỡng cho mẹ và thai nhi', 120000, 'Đang sử dụng'),
(2, 5, 5, '2024-02-05', 'Dị ứng thuốc', 'Tránh dùng nhóm thuốc Penicillin', 80000, 'Đã hoàn thành'),
(15, 6, 6, '2024-02-08', 'Viêm khớp dạng thấp', 'Dùng thuốc đúng giờ, theo dõi tác dụng phụ', 200000, 'Đang sử dụng'),
(16, 7, 7, '2024-02-10', 'Đau đầu căng thẳng', 'Dùng khi cần, không quá 3 viên/ngày', 45000, 'Đang sử dụng'),
(1, 8, 8, '2024-02-12', 'Viêm mũi dị ứng', 'Xịt mũi 2 lần/ngày, uống thuốc khi có triệu chứng', 85000, 'Đang sử dụng'),
(12, 9, 9, '2024-02-15', 'Viêm da tiếp xúc', 'Bôi kem mỏng 2 lần/ngày, tránh tiếp xúc chất gây dị ứng', 60000, 'Đang sử dụng'),
(4, NULL, 4, '2023-03-15', 'Thiếu máu sau sinh', 'Bổ sung sắt và folate sau sinh', 75000, 'Đã hoàn thành'),
(19, NULL, 4, '2023-08-20', 'Chuẩn bị thụ tinh nhân tạo', 'Thuốc kích thích rụng trứng', 450000, 'Đã hoàn thành');

-- 11. Dữ liệu Prescription_Details (20 chi tiết đơn thuốc)
INSERT INTO Prescription_Details (prescription_id, medication_id, dosage, frequency, duration_days, total_quantity, instructions, start_date, end_date, status) VALUES
(1, 1, '500mg', '2 lần/ngày', 90, 180, 'Uống sau ăn sáng và tối', '2024-01-20', '2024-04-19', 'Đang dùng'),
(2, 3, '75mg', '1 lần/ngày', 180, 180, 'Uống cùng giờ mỗi ngày', '2024-01-25', '2024-07-23', 'Đang dùng'),
(2, 8, '100mg', '1 lần/ngày', 180, 180, 'Uống sau ăn sáng', '2024-01-25', '2024-07-23', 'Đang dùng'),
(3, 11, '200mg', '2 lần/ngày', 60, 120, 'Uống sau ăn, tránh uống cùng sữa', '2024-02-01', '2024-04-01', 'Đang dùng'),
(3, 14, '5mg', '1 lần/ngày', 60, 60, 'Uống buổi sáng', '2024-02-01', '2024-04-01', 'Đang dùng'),
(4, 12, '10mg', '1 lần/ngày', 5, 5, 'Uống khi có triệu chứng dị ứng', '2024-02-05', '2024-02-10', 'Đã hoàn thành'),
(5, 9, '5mg', '2 lần/ngày', 30, 60, 'Uống sau ăn sáng và tối', '2024-02-08', '2024-03-09', 'Đang dùng'),
(6, 4, '500mg', 'Khi cần', 30, 20, 'Uống khi đau đầu, không quá 3g/ngày', '2024-02-10', '2024-03-11', 'Đang dùng'),
(7, 6, '2 nhát', '2 lần/ngày', 14, 1, 'Xịt mỗi bên mũi 1 nhát', '2024-02-12', '2024-02-26', 'Đang dùng'),
(7, 12, '10mg', '1 lần/ngày', 7, 7, 'Uống buổi tối', '2024-02-12', '2024-02-19', 'Đang dùng'),
(8, 9, '5mg', 'Bôi 2 lần/ngày', 14, 1, 'Bôi lớp mỏng lên vùng da bị viêm', '2024-02-15', '2024-03-01', 'Đang dùng'),
(9, 11, '200mg', '3 lần/ngày', 30, 90, 'Uống sau các bữa ăn', '2023-03-15', '2023-04-14', 'Đã hoàn thành'),
(9, 14, '5mg', '1 lần/ngày', 60, 60, 'Uống buổi sáng trước ăn 30 phút', '2023-03-15', '2023-05-14', 'Đã hoàn thành'),
(10, 13, '20 đơn vị', '1 lần/ngày', 90, 5, 'Tiêm dưới da buổi tối', '2023-08-20', '2023-11-18', 'Đã hoàn thành'),
(1, 7, '20mg', '1 lần/ngày', 90, 90, 'Uống buổi tối sau ăn', '2024-01-20', '2024-04-19', 'Đang dùng'),
(2, 2, '5mg', '1 lần/ngày', 180, 180, 'Uống buổi sáng', '2024-01-25', '2024-07-23', 'Đang dùng'),
(5, 4, '500mg', 'Khi cần', 30, 30, 'Uống khi đau khớp, không quá 2g/ngày', '2024-02-08', '2024-03-09', 'Đang dùng'),
(3, 4, '500mg', '3 lần/ngày', 5, 15, 'Uống sau ăn khi đau bụng', '2024-02-01', '2024-02-06', 'Đã hoàn thành'),
(4, 4, '500mg', '4 lần/ngày', 3, 12, 'Uống ngay khi xuất hiện triệu chứng', '2024-02-05', '2024-02-08', 'Đã hoàn thành'),
(6, 15, '5mg', '1/2 viên tối', 14, 7, 'Uống khi khó ngủ do đau đầu', '2024-02-10', '2024-02-24', 'Đang dùng');

-- 12. Dữ liệu Vaccine_Templates (8 loại vaccine)
INSERT INTO Vaccine_Templates (vaccine_name, description, age_from_days, age_to_days, interval_days, total_doses, is_mandatory, notes) VALUES
('Vaccine BCG', 'Phòng lao', 1, 30, 0, 1, TRUE, 'Tiêm trong tháng đầu sau sinh'),
('Vaccine Hepatitis B', 'Phòng viêm gan B', 1, 30, 30, 3, TRUE, 'Tiêm lúc sinh, 1 tháng, 6 tháng tuổi'),
('Vaccine DPT', 'Phòng bạch hầu, ho gà, uốn ván', 60, 1825, 30, 3, TRUE, 'Tiêm 2, 3, 4 tháng tuổi'),
('Vaccine Polio', 'Phòng bại liệt', 60, 1825, 30, 3, TRUE, 'Tiêm cùng lúc với DPT'),
('Vaccine Hib', 'Phòng Haemophilus influenzae type b', 60, 365, 30, 3, TRUE, 'Phòng viêm màng não mủ'),
('Vaccine MMR', 'Phòng sởi, quai bị, rubella', 365, 1825, 1095, 2, TRUE, 'Tiêm 12 tháng và 18 tháng tuổi'),
('Vaccine Varicella', 'Phòng thủy đậu', 365, 1825, 0, 1, FALSE, 'Có thể tiêm từ 12 tháng tuổi'),
('Vaccine HPV', 'Phòng ung thư cổ tử cung', 3285, 4745, 60, 3, FALSE, 'Tiêm cho bé gái 9-13 tuổi');

-- 13. Dữ liệu Vaccination_Records (15 bản ghi tiêm chủng)
INSERT INTO Vaccination_Records (member_id, template_id, vaccine_name, dose_number, vaccination_date, next_due_date, batch_number, clinic_location, status, notes) VALUES
(3, 1, 'BCG', 1, '2010-12-06', NULL, 'BCG2010120', 'Bệnh viện Sản Nhi Hà Nội', 'Đã tiêm', 'Tiêm khi bé 1 ngày tuổi'),
(3, 2, 'Hepatitis B', 1, '2010-12-06', '2011-01-06', 'HB2010120', 'Bệnh viện Sản Nhi Hà Nội', 'Đã tiêm', 'Mũi đầu tiên'),
(3, 2, 'Hepatitis B', 2, '2011-01-06', '2011-06-06', 'HB2011010', 'Trạm y tế phường', 'Đã tiêm', 'Mũi thứ hai'),
(3, 2, 'Hepatitis B', 3, '2011-06-06', NULL, 'HB2011060', 'Trạm y tế phường', 'Đã tiêm', 'Mũi cuối cùng'),
(5, 1, 'BCG', 1, '2015-09-19', NULL, 'BCG2015091', 'Bệnh viện Từ Dũ', 'Đã tiêm', 'Tiêm khi bé 1 ngày tuổi'),
(5, 2, 'Hepatitis B', 1, '2015-09-19', '2015-10-19', 'HB2015091', 'Bệnh viện Từ Dũ', 'Đã tiêm', 'Tiêm cùng BCG'),
(5, 3, 'DPT', 1, '2015-11-18', '2015-12-18', 'DPT2015111', 'Trạm y tế quận 5', 'Đã tiêm', 'Mũi đầu tiên 2 tháng tuổi'),
(9, 1, 'BCG', 1, '2023-03-01', NULL, 'BCG2023030', 'Bệnh viện Phụ sản Hà Nội', 'Đã tiêm', 'Tiêm khi bé 1 ngày tuổi'),
(9, 2, 'Hepatitis B', 1, '2023-03-01', '2023-04-01', 'HB2023030', 'Bệnh viện Phụ sản Hà Nội', 'Đã tiêm', 'Mũi đầu tiên'),
(9, 2, 'Hepatitis B', 2, '2023-04-01', '2023-09-01', 'HB2023040', 'Trạm y tế phường', 'Đã tiêm', 'Mũi thứ hai'),
(9, 3, 'DPT', 1, '2023-05-01', '2023-06-01', 'DPT2023050', 'Trạm y tế phường', 'Đã tiêm', 'Mũi đầu tiên 2 tháng tuổi'),
(13, 3, 'DPT', 1, '2018-06-15', '2018-07-15', 'DPT2018061', 'Bệnh viện Nhi Trung ương', 'Đã tiêm', 'Mũi đầu tiên'),
(13, 6, 'MMR', 1, '2019-04-15', '2019-10-15', 'MMR2019041', 'Trạm y tế phường', 'Đã tiêm', 'Tiêm 12 tháng tuổi'),
(17, 3, 'DPT', 1, '2020-09-10', '2020-10-10', 'DPT2020091', 'Bệnh viện Nhi Đồng 1', 'Đã tiêm', 'Mũi đầu tiên'),
(17, 4, 'Polio', 1, '2020-09-10', '2020-10-10', 'POLIO2020091', 'Bệnh viện Nhi Đồng 1', 'Đã tiêm', 'Tiêm cùng DPT');

-- 14. Dữ liệu Documents (10 tài liệu)
INSERT INTO Documents (member_id, appointment_id, document_type, title, file_path, description, document_date) VALUES
(3, 1, 'Kết quả xét nghiệm', 'Xét nghiệm đường huyết HbA1c', '/documents/member3_hba1c_20240120.pdf', 'Kết quả HbA1c: 6.8%', '2024-01-20'),
(10, 3, 'Chẩn đoán hình ảnh', 'Chụp mạch vành sau stent', '/documents/member10_angio_20240125.pdf', 'Stent hoạt động tốt, không hẹp tái phát', '2024-01-25'),
(8, 4, 'Kết quả xét nghiệm', 'Siêu âm thai 28 tuần', '/documents/member8_sieuam_20240201.pdf', 'Thai nhi phát triển bình thường', '2024-02-01'),
(2, 5, 'Bệnh án', 'Hồ sơ cấp cứu dị ứng thuốc', '/documents/member2_capuu_20240205.pdf', 'Xử lý phản ứng dị ứng Amoxicillin', '2024-02-05'),
(15, 6, 'Kết quả xét nghiệm', 'Xét nghiệm CRP, RF', '/documents/member15_crp_rf_20240208.pdf', 'CRP tăng, RF dương tính', '2024-02-08'),
(1, NULL, 'Kết quả xét nghiệm', 'Nội soi dạ dày', '/documents/member1_noisi_20220910.pdf', 'Loét dạ dày đã lành, H.Pylori âm tính', '2022-09-10'),
(4, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm máu sau sinh', '/documents/member4_xetnghiem_20230315.pdf', 'Hemoglobin: 9.2 g/dL', '2023-03-15'),
(18, NULL, 'Chẩn đoán hình ảnh', 'CT bụng chẩn đoán ruột thừa', '/documents/member18_ct_20180412.pdf', 'Viêm ruột thừa cấp, cần phẫu thuật', '2018-04-12'),
(19, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm hormone sinh sản', '/documents/member19_hormone_20230120.pdf', 'FSH, LH, E2 trong giới hạn bình thường', '2023-01-20'),
(12, NULL, 'Bệnh án', 'Đánh giá tâm lý học', '/documents/member12_tamlyhoc_20230518.pdf', 'Chẩn đoán lo âu trầm cảm nhẹ', '2023-05-18');
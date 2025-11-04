-- Giả định 'Nguyễn Văn An' (Bản thân) có member_id = 1

-- 1. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm họng cấp', '2025-03-10', FALSE, 'Nhẹ', 'Đã khỏi', 'Uống thuốc 5 ngày theo đơn bác sĩ.');

-- 2. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Cảm cúm (Virus Influenza A)', '2024-11-01', FALSE, 'Trung bình', 'Đã khỏi', 'Nghỉ ngơi 7 ngày, dùng Tamiflu.');

-- 3. Bệnh mãn tính (Đang kiểm soát)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm mũi dị ứng', '2015-01-01', TRUE, 'Nhẹ', 'Kiểm soát được', 'Dị ứng với phấn hoa và lông mèo. Dùng thuốc khi có triệu chứng.');

-- 4. Bệnh mãn tính (Đang điều trị)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Tăng huyết áp (Huyết áp cao)', '2022-05-20', TRUE, 'Trung bình', 'Đang điều trị', 'Uống Amlodipin 5mg mỗi ngày. Tái khám định kỳ 3 tháng/lần.');

-- 5. Bệnh đã khỏi (Có nhập viện - Phẫu thuật)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes, hospital_admission_date, hospital_discharge_date, hospital_name, hospital_address) 
VALUES 
    (1, 'Viêm ruột thừa cấp', '2021-08-15', FALSE, 'Nặng', 'Đã khỏi', 'Phẫu thuật nội soi cắt ruột thừa.', '2021-08-15', '2021-08-18', 'Bệnh viện Bạch Mai', '78 Giải Phóng, P. Phương Mai, Q. Đống Đa, Hà Nội');

-- 6. Bệnh mãn tính (Đang điều trị)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm loét dạ dày (HP+)', '2023-01-10', TRUE, 'Trung bình', 'Đang điều trị', 'Đã điều trị hết HP. Hiện đang duy trì thuốc giảm tiết acid khi cần.');

-- 7. Bệnh đã khỏi (Có nhập viện - Tai nạn)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes, hospital_admission_date, hospital_discharge_date, hospital_name, hospital_address) 
VALUES 
    (1, 'Gãy xương cẳng tay (Trái)', '2020-04-02', FALSE, 'Nặng', 'Đã khỏi', 'Tai nạn xe máy. Bó bột 8 tuần.', '2020-04-02', '2020-04-04', 'Bệnh viện Hữu nghị Việt Đức', '40 Tràng Thi, P. Hàng Bông, Q. Hoàn Kiếm, Hà Nội');

-- 8. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm xoang cấp', '2023-10-05', FALSE, 'Trung bình', 'Đã khỏi', 'Điều trị kháng sinh 10 ngày.');

-- 9. Bệnh mãn tính (Đang điều trị)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Rối loạn lipid máu (Mỡ máu cao)', '2022-05-20', TRUE, 'Trung bình', 'Đang điều trị', 'Dùng Rosuvastatin 10mg. Thay đổi lối sống, ăn kiêng.');

-- 10. Bệnh đã khỏi (Có nhập viện)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes, hospital_admission_date, hospital_discharge_date, hospital_name, hospital_address) 
VALUES 
    (1, 'Sốt xuất huyết Dengue', '2019-07-22', FALSE, 'Nặng', 'Đã khỏi', 'Nhập viện theo dõi tiểu cầu.', '2019-07-24', '2019-07-28', 'Bệnh viện Bạch Mai', '78 Giải Phóng, P. Phương Mai, Q. Đống Đa, Hà Nội');

-- 11. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Đau mắt đỏ (Viêm kết mạc)', '2024-09-15', FALSE, 'Nhẹ', 'Đã khỏi', 'Dùng thuốc nhỏ mắt Tobrex 7 ngày.');

-- 12. Bệnh đã khỏi (Có nhập viện)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes, hospital_admission_date, hospital_discharge_date, hospital_name, hospital_address) 
VALUES 
    (1, 'Viêm phổi cộng đồng', '2018-12-01', FALSE, 'Trung bình', 'Đã khỏi', 'Điều trị kháng sinh tĩnh mạch 5 ngày.', '2018-12-01', '2018-12-06', 'Bệnh viện Đa khoa Quốc tế Vinmec Times City', '458 Minh Khai, P. Vĩnh Tuy, Q. Hai Bà Trưng, Hà Nội');

-- 13. Bệnh mãn tính (Đang điều trị)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Thoái hóa đốt sống cổ (C5-C6)', '2023-03-30', TRUE, 'Trung bình', 'Đang điều trị', 'Tập vật lý trị liệu. Tránh ngồi lâu.');

-- 14. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Ngộ độc thực phẩm', '2022-07-11', FALSE, 'Trung bình', 'Đã khỏi', 'Nôn và tiêu chảy sau khi ăn hải sản.');

-- 15. Bệnh mãn tính (Đang kiểm soát)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Bệnh Gout (Gút)', '2021-06-01', TRUE, 'Trung bình', 'Kiểm soát được', 'Đã có 2 cơn Gout cấp. Hiện đang kiêng rượu bia, nội tạng động vật. Acid uric ổn định.');

-- 16. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm phế quản cấp', '2021-02-15', FALSE, 'Nhẹ', 'Đã khỏi', 'Ho nhiều về đêm, dùng siro ho và kháng sinh.');

-- 17. Bệnh mãn tính (Đang kiểm soát)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Viêm da cơ địa (Eczema)', '2010-01-01', TRUE, 'Nhẹ', 'Kiểm soát được', 'Thường bị khô da, ngứa ở khuỷu tay khi thời tiết khô. Dùng kem dưỡng ẩm.');

-- 18. Bệnh đã khỏi (Không mãn tính)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Trật sơ mi cổ chân (Phải)', '2023-05-01', FALSE, 'Nhẹ', 'Đã khỏi', 'Do chơi thể thao. Nghỉ ngơi 1 tuần.');

-- 19. Bệnh mãn tính (Đang điều trị - Bệnh nặng)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes, hospital_name) 
VALUES 
    (1, 'Tiểu đường Type 2', '2024-01-15', TRUE, 'Trung bình', 'Đang điều trị', 'Đang dùng Metformin 1000mg/ngày. Khám tại Khoa Nội tiết - Bệnh viện 108.', 'Bệnh viện Trung ương Quân đội 108');

-- 20. Bệnh mãn tính (Đang kiểm soát)
INSERT INTO Medical_History 
    (member_id, condition_name, diagnosis_date, is_chronic, severity, status, notes) 
VALUES 
    (1, 'Trĩ nội (Độ 1)', '2022-11-10', TRUE, 'Nhẹ', 'Kiểm soát được', 'Không triệu chứng, phát hiện khi nội soi đại tràng. Thay đổi chế độ ăn nhiều chất xơ.');

-- Giả định 'Nguyễn Văn An' (Bản thân) có member_id = 1

-- 1. Dị ứng thuốc (Nguy hiểm)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes)
VALUES
    (1, 'Penicillin', 'Thuốc', 'Nguy hiểm', 'Phát ban toàn thân, khó thở, sưng họng, sốc phản vệ.', '2010-05-15', 'Phải luôn báo cho bác sĩ trước khi dùng kháng sinh.');

-- 2. Dị ứng thức ăn (Nặng)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes)
VALUES
    (1, 'Tôm (Hải sản có vỏ)', 'Thức ăn', 'Nặng', 'Sưng môi, ngứa họng, nổi mề đay, đau bụng.', '2018-07-20', 'Các loại tôm, cua đều bị.');

-- 3. Dị ứng thức ăn (Nguy hiểm)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes)
VALUES
    (1, 'Đậu phộng (Lạc)', 'Thức ăn', 'Nguy hiểm', 'Khó thở, sưng mặt, chóng mặt, có thể gây sốc phản vệ.', '2005-01-01', 'Tránh xa tất cả các sản phẩm có chứa đậu phộng.');

-- 4. Dị ứng môi trường (Trung bình)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes)
VALUES
    (1, 'Lông mèo', 'Môi trường', 'Trung bình', 'Hắt hơi liên tục, ngứa mắt, chảy nước mắt, nghẹt mũi.', NULL, 'Triệu chứng xuất hiện sau khi tiếp xúc khoảng 15 phút.');

-- 5. Dị ứng môi trường (Nhẹ)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date)
VALUES
    (1, 'Phấn hoa (Cỏ dại)', 'Môi trường', 'Nhẹ', 'Hắt hơi, sổ mũi, ngứa mũi.', '2015-03-01');

-- 6. Dị ứng thuốc (Trung bình)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes)
VALUES
    (1, 'Aspirin (NSAIDs)', 'Thuốc', 'Trung bình', 'Nổi mề đay, tức ngực, hen suyễn.', '2022-01-10', 'Cần cẩn trọng với các loại thuốc chống viêm không steroid khác.');

-- 7. Dị ứng thức ăn (Nhẹ)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, discovered_date)
VALUES
    (1, 'Sữa bò (Không dung nạp Lactose)', 'Thức ăn', 'Nhẹ', 'Đầy hơi, chướng bụng, tiêu chảy sau khi uống sữa.', '2019-01-01');

-- 8. Dị ứng khác (Nặng)
INSERT INTO Allergies
    (member_id, allergen, allergy_type, severity, symptoms, notes)
VALUES
    (1, 'Nọc ong vò vẽ', 'Khác', 'Nặng', 'Sưng tấy nghiêm trọng tại chỗ đốt, chóng mặt, buồn nôn.', 'Bị đốt năm 2021, sưng rất to.');
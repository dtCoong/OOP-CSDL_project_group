-- ---------------------------------
-- SỬ DỤNG DATABASE
-- ---------------------------------
USE EHR_DB;

-- ---------------------------------
-- BƯỚC 1: XÓA DỮ LIỆU CŨ ĐỂ TRÁNH TRÙNG LẶP VÀ LỖI KHÓA NGOẠI
-- (Phải xóa theo thứ tự phụ thuộc: Vaccination_Records -> Vaccine_Templates -> Family_Members)
-- ---------------------------------
SET FOREIGN_KEY_CHECKS = 0; -- Tắt kiểm tra khóa ngoại để TRUNCATE
TRUNCATE TABLE Vaccination_Records;
TRUNCATE TABLE Vaccine_Templates;
TRUNCATE TABLE Family_Members;
SET FOREIGN_KEY_CHECKS = 1; -- Bật lại kiểm tra khóa ngoại

-- ---------------------------------
-- BƯỚC 2: THÊM DỮ LIỆU MẪU
-- (Phải thêm vào Family_Members và Vaccine_Templates trước Vaccination_Records)
-- ---------------------------------

-- Thêm dữ liệu mẫu vào Family_Members
INSERT INTO Family_Members 
    (member_id, user_id, name, dob, gender, relationship, blood_type, insurance_number, avatar_path, created_at)
VALUES
    (1, 101, 'Nguyen Van A', '1990-05-15', 'Nam', 'Bản thân', 'A+', 'BH123456789', '/avatars/nguyenvana.jpg', NOW()),
    (2, 101, 'Le Thi B', '1992-11-20', 'Nu', 'Vợ/Chồng', 'B-', 'BH987654321', '/avatars/lethib.jpg', NOW()),
    (3, 101, 'Nguyen Van C', '2020-03-10', 'Nam', 'Con', 'O+', 'BH001122334', '/avatars/nguyenvanc.jpg', NOW()),
    (4, 102, 'Tran Thi D', '1985-01-22', 'Nu', 'Bản thân', 'AB+', 'BH112233445', '/avatars/tranthid.jpg', NOW()); -- Ví dụ cho user_id khác

-- ===== THÊM DANH SÁCH VACCINE MẪU (Vaccine_Templates) =====
INSERT INTO Vaccine_Templates 
    (vaccine_name, description, age_from_days, age_to_days, interval_days, total_doses, notes, created_at)
VALUES
    ('Lao (BCG)', 'Phòng bệnh Lao', 0, 30, 0, 1, 'Tiêm 1 mũi duy nhất càng sớm càng tốt sau sinh.', NOW()), -- template_id = 1
    ('Viêm gan B (HepB) sơ sinh', 'Phòng Viêm gan B', 0, 1, 0, 1, 'Tiêm 1 mũi trong 24 giờ đầu sau sinh.', NOW()), -- template_id = 2
    ('5-trong-1 (DPT-VGB-Hib)', 'Phòng Bạch hầu, Ho gà, Uốn ván, Viêm gan B, Viêm phổi/màng não do Hib', 60, 120, 30, 3, '3 mũi cơ bản lúc 2, 3, 4 tháng tuổi. Tiêm nhắc DPT4 lúc 18 tháng.', NOW()), -- template_id = 3
    ('Bại liệt (OPV)', 'Phòng Bại liệt (dạng uống)', 60, 120, 30, 3, 'Uống 3 liều lúc 2, 3, 4 tháng tuổi.', NOW()), -- template_id = 4
    ('Sởi (MVAC)', 'Phòng bệnh Sởi', 270, 365, 0, 1, 'Tiêm 1 mũi lúc 9 tháng tuổi.', NOW()), -- template_id = 5
    ('Sởi - Rubella (MR)', 'Phòng Sởi và Rubella', 540, 730, 0, 1, 'Tiêm 1 mũi lúc 18 tháng tuổi.', NOW()), -- template_id = 6
    ('Viêm não Nhật Bản (JE)', 'Phòng Viêm não Nhật Bản', 365, 1095, 365, 3, '3 mũi: Mũi 1 lúc 1 tuổi, Mũi 2 sau 1-2 tuần, Mũi 3 sau 1 năm.', NOW()), -- template_id = 7
    ('6-trong-1 (Infanrix Hexa/Hexaxim)', 'Phòng Bạch hầu, Ho gà, Uốn ván, Viêm gan B, Hib, Bại liệt', 60, 730, 30, 4, '3 mũi cơ bản (cách nhau 1-2 tháng) và 1 mũi nhắc lại.', NOW()), -- template_id = 8
    ('Phế cầu (Prevenar 13)', 'Phòng viêm phổi, viêm màng não do phế cầu khuẩn', 60, 99999, 30, 4, 'Lịch tiêm thay đổi tùy độ tuổi bắt đầu.', NOW()), -- template_id = 9
    ('Tiêu chảy do Rotavirus (Rotarix)', 'Phòng viêm dạ dày ruột cấp do Rotavirus', 42, 168, 30, 2, 'Uống 2 liều, phải hoàn thành trước 6 tháng tuổi.', NOW()), -- template_id = 10
    ('Thủy đậu (Varivax/Varicella)', 'Phòng bệnh Thủy đậu', 365, 99999, 90, 2, 'Mũi 1 lúc 12 tháng. Mũi 2 cách mũi 1 ít nhất 3 tháng.', NOW()), -- template_id = 11
    ('MMR II / Priorix (Sởi - Quai bị - Rubella)', 'Phòng Sởi - Quai bị - Rubella', 365, 99999, 90, 2, 'Mũi 1 lúc 12-15 tháng. Mũi 2 lúc 4-6 tuổi.', NOW()), -- template_id = 12
    ('Viêm gan A (Avaxim/Havrix)', 'Phòng Viêm gan A', 365, 99999, 180, 2, '2 mũi tiêm cách nhau 6-12 tháng.', NOW()), -- template_id = 13
    ('Viêm màng não (Menactra)', 'Phòng viêm màng não do não mô cầu ACYW-135', 270, 99999, 90, 2, 'Tiêm từ 9 tháng tuổi. Lịch tiêm tùy độ tuổi.', NOW()), -- template_id = 14
    ('HPV (Gardasil 9)', 'Phòng ung thư cổ tử cung, hậu môn... do HPV', 3285, 9855, 180, 3, 'Dành cho trẻ em và người lớn (9-26 tuổi). 3 mũi 0-2-6 tháng.', NOW()), -- template_id = 15
    ('Cúm mùa (Vaxigrip/Influvac)', 'Phòng Cúm mùa hàng năm', 180, 99999, 365, 1, 'Tiêm nhắc lại hàng năm. Trẻ < 9 tuổi lần đầu tiêm 2 mũi.', NOW()), -- template_id = 16
    ('Zona (Shingrix)', 'Phòng bệnh Zona (Shingles)', 18250, 99999, 60, 2, 'Dành cho người lớn 50 tuổi trở lên. 2 mũi cách nhau 2-6 tháng.', NOW()), -- template_id = 17
    ('Bệnh dại (Verorab)', 'Phòng bệnh Dại (sau phơi nhiễm)', 0, 99999, 3, 5, 'Tiêm 5 mũi vào các ngày 0, 3, 7, 14, 28 (nếu chưa tiêm dự phòng).', NOW()), -- template_id = 18
    ('COVID-19 (Pfizer)', 'Phòng COVID-19', 4380, 99999, 21, 2, '2 mũi cơ bản cách nhau 3 tuần. Có thể cần mũi tăng cường.', NOW()), -- template_id = 19
    ('COVID-19 (Moderna)', 'Phòng COVID-19', 4380, 99999, 28, 2, '2 mũi cơ bản cách nhau 4 tuần.', NOW()), -- template_id = 20
    ('COVID-19 (AstraZeneca)', 'Phòng COVID-19', 6570, 99999, 56, 2, '2 mũi cơ bản cách nhau 8-12 tuần.', NOW()); -- template_id = 21

-- ===== THÊM HỒ SƠ TIÊM CHỦNG (Vaccination_Records) =====
INSERT INTO Vaccination_Records 
    (member_id, template_id, vaccine_name, dose_number, vaccination_date, next_due_date, 
     batch_number, status, side_effects, notes, created_at)
VALUES
    -- Dữ liệu mẫu (sử dụng member_id = 1, 2, 3, 4 đã được tạo ở trên)
    -- Comirnaty (Pfizer) -> template_id = 19
    (1, 19, 'COVID-19 (Pfizer)', 1, '2025-09-17', '2025-10-08', 'PF123', 'Đã tiêm', NULL, '', NOW()),
    (1, 19, 'COVID-19 (Pfizer)', 2, '2025-10-08', NULL, 'PF456', 'Đã tiêm', 'Sốt nhẹ', 'Hoàn thành', NOW()),
    (1, 16, 'Cúm mùa (Vaxigrip/Influvac)', 1, '2024-11-01', '2025-11-01', 'FLU987', 'Đã tiêm', NULL, 'Nhắc hàng năm', NOW()),
    
    -- Spikevax (Moderna) -> template_id = 20
    (2, 20, 'COVID-19 (Moderna)', 1, '2025-10-01', '2025-10-29', 'MD789', 'Đã tiêm', NULL, '', NOW()),
    (2, 17, 'Zona (Shingrix)', 1, '2025-01-10', '2025-03-10', 'SHX555', 'Đã tiêm', NULL, 'Tiêm khi 52 tuổi', NOW()),
    (2, 17, 'Zona (Shingrix)', 2, '2025-03-15', NULL, 'SHX666', 'Đã tiêm', NULL, 'Hoàn thành', NOW()),
    
    -- Hepatitis B (HepB) sơ sinh -> template_id = 2
    (3, 2, 'Viêm gan B (HepB) sơ sinh', 1, '2020-01-15', '2020-02-15', 'HB111', 'Đã tiêm', NULL, 'Tiêm lúc sơ sinh', NOW()),
    
    -- 5-trong-1 (DPT-VGB-Hib) -> template_id = 3
    (3, 3, '5-trong-1 (DPT-VGB-Hib)', 1, '2020-03-15', '2020-04-15', 'HB222', 'Đã tiêm', NULL, '', NOW()),
    (3, 3, '5-trong-1 (DPT-VGB-Hib)', 2, '2020-05-15', '2020-07-15', 'HB333', 'Đã tiêm', NULL, '', NOW()),
    (3, 3, '5-trong-1 (DPT-VGB-Hib)', 3, '2020-07-15', '2022-01-15', 'HB444', 'Đã tiêm', NULL, 'Đã tiêm 3 mũi cơ bản', NOW()),
    
    -- 5-trong-1 (DPT-VGB-Hib) -> template_id = 3
    (4, 3, '5-trong-1 (DPT-VGB-Hib)', 1, '2024-03-01', '2024-05-01', 'DTAP01', 'Đã tiêm', NULL, '', NOW()),
    (4, 3, '5-trong-1 (DPT-VGB-Hib)', 2, '2024-05-05', '2024-07-05', 'DTAP02', 'Đã tiêm', 'Sốt nhẹ', '', NOW()),
    (4, 3, '5-trong-1 (DPT-VGB-Hib)', 3, '2024-07-10', '2025-04-10', 'DTAP03', 'Đã tiêm', NULL, 'Đã tiêm 3 mũi cơ bản', NOW()),
    (4, 12, 'MMR II (Sởi - Quai bị - Rubella)', 1, '2025-01-05', '2028-01-05', 'MMR777', 'Đã tiêm', NULL, 'Tiêm lúc 12 tháng tuổi', NOW());


-- ---------------------------------
-- BƯỚC 3: KIỂM TRA LẠI DỮ LIỆU
-- ---------------------------------
SELECT * FROM Family_Members;
SELECT * FROM Vaccine_Templates;
SELECT * FROM Vaccination_Records;
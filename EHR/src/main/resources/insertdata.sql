-- ---------------------------------
-- SỬ DỤNG DATABASE
-- ---------------------------------
USE EHR_DB;

-- ---------------------------------
-- BƯỚC 1: XÓA DỮ LIỆU CŨ ĐỂ TRÁNH TRÙNG LẶP VÀ LỖI KHÓA NGOẠI
-- (Phải xóa 'VaccinationRecords' trước vì nó có khóa ngoại)
-- ---------------------------------
SET FOREIGN_KEY_CHECKS = 0; -- Tắt kiểm tra khóa ngoại
TRUNCATE TABLE VaccinationRecords;
TRUNCATE TABLE VaccineTemplates;
SET FOREIGN_KEY_CHECKS = 1; -- Bật lại kiểm tra khóa ngoại

-- ---------------------------------
-- BƯỚC 2: THÊM DANH SÁCH VACCINE MẪU (VaccineTemplates)
-- Các ID (1, 2, 3...) sẽ được TỰ ĐỘNG TĂNG (AUTO_INCREMENT)
-- ---------------------------------

-- ===== VẮC-XIN TRONG TIÊM CHỦNG MỞ RỘNG (MIỄN PHÍ) =====
INSERT INTO VaccineTemplates 
    (vaccineName, description, ageFromDays, ageToDays, intervalDays, totalDoses, notes, createdAt)
VALUES
    ('Lao (BCG)', 'Phòng bệnh Lao', 0, 30, 0, 1, 'Tiêm 1 mũi duy nhất càng sớm càng tốt sau sinh.', NOW()), -- ID = 1
    ('Viêm gan B (HepB) sơ sinh', 'Phòng Viêm gan B', 0, 1, 0, 1, 'Tiêm 1 mũi trong 24 giờ đầu sau sinh.', NOW()), -- ID = 2
    ('5-trong-1 (DPT-VGB-Hib)', 'Phòng Bạch hầu, Ho gà, Uốn ván, Viêm gan B, Viêm phổi/màng não do Hib', 60, 120, 30, 3, '3 mũi cơ bản lúc 2, 3, 4 tháng tuổi. Tiêm nhắc DPT4 lúc 18 tháng.', NOW()), -- ID = 3
    ('Bại liệt (OPV)', 'Phòng Bại liệt (dạng uống)', 60, 120, 30, 3, 'Uống 3 liều lúc 2, 3, 4 tháng tuổi.', NOW()), -- ID = 4
    ('Sởi (MVAC)', 'Phòng bệnh Sởi', 270, 365, 0, 1, 'Tiêm 1 mũi lúc 9 tháng tuổi.', NOW()), -- ID = 5
    ('Sởi - Rubella (MR)', 'Phòng Sởi và Rubella', 540, 730, 0, 1, 'Tiêm 1 mũi lúc 18 tháng tuổi.', NOW()), -- ID = 6
    ('Viêm não Nhật Bản (JE)', 'Phòng Viêm não Nhật Bản', 365, 1095, 365, 3, '3 mũi: Mũi 1 lúc 1 tuổi, Mũi 2 sau 1-2 tuần, Mũi 3 sau 1 năm.', NOW()); -- ID = 7

-- ===== VẮC-XIN DỊCH VỤ (TRẢ PHÍ) VÀ CÁC VẮC-XIN KHÁC =====
INSERT INTO VaccineTemplates 
    (vaccineName, description, ageFromDays, ageToDays, intervalDays, totalDoses, notes, createdAt)
VALUES
    ('6-trong-1 (Infanrix Hexa/Hexaxim)', 'Phòng Bạch hầu, Ho gà, Uốn ván, Viêm gan B, Hib, Bại liệt', 60, 730, 30, 4, '3 mũi cơ bản (cách nhau 1-2 tháng) và 1 mũi nhắc lại.', NOW()), -- ID = 8
    ('Phế cầu (Prevenar 13)', 'Phòng viêm phổi, viêm màng não do phế cầu khuẩn', 60, 99999, 30, 4, 'Lịch tiêm thay đổi tùy độ tuổi bắt đầu.', NOW()), -- ID = 9
    ('Tiêu chảy do Rotavirus (Rotarix)', 'Phòng viêm dạ dày ruột cấp do Rotavirus', 42, 168, 30, 2, 'Uống 2 liều, phải hoàn thành trước 6 tháng tuổi.', NOW()), -- ID = 10
    ('Thủy đậu (Varivax/Varicella)', 'Phòng bệnh Thủy đậu', 365, 99999, 90, 2, 'Mũi 1 lúc 12 tháng. Mũi 2 cách mũi 1 ít nhất 3 tháng.', NOW()), -- ID = 11
    ('MMR II / Priorix (Sởi - Quai bị - Rubella)', 'Phòng Sởi - Quai bị - Rubella', 365, 99999, 90, 2, 'Mũi 1 lúc 12-15 tháng. Mũi 2 lúc 4-6 tuổi.', NOW()), -- ID = 12
    ('Viêm gan A (Avaxim/Havrix)', 'Phòng Viêm gan A', 365, 99999, 180, 2, '2 mũi tiêm cách nhau 6-12 tháng.', NOW()), -- ID = 13
    ('Viêm màng não (Menactra)', 'Phòng viêm màng não do não mô cầu ACYW-135', 270, 99999, 90, 2, 'Tiêm từ 9 tháng tuổi. Lịch tiêm tùy độ tuổi.', NOW()), -- ID = 14
    ('HPV (Gardasil 9)', 'Phòng ung thư cổ tử cung, hậu môn... do HPV', 3285, 9855, 180, 3, 'Dành cho trẻ em và người lớn (9-26 tuổi). 3 mũi 0-2-6 tháng.', NOW()), -- ID = 15
    ('Cúm mùa (Vaxigrip/Influvac)', 'Phòng Cúm mùa hàng năm', 180, 99999, 365, 1, 'Tiêm nhắc lại hàng năm. Trẻ < 9 tuổi lần đầu tiêm 2 mũi.', NOW()), -- ID = 16
    ('Zona (Shingrix)', 'Phòng bệnh Zona (Shingles)', 18250, 99999, 60, 2, 'Dành cho người lớn 50 tuổi trở lên. 2 mũi cách nhau 2-6 tháng.', NOW()), -- ID = 17
    ('Bệnh dại (Verorab)', 'Phòng bệnh Dại (sau phơi nhiễm)', 0, 99999, 3, 5, 'Tiêm 5 mũi vào các ngày 0, 3, 7, 14, 28 (nếu chưa tiêm dự phòng).', NOW()), -- ID = 18
    ('COVID-19 (Pfizer)', 'Phòng COVID-19', 4380, 99999, 21, 2, '2 mũi cơ bản cách nhau 3 tuần. Có thể cần mũi tăng cường.', NOW()), -- ID = 19
    ('COVID-19 (Moderna)', 'Phòng COVID-19', 4380, 99999, 28, 2, '2 mũi cơ bản cách nhau 4 tuần.', NOW()), -- ID = 20
    ('COVID-19 (AstraZeneca)', 'Phòng COVID-19', 6570, 99999, 56, 2, '2 mũi cơ bản cách nhau 8-12 tuần.', NOW()); -- ID = 21

-- ---------------------------------
-- BƯỚC 3: THÊM LẠI HỒ SƠ TIÊM CHỦNG (VaccinationRecords)
-- (Quan trọng: Các 'templateId' đã được CẬP NHẬT để khớp với ID mới ở trên)
-- ---------------------------------
INSERT INTO VaccinationRecords 
    (memberId, templateId, vaccineName, doseNumber, vaccinationDate, nextDueDate, batchNumber, status, notes, createdAt)
VALUES
    -- Dữ liệu cũ của bạn (đã ánh xạ lại ID):
    -- Comirnaty (Pfizer) -> ID = 19
    (5, 19, 'Comirnaty (Pfizer)', 1, '2025-09-17', '2025-10-08', 'PF123', 'Đã tiêm', '', NOW()),
    (5, 19, 'Comirnaty (Pfizer)', 2, '2025-10-08', NULL, 'PF456', 'Đã tiêm', 'Hoàn thành', NOW()),
    
    -- Spikevax (Moderna) -> ID = 20
    (7, 20, 'Spikevax (Moderna)', 1, '2025-10-01', '2025-10-29', 'MD789', 'Đã tiêm', '', NOW()),
    
    -- Hepatitis B (HepB) -> ID = 2 (Loại sơ sinh)
    (5, 2, 'Hepatitis B (HepB)', 1, '2020-01-15', '2020-02-15', 'HB111', 'Đã tiêm', 'Tiêm lúc sơ sinh', NOW()),
    -- (Giả sử mũi 2 & 3 là loại 5-trong-1 hoặc 6-trong-1)
    (5, 3, '5-trong-1 (DPT-VGB-Hib)', 1, '2020-03-15', '2020-04-15', 'HB222', 'Đã tiêm', '', NOW()),
    
    -- Influenza (Cúm mùa) -> ID = 16
    (7, 16, 'Influenza (Cúm mùa)', 1, '2024-11-01', '2025-11-01', 'FLU987', 'Đã tiêm', 'Nhắc hàng năm', NOW()),
    
    -- Shingrix (Zona) -> ID = 17
    (7, 17, 'Shingrix (Zona)', 1, '2025-01-10', '2025-03-10', 'SHX555', 'Đã tiêm', 'Tiêm khi 52 tuổi', NOW()),
    (7, 17, 'Shingrix (Zona)', 2, '2025-03-15', NULL, 'SHX666', 'Đã tiêm', 'Hoàn thành', NOW()),
    
    -- DTaP (Gần giống 5-trong-1) -> ID = 3
    (8, 3, '5-trong-1 (DPT-VGB-Hib)', 1, '2024-03-01', '2024-05-01', 'DTAP01', 'Đã tiêm', '', NOW()),
    (8, 3, '5-trong-1 (DPT-VGB-Hib)', 2, '2024-05-05', '2024-07-05', 'DTAP02', 'Đã tiêm', '', NOW()),
    (8, 3, '5-trong-1 (DPT-VGB-Hib)', 3, '2024-07-10', '2025-04-10', 'DTAP03', 'Đã tiêm', 'Đã tiêm 3 mũi cơ bản', NOW()),
    
    -- MMR -> ID = 12
    (8, 12, 'MMR II (Sởi - Quai bị - Rubella)', 1, '2025-01-05', '2028-01-05', 'MMR777', 'Đã tiêm', 'Tiêm lúc 12 tháng tuổi', NOW());

-- ---------------------------------
-- BƯỚC 4: KIỂM TRA LẠI
-- ---------------------------------
SELECT * FROM VaccineTemplates;
SELECT * FROM VaccinationRecords;
-- Xóa CSDL nếu nó đã tồn tại (để làm lại từ đầu)
DROP DATABASE IF EXISTS EHR_DB;

-- Tạo CSDL mới
CREATE DATABASE EHR_DB;

-- Sử dụng CSDL vừa tạo
USE EHR_DB;

-- ---------------------------------
-- Bảng 1: VaccineTemplates (Các mẫu vắc-xin)
-- ---------------------------------
CREATE TABLE VaccineTemplates (
    vaccineTemplateId INT PRIMARY KEY AUTO_INCREMENT,
    vaccineName VARCHAR(255) NOT NULL,
    description TEXT,
    ageFromDays INT,
    ageToDays INT,
    intervalDays INT,
    totalDoses INT,
    notes TEXT,
    createdAt DATETIME
);

-- ---------------------------------
-- Bảng 2: VaccinationRecords (Hồ sơ tiêm chủng)
-- ---------------------------------
CREATE TABLE VaccinationRecords (
    vaccinationId INT PRIMARY KEY AUTO_INCREMENT,
    memberId INT NOT NULL,  -- ID của bệnh nhân/thành viên
    templateId INT,         -- Khóa ngoại liên kết với VaccineTemplates
    
    vaccineName VARCHAR(255),
    doseNumber INT,
    vaccinationDate DATE,   -- Ngày đã tiêm
    nextDueDate DATE,       -- Ngày hẹn tiêm mũi tiếp theo
    batchNumber VARCHAR(100),
    status VARCHAR(100),
    notes TEXT,
    createdAt DATETIME,
    
    -- Thiết lập ràng buộc khóa ngoại
    FOREIGN KEY (templateId) 
        REFERENCES VaccineTemplates(vaccineTemplateId)
        ON DELETE SET NULL -- Nếu 1 mẫu vaccine bị xóa, hồ sơ vẫn giữ lại (templateId = NULL)
);
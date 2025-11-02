-- Xóa CSDL nếu nó đã tồn tại (để làm lại từ đầu)
DROP DATABASE IF EXISTS EHR_DB;

-- Tạo CSDL mới
CREATE DATABASE EHR_DB;

-- Sử dụng CSDL vừa tạo
USE EHR_DB;

-- ---------------------------------
-- Bảng 1: Family_Members (Đã tạo theo cấu trúc bạn cung cấp)
-- Mức đích: Lưu trữ thông tin cá nhân của các thành viên gia đình
-- ---------------------------------
CREATE TABLE Family_Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT, -- Liên kết với người dùng quản lý (Giả định sẽ có bảng Users sau)
    name VARCHAR(255) NOT NULL,
    dob DATE,
    gender VARCHAR(10),
    relationship VARCHAR(50), -- Ví dụ: 'Bản thân', 'Con', 'Vợ/Chồng', 'Cha mẹ', 'Khác'
    blood_type VARCHAR(5),    -- Nhóm máu
    insurance_number VARCHAR(50), -- Số bảo hiểm y tế
    avatar_path VARCHAR(255), -- Đường dẫn ảnh đại diện
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------
-- Bảng 2: Vaccine_Templates (lịch tiêm chủng mẫu)
-- ---------------------------------
CREATE TABLE Vaccine_Templates (
    template_id INT PRIMARY KEY AUTO_INCREMENT,
    vaccine_name VARCHAR(100) NOT NULL,
    description TEXT,
    age_from_days INT NOT NULL,
    age_to_days INT,
    interval_days INT,
    total_doses INT DEFAULT 1,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------
-- Bảng 3: Vaccination_Records (hồ sơ tiêm chủng)
-- ---------------------------------
CREATE TABLE Vaccination_Records (
    vaccination_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,  
    template_id INT,         
    
    vaccine_name VARCHAR(100) NOT NULL,
    dose_number INT DEFAULT 1,          
    vaccination_date DATE,   
    next_due_date DATE,      
    batch_number VARCHAR(50),
    status ENUM('Đã tiêm', 'Chưa tiêm', 'Quá hạn', 'Hoãn', 'Từ chối') DEFAULT 'Chưa tiêm',
    side_effects TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Thiết lập ràng buộc khóa ngoại
    FOREIGN KEY (member_id) 
        REFERENCES Family_Members(member_id)
        ON DELETE CASCADE, 

    FOREIGN KEY (template_id) 
        REFERENCES Vaccine_Templates(template_id)
        ON DELETE SET NULL 
);
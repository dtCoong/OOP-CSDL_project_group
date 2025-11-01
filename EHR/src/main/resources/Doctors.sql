-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bác sỹ (mỗi bác sỹ thuộc 1 khoa)
CREATE TABLE Doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    department_id INT NOT NULL,  -- liên kết khoa
    specialization VARCHAR(100) NOT NULL,
    degree ENUM('Bác sỹ', 'Thạc sỹ', 'Tiến sỹ', 'Giáo sư') DEFAULT 'Bác sỹ',
    experience_years INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    full_name VARCHAR(100) NOT NULL,
    avatar_path VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE CASCADE
);
INSERT INTO Doctors (department_id, full_name, specialization, degree, experience_years, avatar_path)
VALUES
(
    1, 'Nguyễn Văn Mạnh', 'Can thiệp tim mạch', 'Tiến sỹ', 15, 
    '/avatars/doctors/nguyenvanmanh.jpg'
),
(
    1, 'Trần Thị Lan Anh', 'Nội tim mạch', 'Thạc sỹ', 8, 
    '/avatars/doctors/tranthilananh.jpg'
),
(
    2, 'Lê Hùng Sơn', 'Nội tiết - Tiểu đường', 'Giáo sư', 25, 
    '/avatars/doctors/lehungson.jpg'
),
(
    3, 'Phạm Gia Khiêm', 'Phẫu thuật cột sống', 'Tiến sỹ', 18, 
    NULL
),
(
    3, 'Vũ Thị Mai', 'Chấn thương thể thao', 'Bác sỹ', 5, 
    '/avatars/doctors/vuthimai.jpg'
),
(
    4, 'Hoàng Minh Đức', 'Nội soi tiêu hóa', 'Thạc sỹ', 12, 
    '/avatars/doctors/hoangminhduc.jpg'
),
(
    5, 'Đặng Thu Hà', 'Đột quỵ não', 'Tiến sỹ', 14, 
    NULL
),
(
    6, 'Nguyễn Phương Thảo', 'Sản khoa', 'Thạc sỹ', 10, 
    '/avatars/doctors/nguyenphuongthao.jpg'
),
(
    7, 'Bùi Văn Nam', 'Nhi Sơ sinh', 'Bác sỹ', 7, 
    '/avatars/doctors/buivannam.jpg'
),
(
    10, 'Trịnh Ngọc Tuấn', 'Hồi sức cấp cứu', 'Thạc sỹ', 9, 
    '/avatars/doctors/trinhngoctuan.jpg'
);
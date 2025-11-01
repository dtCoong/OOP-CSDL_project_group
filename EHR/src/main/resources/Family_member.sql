-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
-- Bảng Family_Members - hồ sơ sức khỏe gia đình
CREATE TABLE Family_Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Nam', 'Nữ', 'Khác') NOT NULL,
    relationship ENUM('Bản thân', 'Con', 'Vợ/Chồng', 'Cha mẹ', 'Khác') NOT NULL,
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    insurance_number VARCHAR(50),
    phone VARCHAR(15) UNIQUE,  -- chỉ còn một số điện thoại duy nhất
    avatar_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
INSERT INTO Family_Members (user_id, name, dob, gender, relationship, blood_type, insurance_number, phone, avatar_path)
VALUES
(
    1, 'Nguyễn Văn An', '1990-05-15', 'Nam', 'Bản thân', 
    'O+', 'BH111222333', '0901234567', '/avatars/nguyenvanan.jpg'
),
(
    1, 'Trần Thị Bích', '1992-08-20', 'Nữ', 'Vợ/Chồng', 
    'A+', 'BH222333444', '0902345678', '/avatars/tranthibich.jpg'
),
(
    1, 'Nguyễn Gia Bảo', '2018-10-10', 'Nam', 'Con', 
    'O+', 'BH333444555', '0903456789', '/avatars/nguyengiabao.jpg'
),
(
    1, 'Nguyễn Văn Hùng', '1960-01-01', 'Nam', 'Cha mẹ', 
    'B+', 'BH444555666', '0904567890', '/avatars/nguyenvanhung.jpg'
),
(
    2, 'Lê Thị Mai', '1985-11-30', 'Nữ', 'Bản thân', 
    'AB+', 'BH555666777', '0911223344', '/avatars/lethimai.jpg'
),
(
    2, 'Phạm Văn Dũng', '1983-07-12', 'Nam', 'Vợ/Chồng', 
    'O-', 'BH666777888', '0912334455', '/avatars/phamvandung.jpg'
),
(
    2, 'Phạm Gia Hân', '2015-06-25', 'Nữ', 'Con', 
    'AB+', 'BH777888999', '0913445566', '/avatars/phamgiahan.jpg'
),
(
    3, 'Trần Văn Hùng', '1995-02-18', 'Nam', 'Bản thân', 
    'A-', 'BH888999000', '0922334455', '/avatars/tranvanhung.jpg'
),
(
    3, 'Hoàng Thị Lan', '1968-07-22', 'Nữ', 'Cha mẹ', 
    'B-', 'BH999000111', '0923445566', '/avatars/hoangthilan.jpg'
),
(
    1, 'Trần Văn Nam', '1995-12-12', 'Nam', 'Khác', 
    'B-', 'BH000111222', '0915667788', '/avatars/tranvannam.jpg'
);
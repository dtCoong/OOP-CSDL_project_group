-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;

-- Bệnh viện
CREATE TABLE Hospitals (
    hospital_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    hotline VARCHAR(20), 
    website VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO Hospitals (name, address, hotline, website)
VALUES
(
    'Bệnh viện Bạch Mai', 
    '78 Đường Giải Phóng, Phương Đình, Đống Đa, Hà Nội', 
    '1900 889 988', 
    'https://bachmai.gov.vn'
);
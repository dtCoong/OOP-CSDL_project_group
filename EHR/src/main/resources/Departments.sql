-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;

-- Khoa (mỗi khoa thuộc 1 bệnh viện)
CREATE TABLE Departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    hospital_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    location_details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
);
INSERT INTO Departments (hospital_id, name, description, location_details)
VALUES
(
    1, 'Khoa Tim mạch', 
    'Chuyên điều trị các bệnh lý về tim mạch, cao huyết áp, rối loạn nhịp tim.', 
    'Tòa nhà A5, Tầng 3, Bệnh viện Bạch Mai'
),
(
    1, 'Khoa Nội tổng hợp', 
    'Khám và điều trị các bệnh lý nội khoa chung như tiểu đường, hô hấp, cơ xương khớp.', 
    'Tòa nhà A1, Tầng 2, Bệnh viện Bạch Mai'
),
(
    1, 'Khoa Ngoại chấn thương', 
    'Điều trị phẫu thuật các chấn thương do tai nạn, gãy xương, chỉnh hình.', 
    'Tòa nhà A8, Tầng 1, Bệnh viện Bạch Mai'
),
(
    1, 'Khoa Tiêu hóa', 
    'Chuyên sâu về các bệnh lý dạ dày, ruột, gan, mật, tụy.', 
    'Tòa nhà A5, Tầng 4, Bệnh viện Bạch Mai'
),
(
    1, 'Khoa Thần kinh', 
    'Điều trị đột quỵ, Parkinson, Alzheimer và các bệnh lý thần kinh trung ương và ngoại biên.', 
    'Tòa nhà A3, Tầng 5, Bệnh viện Bạch Mai'
),
(
    1, 'Khoa Sản', 
    'Chăm sóc sức khỏe sinh sản, theo dõi thai kỳ, đỡ đẻ và điều trị phụ khoa.', 
    'Tòa nhà B2 (Việt Nhật), Tầng 3'
),
(
    1, 'Khoa Nhi', 
    'Chăm sóc sức khỏe toàn diện cho trẻ em và trẻ sơ sinh.', 
    'Tòa nhà C1, Tầng 2'
),
(
    1, 'Khoa Ung bướu', 
    'Điều trị xạ trị, hóa trị và chăm sóc giảm nhẹ cho bệnh nhân ung thư.', 
    'Tòa nhà A9 (Trung tâm Ung bướu), Tầng 1'
),
(
    1, 'Khoa Tai Mũi Họng', 
    'Điều trị các bệnh lý về tai, mũi, họng, xoang và thanh quản.', 
    'Tòa nhà A1, Tầng 4 (Khu Khám bệnh)'
),
(
    1, 'Khoa Cấp cứu', 
    'Tiếp nhận và xử lý các trường hợp cấp cứu 24/7.', 
    'Tòa nhà C3, Lối vào cổng chính (Đường Giải Phóng)'
);
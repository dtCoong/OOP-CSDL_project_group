
CREATE DATABASE IF NOT EXISTS personal_health_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE personal_health_management;

-- Bảng Users
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- User mẫu 
INSERT INTO Users 
    (username, password_hash, phone, full_name, is_active, last_login, created_at, updated_at)
VALUES 
    (
        '0101010101', '$2a$10$p1nQjUAqGE9Nt9NJVz9Apu7w0Oni1RHSP2AmqX6/wwyZh/Tmhlycu','01010010101', 'Nguyễn Văn An',TRUE, '2025-11-02 03:45:20',NOW(),NOW()
    );


-- Bảng Family_Members
CREATE TABLE Family_Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Nam', 'Nữ', 'Khác') NOT NULL,
    relationship ENUM('Bản thân', 'Con', 'Vợ/Chồng', 'Cha mẹ', 'Khác') NOT NULL,
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-','UNKNOWN') DEFAULT 'UNKNOWN',
    insurance_number VARCHAR(50),
    phone VARCHAR(15) ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO Family_Members 
    (user_id, name, dob, gender, relationship, blood_type, insurance_number, phone)
VALUES 
(
        1, 'Nguyễn Văn An', '2000-01-01','Nam', 'Bản thân', 'B+', '0101010101', '0101010101'
    ),
    (
        1,  'Nguyễn Văn B',  '2025-01-01', 'Nam',  'Con', 'UNKNOWN', NULL, '0101010101'
    ),
    (
        1,  'Nguyễn Văn C',  '2025-01-01',  'Nam', 'Vợ/Chồng', 'B+',NULL, '0101010101' 
    ),
    (
        1, 'Nguyễn Văn D','1953-01-01', 'Nam', 'Cha mẹ', 'UNKNOWN',  NULL,'0101010101' 
    );

-- Bệnh viện
CREATE TABLE Hospitals (
    hospital_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    hotline VARCHAR(20),
    website VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO hospitals (hospital_id, name, address, hotline, website, created_at) 
VALUES 
(1, 'Bệnh viện Đa khoa Quốc tế Vinmec Times City', '458 Minh Khai, P. Vĩnh Tuy, Q. Hai Bà Trưng, Hà Nội', '1900 232 389', 'https://www.vinmec.com', NOW()),
(2, 'Bệnh viện Bạch Mai', '78 Giải Phóng, P. Phương Mai, Q. Đống Đa, Hà Nội', '1900 6422', 'https://bachmai.gov.vn', NOW()),
(3, 'Bệnh viện Chợ Rẫy', '201B Nguyễn Chí Thanh, Phường 12, Quận 5, TP. Hồ Chí Minh', '028 3855 4137', 'https://choray.vn', NOW()),
(4, 'Bệnh viện Trung ương Quân đội 108', 'Số 1 Trần Hưng Đạo, P. Bạch Đằng, Q. Hai Bà Trưng, Hà Nội', '096 775 1616', 'https://www.benhvien108.vn', NOW()),
(5, 'Bệnh viện Hữu nghị Việt Đức', '40 Tràng Thi, P. Hàng Bông, Q. Hoàn Kiếm, Hà Nội', '1900 1902', 'https://benhvienvietduc.org', NOW());

-- Khoa (mỗi khoa thuộc 1 bệnh viện)
CREATE TABLE Departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    hospital_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    location_details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO departments (department_id, hospital_id, name, description, location_details, created_at) 
VALUES 
(101, 1, 'Khoa Tim mạch', 'Chuyên khoa điều trị các bệnh lý về tim và mạch máu.', 'Tòa nhà A, Tầng 5', NOW()),
(102, 1, 'Khoa Tiêu hóa', 'Chẩn đoán và điều trị các bệnh về đường tiêu hóa.', 'Tòa nhà A, Tầng 3', NOW()),
(103, 1, 'Khoa Nhi', 'Chăm sóc sức khỏe toàn diện cho trẻ em.', 'Tòa nhà C, Tầng 2', NOW()),
(104, 1, 'Khoa Cấp cứu', 'Tiếp nhận và xử lý các trường hợp khẩn cấp 24/7.', 'Tòa nhà A, Tầng 1', NOW()),
(105, 1, 'Khoa Ngoại tổng hợp', 'Thực hiện các ca phẫu thuật.', 'Tòa nhà B, Tầng 3', NOW()),
(106, 1, 'Khoa Phụ sản', 'Chăm sóc sức khỏe sinh sản, thai kỳ.', 'Tòa nhà C, Tầng 3', NOW()),
(107, 1, 'Khoa Tai Mũi Họng', 'Điều trị các bệnh lý về tai, mũi và họng.', 'Tòa nhà B, Tầng 1', NOW()),
(108, 1, 'Khoa Mắt (Nhãn khoa)', 'Khám và điều trị các bệnh về mắt.', 'Tòa nhà B, Tầng 1', NOW()),
(109, 1, 'Khoa Chẩn đoán hình ảnh', 'Thực hiện X-quang, CT, MRI, Siêu âm.', 'Tòa nhà A, Tầng 1', NOW()),
(110, 1, 'Khoa Nội tiết', 'Điều trị các bệnh lý rối loạn nội tiết, tiểu đường.', 'Tòa nhà A, Tầng 4', NOW());
INSERT INTO departments (department_id, hospital_id, name, description, location_details, created_at) 
VALUES 
(111, 2, 'Khoa Tim mạch', 'Chuyên khoa điều trị các bệnh lý về tim và mạch máu.', 'Khu nhà C, Tầng 5', NOW()),
(112, 2, 'Khoa Tiêu hóa', 'Chẩn đoán và điều trị các bệnh về đường tiêu hóa.', 'Khu nhà C, Tầng 3', NOW()),
(113, 2, 'Khoa Nhi', 'Chăm sóc sức khỏe toàn diện cho trẻ em.', 'Khu nhà D, Tầng 2', NOW()),
(114, 2, 'Khoa Cấp cứu (A9)', 'Tiếp nhận và xử lý các trường hợp cấp cứu.', 'Khu nhà A, Tầng 1', NOW()),
(115, 2, 'Khoa Ngoại tổng hợp', 'Thực hiện các ca phẫu thuật.', 'Khu nhà A, Tầng 4', NOW()),
(116, 2, 'Khoa Phụ sản', 'Chăm sóc sức khỏe sinh sản.', 'Khu nhà D, Tầng 3', NOW()),
(117, 2, 'Khoa Tai Mũi Họng', 'Điều trị các bệnh lý về tai, mũi và họng.', 'Khu nhà B, Tầng 2', NOW()),
(118, 2, 'Khoa Mắt', 'Khám và điều trị các bệnh về mắt.', 'Khu nhà B, Tầng 3', NOW()),
(119, 2, 'Khoa Chẩn đoán hình ảnh', 'Cung cấp dịch vụ X-quang, MRI, CT.', 'Khu nhà A, Tầng 1', NOW()),
(120, 2, 'Khoa Nội tiết & Đái tháo đường', 'Điều trị các bệnh lý rối loạn nội tiết.', 'Khu nhà C, Tầng 4', NOW());
INSERT INTO departments (department_id, hospital_id, name, description, location_details, created_at) 
VALUES 
(121, 3, 'Khoa Tim mạch can thiệp', 'Chuyên sâu về can thiệp mạch vành, mạch máu.', 'Khu C, Tầng 2', NOW()),
(122, 3, 'Khoa Tiêu hóa', 'Điều trị bệnh lý gan, mật, tụy, dạ dày, ruột.', 'Khu B, Tầng 6', NOW()),
(123, 3, 'Khoa Nhi', 'Chăm sóc sức khỏe cho bệnh nhi.', 'Khu D, Tầng 1', NOW()),
(124, 3, 'Khoa Cấp cứu', 'Tiếp nhận và xử lý các trường hợp cấp cứu.', 'Khu A, Tầng 1', NOW()),
(125, 3, 'Khoa Ngoại Chấn thương chỉnh hình', 'Điều trị các chấn thương xương khớp.', 'Khu D, Tầng 4', NOW()),
(126, 3, 'Khoa Phụ sản', 'Khám và điều trị các bệnh lý phụ khoa, sản khoa.', 'Khu D, Tầng 2', NOW()),
(127, 3, 'Khoa Tai Mũi Họng', 'Phẫu thuật và điều trị tai, mũi, họng.', 'Khu E, Tầng 3', NOW()),
(128, 3, 'Khoa Mắt', 'Điều trị các bệnh lý về mắt.', 'Khu E, Tầng 4', NOW()),
(129, 3, 'Khoa Chẩn đoán hình ảnh', 'Thực hiện siêu âm, X-quang, CT, MRI.', 'Khu A, Tầng 2', NOW()),
(130, 3, 'Khoa Nội tiết', 'Điều trị các bệnh lý nội tiết.', 'Khu B, Tầng 7', NOW());
INSERT INTO departments (department_id, hospital_id, name, description, location_details, created_at) 
VALUES 
(131, 4, 'Khoa Tim mạch', 'Điều trị các bệnh lý về tim và mạch máu.', 'Tòa nhà Trung tâm, Tầng 8', NOW()),
(132, 4, 'Khoa Tiêu hóa', 'Điều trị các bệnh về đường tiêu hóa, gan, mật.', 'Tòa nhà Trung tâm, Tầng 9', NOW()),
(133, 4, 'Khoa Nhi', 'Chăm sóc sức khỏe cho trẻ em.', 'Tòa nhà Trung tâm, Tầng 6', NOW()),
(134, 4, 'Khoa Cấp cứu', 'Tiếp nhận và xử lý các trường hợp khẩn cấp.', 'Tòa nhà Trung tâm, Tầng 1', NOW()),
(135, 4, 'Khoa Ngoại Chấn thương', 'Phẫu thuật và điều trị chấn thương.', 'Tòa nhà Phẫu thuật, Tầng 3', NOW()),
(136, 4, 'Khoa Phụ sản', 'Chăm sóc sức khỏe sinh sản.', 'Tòa nhà Trung tâm, Tầng 7', NOW()),
(137, 4, 'Khoa Tai Mũi Họng', 'Điều trị các bệnh lý về tai, mũi và họng.', 'Khu nhà khám bệnh, Tầng 2', NOW()),
(138, 4, 'Khoa Mắt (Nhãn khoa)', 'Khám và điều trị các bệnh về mắt.', 'Khu nhà khám bệnh, Tầng 3', NOW()),
(139, 4, 'Khoa Chẩn đoán hình ảnh', 'Thực hiện X-quang, CT, MRI, Siêu âm.', 'Tòa nhà Trung tâm, Tầng 2', NOW()),
(140, 4, 'Khoa Nội tiết', 'Điều trị các bệnh lý nội tiết.', 'Tòa nhà Trung tâm, Tầng 5', NOW());
INSERT INTO departments (department_id, hospital_id, name, description, location_details, created_at) 
VALUES 
(141, 5, 'Khoa Tim mạch và Lồng ngực', 'Phẫu thuật tim, lồng ngực, mạch máu.', 'Tòa nhà C, Tầng 3', NOW()),
(142, 5, 'Khoa Tiêu hóa', 'Phẫu thuật các bệnh lý dạ dày, gan, mật.', 'Tòa nhà C, Tầng 1', NOW()),
(143, 5, 'Khoa Nhi', 'Phẫu thuật nhi khoa.', 'Tòa nhà D, Tầng 2', NOW()),
(144, 5, 'Khoa Cấp cứu', 'Tiếp nhận và xử lý các trường hợp khẩn cấp.', 'Tòa nhà A, Tầng 1', NOW()),
(145, 5, 'Khoa Phẫu thuật Thần kinh', 'Phẫu thuật các bệnh lý não và cột sống.', 'Tòa nhà B, Tầng 3', NOW()),
(146, 5, 'Khoa Phẫu thuật Chấn thương chung', 'Điều trị các chấn thương xương khớp.', 'Tòa nhà B, Tầng 1', NOW()),
(147, 5, 'Khoa Tai Mũi Họng', 'Điều trị các bệnh lý về tai, mũi và họng.', 'Tòa nhà B, Tầng 1', NOW()),
(148, 5, 'Khoa Mắt', 'Khám và điều trị các bệnh về mắt.', 'Tòa nhà A, Tầng 3', NOW()),
(149, 5, 'Khoa Chẩn đoán hình ảnh', 'Thực hiện X-quang, CT, MRI, Siêu âm.', 'Tòa nhà A, Tầng 1', NOW()),
(150, 5, 'Khoa Nội tổng hợp (Điều trị)', 'Điều trị nội khoa hỗ trợ phẫu thuật.', 'Tòa nhà A, Tầng 2', NOW());


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO Hospitals (name, address, hotline, website)
VALUES
('Bệnh viện Đa khoa Quốc tế Vinmec', '458 Minh Khai, Hai Bà Trưng, Hà Nội', '1900 232 389', 'https://vinmec.com'),
('Bệnh viện Chợ Rẫy', '201B Nguyễn Chí Thanh, Phường 12, Quận 5, TP.HCM', '028 3855 4137', 'https://choray.vn'),
('Bệnh viện Bạch Mai', '78 Giải Phóng, Đống Đa, Hà Nội', '1900 575 758', 'https://bachmai.gov.vn');


INSERT INTO Departments (hospital_id, name, description, location_details)
VALUES
(1, 'Khoa Tim Mạch', 'Chuyên điều trị các bệnh lý tim mạch.', 'Tòa nhà A, Tầng 3'),
(1, 'Khoa Nhi', 'Chăm sóc sức khỏe toàn diện cho trẻ em.', 'Tòa nhà C, Tầng 2'),
(1, 'Khoa Tiêu hóa', 'Chuyên khám và điều trị bệnh lý tiêu hóa.', 'Tòa nhà A, Tầng 4');
-- hospital_id = 2 (Chợ Rẫy)
INSERT INTO Departments (hospital_id, name, description, location_details)
VALUES
(2, 'Khoa Ngoại Thần kinh', 'Phẫu thuật và điều trị các bệnh lý thần kinh sọ não.', 'Khu D, Tầng 5'),
(2, 'Khoa Chấn thương Chỉnh hình', 'Điều trị gãy xương và các chấn thương cơ xương khớp.', 'Khu E, Tầng 1');
-- hospital_id = 3 (Bạch Mai)
INSERT INTO Departments (hospital_id, name, description, location_details)
VALUES
(3, 'Khoa Hô hấp', 'Điều trị các bệnh lý về phổi và đường hô hấp.', 'Tòa nhà Việt Nhật, Tầng 6');

-- Bảng Medical_History - tiền sử bệnh
CREATE TABLE Medical_History (
    history_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    condition_name VARCHAR(200) NOT NULL,
    diagnosis_date DATE,
    is_chronic BOOLEAN DEFAULT FALSE,
    severity ENUM('Nhẹ', 'Trung bình', 'Nặng') DEFAULT 'Nhẹ',
    status ENUM('Đang điều trị', 'Đã khỏi', 'Kiểm soát được', 'Trầm trọng') DEFAULT 'Đang điều trị',
    notes TEXT,
    hospital_admission_date DATE NULL,      
    hospital_discharge_date DATE NULL,      
    hospital_name VARCHAR(200) NULL,      
    hospital_address VARCHAR(300) NULL, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Allergies - dị ứng
CREATE TABLE Allergies (
    allergy_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    allergen VARCHAR(200) NOT NULL,
    allergy_type ENUM('Thuốc', 'Thức ăn', 'Môi trường', 'Khác') NOT NULL,
    severity ENUM('Nhẹ', 'Trung bình', 'Nặng', 'Nguy hiểm') DEFAULT 'Nhẹ',
    symptoms TEXT,
    discovered_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Appointments
CREATE TABLE Appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    doctor_id INT,
    hospital_id INT,
    department_id INT,
    appointment_date DATETIME NOT NULL,
    type ENUM('Khám tổng quát', 'Tái khám', 'Khẩn cấp') DEFAULT 'Khám tổng quát',
    status ENUM('Đã đặt', 'Hoàn thành', 'Hủy bỏ', 'Không đến') DEFAULT 'Đã đặt',
    chief_complaint TEXT,
    diagnosis TEXT,
    treatment_notes TEXT,
    follow_up_date DATE,
    cost DECIMAL(10,2),
    payment_status ENUM('Chưa thanh toán', 'Đã thanh toán', 'Bảo hiểm') DEFAULT 'Chưa thanh toán',
    room_number VARCHAR(20),
    queue_number INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id),
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id),
    FOREIGN KEY (department_id) REFERENCES Departments(department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

ALTER TABLE Appointments ADD COLUMN appointment_slot VARCHAR(50) NULL AFTER appointment_date;
-- Bảng Medications - danh mục thuốc
CREATE TABLE Medications (
    medication_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    generic_name VARCHAR(150),
    description TEXT,
    side_effects TEXT,
    contraindications TEXT,
    interactions TEXT,
    manufacturer VARCHAR(100),
    unit ENUM('Viên', 'Ml', 'Mg', 'Gói', 'Chai', 'Tuýp', 'Ống tiêm') DEFAULT 'Viên',
    requires_prescription BOOLEAN DEFAULT TRUE,
    barcode VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Prescriptions - đơn thuốc
CREATE TABLE Prescriptions (
    prescription_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    appointment_id INT,
    doctor_id INT,
    prescription_date DATE NOT NULL,
    diagnosis TEXT,
    notes TEXT,
    total_cost DECIMAL(10,2),
    status ENUM('Đang sử dụng', 'Đã hoàn thành', 'Đã dừng', 'Hết hạn') DEFAULT 'Đang sử dụng',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Prescription_Details - chi tiết đơn thuốc
CREATE TABLE Prescription_Details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    prescription_id INT NOT NULL,
    medication_id INT NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration_days INT NOT NULL,
    total_quantity INT NOT NULL,
    instructions TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('Chưa bắt đầu', 'Đang dùng', 'Đã hoàn thành', 'Đã dừng') DEFAULT 'Chưa bắt đầu',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prescription_id) REFERENCES Prescriptions(prescription_id) ON DELETE CASCADE,
    FOREIGN KEY (medication_id) REFERENCES Medications(medication_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Medication_Schedule - lịch uống thuốc
CREATE TABLE Medication_Schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    detail_id INT NOT NULL,
    scheduled_time TIME NOT NULL,
    daily_dosage VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detail_id) REFERENCES Prescription_Details(detail_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng Documents - tài liệu y tế
CREATE TABLE Documents (
    document_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    appointment_id INT,
    document_type ENUM('Kết quả xét nghiệm', 'Chẩn đoán hình ảnh', 'Đơn thuốc', 'Bệnh án', 'Khác') NOT NULL,
    title VARCHAR(200) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    description TEXT,
    document_date DATE,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
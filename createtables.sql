-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;

-- Bảng Users - chỉ có bệnh nhân
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- Bệnh viện
CREATE TABLE Hospitals (
    hospital_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(300),
    hotline VARCHAR(20), 
    website VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Khoa (mỗi khoa thuộc 1 bệnh viện)
CREATE TABLE Departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    hospital_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id) ON DELETE CASCADE
);

-- Bác sỹ (mỗi bác sỹ thuộc 1 khoa)
CREATE TABLE Doctors (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    department_id INT NOT NULL,  -- liên kết khoa
    doctor_code VARCHAR(20) UNIQUE NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    degree ENUM('Bác sỹ', 'Thạc sỹ', 'Tiến sỹ', 'Giáo sư') DEFAULT 'Bác sỹ',
    experience_years INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE CASCADE
);

-- Bảng Patient_Access_Permissions
CREATE TABLE Patient_Access_Permissions (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_user_id INT NOT NULL,
    hospital_id INT NOT NULL,
    access_type ENUM('Đọc', 'Đọc-Ghi') DEFAULT 'Đọc',
    granted_date DATETIME NOT NULL,
    expires_date DATETIME,
    granted_by INT,
    reason TEXT,
    status ENUM('Hoạt động', 'Hết hạn', 'Thu hồi') DEFAULT 'Hoạt động',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_user_id) REFERENCES Users(user_id),
    FOREIGN KEY (hospital_id) REFERENCES Hospitals(hospital_id),
    FOREIGN KEY (granted_by) REFERENCES Users(user_id)
);

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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE
);

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
);

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
);

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
);

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
);

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
);

-- Bảng Medication_Schedule - lịch uống thuốc
CREATE TABLE Medication_Schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    detail_id INT NOT NULL,
    scheduled_time TIME NOT NULL,
    daily_dosage VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detail_id) REFERENCES Prescription_Details(detail_id) ON DELETE CASCADE
);

-- Bảng Medication_Logs - log uống thuốc
CREATE TABLE Medication_Logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    detail_id INT NOT NULL,
    scheduled_date DATE NOT NULL,
    scheduled_time TIME NOT NULL,
    actual_time DATETIME,
    status ENUM('Chưa uống', 'Đã uống', 'Bỏ lỡ', 'Uống muộn') DEFAULT 'Chưa uống',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detail_id) REFERENCES Prescription_Details(detail_id) ON DELETE CASCADE
);

-- Bảng Vaccine_Templates - lịch tiêm chủng mẫu
CREATE TABLE Vaccine_Templates (
    template_id INT PRIMARY KEY AUTO_INCREMENT,
    vaccine_name VARCHAR(100) NOT NULL,
    description TEXT,
    age_from_days INT NOT NULL,
    age_to_days INT,
    interval_days INT,
    total_doses INT DEFAULT 1,
    is_mandatory BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bảng Vaccination_Records - hồ sơ tiêm chủng
CREATE TABLE Vaccination_Records (
    vaccination_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    template_id INT,
    vaccine_name VARCHAR(100) NOT NULL,
    dose_number INT DEFAULT 1,
    vaccination_date DATE,
    next_due_date DATE,
    batch_number VARCHAR(50),
    clinic_location VARCHAR(200),
    administered_by VARCHAR(100),
    status ENUM('Đã tiêm', 'Chưa tiêm', 'Quá hạn', 'Hoãn', 'Từ chối') DEFAULT 'Chưa tiêm',
    side_effects TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES Vaccine_Templates(template_id)
);

-- Bảng Documents - tài liệu y tế
CREATE TABLE Documents (
    document_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    appointment_id INT,
    document_type ENUM('Kết quả xét nghiệm', 'Chẩn đoán hình ảnh', 'Đơn thuốc', 'Bệnh án', 'Khác') NOT NULL,
    title VARCHAR(200) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size INT,
    mime_type VARCHAR(100),
    description TEXT,
    document_date DATE,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES Family_Members(member_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id)
);
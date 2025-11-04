-- *** 5 CUỘC HẸN CHO Member 1: Nguyễn Văn An (ID 1) ***
-- (Người lớn, có tiền sử bệnh Tăng huyết áp, Tiểu đường)

-- 1. Tái khám Tim mạch (Tăng huyết áp)
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, follow_up_date, cost, payment_status, room_number, queue_number)
VALUES
    (
        1, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 101 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Nguyễn Văn Hùng) tại Khoa 101
        1, -- hospital_id (Vinmec)
        101, -- department_id (Khoa Tim mạch - Vinmec)
        '2025-11-10 09:00:00', -- appointment_date
        'Tái khám', -- type
        'Đã đặt', -- status
        'Tái khám tăng huyết áp.', -- chief_complaint
        NULL, -- diagnosis
        NULL, -- treatment_notes
        NULL, -- follow_up_date
        NULL, -- cost
        'Chưa thanh toán', -- payment_status
        'A502', -- room_number
        10 -- queue_number
    );

-- 2. Tái khám Nội tiết (Tiểu đường) - Đã hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, follow_up_date, cost, payment_status, room_number, queue_number)
VALUES
    (
        1, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 140 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Nguyễn Thị Phương) tại Khoa 140
        4, -- hospital_id (BV 108)
        140, -- department_id (Khoa Nội tiết - BV 108)
        '2025-10-15 10:30:00', -- appointment_date
        'Tái khám', -- type
        'Hoàn thành', -- status
        'Tái khám tiểu đường Type 2.', -- chief_complaint
        'Tiểu đường Type 2 (Kiểm soát tốt)', -- diagnosis
        'Tiếp tục dùng Metformin 1000mg/ngày. Hẹn 3 tháng tái khám.', -- treatment_notes
        '2026-01-15', -- follow_up_date
        350000.00, -- cost
        'Đã thanh toán', -- payment_status
        'T5.P501', -- room_number
        25 -- queue_number
    );

-- 3. Khám Tiêu hóa (Viêm loét dạ dày) - Đã hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, follow_up_date, cost, payment_status, room_number, queue_number)
VALUES
    (
        1, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 122 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Hồ Tấn Phát) tại Khoa 122
        3, -- hospital_id (Chợ Rẫy)
        122, -- department_id (Khoa Tiêu hóa - Chợ Rẫy)
        '2025-09-05 08:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hoàn thành', -- status
        'Đau thượng vị, ợ chua.', -- chief_complaint
        'Viêm dạ dày mạn tính (HP âm tính)', -- diagnosis
        'Kê đơn thuốc giảm tiết acid (Omeprazol) 4 tuần.', -- treatment_notes
        NULL, -- follow_up_date
        500000.00, -- cost
        'Bảo hiểm', -- payment_status
        'B6.P10', -- room_number
        5 -- queue_number
    );

-- 4. Khám Mắt (Kiểm tra) - Hủy bỏ
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number)
VALUES
    (
        1, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 108 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Bùi Thị Lan) tại Khoa 108
        1, -- hospital_id (Vinmec)
        108, -- department_id (Khoa Mắt - Vinmec)
        '2025-11-05 14:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hủy bỏ', -- status
        'Mắt mờ, kiểm tra định kỳ do tiểu đường.', -- chief_complaint
        'B1.P112' -- room_number
    );

-- 5. Khám Tai Mũi Họng (Viêm họng) - Đã đặt
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number, queue_number)
VALUES
    (
        1, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 117 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Phạm Thị Bích Đào) tại Khoa 117
        2, -- hospital_id (Bạch Mai)
        117, -- department_id (Khoa TMH - Bạch Mai)
        '2025-11-20 16:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Đã đặt', -- status
        'Ho, đau rát họng 3 ngày.', -- chief_complaint
        'B2.P201', -- room_number
        40
    );

-- *** 5 CUỘC HẸN CHO Member 2: Nguyễn Văn B (ID 2) ***
-- (Trẻ em, sinh 2025)

-- 6. Khám Nhi (Sốt) - Hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, follow_up_date, cost, payment_status, room_number, queue_number)
VALUES
    (
        2, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Phan Thị Thanh) tại Khoa 103
        1, -- hospital_id (Vinmec)
        103, -- department_id (Khoa Nhi - Vinmec)
        '2025-10-28 09:30:00', -- appointment_date
        'Khẩn cấp', -- type
        'Hoàn thành', -- status
        'Bé sốt cao 39 độ, quấy khóc.', -- chief_complaint
        'Sốt virus', -- diagnosis
        'Hạ sốt (Hapacol), bù dịch (Oresol). Theo dõi thêm.', -- treatment_notes
        '2025-10-30', -- follow_up_date
        450000.00, -- cost
        'Đã thanh toán', -- payment_status
        'C2.P10', -- room_number
        3
    );

-- 7. Tái khám Nhi (Sau sốt) - Đã đặt
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number, queue_number)
VALUES
    (
        2, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Phan Thị Thanh) tại Khoa 103
        1, -- hospital_id (Vinmec)
        103, -- department_id (Khoa Nhi - Vinmec)
        '2025-10-30 10:00:00', -- appointment_date
        'Tái khám', -- type
        'Đã đặt', -- status
        'Tái khám sau 2 ngày sốt virus.', -- chief_complaint
        'C2.P10', -- room_number
        15
    );

-- 8. Khám Nhi (Tiêm phòng 6in1) - Hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, cost, payment_status, room_number)
VALUES
    (
        2, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 113 LIMIT 1 OFFSET 1), -- Bác sĩ 2 (Lê Thị Hồng Hạnh) tại Khoa 113
        2, -- hospital_id (Bạch Mai)
        113, -- department_id (Khoa Nhi - Bạch Mai)
        '2025-08-01 14:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hoàn thành', -- status
        'Tiêm vắc xin 6 trong 1 (Mũi 3).', -- chief_complaint
        'Trẻ khỏe mạnh', -- diagnosis
        'Đã tiêm Hexaxim. Theo dõi 30 phút sau tiêm.', -- treatment_notes
        1050000.00, -- cost
        'Đã thanh toán', -- payment_status
        'D2.P205' -- room_number
    );

-- 9. Khám Nhi (Tư vấn dinh dưỡng) - Đã đặt
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number, queue_number)
VALUES
    (
        2, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 133 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Nguyễn Thị Hảo) tại Khoa 133
        4, -- hospital_id (BV 108)
        133, -- department_id (Khoa Nhi - BV 108)
        '2025-11-15 11:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Đã đặt', -- status
        'Bé chậm tăng cân.', -- chief_complaint
        'T6.P602', -- room_number
        12
    );

-- 10. Khám Nhi (Viêm da) - Không đến
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint)
VALUES
    (
        2, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 103 LIMIT 1 OFFSET 2), -- Bác sĩ 3 (Võ Thị Kim Liên) tại Khoa 103
        1, -- hospital_id (Vinmec)
        103, -- department_id (Khoa Nhi - Vinmec)
        '2025-09-20 15:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Không đến', -- status
        'Bé nổi mẩn đỏ ở tay.' -- chief_complaint
    );

-- *** 5 CUỘC HẸN CHO Member 3: Nguyễn Văn C (ID 3) ***
-- (Người lớn, Vợ/Chồng)

-- 11. Khám Phụ sản (Khám định kỳ) - Hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, cost, payment_status, room_number, queue_number)
VALUES
    (
        3, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 106 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Trần Thị Phương) tại Khoa 106
        1, -- hospital_id (Vinmec)
        106, -- department_id (Khoa Phụ sản - Vinmec)
        '2025-10-01 10:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hoàn thành', -- status
        'Khám phụ khoa định kỳ.', -- chief_complaint
        'Viêm lộ tuyến cổ tử cung (Nhẹ)', -- diagnosis
        'Kê đơn thuốc đặt tại chỗ. Hẹn 6 tháng tái khám.', -- treatment_notes
        600000.00, -- cost
        'Đã thanh toán', -- payment_status
        'C3.P301', -- room_number
        8
    );

-- 12. Tái khám Phụ sản - Đã đặt
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number, queue_number)
VALUES
    (
        3, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 106 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Trần Thị Phương) tại Khoa 106
        1, -- hospital_id (Vinmec)
        106, -- department_id (Khoa Phụ sản - Vinmec)
        '2026-04-01 10:00:00', -- appointment_date
        'Tái khám', -- type
        'Đã đặt', -- status
        'Tái khám phụ khoa định kỳ (6 tháng).', -- chief_complaint
        'C3.P301', -- room_number
        11
    );

-- 13. Khám Tiêu hóa (Đau bụng) - Hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, cost, payment_status, room_number, queue_number)
VALUES
    (
        3, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 112 LIMIT 1 OFFSET 1), -- Bác sĩ 2 (Vũ Trường Khanh) tại Khoa 112
        2, -- hospital_id (Bạch Mai)
        112, -- department_id (Khoa Tiêu hóa - Bạch Mai)
        '2025-06-15 11:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hoàn thành', -- status
        'Đau bụng âm ỉ vùng hạ sườn phải.', -- chief_complaint
        'Rối loạn tiêu hóa (Nghi ngờ Sỏi túi mật)', -- diagnosis
        'Cho làm siêu âm ổ bụng. Kê đơn giảm đau. Hẹn tái khám khi có kết quả.', -- treatment_notes
        400000.00, -- cost
        'Bảo hiểm', -- payment_status
        'C3.P305', -- room_number
        33
    );

-- 14. Khám Mắt (Kiểm tra thị lực) - Đã đặt
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, room_number, queue_number)
VALUES
    (
        3, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 138 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Phạm Văn Hùng) tại Khoa 138
        4, -- hospital_id (BV 108)
        138, -- department_id (Khoa Mắt - BV 108)
        '2025-11-25 14:30:00', -- appointment_date
        'Khám tổng quát', -- type
        'Đã đặt', -- status
        'Mắt nhìn mờ dạo gần đây, đo lại kính.', -- chief_complaint
        'KKB.P302', -- room_number
        18
    );

-- 15. Khám Nội tiết (Tầm soát tuyến giáp) - Hoàn thành
INSERT INTO Appointments 
    (member_id, doctor_id, hospital_id, department_id, appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, cost, payment_status, room_number, queue_number)
VALUES
    (
        3, -- member_id
        (SELECT doctor_id FROM Doctors WHERE department_id = 110 LIMIT 1 OFFSET 0), -- Bác sĩ 1 (Vũ Bích Nga) tại Khoa 110
        1, -- hospital_id (Vinmec)
        110, -- department_id (Khoa Nội tiết - Vinmec)
        '2025-05-10 09:00:00', -- appointment_date
        'Khám tổng quát', -- type
        'Hoàn thành', -- status
        'Tầm soát tuyến giáp (Gia đình có tiền sử).', -- chief_complaint
        'Bình thường', -- diagnosis
        'Các chỉ số TSH, FT4, FT3 trong giới hạn bình thường. Siêu âm không thấy nhân giáp.', -- treatment_notes
        850000.00, -- cost
        'Đã thanh toán', -- payment_status
        'A4.P401', -- room_number
        7
    );

-- *** 5 CUỘC HẸN CHO Member 4: Nguyễn Văn D (ID 4) ***
-- (Người lớn tuổi, sinh 1953, Cha mẹ)
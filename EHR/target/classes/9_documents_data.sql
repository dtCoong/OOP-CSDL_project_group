-- Insert data for Documents table
-- Linking to existing family members (member_id: 1, 2, 3, 4)

USE personal_health_management;

-- Documents for member_id = 1 (Nguyễn Văn An - Bản thân)
INSERT INTO Documents 
    (member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at)
VALUES 
    (1, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm máu tổng quát tháng 10/2025', '/documents/member_1/xet_nghiem_mau_202510.pdf', 'Kết quả xét nghiệm máu định kỳ, các chỉ số bình thường', '2025-10-15', '2025-10-16 09:30:00'),
    
    (1, NULL, 'Chẩn đoán hình ảnh', 'X-quang phổi', '/documents/member_1/xquang_phoi_202509.pdf', 'Chụp X-quang phổi kiểm tra sức khỏe định kỳ', '2025-09-20', '2025-09-21 14:15:00'),
    
    (1, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm chức năng gan', '/documents/member_1/xet_nghiem_gan_202508.pdf', 'Kiểm tra chức năng gan, kết quả GOT, GPT bình thường', '2025-08-10', '2025-08-11 10:00:00'),
    
    (1, NULL, 'Chẩn đoán hình ảnh', 'Siêu âm bụng tổng quát', '/documents/member_1/sieu_am_bung_202507.pdf', 'Siêu âm gan, mật, lách, tụy - không phát hiện bất thường', '2025-07-05', '2025-07-06 16:20:00'),
    
    (1, NULL, 'Đơn thuốc', 'Đơn thuốc điều trị viêm họng', '/documents/member_1/don_thuoc_viem_hong_202506.pdf', 'Đơn thuốc kháng sinh và thuốc ho cho đợt viêm họng cấp', '2025-06-12', '2025-06-12 11:30:00'),
    
    (1, NULL, 'Bệnh án', 'Bệnh án nội trú - Viêm phổi', '/documents/member_1/benh_an_viem_phoi_202405.pdf', 'Nhập viện điều trị viêm phổi từ 15/05 đến 20/05/2025', '2025-05-15', '2025-05-21 08:00:00'),
    
    (1, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm đường huyết', '/documents/member_1/xet_nghiem_duong_huyet_202504.pdf', 'Xét nghiệm glucose máu lúc đói, kết quả 95 mg/dL', '2025-04-18', '2025-04-19 09:45:00'),
    
    (1, NULL, 'Khác', 'Giấy chứng nhận tiêm vaccine COVID-19', '/documents/member_1/vaccine_covid_2024.pdf', 'Chứng nhận hoàn thành 3 mũi vaccine COVID-19', '2024-12-10', '2024-12-11 13:00:00');

-- Documents for member_id = 2 (Nguyễn Văn B - Con)
INSERT INTO Documents 
    (member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at)
VALUES 
    (2, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm sàng lọc sơ sinh', '/documents/member_2/xet_nghiem_so_sinh_202501.pdf', 'Xét nghiệm sàng lọc bệnh bẩm sinh cho trẻ sơ sinh', '2025-01-03', '2025-01-04 10:00:00'),
    
    (2, NULL, 'Khác', 'Sổ khám sức khỏe trẻ em', '/documents/member_2/so_kham_sk_tre_em_202501.pdf', 'Sổ theo dõi sức khỏe và tiêm chủng cho trẻ', '2025-01-02', '2025-01-05 14:30:00'),
    
    (2, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm máu tháng 3/2025', '/documents/member_2/xet_nghiem_mau_202503.pdf', 'Xét nghiệm máu định kỳ 3 tháng tuổi', '2025-03-15', '2025-03-16 09:00:00'),
    
    (2, NULL, 'Chẩn đoán hình ảnh', 'Siêu âm não sơ sinh', '/documents/member_2/sieu_am_nao_202501.pdf', 'Siêu âm não kiểm tra phát triển não bộ', '2025-01-10', '2025-01-11 11:20:00'),
    
    (2, NULL, 'Khác', 'Sổ tiêm chủng mở rộng', '/documents/member_2/so_tiem_chung_202501.pdf', 'Sổ theo dõi các mũi tiêm chủng mở rộng', '2025-01-15', '2025-01-16 08:30:00'),
    
    (2, NULL, 'Đơn thuốc', 'Đơn thuốc điều trị viêm amidan', '/documents/member_2/don_thuoc_viem_amidan_202510.pdf', 'Thuốc kháng sinh và hạ sốt cho bé', '2025-10-20', '2025-10-20 15:45:00');

-- Documents for member_id = 3 (Nguyễn Văn C - Vợ/Chồng)
INSERT INTO Documents 
    (member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at)
VALUES 
    (3, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm sức khỏe tiền hôn nhân', '/documents/member_3/xet_nghiem_tien_hon_nhan_202412.pdf', 'Xét nghiệm sức khỏe toàn diện trước khi kết hôn', '2024-12-15', '2024-12-16 10:30:00'),
    
    (3, NULL, 'Chẩn đoán hình ảnh', 'X-quang ngực', '/documents/member_3/xquang_nguc_202409.pdf', 'Chụp X-quang phổi kiểm tra sức khỏe', '2024-09-10', '2024-09-11 14:00:00'),
    
    (3, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm hormone tuyến giáp', '/documents/member_3/xet_nghiem_tuyen_giap_202508.pdf', 'Kiểm tra chức năng tuyến giáp - TSH, T3, T4', '2025-08-25', '2025-08-26 09:15:00'),
    
    (3, NULL, 'Chẩn đoán hình ảnh', 'Siêu âm vú', '/documents/member_3/sieu_am_vu_202507.pdf', 'Siêu âm tuyến vú kiểm tra định kỳ', '2025-07-12', '2025-07-13 16:00:00'),
    
    (3, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm máu tổng quát', '/documents/member_3/xet_nghiem_mau_202506.pdf', 'Xét nghiệm máu toàn bộ, công thức máu', '2025-06-05', '2025-06-06 10:45:00'),
    
    (3, NULL, 'Đơn thuốc', 'Đơn thuốc bổ sung vitamin', '/documents/member_3/don_thuoc_vitamin_202505.pdf', 'Vitamin D3, Calcium bổ sung cho sức khỏe xương', '2025-05-20', '2025-05-21 11:00:00'),
    
    (3, NULL, 'Khác', 'Chứng nhận tiêm vaccine phòng ngừa', '/documents/member_3/vaccine_phong_ngua_202503.pdf', 'Chứng nhận tiêm vaccine HPV và viêm gan B', '2025-03-18', '2025-03-19 13:30:00');

-- Documents for member_id = 4 (Nguyễn Văn D - Cha mẹ)
INSERT INTO Documents 
    (member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at)
VALUES 
    (4, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm đường huyết và lipid máu', '/documents/member_4/xet_nghiem_duong_huyet_lipid_202510.pdf', 'Kiểm tra đường huyết và mỡ máu - phát hiện tiểu đường type 2', '2025-10-08', '2025-10-09 08:30:00'),
    
    (4, NULL, 'Bệnh án', 'Bệnh án điều trị đái tháo đường', '/documents/member_4/benh_an_dai_thao_duong_202509.pdf', 'Bệnh án nội trú điều trị đái tháo đường mới phát hiện', '2025-09-15', '2025-09-22 10:00:00'),
    
    (4, NULL, 'Đơn thuốc', 'Đơn thuốc điều trị đái tháo đường', '/documents/member_4/don_thuoc_dai_thao_duong_202509.pdf', 'Metformin 500mg, Glimepiride 2mg - kiểm soát đường huyết', '2025-09-18', '2025-09-18 14:30:00'),
    
    (4, NULL, 'Chẩn đoán hình ảnh', 'Điện tâm đồ (ECG)', '/documents/member_4/dien_tam_do_202508.pdf', 'Kiểm tra tim mạch - phát hiện rối loạn nhịp nhẹ', '2025-08-22', '2025-08-23 09:00:00'),
    
    (4, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm chức năng thận', '/documents/member_4/xet_nghiem_than_202507.pdf', 'Kiểm tra Creatinine, Urea - chức năng thận giảm nhẹ', '2025-07-30', '2025-07-31 11:15:00'),
    
    (4, NULL, 'Chẩn đoán hình ảnh', 'Siêu âm Doppler động mạch cảnh', '/documents/member_4/sieu_am_doppler_202506.pdf', 'Kiểm tra mạch máu não - phát hiện xơ vữa nhẹ', '2025-06-18', '2025-06-19 15:30:00'),
    
    (4, NULL, 'Đơn thuốc', 'Đơn thuốc điều trị tăng huyết áp', '/documents/member_4/don_thuoc_tang_huyet_ap_202505.pdf', 'Amlodipine 5mg, Losartan 50mg - kiểm soát huyết áp', '2025-05-10', '2025-05-11 10:00:00'),
    
    (4, NULL, 'Kết quả xét nghiệm', 'Xét nghiệm HbA1c', '/documents/member_4/xet_nghiem_hba1c_202510.pdf', 'Theo dõi kiểm soát đường huyết 3 tháng - HbA1c: 7.2%', '2025-10-25', '2025-10-26 09:30:00'),
    
    (4, NULL, 'Chẩn đoán hình ảnh', 'X-quang cột sống thắt lưng', '/documents/member_4/xquang_cot_song_202504.pdf', 'Chụp X-quang cột sống - thoái hóa đốt sống thắt lưng', '2025-04-12', '2025-04-13 13:45:00'),
    
    (4, NULL, 'Khác', 'Sổ theo dõi đường huyết tại nhà', '/documents/member_4/so_theo_doi_duong_huyet_202510.pdf', 'Ghi chép đường huyết đo tại nhà hàng ngày', '2025-10-01', '2025-10-28 16:00:00'),
    
    (4, NULL, 'Bệnh án', 'Bệnh án điều trị viêm khớp', '/documents/member_4/benh_an_viem_khop_202503.pdf', 'Điều trị viêm khớp gối - đau nhức do thoái hóa', '2025-03-20', '2025-03-25 11:00:00');

-- Summary statistics
SELECT 
    document_type,
    COUNT(*) as total_documents
FROM Documents
GROUP BY document_type
ORDER BY total_documents DESC;

SELECT 
    m.name as member_name,
    m.relationship,
    COUNT(d.document_id) as total_documents
FROM Family_Members m
LEFT JOIN Documents d ON m.member_id = d.member_id
GROUP BY m.member_id, m.name, m.relationship
ORDER BY m.member_id;

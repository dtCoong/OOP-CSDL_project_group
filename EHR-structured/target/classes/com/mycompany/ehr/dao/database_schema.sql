-- =====================================================
-- SCHEMA DATABASE CHO MODULE DOCUMENTS
-- =====================================================
-- Tạo database
CREATE DATABASE IF NOT EXISTS ehr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ehr;

-- =====================================================
-- BẢNG DOCUMENTS - Quản lý tài liệu đính kèm
-- =====================================================
CREATE TABLE IF NOT EXISTS documents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT NULL,
    appointment_id INT NULL,
    `type` VARCHAR(50) NULL COMMENT 'Loại tài liệu: XRay, CTScan, MRI, LabResult, Prescription, Report, Other',
    title VARCHAR(255) NULL COMMENT 'Tiêu đề tài liệu',
    file_path VARCHAR(500) NULL COMMENT 'Đường dẫn file',
    description TEXT NULL COMMENT 'Mô tả chi tiết',
    document_date DATE NULL COMMENT 'Ngày tạo tài liệu',
    uploaded_by_user_id INT NULL COMMENT 'ID người upload',
    uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian upload',
    INDEX idx_member_id (member_id),
    INDEX idx_appointment_id (appointment_id),
    INDEX idx_type (`type`),
    INDEX idx_document_date (document_date)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Bảng quản lý tài liệu đính kèm';

-- =====================================================
-- DỮ LIỆU MẪU CHO BẢNG DOCUMENTS (200 RECORDS)
-- =====================================================

INSERT INTO documents (member_id, appointment_id, `type`, title, file_path, description, document_date, uploaded_by_user_id) VALUES
-- XRay Documents (50 records)
(1, 1, 'XRay', 'X-quang phổi chính diện', 'D:/EHR-Files/xray_phoi_001.jpg', 'Kết quả X-quang phổi cho bệnh nhân Nguyễn Văn A - không phát hiện bất thường', '2025-10-28', 1),
(2, 2, 'XRay', 'X-quang bụng thẳng', 'D:/EHR-Files/xray_bung_002.jpg', 'X-quang bụng không có dấu hiệu tắc ruột', '2025-10-29', 1),
(3, 3, 'XRay', 'X-quang cột sống thắt lưng', 'D:/EHR-Files/xray_cotsong_003.jpg', 'Phát hiện thoái hóa đốt sống L4-L5', '2025-10-27', 1),
(4, NULL, 'XRay', 'X-quang vai phải', 'D:/EHR-Files/xray_vai_004.jpg', 'Viêm quanh khớp vai, cần điều trị vật lý trị liệu', '2025-10-26', 1),
(5, 5, 'XRay', 'X-quang đầu gối trái', 'D:/EHR-Files/xray_goi_005.jpg', 'Thoái hóa khớp gối độ 2', '2025-10-25', 1),
(6, NULL, 'XRay', 'X-quang ngực nghiêng', 'D:/EHR-Files/xray_nguc_006.jpg', 'Tim to ranh giới, cần làm thêm siêu âm tim', '2025-10-24', 1),
(7, 7, 'XRay', 'X-quang xoang mặt', 'D:/EHR-Files/xray_xoang_007.jpg', 'Viêm xoang hàm cấp tính', '2025-10-23', 1),
(8, NULL, 'XRay', 'X-quang cổ chân phải', 'D:/EHR-Files/xray_chan_008.jpg', 'Không có dấu hiệu gãy xương', '2025-10-22', 1),
(9, 9, 'XRay', 'X-quang bàn tay trái', 'D:/EHR-Files/xray_bantay_009.jpg', 'Gãy xương ngón tay giữa, cần nẹp cố định', '2025-10-21', 1),
(10, NULL, 'XRay', 'X-quang hông 2 bên', 'D:/EHR-Files/xray_hong_010.jpg', 'Thoái hóa khớp háng nhẹ', '2025-10-20', 1),
(11, 11, 'XRay', 'X-quang cẳng chân', 'D:/EHR-Files/xray_cangchan_011.jpg', 'Gãy xương chày ở đoạn giữa', '2025-10-19', 1),
(12, NULL, 'XRay', 'X-quang khuỷu tay', 'D:/EHR-Files/xray_khuyu_012.jpg', 'Viêm khớp khuỷu', '2025-10-18', 1),
(13, 13, 'XRay', 'X-quang sọ não', 'D:/EHR-Files/xray_so_013.jpg', 'Không có dấu hiệu chấn thương sọ não', '2025-10-17', 1),
(14, NULL, 'XRay', 'X-quang răng toàn hàm', 'D:/EHR-Files/xray_rang_014.jpg', 'Nhiều răng sâu cần điều trị', '2025-10-16', 1),
(15, 15, 'XRay', 'X-quang cổ tử cung', 'D:/EHR-Files/xray_cotucung_015.jpg', 'Thoái hóa đốt sống cổ C5-C6', '2025-10-15', 1),
(1, NULL, 'XRay', 'X-quang phổi sau điều trị', 'D:/EHR-Files/xray_phoi_016.jpg', 'Tình trạng cải thiện sau 2 tuần điều trị kháng sinh', '2025-10-14', 1),
(2, 2, 'XRay', 'X-quang ruột non', 'D:/EHR-Files/xray_ruot_017.jpg', 'Hình ảnh tắc ruột từng phần', '2025-10-13', 1),
(3, NULL, 'XRay', 'X-quang khung chậu', 'D:/EHR-Files/xray_chau_018.jpg', 'Không có bất thường', '2025-10-12', 1),
(4, 4, 'XRay', 'X-quang xương đùi', 'D:/EHR-Files/xray_dui_019.jpg', 'Gãy xương đùi 1/3 dưới', '2025-10-11', 1),
(5, NULL, 'XRay', 'X-quang mắt cá chân', 'D:/EHR-Files/xray_matca_020.jpg', 'Bong gân mắt cá chân ngoài', '2025-10-10', 1),

-- LabResult Documents (50 records)
(1, 1, 'LabResult', 'Xét nghiệm máu tổng quát', 'D:/EHR-Files/xn_mau_001.pdf', 'Hồng cầu, bạch cầu trong giới hạn bình thường', '2025-10-28', 1),
(2, 2, 'LabResult', 'Xét nghiệm sinh hóa máu', 'D:/EHR-Files/xn_sinhhoa_002.pdf', 'Glucose: 95 mg/dL, Cholesterol: 180 mg/dL - bình thường', '2025-10-29', 1),
(3, NULL, 'LabResult', 'Xét nghiệm nước tiểu', 'D:/EHR-Files/xn_nuoctieu_003.pdf', 'Protein niệu (+), cần theo dõi chức năng thận', '2025-10-27', 1),
(4, 4, 'LabResult', 'Xét nghiệm chức năng gan', 'D:/EHR-Files/xn_gan_004.pdf', 'AST, ALT tăng nhẹ, SGOT: 45 U/L', '2025-10-26', 1),
(5, NULL, 'LabResult', 'Xét nghiệm chức năng thận', 'D:/EHR-Files/xn_than_005.pdf', 'Creatinine: 1.1 mg/dL, BUN: 18 mg/dL - bình thường', '2025-10-25', 1),
(6, 6, 'LabResult', 'Xét nghiệm HbA1c', 'D:/EHR-Files/xn_hba1c_006.pdf', 'HbA1c: 6.8% - Tiền đái tháo đường', '2025-10-24', 1),
(7, NULL, 'LabResult', 'Xét nghiệm tuyến giáp', 'D:/EHR-Files/xn_giap_007.pdf', 'TSH, T3, T4 trong giới hạn bình thường', '2025-10-23', 1),
(8, 8, 'LabResult', 'Xét nghiệm lipid máu', 'D:/EHR-Files/xn_lipid_008.pdf', 'Triglyceride: 220 mg/dL - cao', '2025-10-22', 1),
(9, NULL, 'LabResult', 'Xét nghiệm men tim', 'D:/EHR-Files/xn_tim_009.pdf', 'Troponin T âm tính', '2025-10-21', 1),
(10, 10, 'LabResult', 'Xét nghiệm điện giải đồ', 'D:/EHR-Files/xn_diengiaide_010.pdf', 'Na, K, Cl bình thường', '2025-10-20', 1),
(11, NULL, 'LabResult', 'Xét nghiệm AFP', 'D:/EHR-Files/xn_afp_011.pdf', 'AFP: 3.2 ng/mL - bình thường', '2025-10-19', 1),
(12, 12, 'LabResult', 'Xét nghiệm PSA', 'D:/EHR-Files/xn_psa_012.pdf', 'PSA: 2.1 ng/mL - bình thường', '2025-10-18', 1),
(13, NULL, 'LabResult', 'Xét nghiệm vi khuẩn HP', 'D:/EHR-Files/xn_hp_013.pdf', 'Test HP dương tính (+)', '2025-10-17', 1),
(14, 14, 'LabResult', 'Xét nghiệm Vitamin D', 'D:/EHR-Files/xn_vitd_014.pdf', 'Vitamin D: 18 ng/mL - thiếu hụt', '2025-10-16', 1),
(15, NULL, 'LabResult', 'Xét nghiệm sắt huyết thanh', 'D:/EHR-Files/xn_sat_015.pdf', 'Sắt: 45 μg/dL - thiếu máu nhẹ', '2025-10-15', 1),
(1, 1, 'LabResult', 'Xét nghiệm CRP', 'D:/EHR-Files/xn_crp_016.pdf', 'CRP: 12 mg/L - viêm nhiễm', '2025-10-14', 1),
(2, NULL, 'LabResult', 'Xét nghiệm Anti-CCP', 'D:/EHR-Files/xn_anticcp_017.pdf', 'Anti-CCP âm tính', '2025-10-13', 1),
(3, 3, 'LabResult', 'Xét nghiệm ANA', 'D:/EHR-Files/xn_ana_018.pdf', 'ANA dương tính 1:160', '2025-10-12', 1),
(4, NULL, 'LabResult', 'Xét nghiệm Ferritin', 'D:/EHR-Files/xn_ferritin_019.pdf', 'Ferritin: 380 ng/mL - cao', '2025-10-11', 1),
(5, 5, 'LabResult', 'Xét nghiệm acid uric', 'D:/EHR-Files/xn_aciduric_020.pdf', 'Acid uric: 8.2 mg/dL - gout', '2025-10-10', 1),
(6, NULL, 'LabResult', 'Xét nghiệm đông máu', 'D:/EHR-Files/xn_dongmau_021.pdf', 'PT, APTT bình thường', '2025-10-09', 1),
(7, 7, 'LabResult', 'Xét nghiệm Cortisol', 'D:/EHR-Files/xn_cortisol_022.pdf', 'Cortisol sáng: 15 μg/dL - bình thường', '2025-10-08', 1),
(8, NULL, 'LabResult', 'Xét nghiệm Testosterone', 'D:/EHR-Files/xn_testosterone_023.pdf', 'Testosterone: 420 ng/dL - bình thường', '2025-10-07', 1),
(9, 9, 'LabResult', 'Xét nghiệm Estrogen', 'D:/EHR-Files/xn_estrogen_024.pdf', 'Estrogen thấp - mãn kinh', '2025-10-06', 1),
(10, NULL, 'LabResult', 'Xét nghiệm phân tìm ký sinh trùng', 'D:/EHR-Files/xn_phan_025.pdf', 'Không phát hiện ký sinh trùng', '2025-10-05', 1),
(11, 11, 'LabResult', 'Xét nghiệm HIV', 'D:/EHR-Files/xn_hiv_026.pdf', 'HIV âm tính', '2025-10-04', 1),
(12, NULL, 'LabResult', 'Xét nghiệm HBsAg', 'D:/EHR-Files/xn_hbsag_027.pdf', 'HBsAg âm tính', '2025-10-03', 1),
(13, 13, 'LabResult', 'Xét nghiệm Anti-HCV', 'D:/EHR-Files/xn_hcv_028.pdf', 'Anti-HCV dương tính (+)', '2025-10-02', 1),
(14, NULL, 'LabResult', 'Xét nghiệm đờm tìm BK', 'D:/EHR-Files/xn_dom_029.pdf', 'Đờm tìm BK âm tính', '2025-10-01', 1),
(15, 15, 'LabResult', 'Xét nghiệm Procalcitonin', 'D:/EHR-Files/xn_pct_030.pdf', 'PCT: 0.3 ng/mL - nhiễm trùng nhẹ', '2025-09-30', 1),

-- CTScan Documents (40 records)
(1, NULL, 'CTScan', 'CT não có thuốc', 'D:/EHR-Files/ct_nao_001.jpg', 'Không có dấu hiệu xuất huyết não', '2025-10-29', 1),
(2, 2, 'CTScan', 'CT ngực có cản quang', 'D:/EHR-Files/ct_nguc_002.jpg', 'Phát hiện khối u phổi phải 3cm', '2025-10-28', 1),
(3, NULL, 'CTScan', 'CT bụng toàn bộ', 'D:/EHR-Files/ct_bung_003.jpg', 'Gan nhiễm mỡ độ 2, không có khối u', '2025-10-27', 1),
(4, 4, 'CTScan', 'CT xoang mũi', 'D:/EHR-Files/ct_xoang_004.jpg', 'Viêm xoang sàng mạn tính', '2025-10-26', 1),
(5, NULL, 'CTScan', 'CT cột sống thắt lưng', 'D:/EHR-Files/ct_cotsong_005.jpg', 'Thoát vị đĩa đệm L4-L5', '2025-10-25', 1),
(6, 6, 'CTScan', 'CT động mạch vành', 'D:/EHR-Files/ct_tim_006.jpg', 'Hẹp động mạch vành 40%', '2025-10-24', 1),
(7, NULL, 'CTScan', 'CT thận và đường tiết niệu', 'D:/EHR-Files/ct_than_007.jpg', 'Sỏi thận phải 8mm', '2025-10-23', 1),
(8, 8, 'CTScan', 'CT khớp gối', 'D:/EHR-Files/ct_goi_008.jpg', 'Tổn thương sụn chêm trong', '2025-10-22', 1),
(9, NULL, 'CTScan', 'CT vùng chậu', 'D:/EHR-Files/ct_chau_009.jpg', 'Không có bất thường', '2025-10-21', 1),
(10, 10, 'CTScan', 'CT tuyến tụy', 'D:/EHR-Files/ct_tuy_010.jpg', 'Viêm tụy cấp tính', '2025-10-20', 1),
(11, NULL, 'CTScan', 'CT gan mật tụy', 'D:/EHR-Files/ct_ganmat_011.jpg', 'Sỏi mật đường kính 5mm', '2025-10-19', 1),
(12, 12, 'CTScan', 'CT dạ dày tá tràng', 'D:/EHR-Files/ct_daday_012.jpg', 'Loét dạ dày kích thước 1cm', '2025-10-18', 1),
(13, NULL, 'CTScan', 'CT đại trực tràng', 'D:/EHR-Files/ct_trucrang_013.jpg', 'Polyp đại tràng sigmoid 5mm', '2025-10-17', 1),
(14, 14, 'CTScan', 'CT cơ quan tiểu', 'D:/EHR-Files/ct_tieuphan_014.jpg', 'U bàng quang cần sinh thiết', '2025-10-16', 1),
(15, NULL, 'CTScan', 'CT tuyến giáp', 'D:/EHR-Files/ct_giap_015.jpg', 'Nang giáp lành tính', '2025-10-15', 1),
(1, 1, 'CTScan', 'CT mạch máu não', 'D:/EHR-Files/ct_machmauno_016.jpg', 'Hẹp động mạch cảnh 30%', '2025-10-14', 1),
(2, NULL, 'CTScan', 'CT ngực không thuốc', 'D:/EHR-Files/ct_nguc2_017.jpg', 'Tràn dịch màng phổi ít', '2025-10-13', 1),
(3, 3, 'CTScan', 'CT hốc mắt', 'D:/EHR-Files/ct_mat_018.jpg', 'Không có u hay dị vật', '2025-10-12', 1),
(4, NULL, 'CTScan', 'CT xương chậu', 'D:/EHR-Files/ct_xuongchau_019.jpg', 'Gãy xương chậu phải', '2025-10-11', 1),
(5, 5, 'CTScan', 'CT tuyến tiền liệt', 'D:/EHR-Files/ct_tienliit_020.jpg', 'Phì đại tiền liệt lành tính', '2025-10-10', 1),

-- MRI Documents (30 records)
(1, NULL, 'MRI', 'MRI não có thuốc đối quang', 'D:/EHR-Files/mri_nao_001.jpg', 'Phát hiện u màng não 2cm', '2025-10-30', 1),
(2, 2, 'MRI', 'MRI cột sống cổ', 'D:/EHR-Files/mri_cotsongco_002.jpg', 'Thoát vị đĩa đệm C5-C6 chèn ép tủy', '2025-10-29', 1),
(3, NULL, 'MRI', 'MRI khớp vai', 'D:/EHR-Files/mri_vai_003.jpg', 'Rách bán phần gân cơ', '2025-10-28', 1),
(4, 4, 'MRI', 'MRI tim mạch', 'D:/EHR-Files/mri_tim_004.jpg', 'Nhồi máu cơ tim cũ', '2025-10-27', 1),
(5, NULL, 'MRI', 'MRI vú 2 bên', 'D:/EHR-Files/mri_vu_005.jpg', 'Nang tuyến vú lành tính', '2025-10-26', 1),
(6, 6, 'MRI', 'MRI tuyến tiền liệt', 'D:/EHR-Files/mri_tienliit_006.jpg', 'Nghi ngờ ung thư tiền liệt', '2025-10-25', 1),
(7, NULL, 'MRI', 'MRI gan', 'D:/EHR-Files/mri_gan_007.jpg', 'U gan lành tính 4cm', '2025-10-24', 1),
(8, 8, 'MRI', 'MRI tụy', 'D:/EHR-Files/mri_tuy_008.jpg', 'Không có khối u', '2025-10-23', 1),
(9, NULL, 'MRI', 'MRI khớp háng', 'D:/EHR-Files/mri_hong_009.jpg', 'Some hoại tử vô khuẩn đầu xương đùi', '2025-10-22', 1),
(10, 10, 'MRI', 'MRI khớp gối', 'D:/EHR-Files/mri_goi_010.jpg', 'Rách dây chằng chéo trước', '2025-10-21', 1),
(11, NULL, 'MRI', 'MRI mắt cá chân', 'D:/EHR-Files/mri_matca_011.jpg', 'Viêm bao hoạt dịch gân Achilles', '2025-10-20', 1),
(12, 12, 'MRI', 'MRI cột sống ngực', 'D:/EHR-Files/mri_cotsongngoai_012.jpg', 'Gù vẹo cột sống nhẹ', '2025-10-19', 1),
(13, NULL, 'MRI', 'MRI thận 2 bên', 'D:/EHR-Files/mri_than_013.jpg', 'Nang thận đơn giản 3cm', '2025-10-18', 1),
(14, 14, 'MRI', 'MRI tử cung phần phụ', 'D:/EHR-Files/mri_tucung_014.jpg', 'U xơ tử cung 5cm', '2025-10-17', 1),
(15, NULL, 'MRI', 'MRI buồng trứng', 'D:/EHR-Files/mri_buongtrung_015.jpg', 'Nang buồng trứng 4cm', '2025-10-16', 1),

-- Prescription Documents (40 records)
(1, 1, 'Prescription', 'Đơn thuốc điều trị viêm phổi', 'D:/EHR-Files/toa_001.pdf', 'Kháng sinh Amoxicillin 500mg x 7 ngày', '2025-10-29', 1),
(2, NULL, 'Prescription', 'Đơn thuốc hạ huyết áp', 'D:/EHR-Files/toa_002.pdf', 'Losartan 50mg x 1 viên/ngày', '2025-10-28', 1),
(3, 3, 'Prescription', 'Đơn thuốc điều trị tiểu đường', 'D:/EHR-Files/toa_003.pdf', 'Metformin 500mg x 2 lần/ngày', '2025-10-27', 1),
(4, NULL, 'Prescription', 'Đơn thuốc giảm đau', 'D:/EHR-Files/toa_004.pdf', 'Paracetamol 500mg khi đau', '2025-10-26', 1),
(5, 5, 'Prescription', 'Đơn thuốc chống viêm', 'D:/EHR-Files/toa_005.pdf', 'Ibuprofen 400mg x 3 lần/ngày sau ăn', '2025-10-25', 1),
(6, NULL, 'Prescription', 'Đơn thuốc điều trị dạ dày', 'D:/EHR-Files/toa_006.pdf', 'Omeprazole 20mg x 2 lần/ngày trước ăn', '2025-10-24', 1),
(7, 7, 'Prescription', 'Đơn thuốc kháng sinh', 'D:/EHR-Files/toa_007.pdf', 'Cephalexin 500mg x 4 lần/ngày', '2025-10-23', 1),
(8, NULL, 'Prescription', 'Đơn thuốc hen phế quản', 'D:/EHR-Files/toa_008.pdf', 'Salbutamol xịt 2 nhát khi khó thở', '2025-10-22', 1),
(9, 9, 'Prescription', 'Đơn thuốc chống dị ứng', 'D:/EHR-Files/toa_009.pdf', 'Cetirizine 10mg x 1 viên tối', '2025-10-21', 1),
(10, NULL, 'Prescription', 'Đơn thuốc an thần', 'D:/EHR-Files/toa_010.pdf', 'Alprazolam 0.25mg x 1 viên tối', '2025-10-20', 1),
(11, 11, 'Prescription', 'Đơn thuốc vitamin tổng hợp', 'D:/EHR-Files/toa_011.pdf', 'Vitamin B complex x 1 viên sáng', '2025-10-19', 1),
(12, NULL, 'Prescription', 'Đơn thuốc bổ máu', 'D:/EHR-Files/toa_012.pdf', 'Sắt Sulfat 200mg x 1 viên/ngày', '2025-10-18', 1),
(13, 13, 'Prescription', 'Đơn thuốc hạ lipid', 'D:/EHR-Files/toa_013.pdf', 'Atorvastatin 10mg x 1 viên tối', '2025-10-17', 1),
(14, NULL, 'Prescription', 'Đơn thuốc chống đông', 'D:/EHR-Files/toa_014.pdf', 'Aspirin 100mg x 1 viên/ngày', '2025-10-16', 1),
(15, 15, 'Prescription', 'Đơn thuốc trị gout', 'D:/EHR-Files/toa_015.pdf', 'Allopurinol 300mg x 1 viên/ngày', '2025-10-15', 1),
(1, NULL, 'Prescription', 'Đơn thuốc điều trị viêm khớp', 'D:/EHR-Files/toa_016.pdf', 'Celecoxib 200mg x 2 lần/ngày', '2025-10-14', 1),
(2, 2, 'Prescription', 'Đơn thuốc trị táo bón', 'D:/EHR-Files/toa_017.pdf', 'Bisacodyl 5mg khi cần', '2025-10-13', 1),
(3, NULL, 'Prescription', 'Đơn thuốc lợi tiểu', 'D:/EHR-Files/toa_018.pdf', 'Furosemide 40mg x 1 viên sáng', '2025-10-12', 1),
(4, 4, 'Prescription', 'Đơn thuốc bổ sung Vitamin D', 'D:/EHR-Files/toa_019.pdf', 'Vitamin D3 1000IU x 1 viên/ngày', '2025-10-11', 1),
(5, NULL, 'Prescription', 'Đơn thuốc điều trị loãng xương', 'D:/EHR-Files/toa_020.pdf', 'Alendronate 70mg x 1 viên/tuần', '2025-10-10', 1),

-- Report Documents (20 records)
(1, 1, 'Report', 'Báo cáo tổng kết khám tổng quát', 'D:/EHR-Files/bc_tongquat_001.pdf', 'Tổng quan tình trạng sức khỏe bệnh nhân', '2025-10-30', 1),
(2, NULL, 'Report', 'Báo cáo phẫu thuật ruột thừa', 'D:/EHR-Files/bc_phauthuat_002.pdf', 'Phẫu thuật cắt ruột thừa thành công', '2025-10-29', 1),
(3, 3, 'Report', 'Báo cáo sinh thiết gan', 'D:/EHR-Files/bc_sinhthiet_003.pdf', 'Kết quả: viêm gan mạn tính', '2025-10-28', 1),
(4, NULL, 'Report', 'Báo cáo nội soi dạ dày', 'D:/EHR-Files/bc_noisoi_004.pdf', 'Loét dạ dày HP dương tính', '2025-10-27', 1),
(5, 5, 'Report', 'Báo cáo siêu âm tim', 'D:/EHR-Files/bc_sieusim_005.pdf', 'EF: 55%, chức năng tim bình thường', '2025-10-26', 1),
(6, NULL, 'Report', 'Báo cáo điện tim', 'D:/EHR-Files/bc_dientim_006.pdf', 'Nhịp xoang đều, không rối loạn', '2025-10-25', 1),
(7, 7, 'Report', 'Báo cáo đo loãng xương', 'D:/EHR-Files/bc_loangxuong_007.pdf', 'T-score: -2.5 - loãng xương', '2025-10-24', 1),
(8, NULL, 'Report', 'Báo cáo thăm dò chức năng hô hấp', 'D:/EHR-Files/bc_hohap_008.pdf', 'FEV1/FVC: 65% - COPD độ 2', '2025-10-23', 1),
(9, 9, 'Report', 'Báo cáo điện não đồ', 'D:/EHR-Files/bc_diennaodo_009.pdf', 'Không có dấu hiệu động kinh', '2025-10-22', 1),
(10, NULL, 'Report', 'Báo cáo siêu âm tuyến giáp', 'D:/EHR-Files/bc_sieusamgiap_010.pdf', 'Nang giáp lành tính TIRADS 2', '2025-10-21', 1),

-- Other Documents (20 records)
(NULL, NULL, 'Other', 'Giấy xác nhận khám sức khỏe', 'D:/EHR-Files/other_xacnhan_001.pdf', 'Xác nhận đủ sức khỏe làm việc', '2025-10-25', 1),
(NULL, NULL, 'Other', 'Giấy chứng nhận phẫu thuật', 'D:/EHR-Files/other_chungnhan_002.pdf', 'Đã phẫu thuật ruột thừa ngày 15/10/2025', '2025-10-20', 1),
(NULL, NULL, 'Other', 'Giấy ra viện', 'D:/EHR-Files/other_ravien_003.pdf', 'Xuất viện sau 7 ngày điều trị', '2025-10-18', 1),
(NULL, NULL, 'Other', 'Hướng dẫn chế độ ăn cho bệnh tiểu đường', 'D:/EHR-Files/other_chedo_004.pdf', 'Chế độ ăn ít đường, nhiều chất xơ', '2025-10-15', 1),
(NULL, NULL, 'Other', 'Lịch tái khám', 'D:/EHR-Files/other_taikham_005.pdf', 'Tái khám sau 2 tuần', '2025-10-12', 1),
(NULL, NULL, 'Other', 'Phiếu đồng ý phẫu thuật', 'D:/EHR-Files/other_dongyphau_006.pdf', 'Đồng ý phẫu thuật cắt túi mật', '2025-10-10', 1),
(NULL, NULL, 'Other', 'Giấy giới thiệu chuyển viện', 'D:/EHR-Files/other_chuyenvien_007.pdf', 'Chuyển tuyến trên để điều trị', '2025-10-08', 1),
(NULL, NULL, 'Other', 'Cam kết điều trị', 'D:/EHR-Files/other_camket_008.pdf', 'Cam kết tuân thủ điều trị', '2025-10-05', 1),
(NULL, NULL, 'Other', 'Bảng theo dõi đường huyết', 'D:/EHR-Files/other_theodoi_009.pdf', 'Nhật ký đường huyết 1 tháng', '2025-10-03', 1),
(NULL, NULL, 'Other', 'Hướng dẫn tập vật lý trị liệu', 'D:/EHR-Files/other_vatly_010.pdf', 'Bài tập phục hồi chức năng', '2025-10-01', 1);

-- =====================================================
-- INDEXES BỔ SUNG ĐỂ TỐI ƯU HIỆU SUẤT
-- =====================================================

-- Composite index cho tìm kiếm theo member và type
CREATE INDEX idx_documents_member_type ON documents(member_id, `type`);

-- Index cho tìm kiếm theo title
CREATE INDEX idx_documents_title ON documents(title);

-- =====================================================
-- GRANT PERMISSIONS
-- =====================================================

-- Tạo user và cấp quyền
CREATE USER IF NOT EXISTS 'ehruser'@'localhost' IDENTIFIED BY 'Quangthu2005@';
GRANT ALL PRIVILEGES ON ehr.* TO 'ehruser'@'localhost';
FLUSH PRIVILEGES;

-- =====================================================
-- THỐNG KÊ DỮ LIỆU
-- =====================================================

SELECT 'Database schema created successfully!' AS status;
SELECT COUNT(*) AS total_documents FROM documents;
SELECT `type`, COUNT(*) AS count FROM documents GROUP BY `type`;

-- =====================================================
-- BẢNG NOTIFICATIONS - Quản lý thông báo
-- =====================================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    recipient_user_id INT NULL COMMENT 'ID người nhận thông báo',
    recipient_name VARCHAR(255) NULL COMMENT 'Tên người nhận',
    title VARCHAR(500) NOT NULL COMMENT 'Tiêu đề thông báo',
    message TEXT NULL COMMENT 'Nội dung thông báo',
    `type` VARCHAR(50) NOT NULL DEFAULT 'ThongTinChung' COMMENT 'Loại thông báo',
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT 'Mức độ ưu tiên: LOW, NORMAL, HIGH, URGENT',
    related_document_id INT NULL COMMENT 'ID tài liệu liên quan',
    related_appointment_id INT NULL COMMENT 'ID cuộc hẹn liên quan',
    is_read BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Đã đọc chưa',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    read_at DATETIME NULL COMMENT 'Thời gian đọc',
    action_url VARCHAR(500) NULL COMMENT 'URL để navigate khi click',
    requires_response BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Có cần phản hồi không',
    response_text TEXT NULL COMMENT 'Phản hồi từ người dùng',
    responded_at DATETIME NULL COMMENT 'Thời gian phản hồi',
    
    INDEX idx_recipient (recipient_user_id),
    INDEX idx_type (`type`),
    INDEX idx_priority (priority),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at),
    INDEX idx_related_document (related_document_id),
    INDEX idx_related_appointment (related_appointment_id),
    
    FOREIGN KEY (related_document_id) REFERENCES documents(id) ON DELETE SET NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Bảng quản lý thông báo';

-- =====================================================
-- DỮ LIỆU MẪU CHO BẢNG NOTIFICATIONS (50 RECORDS)
-- =====================================================

-- Thông báo tài liệu mới
INSERT INTO notifications (recipient_user_id, recipient_name, title, message, type, priority, related_document_id, is_read) VALUES
(1, 'Bác sĩ Nguyễn Văn A', 'Tài liệu X-quang mới', 'X-quang phổi chính diện đã được upload cho bệnh nhân #1', 'TaiLieuMoi', 'NORMAL', 1, false),
(2, 'Bác sĩ Trần Thị B', 'Kết quả xét nghiệm máu', 'Kết quả xét nghiệm máu tổng quát đã có cho bệnh nhân #1', 'KetQuaXetNghiem', 'HIGH', 21, false),
(3, 'Y tá Lê Văn C', 'X-quang bụng mới', 'X-quang bụng thẳng đã được upload', 'TaiLieuMoi', 'NORMAL', 2, true),
(1, 'Bác sĩ Nguyễn Văn A', 'Cảnh báo: Kết quả bất thường', 'Phát hiện thoái hóa đốt sống L4-L5 ở bệnh nhân #3', 'CanhBaoKhan', 'URGENT', 3, false),
(4, 'Bệnh nhân Phạm Thị D', 'Lịch hẹn khám sắp tới', 'Bạn có lịch khám ngày 2025-11-05 lúc 09:00', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Kết quả sinh hóa máu', 'Kết quả xét nghiệm sinh hóa máu đã sẵn sàng', 'KetQuaXetNghiem', 'NORMAL', 22, true),
(2, 'Bác sĩ Trần Thị B', 'Yêu cầu xác nhận', 'Vui lòng xác nhận đã xem X-quang vai phải', 'XacNhanBacSi', 'HIGH', 4, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Đơn thuốc mới', 'Đơn thuốc điều trị viêm khớp đã được kê', 'DonThuoc', 'HIGH', 45, false),
(1, 'Bác sĩ Nguyễn Văn A', 'X-quang đầu gối', 'X-quang đầu gối trái - thoái hóa khớp độ 2', 'TaiLieuMoi', 'NORMAL', 5, true),
(3, 'Y tá Lê Văn C', 'Nhắc nhở khám định kỳ', 'Bệnh nhân #6 cần khám định kỳ vào tuần tới', 'NhacNhoKhamBenh', 'NORMAL', NULL, false),

-- Thông báo xét nghiệm
(1, 'Bác sĩ Nguyễn Văn A', 'Xét nghiệm nước tiểu', 'Protein niệu (+), cần theo dõi chức năng thận', 'KetQuaXetNghiem', 'HIGH', 23, false),
(2, 'Bác sĩ Trần Thị B', 'Kết quả chức năng gan', 'Các chỉ số gan bình thường', 'KetQuaXetNghiem', 'NORMAL', 24, true),
(6, 'Bệnh nhân Vũ Thị F', 'Lịch hẹn xét nghiệm', 'Lịch xét nghiệm máu vào ngày mai 08:00', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'HIV test', 'Kết quả HIV âm tính', 'KetQuaXetNghiem', 'NORMAL', 25, true),
(3, 'Y tá Lê Văn C', 'Đường huyết', 'Đường huyết cao - 180 mg/dL, cần điều chỉnh thuốc', 'CanhBaoKhan', 'URGENT', 26, false),

-- Thông báo cảnh báo
(1, 'Bác sĩ Nguyễn Văn A', 'KHẨN: Chức năng thận suy giảm', 'Creatinine 2.5 mg/dL - cần can thiệp ngay', 'CanhBaoKhan', 'URGENT', 27, false),
(2, 'Bác sĩ Trần Thị B', 'Huyết áp cao', 'Huyết áp 180/110 mmHg - nguy cơ cao', 'CanhBaoKhan', 'URGENT', NULL, false),
(4, 'Bệnh nhân Phạm Thị D', 'Nhắc uống thuốc', 'Đã đến giờ uống thuốc huyết áp', 'NhacNhoKhamBenh', 'NORMAL', NULL, true),
(7, 'Bệnh nhân Ngô Văn G', 'Hết hạn đơn thuốc', 'Đơn thuốc sắp hết hạn, cần tái khám', 'HetHanTaiLieu', 'HIGH', 46, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Kết quả X-quang', 'X-quang ngực nghiêng cho thấy tim to', 'TaiLieuMoi', 'HIGH', 6, false),

-- Thông báo CT Scan và MRI
(1, 'Bác sĩ Nguyễn Văn A', 'CT Scan não mới', 'CT Scan não đã được upload', 'TaiLieuMoi', 'NORMAL', 51, false),
(2, 'Bác sĩ Trần Thị B', 'MRI cột sống', 'MRI cột sống thắt lưng có sẵn', 'TaiLieuMoi', 'NORMAL', 71, true),
(8, 'Bệnh nhân Đặng Thị H', 'Lịch chụp MRI', 'Lịch chụp MRI não ngày 2025-11-10', 'LichHenSapToi', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'CT Scan ngực', 'Phát hiện tổn thương phổi', 'CanhBaoKhan', 'URGENT', 52, false),
(3, 'Y tá Lê Văn C', 'MRI đầu gối', 'MRI đầu gối - tổn thương sụn khớp', 'TaiLieuMoi', 'HIGH', 72, false),

-- Thông báo đơn thuốc
(4, 'Bệnh nhân Phạm Thị D', 'Đơn thuốc kháng sinh', 'Đơn thuốc Amoxicillin 500mg', 'DonThuoc', 'HIGH', 91, false),
(5, 'Bệnh nhân Hoàng Văn E', 'Đơn thuốc tim mạch', 'Đơn thuốc điều trị huyết áp', 'DonThuoc', 'HIGH', 92, true),
(6, 'Bệnh nhân Vũ Thị F', 'Đơn thuốc tiểu đường', 'Đơn thuốc Metformin 850mg', 'DonThuoc', 'HIGH', 93, false),
(9, 'Bệnh nhân Bùi Văn I', 'Nhắc uống thuốc', 'Đã đến giờ uống thuốc huyết áp buổi tối', 'NhacNhoKhamBenh', 'NORMAL', NULL, false),
(7, 'Bệnh nhân Ngô Văn G', 'Đơn thuốc kháng viêm', 'Đơn thuốc Ibuprofen 400mg', 'DonThuoc', 'NORMAL', 94, true),

-- Thông báo yêu cầu bổ sung
(1, 'Bác sĩ Nguyễn Văn A', 'Yêu cầu X-quang thêm', 'Cần chụp X-quang thêm góc nghiêng', 'YeuCauBoSung', 'HIGH', 1, false),
(2, 'Bác sĩ Trần Thị B', 'Yêu cầu xét nghiệm bổ sung', 'Cần làm thêm xét nghiệm chức năng thận', 'YeuCauBoSung', 'HIGH', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Cần thông tin thêm', 'Vui lòng cung cấp tiền sử bệnh', 'YeuCauBoSung', 'NORMAL', NULL, true),
(3, 'Y tá Lê Văn C', 'Cập nhật hồ sơ', 'Cần cập nhật thông tin liên lạc', 'ThongTinChung', 'LOW', NULL, false),
(10, 'Bệnh nhân Lý Thị K', 'Xác nhận lịch hẹn', 'Vui lòng xác nhận lịch khám ngày 2025-11-15', 'LichHenSapToi', 'NORMAL', NULL, false),

-- Thông báo báo cáo
(1, 'Bác sĩ Nguyễn Văn A', 'Báo cáo tổng kết', 'Báo cáo tổng kết quý 4/2025 đã sẵn sàng', 'ThongTinChung', 'NORMAL', 111, true),
(2, 'Bác sĩ Trần Thị B', 'Báo cáo ca bệnh', 'Báo cáo ca bệnh tim mạch', 'ThongTinChung', 'NORMAL', 112, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Kết quả siêu âm', 'Siêu âm tim - van tim có vấn đề', 'KetQuaXetNghiem', 'HIGH', 113, false),
(3, 'Y tá Lê Văn C', 'Báo cáo theo dõi', 'Báo cáo theo dõi bệnh nhân nội trú', 'ThongTinChung', 'LOW', 114, true),
(2, 'Bác sĩ Trần Thị B', 'Xác nhận đã xem', 'Vui lòng xác nhận đã xem báo cáo', 'XacNhanBacSi', 'NORMAL', 115, false),

-- Thông báo hệ thống
(NULL, 'Tất cả người dùng', 'Bảo trì hệ thống', 'Hệ thống sẽ bảo trì vào 02:00 sáng ngày 2025-11-01', 'ThongTinChung', 'NORMAL', NULL, false),
(NULL, 'Tất cả người dùng', 'Cập nhật phần mềm', 'Phiên bản mới 2.0 đã được cài đặt', 'ThongTinChung', 'LOW', NULL, true),
(NULL, 'Tất cả bác sĩ', 'Họp khoa', 'Họp khoa nội vào 14:00 chiều nay', 'ThongTinChung', 'NORMAL', NULL, false),
(1, 'Bác sĩ Nguyễn Văn A', 'Báo cáo tuần', 'Báo cáo tuần cần nộp trước 17:00', 'ThongTinChung', 'NORMAL', NULL, false),
(2, 'Bác sĩ Trần Thị B', 'Đào tạo mới', 'Khóa đào tạo về COVID-19 vào thứ 6', 'ThongTinChung', 'LOW', NULL, false);

-- =====================================================
-- BẢNG NOTIFICATION_PREFERENCES - Cài đặt thông báo người dùng
-- =====================================================
CREATE TABLE IF NOT EXISTS notification_preferences (
    user_id INT PRIMARY KEY COMMENT 'ID người dùng',
    enabled_types TEXT NULL COMMENT 'Các loại thông báo được bật (JSON array)',
    enable_sound BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật âm thanh',
    enable_popup BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật popup',
    enable_badge BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Bật badge số',
    reminder_days_before INT NOT NULL DEFAULT 1 COMMENT 'Nhắc trước bao nhiêu ngày',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT 'Cài đặt thông báo của người dùng';

-- Dữ liệu mẫu preferences
INSERT INTO notification_preferences (user_id, enabled_types, enable_sound, enable_popup, enable_badge, reminder_days_before) VALUES
(1, '["TaiLieuMoi","KetQuaXetNghiem","CanhBaoKhan","XacNhanBacSi"]', true, true, true, 1),
(2, '["TaiLieuMoi","KetQuaXetNghiem","CanhBaoKhan"]', true, true, true, 2),
(3, '["TaiLieuMoi","NhacNhoKhamBenh","ThongTinChung"]', false, true, true, 1),
(4, '["LichHenSapToi","DonThuoc","NhacNhoKhamBenh"]', true, true, true, 3),
(5, '["LichHenSapToi","DonThuoc","HetHanTaiLieu"]', true, false, true, 1);

SELECT 'Notifications tables created successfully!' AS status;
SELECT COUNT(*) AS total_notifications FROM notifications;
SELECT `type`, COUNT(*) AS count FROM notifications GROUP BY `type`;
SELECT priority, COUNT(*) AS count FROM notifications GROUP BY priority;
SELECT is_read, COUNT(*) AS count FROM notifications GROUP BY is_read;

-- =====================================================
-- HOÀN THÀNH
-- =====================================================

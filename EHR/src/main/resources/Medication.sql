-- Tạo cơ sở dữ liệu
CREATE DATABASE IF NOT EXISTS personal_health_management;
USE personal_health_management;
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
INSERT INTO Medications (
    name, generic_name, description, side_effects, 
    contraindications, interactions, manufacturer, 
    unit, requires_prescription, barcode
)
VALUES
(
    'Paracetamol 500mg', 'Paracetamol (Acetaminophen)', 
    'Thuốc giảm đau, hạ sốt thông thường.', 
    'Hiếm gặp, có thể gây phát ban da. Dùng quá liều gây tổn thương gan nghiêm trọng.', 
    'Quá mẫn với paracetamol. Bệnh gan nặng.', 
    'Rượu (tăng nguy cơ tổn thương gan), Warfarin (tăng tác dụng chống đông).', 
    'Dược Hậu Giang', 'Viên', FALSE, '8934567890011'
),
(
    'Amoxicillin 500mg', 'Amoxicillin', 
    'Kháng sinh nhóm penicillin, điều trị nhiễm khuẩn tai, mũi, họng, da, đường tiết niệu.', 
    'Tiêu chảy, buồn nôn, phát ban.', 
    'Dị ứng với penicillin.', 
    'Thuốc tránh thai (giảm hiệu quả), Methotrexate.', 
    'GlaxoSmithKline', 'Viên', TRUE, '8934567890022'
),
(
    'Atorvastatin 20mg', 'Atorvastatin', 
    'Thuốc Statin, dùng để hạ cholesterol máu và ngăn ngừa bệnh tim mạch.', 
    'Đau cơ, tiêu chảy, tăng men gan.', 
    'Bệnh gan tiến triển, phụ nữ có thai và cho con bú.', 
    'Nước bưởi, thuốc kháng nấm Azole, Cyclosporine.', 
    'Pfizer', 'Viên', TRUE, '8934567890033'
),
(
    'Oresol Cam 4.1g', 'Oral Rehydration Salts (ORS)', 
    'Bù nước và điện giải trong các trường hợp tiêu chảy, sốt cao, nôn mửa.', 
    'Nôn nhẹ. Nếu pha quá đặc có thể gây tăng natri máu.', 
    'Suy thận cấp, tắc ruột, thủng ruột.', 
    'Không có tương tác đáng kể khi dùng đúng liều.', 
    'Traphaco', 'Gói', FALSE, '8934567890066'
),
(
    'Salbutamol 100mcg (Ventolin)', 'Salbutamol Sulfate', 
    'Thuốc giãn phế quản, dùng để cắt cơn hen suyễn và co thắt phế quản.', 
    'Run tay, nhịp tim nhanh, đau đầu.', 
    'Quá mẫn với Salbutamol.', 
    'Thuốc chẹn beta (như Propranolol), thuốc lợi tiểu.', 
    'GSK', 'Chai', TRUE, '8934567890055'
),
(
    'Metformin 850mg', 'Metformin Hydrochloride', 
    'Thuốc điều trị đái tháo đường tuýp 2.', 
    'Rối loạn tiêu hóa (tiêu chảy, buồn nôn), vị kim loại trong miệng.', 
    'Suy thận nặng, nhiễm toan ceton, suy gan.', 
    'Thuốc cản quang tiêm tĩnh mạch, Cimetidine, rượu.', 
    'Stada', 'Viên', TRUE, '8934567890044'
),
(
    'Clotrimazole 1% Cream (Canesten)', 'Clotrimazole', 
    'Kem chống nấm, điều trị nấm da, lang ben, nấm kẽ chân, nấm âm đạo.', 
    'Kích ứng da tại chỗ, nóng rát, mẩn đỏ (hiếm gặp).', 
    'Quá mẫn với Clotrimazole hoặc các thành phần khác của kem.', 
    'Không có tương tác đáng kể khi dùng ngoài da.', 
    'Bayer', 'Tuýp', FALSE, '8934567890100'
),
(
    'Omeprazole 20mg', 'Omeprazole', 
    'Thuốc ức chế bơm proton (PPI), điều trị trào ngược dạ dày (GERD) và loét dạ dày.', 
    'Đau đầu, buồn nôn, tiêu chảy.', 
    'Quá mẫn với omeprazole. Không dùng chung với Nelfinavir.', 
    'Clopidogrel (giảm hiệu quả), Warfarin, Diazepam.', 
    'AstraZeneca', 'Viên', TRUE, '8934567890088'
),
(
    'Insulin Aspart (NovoRapid FlexPen)', 'Insulin Aspart', 
    'Insulin tác dụng nhanh, dùng tiêm dưới da để kiểm soát đường huyết cao.', 
    'Hạ đường huyết (triệu chứng: run rẩy, vã mồ hôi, đói). Phản ứng tại chỗ tiêm.', 
    'Đang trong cơn hạ đường huyết.', 
    'Thuốc chẹn beta, corticosteroid, rượu (có thể tăng hoặc giảm tác dụng).', 
    'Novo Nordisk', 'Ống tiêm', TRUE, '8934567890099'
),
(
    'Berberin 100mg', 'Berberine Chloride', 
    'Trị lỵ, tiêu chảy, nhiễm khuẩn đường ruột.', 
    'Táo bón (hiếm gặp khi dùng liều thấp).', 
    'Phụ nữ có thai.', 
    'Cyclosporine (tăng nồng độ Cyclosporine).', 
    'Domesco', 'Viên', FALSE, '8934567890077'
);
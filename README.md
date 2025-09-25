# 🏥 Electronic Health Record (EHR)

> Hệ thống quản lý sức khỏe cá nhân và gia đình toàn diện

## 📋 Phân chia công việc dự kiến

>[Phân chia công việc dự kiến](https://docs.google.com/document/d/1jBV0P2Hx4i9kIwkHihEXbp-6xavH4pGbevVb8jYWV4I/edit?tab=t.0#heading=h.hrpb4s29klzk)

## 📋 Cập nhật công việc

>[Cập nhật công việc](https://docs.google.com/document/d/1Q09uLYUqcS2KSsgipiaic_jPDW5aWBW0VCFF_423ycg/edit?usp=sharing)

## 📋 Mục lục

- [Giới thiệu](#-giới-thiệu)
- [Tính năng](#-tính-năng)
- [Cấu trúc Database](#-cấu-trúc-database)

## 🎯 Giới thiệu

Hệ thống quản lý sức khỏe cá nhân là một giải pháp toàn diện giúp cá nhân và gia đình theo dõi, quản lý thông tin sức khỏe một cách có hệ thống và an toàn.

## ✨ Tính năng

### 👨‍👩‍👧‍👦 Quản lý Gia đình
- **Đa thành viên**: Quản lý thông tin sức khỏe cho cả gia đình
- **Phân quyền**: Kiểm soát quyền truy cập thông tin
- **Theo dõi mối quan hệ**: Cha mẹ, con cái, vợ chồng

### 🏥 Hệ thống Y tế
- **Mạng lưới bệnh viện**: Kết nối với các bệnh viện lớn
- **Đặt lịch hẹn**: Quản lý cuộc hẹn khám bệnh
- **Hồ sơ bác sĩ**: Thông tin chi tiết về đội ngũ y tế

### 💊 Quản lý Thuốc
- **Đơn thuốc điện tử**: Lưu trữ và theo dõi đơn thuốc
- **Lịch nhắc nhở**: Nhắc nhở uống thuốc đúng giờ
- **Theo dõi tuân thủ**: Ghi nhận việc uống thuốc

### 💉 Tiêm chủng
- **Lịch tiêm chuẩn**: Theo chương trình tiêm chủng quốc gia
- **Theo dõi mũi tiêm**: Lịch sử và lịch hẹn tiêm
- **Nhắc nhở**: Thông báo khi đến hạn tiêm

### 📁 Lưu trữ Tài liệu
- **Kết quả xét nghiệm**: Upload và phân loại
- **Hình ảnh y tế**: X-quang, CT, MRI
- **Bệnh án**: Lưu trữ hồ sơ khám bệnh

### ⚠️ Cảnh báo Y tế
- **Dị ứng**: Cảnh báo chất gây dị ứng
- **Tiền sử bệnh**: Theo dõi bệnh mãn tính
- **Tương tác thuốc**: Kiểm tra tương tác có hại

## 🗃️ Cấu trúc Database
### (Tổng Quan)(https://docs.google.com/document/d/1ttHrxGBaiZZBr1jj7Y-oIqUlphCpyd-1dt7R8IQw9RA/edit?fbclid=IwY2xjawNCF-5leHRuA2FlbQIxMQABHoZzjHJ6lXB_jhJqQqWHQx0PleQOEkmZXI-pgZ-ZKic18hn4j893XGNiQoqE_aem_WsQOElaLaz1YyTB0f4gAxQ&tab=t.0)
### 📊 Thống kê
- **15 bảng** chính
- **Hỗ trợ Unicode** đầy đủ cho tiếng Việt
- **Tính toàn vẹn** dữ liệu với foreign keys
- **Timestamps** tự động

### 🔑 Các bảng chính

| Bảng | Mô tả |
|------|-------|
| `Users` | Người dùng hệ thống |
| `Family_Members` | Thành viên gia đình |
| `Hospitals` | Bệnh viện |
| `Doctors` | Bác sĩ |
| `Medical_History` | Tiền sử bệnh |
| `Allergies` | Dị ứng |
| `Appointments` | Cuộc hẹn |
| `Medications` | Danh mục thuốc |
| `Prescriptions` | Đơn thuốc |
| `Vaccination_Records` | Tiêm chủng |

### 🔗 Mối quan hệ
```
Users (1) -----> (n) Family_Members
Family_Members (1) -----> (n) Medical_History
Family_Members (1) -----> (n) Allergies
Family_Members (1) -----> (n) Appointments
Appointments (1) -----> (n) Prescriptions
Prescriptions (1) -----> (n) Prescription_Details
```

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
- [Công nghệ sử dụng](#-công-nghệ-sử-dụng)
- [Hướng dẫn Cài đặt và Khởi chạy](#-hướng-dẫn-cài-đặt-và-khởi-chạy)
- [Tổng kết](#-tổng-kết)

## 🎯 Giới thiệu

Hệ thống quản lý sức khỏe cá nhân là một giải pháp toàn diện giúp cá nhân và gia đình theo dõi, quản lý thông tin sức khỏe một cách có hệ thống và an toàn.

## ✨ Tính năng

* **Xác thực & Quản lý Người dùng**: Đăng nhập và đăng ký an toàn (sử dụng `jbcrypt`).
* **Quản lý Gia đình**: Tạo và quản lý hồ sơ sức khỏe cho nhiều thành viên gia đình (cha, mẹ, con cái...).
* **Trang chủ (Dashboard)**: Giao diện chính hiển thị lời chào, tin tức y tế (lấy từ `moh.gov.vn` bằng `Jsoup`), và điều hướng đến các tính năng.
* **Hồ sơ Sức khỏe Chi tiết**: Mỗi thành viên có một trang tổng quan (`MemberDetailsFrame`) để truy cập:
    * **Lịch sử Bệnh án**: Xem, lọc theo năm, mức độ.
    * **Quản lý Dị ứng**: Phân loại (thuốc, thức ăn...) và tô màu cảnh báo theo mức độ.
* **Mô-đun Đặt lịch hẹn**: Một quy trình 3 bước cho phép người dùng chọn Bệnh viện, Khoa, và Bác sĩ, sau đó đặt lịch cho thành viên gia đình.
* **Quản lý Đơn thuốc**: Xem các đơn thuốc đã kê, chi tiết thuốc, và lịch uống thuốc.
* **Quản lý Tiêm chủng**: Xem lịch tiêm chủng chuẩn và hồ sơ tiêm chủng của từng thành viên.
* **Quản lý Tài liệu**: Tải lên và quản lý các tài liệu y tế (X-quang, kết quả xét nghiệm) với chức năng xem trước hình ảnh.
* **Trung tâm Thông báo**: Nhận các thông báo tự động (từ CSDL) về lịch hẹn, lịch tiêm, lịch uống thuốc sắp tới.


## 🗃️ Cấu trúc Database
### [Tổng Quan](https://docs.google.com/document/d/1ttHrxGBaiZZBr1jj7Y-oIqUlphCpyd-1dt7R8IQw9RA/edit?fbclid=IwY2xjawNCF-5leHRuA2FlbQIxMQABHoZzjHJ6lXB_jhJqQqWHQx0PleQOEkmZXI-pgZ-ZKic18hn4j893XGNiQoqE_aem_WsQOElaLaz1YyTB0f4gAxQ&tab=t.0)
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

## 💻 Công nghệ sử dụng

* **Ngôn ngữ**: Java
* **Giao diện (UI)**: Java Swing
* **Cơ sở dữ liệu**: MySQL
* **Thư viện (JARs) chính**:
    * `mysql-connector-java` (Kết nối MySQL)
    * `jbcrypt` (Băm mật khẩu)
    * `jcalendar` / `toedter-calendar` (Bộ chọn ngày)
    * `lgooddatepicker` (Bộ chọn ngày)
    * `jsoup` (Lấy dữ liệu web)
    * `miglayout-swing` (Bố cục UI)

---

## 🚀 Hướng dẫn Cài đặt và Khởi chạy

Để chạy dự án này, bạn cần thiết lập cơ sở dữ liệu MySQL và cấu hình dự án Java.

### Bước 1: Yêu cầu

* Java JDK (phiên bản 11 trở lên).
* MySQL Server (ví dụ: XAMPP, WAMP, MySQL Workbench).
* Một IDE Java (ví dụ: NetBeans, IntelliJ, Eclipse).

### Bước 2: Cài đặt Cơ sở dữ liệu (MySQL)

### Bước 3: Cấu hình Kết nối trong Mã nguồn

Ứng dụng Java (Client) cần biết địa chỉ và mật khẩu của MySQL Server. Vì mỗi lập trình viên có một mật khẩu MySQL riêng, bạn phải cấu hình thủ công:

Mở dự án Java của bạn trong IDE.

Tìm đến file:  
`src/com/mycompany/ehr/util/JDBCUtil.java`

Mở file và thay đổi mật khẩu trong hằng số PASSWORD để khớp với mật khẩu MySQL của bạn:

```java
private static final String URL = "jdbc:mysql://127.0.0.1:3306/personal_health_management";
private static final String USER = "root";

// THAY ĐỔI MẬT KHẨU NÀY ĐỂ KHỚP VỚI MYSQL CỦA BẠN
private static final String PASSWORD = "Admin@123"; 
```

---

### Bước 4: Chạy Dữ liệu Mẫu (SQL Scripts)

Ứng dụng được lập trình để đọc dữ liệu có sẵn (như danh sách bác sĩ, bệnh viện, v.v.). Bạn phải "nạp" dữ liệu mẫu này vào CSDL trước khi chạy ứng dụng.

Để ứng dụng có dữ liệu, bạn phải chạy các file `.sql` trong thư mục `resources` theo đúng thứ tự sau để đảm bảo tính toàn vẹn của khóa ngoại (foreign key):

1. `1_createtable.sql` (Tạo cấu trúc, chèn dữ liệu gốc như bệnh viện, khoa, người dùng mẫu)
2. `2_medical_history_and_allergies.sql` (Chèn lịch sử bệnh, dị ứng)
3. `3_doctors.sql` (Chèn danh sách bác sĩ)
4. `4_Appointments.sql` (Chèn lịch hẹn)
5. `5_insertdata.sql` (TRUNCATE và chèn lại thành viên gia đình, chèn dữ liệu tiêm chủng)
6. `6_Medication.sql` (Chèn danh mục thuốc)
7. `7_Prescriptions.sql` (Chèn đơn thuốc)
8. `8_PrescriptionDetails.sql` (Chèn chi tiết đơn thuốc)
9. `9_documents_data.sql` (Chèn tài liệu)
10. `10_Medication_Schedule.sql` (Chèn lịch uống thuốc)
11. `11_notifications_setup.sql` (Tạo bảng thông báo và tự động tạo thông báo từ dữ liệu đã chèn)

### Bước 6: Khởi chạy Ứng dụng (Java)


### Bước 7: Hướng dẫn Sử dụng

#### Đăng ký:
Tại màn hình `LoginFrame`, nhấp vào liên kết **"Đăng ký"** để mở `RegisterFrame`.

Nhập thông tin của bạn. Khi đăng ký, hệ thống sẽ tự động tạo một tài khoản `User` (dùng CCCD làm username) và một hồ sơ `FamilyMembers` (với quan hệ "Bản thân") cho bạn.

Tài khoản đã được tạo sẵn để kiểm tra các chức năng của ứng dụng:
- Tài khoản: `0101010101`
- Mật khẩu: `Admin@123`

#### Đăng nhập:
Sử dụng CCCD và mật khẩu bạn vừa tạo để đăng nhập.

#### Sử dụng:
Bạn sẽ được đưa đến Trang chủ (`HomeFrame`).  
Tại đây có thể:
- Nhấp vào "Thành viên gia đình" để xem danh sách (`FamilyMembersFrame`).
- Nhấp đúp vào tên của bạn (hoặc thành viên khác) để vào Trang chi tiết Hồ sơ (`MemberDetailsFrame`).

Từ đây, bạn có thể truy cập tất cả các mô-đun:
- Lịch sử bệnh
- Dị ứng
- Đơn thuốc
- Tiêm chủng
- Tài liệu y tế

Quay lại Trang chủ để **"Đặt lịch hẹn"** hoặc xem **"Hòm thư" (Thông báo)**.

---

## ✅ Tổng kết

Dự án EHR này giúp người dùng quản lý toàn diện thông tin sức khỏe cá nhân và gia đình, với giao diện trực quan, thân thiện và khả năng mở rộng dễ dàng.

---


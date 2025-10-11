# EHR Pure Java (No Framework) — Documents & Notifications

This is a **pure Java** (no frameworks) mini-project that implements:
- JDBC connection helper
- POJOs for `Documents` and `Notifications`
- Enums for database ENUMs
- DAO classes (CRUD + common queries)
- Console demo (`MainDemo`)

## Requirements
- JDK 11+ (recommended JDK 17)
- MySQL 8+
- MySQL Connector/J driver JAR placed in `libs/` (e.g. `libs/mysql-connector-j-9.0.0.jar`)

## Configure DB
Edit `src/main/java/com/mycompany/ehr/model/Database.java` and set:
```java
private static final String URL  = "jdbc:mysql://localhost:3306/ehr?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASS = "password";
```

## Compile & Run (Linux/Mac)
```bash
chmod +x scripts/compile.sh scripts/run.sh
./scripts/compile.sh
./scripts/run.sh
```

## Compile & Run (Windows)
```bat
scripts\compile.bat
scripts\run.bat
```

## SQL schema (Tables)
The DAOs assume the following MySQL tables exist:

```sql
-- Documents
CREATE TABLE Documents (
    document_id        INT AUTO_INCREMENT PRIMARY KEY,
    member_id          INT NOT NULL,
    appointment_id     INT,
    document_type      ENUM('XetNghiem', 'ChanDoanHinhAnh', 'DonThuoc', 'BenhAn', 'Khac') NOT NULL,
    title              VARCHAR(200) NOT NULL,
    file_path          VARCHAR(500) NOT NULL,
    description        TEXT,
    document_date      DATE,
    uploaded_by        INT NOT NULL,
    uploaded_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doc_member FOREIGN KEY (member_id) REFERENCES Family_Members(member_id),
    CONSTRAINT fk_doc_appointment FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id),
    CONSTRAINT fk_doc_user FOREIGN KEY (uploaded_by) REFERENCES Users(user_id)
);

-- Notifications
CREATE TABLE Notifications (
    notification_id        INT AUTO_INCREMENT PRIMARY KEY,
    recipient_user_id      INT NOT NULL,
    title                  VARCHAR(200) NOT NULL,
    message                TEXT NOT NULL,
    type                   ENUM('LichHen', 'Thuoc', 'XetNghiem', 'TiemChung', 'Khac') DEFAULT 'Khac',
    related_document_id    INT,
    related_appointment_id INT,
    is_read                BOOLEAN DEFAULT FALSE,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (recipient_user_id) REFERENCES Users(user_id),
    CONSTRAINT fk_notif_doc FOREIGN KEY (related_document_id) REFERENCES Documents(document_id),
    CONSTRAINT fk_notif_app FOREIGN KEY (related_appointment_id) REFERENCES Appointments(appointment_id)
);
```

## Notes
- This project is intentionally **framework-free**. No Spring, no JPA.
- Add indexes in your DB for better performance (optional):
  ```sql
  CREATE INDEX idx_documents_member ON Documents(member_id);
  CREATE INDEX idx_documents_date ON Documents(document_date);
  CREATE INDEX idx_noti_user ON Notifications(recipient_user_id);
  CREATE INDEX idx_noti_type ON Notifications(type);
  ```

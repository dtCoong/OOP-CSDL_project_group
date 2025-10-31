package com.mycompany.ehr.model;

import com.mycompany.ehr.dao.JDBCUtil;
import java.sql.Connection;
import java.sql.Statement;

public final class SchemaInit {
    private SchemaInit() {}
    public static void ensure() throws Exception {
        String sql = """
            CREATE TABLE IF NOT EXISTS documents(
              id INT AUTO_INCREMENT PRIMARY KEY,
              member_id INT NULL,
              appointment_id INT NULL,
              type VARCHAR(50) NULL,
              title VARCHAR(255) NULL,
              file_path VARCHAR(500) NULL,
              description TEXT NULL,
              document_date DATE NULL,
              uploaded_by_user_id INT NULL,
              uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
            ) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci
            """;
        try (Connection c = JDBCUtil.getConnection(); Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }
}

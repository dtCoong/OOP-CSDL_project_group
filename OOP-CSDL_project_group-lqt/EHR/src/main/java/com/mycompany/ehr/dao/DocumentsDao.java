package com.mycompany.ehr.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DocumentsDao {

    public int insert(Documents d) throws SQLException {
        String sql = """                INSERT INTO Documents
              (member_id, appointment_id, document_type, title, file_path, description,
               document_date, uploaded_by, uploaded_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, d.getMemberId());
            if (d.getAppointmentId() == null) ps.setNull(2, Types.INTEGER); else ps.setInt(2, d.getAppointmentId());
            ps.setString(3, d.getDocumentType().name());
            ps.setString(4, d.getTitle());
            ps.setString(5, d.getFilePath());
            if (d.getDescription() == null) ps.setNull(6, Types.LONGVARCHAR); else ps.setString(6, d.getDescription());
            if (d.getDocumentDate() == null) ps.setNull(7, Types.DATE); else ps.setDate(7, Date.valueOf(d.getDocumentDate()));
            ps.setInt(8, d.getUploadedBy());
            if (d.getUploadedAt() == null) ps.setNull(9, Types.TIMESTAMP); else ps.setTimestamp(9, Timestamp.valueOf(d.getUploadedAt()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public Optional<Documents> findById(int id) throws SQLException {
        String sql = "SELECT * FROM Documents WHERE document_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    public List<Documents> listByMember(int memberId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM Documents WHERE member_id = ? ORDER BY uploaded_at DESC LIMIT ? OFFSET ?";
        List<Documents> out = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
        }
        return out;
    }

    public List<Documents> search(Integer memberId, DocumentType type,
                                  LocalDate fromDate, LocalDate toDate, String keyword,
                                  int limit, int offset) throws SQLException {

        StringBuilder sb = new StringBuilder("SELECT * FROM Documents WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (memberId != null) { sb.append(" AND member_id = ?"); params.add(memberId); }
        if (type != null)     { sb.append(" AND document_type = ?"); params.add(type.name()); }
        if (fromDate != null) { sb.append(" AND document_date >= ?"); params.add(Date.valueOf(fromDate)); }
        if (toDate != null)   { sb.append(" AND document_date <= ?"); params.add(Date.valueOf(toDate)); }
        if (keyword != null && !keyword.isBlank()) {
            sb.append(" AND LOWER(title) LIKE LOWER(?)");
            params.add("%" + keyword + "%");
        }
        sb.append(" ORDER BY uploaded_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            List<Documents> out = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(mapRow(rs));
            }
            return out;
        }
    }

    public int updateTitleAndDescription(int id, String title, String description) throws SQLException {
        String sql = "UPDATE Documents SET title = ?, description = ? WHERE document_id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, title);
            if (description == null) ps.setNull(2, Types.LONGVARCHAR); else ps.setString(2, description);
            ps.setInt(3, id);
            return ps.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Documents WHERE document_id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Documents mapRow(ResultSet rs) throws SQLException {
        Documents d = new Documents();
        d.setDocumentId(rs.getInt("document_id"));
        d.setMemberId(rs.getInt("member_id"));
        int appt = rs.getInt("appointment_id"); d.setAppointmentId(rs.wasNull() ? null : appt);
        d.setDocumentType(DocumentType.fromDb(rs.getString("document_type")));
        d.setTitle(rs.getString("title"));
        d.setFilePath(rs.getString("file_path"));
        d.setDescription(rs.getString("description"));
        Date dd = rs.getDate("document_date"); d.setDocumentDate(dd == null ? null : dd.toLocalDate());
        d.setUploadedBy(rs.getInt("uploaded_by"));
        Timestamp ts = rs.getTimestamp("uploaded_at"); d.setUploadedAt(ts == null ? null : ts.toLocalDateTime());
        return d;
    }
}

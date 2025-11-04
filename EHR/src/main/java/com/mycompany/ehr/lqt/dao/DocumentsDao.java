package com.mycompany.ehr.lqt.dao;

import com.mycompany.ehr.lqt.model.DocumentType;
import com.mycompany.ehr.lqt.model.Documents;
import com.mycompany.ehr.util.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Documents - implements DAOInterface
 * Quản lý tất cả các thao tác CRUD với bảng documents
 */
public class DocumentsDao implements DAOInterface<Documents> {

    public int insert(Documents d) {
        String sql = "INSERT INTO documents(member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, d.getMemberId(), Types.INTEGER);
            ps.setObject(2, d.getAppointmentId(), Types.INTEGER);
            ps.setString(3, d.getType() == null ? null : d.getType().getDisplayName());
            ps.setString(4, d.getTitle());
            ps.setString(5, d.getFilePath());
            ps.setString(6, d.getDescription());
            ps.setObject(7, d.getDocumentDate());
            ps.setTimestamp(8, Timestamp.valueOf(d.getUploadedAt() == null ? LocalDateTime.now() : d.getUploadedAt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Documents> listAll(Integer filterMemberId, String titleLike) {
        List<Documents> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT document_id, member_id, appointment_id, document_type, title, file_path, description, document_date, uploaded_at FROM documents WHERE 1=1");
        if (filterMemberId != null) sb.append(" AND member_id=?");
        if (titleLike != null && !titleLike.isBlank()) sb.append(" AND title LIKE ?");
        sb.append(" ORDER BY document_id DESC");
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sb.toString())) {
            int idx = 1;
            if (filterMemberId != null) ps.setInt(idx++, filterMemberId);
            if (titleLike != null && !titleLike.isBlank()) ps.setString(idx++, "%" + titleLike + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(map(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public int deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        StringBuilder q = new StringBuilder("DELETE FROM documents WHERE document_id IN (");
        for (int i = 0; i < ids.size(); i++) q.append(i == 0 ? "?" : ",?");
        q.append(")");
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(q.toString())) {
            for (int i = 0; i < ids.size(); i++) ps.setInt(i + 1, ids.get(i));
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Documents map(ResultSet rs) throws SQLException {
        Documents d = new Documents();
        d.setId(rs.getInt("document_id"));
        d.setMemberId((Integer) rs.getObject("member_id"));
        d.setAppointmentId((Integer) rs.getObject("appointment_id"));
        String t = rs.getString("document_type");
        d.setType(DocumentType.fromDisplayName(t));
        d.setTitle(rs.getString("title"));
        d.setFilePath(rs.getString("file_path"));
        d.setDescription(rs.getString("description"));
        Date dd = rs.getDate("document_date");
        d.setDocumentDate(dd == null ? null : dd.toLocalDate());
        Timestamp up = rs.getTimestamp("uploaded_at");
        d.setUploadedAt(up == null ? null : up.toLocalDateTime());
        return d;
    }
    
    @Override
    public Documents selectById(Documents t) {
        if (t == null || t.getId() == null) return null;
        String sql = "SELECT * FROM documents WHERE document_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Documents t) {
        if (t == null || t.getId() == null) return 0;
        String sql = "UPDATE documents SET member_id=?, appointment_id=?, document_type=?, title=?, file_path=?, description=?, document_date=? WHERE document_id=?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, t.getMemberId(), Types.INTEGER);
            ps.setObject(2, t.getAppointmentId(), Types.INTEGER);
            ps.setString(3, t.getType() == null ? null : t.getType().getDisplayName());
            ps.setString(4, t.getTitle());
            ps.setString(5, t.getFilePath());
            ps.setString(6, t.getDescription());
            ps.setObject(7, t.getDocumentDate());
            ps.setInt(8, t.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Documents t) {
        if (t == null || t.getId() == null) return 0;
        String sql = "DELETE FROM documents WHERE document_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<Documents> selectAll() {
        return new ArrayList<>(listAll(null, null));
    }

    @Override
    public ArrayList<Documents> selectByCondition(String condition) {
        ArrayList<Documents> out = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE " + condition;
        try (Connection c = JDBCUtil.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) out.add(map(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public boolean exists(String condition) {
        String sql = "SELECT COUNT(*) FROM documents WHERE " + condition;
        try (Connection c = JDBCUtil.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
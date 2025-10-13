package com.mycompany.ehr.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationsDao {

    public int insert(Notifications n) throws SQLException {
        String sql = """                INSERT INTO Notifications
            (recipient_user_id, title, message, type, related_document_id,
             related_appointment_id, is_read, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, n.getRecipientUserId());
            ps.setString(2, n.getTitle());
            ps.setString(3, n.getMessage());
            ps.setString(4, (n.getType() == null ? NotificationType.Khac : n.getType()).name());

            if (n.getRelatedDocumentId() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, n.getRelatedDocumentId());
            if (n.getRelatedAppointmentId() == null) ps.setNull(6, Types.INTEGER); else ps.setInt(6, n.getRelatedAppointmentId());

            ps.setBoolean(7, n.getIsRead() != null && n.getIsRead());
            if (n.getCreatedAt() == null) ps.setNull(8, Types.TIMESTAMP); else ps.setTimestamp(8, Timestamp.valueOf(n.getCreatedAt()));

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Notifications> listByUser(int userId, int limit, int offset) throws SQLException {
        String sql = "SELECT * FROM Notifications WHERE recipient_user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<Notifications> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    public List<Notifications> listUnread(int userId, int limit, int offset) throws SQLException {
        String sql = """                SELECT * FROM Notifications
            WHERE recipient_user_id = ? AND is_read = FALSE
            ORDER BY created_at DESC LIMIT ? OFFSET ?
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<Notifications> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    public int countUnread(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE recipient_user_id = ? AND is_read = FALSE";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public List<Notifications> listByType(int userId, NotificationType type, int limit, int offset) throws SQLException {
        String sql = """                SELECT * FROM Notifications WHERE recipient_user_id = ? AND type = ?
            ORDER BY created_at DESC LIMIT ? OFFSET ?
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type.name());
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<Notifications> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    public List<Notifications> listByTimeRange(int userId, LocalDateTime from, LocalDateTime to,
                                               int limit, int offset) throws SQLException {
        String sql = """                SELECT * FROM Notifications
            WHERE recipient_user_id = ?
              AND ( ? IS NULL OR created_at >= ? )
              AND ( ? IS NULL OR created_at <= ? )
            ORDER BY created_at DESC LIMIT ? OFFSET ?
            """;
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            if (from == null) { ps.setNull(2, Types.TIMESTAMP); ps.setNull(3, Types.TIMESTAMP); }
            else { Timestamp f = Timestamp.valueOf(from); ps.setTimestamp(2, f); ps.setTimestamp(3, f); }

            if (to == null) { ps.setNull(4, Types.TIMESTAMP); ps.setNull(5, Types.TIMESTAMP); }
            else { Timestamp t = Timestamp.valueOf(to); ps.setTimestamp(4, t); ps.setTimestamp(5, t); }

            ps.setInt(6, limit);
            ps.setInt(7, offset);

            try (ResultSet rs = ps.executeQuery()) {
                List<Notifications> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        }
    }

    public int markAsRead(int notificationId) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE Notifications SET is_read = TRUE WHERE notification_id = ?")) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate();
        }
    }

    public int markAllAsReadForUser(int userId) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE Notifications SET is_read = TRUE WHERE recipient_user_id = ? AND is_read = FALSE")) {
            ps.setInt(1, userId);
            return ps.executeUpdate();
        }
    }

    public int deleteById(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Notifications WHERE notification_id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    private Notifications mapRow(ResultSet rs) throws SQLException {
        Notifications n = new Notifications();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setRecipientUserId(rs.getInt("recipient_user_id"));
        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        n.setType(NotificationType.fromDb(rs.getString("type")));
        int rd = rs.getInt("related_document_id");   n.setRelatedDocumentId(rs.wasNull() ? null : rd);
        int ra = rs.getInt("related_appointment_id");n.setRelatedAppointmentId(rs.wasNull() ? null : ra);
        n.setIsRead(rs.getBoolean("is_read"));
        Timestamp ts = rs.getTimestamp("created_at"); n.setCreatedAt(ts == null ? null : ts.toLocalDateTime());
        return n;
    }
}

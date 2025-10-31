package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.NotificationPriority;
import com.mycompany.ehr.model.NotificationType;
import com.mycompany.ehr.model.Notifications;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Notifications - implements DAOInterface
 * Quản lý tất cả các thao tác CRUD với bảng notifications
 */
public class NotificationsDao implements DAOInterface<Notifications> {

    @Override
    public int insert(Notifications n) {
        String sql = "INSERT INTO notifications(recipient_user_id, recipient_name, title, message, type, priority, " +
                     "related_document_id, related_appointment_id, is_read, created_at, action_url, requires_response) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setObject(1, n.getRecipientUserId());
            ps.setString(2, n.getRecipientName());
            ps.setString(3, n.getTitle());
            ps.setString(4, n.getMessage());
            ps.setString(5, n.getType() != null ? n.getType().name() : null);
            ps.setString(6, n.getPriority() != null ? n.getPriority().name() : NotificationPriority.NORMAL.name());
            ps.setObject(7, n.getRelatedDocumentId());
            ps.setObject(8, n.getRelatedAppointmentId());
            ps.setBoolean(9, n.getIsRead() != null ? n.getIsRead() : false);
            ps.setObject(10, n.getCreatedAt() != null ? n.getCreatedAt() : LocalDateTime.now());
            ps.setString(11, n.getActionUrl());
            ps.setBoolean(12, n.getRequiresResponse() != null ? n.getRequiresResponse() : false);
            
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Notifications selectById(Notifications t) {
        if (t == null || t.getNotificationId() == null) return null;
        
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, t.getNotificationId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int update(Notifications n) {
        if (n == null || n.getNotificationId() == null) return 0;
        
        String sql = "UPDATE notifications SET recipient_user_id=?, recipient_name=?, title=?, message=?, " +
                     "type=?, priority=?, related_document_id=?, related_appointment_id=?, is_read=?, " +
                     "read_at=?, action_url=?, requires_response=?, response_text=?, responded_at=? " +
                     "WHERE notification_id=?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setObject(1, n.getRecipientUserId());
            ps.setString(2, n.getRecipientName());
            ps.setString(3, n.getTitle());
            ps.setString(4, n.getMessage());
            ps.setString(5, n.getType() != null ? n.getType().name() : null);
            ps.setString(6, n.getPriority() != null ? n.getPriority().name() : null);
            ps.setObject(7, n.getRelatedDocumentId());
            ps.setObject(8, n.getRelatedAppointmentId());
            ps.setBoolean(9, n.getIsRead() != null ? n.getIsRead() : false);
            ps.setObject(10, n.getReadAt());
            ps.setString(11, n.getActionUrl());
            ps.setBoolean(12, n.getRequiresResponse() != null ? n.getRequiresResponse() : false);
            ps.setString(13, n.getResponseText());
            ps.setObject(14, n.getRespondedAt());
            ps.setInt(15, n.getNotificationId());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Notifications n) {
        if (n == null || n.getNotificationId() == null) return 0;
        
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, n.getNotificationId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public ArrayList<Notifications> selectAll() {
        ArrayList<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ArrayList<Notifications> selectByCondition(String condition) {
        ArrayList<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE " + condition + " ORDER BY created_at DESC";
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean exists(String condition) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE " + condition;
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==================== Custom Methods ====================
    
    /**
     * Lấy tất cả thông báo của một người dùng
     */
    public List<Notifications> getByRecipient(Integer userId) {
        List<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE recipient_user_id = ? ORDER BY created_at DESC";
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy thông báo chưa đọc của một người dùng
     */
    public List<Notifications> getUnreadByRecipient(Integer userId) {
        List<Notifications> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE recipient_user_id = ? AND is_read = false ORDER BY created_at DESC";
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Đếm số thông báo chưa đọc
     */
    public int countUnread(Integer userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE recipient_user_id = ? AND is_read = false";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Đánh dấu thông báo đã đọc
     */
    public boolean markAsRead(Integer notificationId) {
        String sql = "UPDATE notifications SET is_read = true, read_at = ? WHERE notification_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setObject(1, LocalDateTime.now());
            ps.setInt(2, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Đánh dấu tất cả thông báo đã đọc
     */
    public int markAllAsRead(Integer userId) {
        String sql = "UPDATE notifications SET is_read = true, read_at = ? WHERE recipient_user_id = ? AND is_read = false";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setObject(1, LocalDateTime.now());
            ps.setInt(2, userId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Xóa thông báo theo danh sách IDs
     */
    public int deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        
        StringBuilder sql = new StringBuilder("DELETE FROM notifications WHERE notification_id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append(i == 0 ? "?" : ",?");
        }
        sql.append(")");
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < ids.size(); i++) {
                ps.setInt(i + 1, ids.get(i));
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lọc thông báo theo nhiều điều kiện
     */
    public List<Notifications> filterNotifications(Integer userId, NotificationType type, Boolean isRead, 
                                                    LocalDateTime fromDate, LocalDateTime toDate) {
        List<Notifications> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM notifications WHERE 1=1");
        
        if (userId != null) {
            sql.append(" AND recipient_user_id = ?");
        }
        if (type != null) {
            sql.append(" AND type = ?");
        }
        if (isRead != null) {
            sql.append(" AND is_read = ?");
        }
        if (fromDate != null) {
            sql.append(" AND created_at >= ?");
        }
        if (toDate != null) {
            sql.append(" AND created_at <= ?");
        }
        sql.append(" ORDER BY created_at DESC");
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (userId != null) ps.setInt(paramIndex++, userId);
            if (type != null) ps.setString(paramIndex++, type.name());
            if (isRead != null) ps.setBoolean(paramIndex++, isRead);
            if (fromDate != null) ps.setObject(paramIndex++, fromDate);
            if (toDate != null) ps.setObject(paramIndex++, toDate);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm phản hồi cho thông báo
     */
    public boolean addResponse(Integer notificationId, String responseText) {
        String sql = "UPDATE notifications SET response_text = ?, responded_at = ? WHERE notification_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, responseText);
            ps.setObject(2, LocalDateTime.now());
            ps.setInt(3, notificationId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==================== Helper Methods ====================
    
    private Notifications mapRow(ResultSet rs) throws SQLException {
        Notifications n = new Notifications();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setRecipientUserId((Integer) rs.getObject("recipient_user_id"));
        n.setRecipientName(rs.getString("recipient_name"));
        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        
        String typeStr = rs.getString("type");
        n.setType(typeStr != null ? NotificationType.valueOf(typeStr) : null);
        
        String priorityStr = rs.getString("priority");
        n.setPriority(priorityStr != null ? NotificationPriority.valueOf(priorityStr) : NotificationPriority.NORMAL);
        
        n.setRelatedDocumentId((Integer) rs.getObject("related_document_id"));
        n.setRelatedAppointmentId((Integer) rs.getObject("related_appointment_id"));
        n.setIsRead(rs.getBoolean("is_read"));
        
        Timestamp createdTs = rs.getTimestamp("created_at");
        n.setCreatedAt(createdTs != null ? createdTs.toLocalDateTime() : null);
        
        Timestamp readTs = rs.getTimestamp("read_at");
        n.setReadAt(readTs != null ? readTs.toLocalDateTime() : null);
        
        n.setActionUrl(rs.getString("action_url"));
        n.setRequiresResponse(rs.getBoolean("requires_response"));
        n.setResponseText(rs.getString("response_text"));
        
        Timestamp respondedTs = rs.getTimestamp("responded_at");
        n.setRespondedAt(respondedTs != null ? respondedTs.toLocalDateTime() : null);
        
        return n;
    }
}

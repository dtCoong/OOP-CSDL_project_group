package com.mycompany.ehr.lqt.dao;

import com.mycompany.ehr.lqt.model.NotificationPriority;
import com.mycompany.ehr.lqt.model.NotificationType;
import com.mycompany.ehr.lqt.model.Notifications;
import com.mycompany.ehr.util.*;
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
        String sql = "INSERT INTO notifications(user_id, member_id, notification_type, title, message, " +
                     "reference_id, reference_type, scheduled_time, is_read, is_sent, priority) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setObject(1, n.getUserId());
            ps.setObject(2, n.getMemberId());
            ps.setString(3, n.getType() != null ? n.getType().getDisplayName() : null);
            ps.setString(4, n.getTitle());
            ps.setString(5, n.getMessage());
            ps.setObject(6, n.getReferenceId());
            ps.setString(7, n.getReferenceType());
            ps.setObject(8, n.getScheduledTime());
            ps.setBoolean(9, n.getIsRead() != null ? n.getIsRead() : false);
            ps.setBoolean(10, n.getIsSent() != null ? n.getIsSent() : false);
            ps.setString(11, n.getPriority() != null ? n.getPriority().getDisplayName() : NotificationPriority.NORMAL.getDisplayName());
            
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
        
        String sql = "UPDATE notifications SET user_id=?, member_id=?, notification_type=?, title=?, message=?, " +
                     "reference_id=?, reference_type=?, scheduled_time=?, is_read=?, is_sent=?, priority=? " +
                     "WHERE notification_id=?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setObject(1, n.getUserId());
            ps.setObject(2, n.getMemberId());
            ps.setString(3, n.getType() != null ? n.getType().getDisplayName() : null);
            ps.setString(4, n.getTitle());
            ps.setString(5, n.getMessage());
            ps.setObject(6, n.getReferenceId());
            ps.setString(7, n.getReferenceType());
            ps.setObject(8, n.getScheduledTime());
            ps.setBoolean(9, n.getIsRead() != null ? n.getIsRead() : false);
            ps.setBoolean(10, n.getIsSent() != null ? n.getIsSent() : false);
            ps.setString(11, n.getPriority() != null ? n.getPriority().getDisplayName() : null);
            ps.setInt(12, n.getNotificationId());
            
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
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY scheduled_time DESC, created_at DESC";
        
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
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = false ORDER BY scheduled_time DESC, created_at DESC";
        
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
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = false";
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
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, notificationId);
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
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ? AND is_read = false";
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
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
            sql.append(" AND user_id = ?");
        }
        if (type != null) {
            sql.append(" AND notification_type = ?");
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
        sql.append(" ORDER BY scheduled_time DESC, created_at DESC");
        
        try (Connection c = JDBCUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (userId != null) ps.setInt(paramIndex++, userId);
            if (type != null) ps.setString(paramIndex++, type.getDisplayName());
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
     * Thêm phản hồi cho thông báo (removed - không có trong schema mới)
     */
    @Deprecated
    public boolean addResponse(Integer notificationId, String responseText) {
        // Không còn được hỗ trợ trong schema mới
        return false;
    }

    // ==================== Helper Methods ====================
    
    private Notifications mapRow(ResultSet rs) throws SQLException {
        Notifications n = new Notifications();
        n.setNotificationId(rs.getInt("notification_id"));
        n.setUserId((Integer) rs.getObject("user_id"));
        n.setMemberId((Integer) rs.getObject("member_id"));
        n.setTitle(rs.getString("title"));
        n.setMessage(rs.getString("message"));
        
        String typeStr = rs.getString("notification_type");
        n.setType(typeStr != null ? NotificationType.fromDisplayName(typeStr) : null);
        
        String priorityStr = rs.getString("priority");
        n.setPriority(priorityStr != null ? NotificationPriority.fromDisplayName(priorityStr) : NotificationPriority.NORMAL);
        
        n.setReferenceId((Integer) rs.getObject("reference_id"));
        n.setReferenceType(rs.getString("reference_type"));
        n.setIsRead(rs.getBoolean("is_read"));
        n.setIsSent(rs.getBoolean("is_sent"));
        
        Timestamp scheduledTs = rs.getTimestamp("scheduled_time");
        n.setScheduledTime(scheduledTs != null ? scheduledTs.toLocalDateTime() : null);
        
        Timestamp createdTs = rs.getTimestamp("created_at");
        n.setCreatedAt(createdTs != null ? createdTs.toLocalDateTime() : null);
        
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        n.setUpdatedAt(updatedTs != null ? updatedTs.toLocalDateTime() : null);
        
        return n;
    }
}
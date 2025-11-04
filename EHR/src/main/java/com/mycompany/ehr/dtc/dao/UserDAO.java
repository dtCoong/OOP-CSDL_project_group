package com.mycompany.ehr.dtc.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;
import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.util.DAOInterface;
import com.mycompany.ehr.util.JDBCUtil;

public class UserDAO implements DAOInterface<User> {
    private static UserDAO instance;
    private UserDAO() {}
    public static UserDAO getInstance() {
        if (instance == null) instance = new UserDAO();
        return instance;
    }
    private String lastError = null;
    public String getLastError() { return lastError; }
    public void clearLastError() { lastError = null; }

    public boolean login(String username, String password) {
        String sql = "SELECT user_id, password_hash FROM Users WHERE username = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                
                if (BCrypt.checkpw(username + password, storedHash)) {
                    updateLastLogin(rs.getInt("user_id"));
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String rawPassword, String email, String phone, String fullName) {
        lastError = null;
        username = (username == null) ? "" : username.trim();
        email = (email == null) ? null : email.trim();
        phone = (phone == null) ? null : phone.trim();
        fullName = (fullName == null) ? null : fullName.trim();

        if (username.isEmpty() || rawPassword == null || rawPassword.isEmpty()) {
            lastError = "Username or password empty";
            return false;
        }

        String checkSql = "SELECT 1 FROM Users WHERE username = ? LIMIT 1";
        String insertSql = "INSERT INTO Users (username, password_hash, phone, full_name, is_active, created_at) VALUES (?, ?, ?, ?, ?, NOW())";

        try (Connection conn = JDBCUtil.getConnection()) {

            try {
                System.out.println("DEBUG Register - JDBC URL: " + conn.getMetaData().getURL());
                System.out.println("DEBUG Register - User: " + conn.getMetaData().getUserName());
                System.out.println("DEBUG Register - Catalog: " + conn.getCatalog());
            } catch (SQLException ignore) {}
            try (Statement st = conn.createStatement();
                 ResultSet rsCount = st.executeQuery("SELECT COUNT(*) FROM Users")) {
                if (rsCount.next()) {
                    System.out.println("DEBUG Register - Users count = " + rsCount.getInt(1));
                }
            } catch (SQLException ex) {
                System.out.println("DEBUG Register - count error: " + ex.getMessage());
            }

            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                System.out.println("DEBUG Register - checkSql: " + checkSql + " params=[" + username + "]");
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        conn.rollback();
                        lastError = "username_exists";
                        System.out.println("DEBUG Register - username exists -> abort");
                        return false;
                    }
                }
            }

            String hashedPassword = BCrypt.hashpw(username + rawPassword, BCrypt.gensalt(12));

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, hashedPassword);
                insertStmt.setString(3, (phone == null || phone.isEmpty()) ? null : phone);
                insertStmt.setString(4, (fullName == null || fullName.isEmpty()) ? null : fullName);
                insertStmt.setBoolean(5, true);

                int affected = insertStmt.executeUpdate();
                if (affected > 0) {
                    conn.commit();
                    System.out.println("DEBUG Register - inserted rows=" + affected);
                    return true;
                } else {
                    conn.rollback();
                    lastError = "insert_failed_no_rows";
                    return false;
                }
            } catch (SQLException ex) {
                conn.rollback();
                lastError = ex.getMessage();
                System.out.println("DEBUG Register - insert error: " + ex.getMessage());
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return false;
    }

    
    @Override
    public int insert(User user) {
        String sql = "INSERT INTO Users (username, password_hash, phone, full_name, is_active, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getPhone() == null ? "" : user.getPhone());
            ps.setString(4, user.getFullName() == null ? "" : user.getFullName());
            ps.setBoolean(5, user.isActive());
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return 0;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return affected;
        } catch (SQLIntegrityConstraintViolationException ex) {
            if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("username")) {
                 lastError = "username_exists";
            } else if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("phone")) {
                 lastError = "phone_exists";
            } else {
                 lastError = ex.getMessage();
            }
            System.out.println("INSERT constraint error: " + ex.getMessage());
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            lastError = ex.getMessage();
            return 0;
        }
    }
    @Override
    public int update(User user) {
        String sql = "UPDATE Users SET username=?, password_hash=?, phone=?, full_name=?, is_active=?, updated_at=NOW() WHERE user_id=?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPasswordHash());
            pst.setString(3, user.getPhone());
            pst.setString(4, user.getFullName());
            pst.setBoolean(5, user.isActive());
            pst.setInt(6, user.getUserId()); 
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public int delete(User user) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, user.getUserId());
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public ArrayList<User> selectAll() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) users.add(mapResultSetToUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public User selectById(User user) {
        return selectByIdInt(user.getUserId());
    }
    public User selectByIdInt(int id) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ArrayList<User> selectByCondition(String condition) {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE " + condition;
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) users.add(mapResultSetToUser(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public boolean exists(String condition) {
        String sql = "SELECT COUNT(*) FROM Users WHERE " + condition;
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String username = rs.getString("username");
        String passHash = rs.getString("password_hash");
        String phone = rs.getString("phone");
        String fullName = rs.getString("full_name");
        boolean active = rs.getBoolean("is_active");
        Timestamp tsLogin = rs.getTimestamp("last_login");
        Timestamp tsCreate = rs.getTimestamp("created_at");
        Timestamp tsUpdate = rs.getTimestamp("updated_at");
        return new User(
                id, username, passHash, phone, fullName, active,
                tsLogin != null ? tsLogin.toLocalDateTime() : null,
                tsCreate != null ? tsCreate.toLocalDateTime() : LocalDateTime.now(),
                tsUpdate != null ? tsUpdate.toLocalDateTime() : LocalDateTime.now()
        );
    }
    public void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET last_login = NOW() WHERE user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, userId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ? LIMIT 1";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
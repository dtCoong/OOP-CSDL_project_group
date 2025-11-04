package com.mycompany.ehr.lqt.model;

import java.awt.Color;

public enum NotificationPriority {
    LOW("Thấp", new Color(76, 175, 80), "🟢"),
    NORMAL("Bình thường", new Color(33, 150, 243), "🔵"),
    HIGH("Cao", new Color(255, 152, 0), "🟠"),
    URGENT("Khẩn cấp", new Color(244, 67, 54), "🔴");
    
    private final String displayName;
    private final Color color;
    private final String icon;
    
    NotificationPriority(String displayName, Color color, String icon) {
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Color getColor() {
        return color;
    }
    
    public String getIcon() {
        return icon;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    // Chuyển đổi từ tên database ENUM thành Java enum
    public static NotificationPriority fromDisplayName(String displayName) {
        if (displayName == null) return NORMAL;
        for (NotificationPriority priority : values()) {
            if (priority.displayName.equals(displayName)) {
                return priority;
            }
        }
        return NORMAL; // Mặc định
    }
}

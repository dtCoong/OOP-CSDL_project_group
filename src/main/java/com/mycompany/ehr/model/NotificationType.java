package com.mycompany.ehr.model;

public enum NotificationType {
    LichHen, Thuoc, XetNghiem, TiemChung, Khac;
    public static NotificationType fromDb(String s) {
        return s == null ? null : NotificationType.valueOf(s);
    }
}

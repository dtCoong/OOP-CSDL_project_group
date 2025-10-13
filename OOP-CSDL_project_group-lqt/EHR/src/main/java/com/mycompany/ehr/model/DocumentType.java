package com.mycompany.ehr.model;

public enum DocumentType {
    XetNghiem, ChanDoanHinhAnh, DonThuoc, BenhAn, Khac;
    public static DocumentType fromDb(String s) {
        return s == null ? null : DocumentType.valueOf(s);
    }
}

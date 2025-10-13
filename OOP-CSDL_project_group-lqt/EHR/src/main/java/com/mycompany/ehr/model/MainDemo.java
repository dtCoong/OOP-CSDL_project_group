package com.mycompany.ehr.model;

import java.time.LocalDate;
import java.util.List;

public class MainDemo {
    public static void main(String[] args) throws Exception {
        DocumentsDao docDao = new DocumentsDao();
        NotificationsDao notiDao = new NotificationsDao();

        // 1) Insert a document
        Documents d = new Documents();
        d.setMemberId(1001);
        d.setDocumentType(DocumentType.XetNghiem);
        d.setTitle("XN mau tong quat");
        d.setFilePath("/files/1001/xn-mau-2025-10-10.pdf");
        d.setDescription("Demo insert - Cholesterol OK");
        d.setDocumentDate(LocalDate.now());
        d.setUploadedBy(7);

        int newDocId = docDao.insert(d);
        System.out.println("Inserted document id = " + newDocId);

        // 2) Create a notification
        Notifications n = new Notifications();
        n.setRecipientUserId(7);
        n.setTitle("Tai lieu moi");
        n.setMessage("Ban vua tai len: " + d.getTitle());
        n.setType(NotificationType.XetNghiem);
        n.setRelatedDocumentId(newDocId);
        notiDao.insert(n);

        // 3) List documents by member
        List<Documents> docs = docDao.listByMember(1001, 20, 0);
        System.out.println("Documents of member 1001:");
        for (Documents it : docs) System.out.println(" - " + it);

        // 4) Count unread notifications
        int unread = notiDao.countUnread(7);
        System.out.println("Unread notifications of user 7: " + unread);

        // 5) Mark all read
        notiDao.markAllAsReadForUser(7);
        System.out.println("Marked all as read for user 7");
    }
}

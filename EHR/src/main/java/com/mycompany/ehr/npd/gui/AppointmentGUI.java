package com.mycompany.ehr.npd.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LỚP 1: LỚP MAIN (public)
 */
public class AppointmentGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý Lịch hẹn");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            frame.add(new AppointmentPanel());
            
            frame.setMinimumSize(new Dimension(800, 600));
            frame.pack(); 
            frame.setLocationRelativeTo(null); 
            frame.setVisible(true);
        });
    }
}

/**
 * LỚP 2: VIEW (GUI)
 */
class AppointmentPanel extends JPanel {

    private final AppointmentController controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    private JTextField idField, appointmentDateField, chiefComplaintField, costField;
    private JSpinner memberIdSpinner, doctorIdSpinner;
    private JComboBox<Appointment.AppointmentType> typeComboBox;
    private JComboBox<Appointment.AppointmentStatus> statusComboBox;
    private JComboBox<Appointment.PaymentStatus> paymentStatusComboBox;
    
    private JButton addButton, updateButton, deleteButton, clearButton;

    /**
     * Lớp nội bộ (inner class) để render tên hiển thị của Enums trong JComboBox.
     */
    private class EnumRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Appointment.AppointmentType) {
                setText(((Appointment.AppointmentType) value).getDisplayName());
            } else if (value instanceof Appointment.AppointmentStatus) {
                setText(((Appointment.AppointmentStatus) value).getDisplayName());
            } else if (value instanceof Appointment.PaymentStatus) {
                setText(((Appointment.PaymentStatus) value).getDisplayName());
            }
            return this;
        }
    }


    public AppointmentPanel() {
        this.controller = new AppointmentController();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.SOUTH);

        loadAppointmentData();
    }

    private JComponent createTablePanel() {
        String[] columnNames = {"ID", "Bệnh nhân (ID)", "Bác sĩ (ID)", "Ngày hẹn", "Loại", "Trạng thái", "Triệu chứng"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentTable = new JTable(tableModel);

        appointmentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySelectedRowData();
            }
        });

        return new JScrollPane(appointmentTable);
    }

    private JComponent createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(0, 4, 10, 5)); 
        
        fieldsPanel.add(new JLabel("ID Lịch hẹn:"));
        idField = new JTextField();
        idField.setEditable(false);
        fieldsPanel.add(idField);
        
        fieldsPanel.add(new JLabel("ID Bệnh nhân:"));
        memberIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        fieldsPanel.add(memberIdSpinner);

        fieldsPanel.add(new JLabel("Ngày hẹn (yyyy-MM-dd HH:mm):"));
        appointmentDateField = new JTextField();
        fieldsPanel.add(appointmentDateField);

        fieldsPanel.add(new JLabel("ID Bác sĩ:"));
        doctorIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        fieldsPanel.add(doctorIdSpinner);
        
        fieldsPanel.add(new JLabel("Loại hẹn:"));
        typeComboBox = new JComboBox<>(Appointment.AppointmentType.values());
        typeComboBox.setRenderer(new EnumRenderer());
        fieldsPanel.add(typeComboBox);

        fieldsPanel.add(new JLabel("Chi phí:"));
        costField = new JTextField("0.00");
        fieldsPanel.add(costField);
        
        fieldsPanel.add(new JLabel("Trạng thái hẹn:"));
        statusComboBox = new JComboBox<>(Appointment.AppointmentStatus.values());
        statusComboBox.setRenderer(new EnumRenderer());
        fieldsPanel.add(statusComboBox);

        fieldsPanel.add(new JLabel("Trạng thái thanh toán:"));
        paymentStatusComboBox = new JComboBox<>(Appointment.PaymentStatus.values());
        paymentStatusComboBox.setRenderer(new EnumRenderer());
        fieldsPanel.add(paymentStatusComboBox);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(new JLabel("Triệu chứng:"), BorderLayout.NORTH);
        chiefComplaintField = new JTextField();
        bottomPanel.add(chiefComplaintField, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addButton = new JButton("Thêm");
        updateButton = new JButton("Cập Nhật");
        deleteButton = new JButton("Xóa");
        clearButton = new JButton("Làm Mới");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        addListeners();

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private void loadAppointmentData() {
        tableModel.setRowCount(0); 
        
        List<Appointment> appointments = controller.getAllAppointments();
        for (Appointment app : appointments) {
            tableModel.addRow(new Object[]{
                app.getAppointmentId(),
                app.getMemberId(),
                app.getDoctorId(),
                app.getAppointmentDate() != null ? app.getAppointmentDate().format(formatter) : null,
                app.getType() != null ? app.getType().getDisplayName() : null,
                app.getStatus() != null ? app.getStatus().getDisplayName() : null,
                app.getChiefComplaint()
            });
        }
    }

    private void displaySelectedRowData() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow < 0) return;
        
        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        Appointment selectedApp = controller.getAllAppointments().stream()
                                    .filter(a -> a.getAppointmentId().equals(id))
                                    .findFirst().orElse(null);
        
        if (selectedApp == null) {
            clearForm();
            return;
        }

        idField.setText(selectedApp.getAppointmentId().toString());
        memberIdSpinner.setValue(selectedApp.getMemberId() != null ? selectedApp.getMemberId() : 1);
        doctorIdSpinner.setValue(selectedApp.getDoctorId() != null ? selectedApp.getDoctorId() : 1);
        appointmentDateField.setText(selectedApp.getAppointmentDate() != null ? selectedApp.getAppointmentDate().format(formatter) : "");
        chiefComplaintField.setText(selectedApp.getChiefComplaint());
        costField.setText(selectedApp.getCost() != null ? selectedApp.getCost().toPlainString() : "0.00");
        
        typeComboBox.setSelectedItem(selectedApp.getType());
        statusComboBox.setSelectedItem(selectedApp.getStatus());
        paymentStatusComboBox.setSelectedItem(selectedApp.getPaymentStatus());
    }

    private void clearForm() {
        idField.setText("");
        memberIdSpinner.setValue(1);
        doctorIdSpinner.setValue(1);
        appointmentDateField.setText("");
        chiefComplaintField.setText("");
        costField.setText("0.00");
        typeComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        paymentStatusComboBox.setSelectedIndex(0);
        appointmentTable.clearSelection();
    }
    
    private void addListeners() {
        addButton.addActionListener(e -> addAppointment());
        updateButton.addActionListener(e -> updateAppointment());
        deleteButton.addActionListener(e -> deleteAppointment());
        clearButton.addActionListener(e -> clearForm());
    }

    private void addAppointment() {
        try {
            boolean success = controller.addAppointment(
                (Integer) memberIdSpinner.getValue(),
                (Integer) doctorIdSpinner.getValue(),
                appointmentDateField.getText(),
                (Appointment.AppointmentType) typeComboBox.getSelectedItem(),
                (Appointment.AppointmentStatus) statusComboBox.getSelectedItem(),
                chiefComplaintField.getText(),
                costField.getText(),
                (Appointment.PaymentStatus) paymentStatusComboBox.getSelectedItem()
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm lịch hẹn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadAppointmentData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Kiểm tra lại định dạng ngày (yyyy-MM-dd HH:mm).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAppointment() {
        try {
            Integer id = Integer.parseInt(idField.getText());
            
            boolean success = controller.updateAppointment(
                id,
                (Integer) memberIdSpinner.getValue(),
                (Integer) doctorIdSpinner.getValue(),
                appointmentDateField.getText(),
                (Appointment.AppointmentType) typeComboBox.getSelectedItem(),
                (Appointment.AppointmentStatus) statusComboBox.getSelectedItem(),
                chiefComplaintField.getText(),
                costField.getText(),
                (Appointment.PaymentStatus) paymentStatusComboBox.getSelectedItem()
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadAppointmentData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Kiểm tra lại định dạng ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lịch hẹn từ bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAppointment() {
        try {
            int id = Integer.parseInt(idField.getText());
            
            int choice = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa lịch hẹn này?", "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (choice == JOptionPane.YES_OPTION) {
                if (controller.deleteAppointment(id)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadAppointmentData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại (Không tìm thấy ID).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lịch hẹn từ bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}


/**
 * LỚP 3: CONTROLLER
 */
class AppointmentController {

    private AppointmentDAOMock appointmentDAO;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public AppointmentController() {
        this.appointmentDAO = AppointmentDAOMock.getInstance();
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.selectAll();
    }

    public boolean addAppointment(Integer memberId, Integer doctorId, String appointmentDateStr,
                                  Appointment.AppointmentType type, Appointment.AppointmentStatus status,
                                  String chiefComplaint, String costStr, Appointment.PaymentStatus paymentStatus) {
        try {
            LocalDateTime appointmentDate = LocalDateTime.parse(appointmentDateStr, formatter);
            BigDecimal cost = new BigDecimal(costStr);

            if (memberId <= 0 || doctorId <= 0) return false;
            
            Appointment app = new Appointment();
            app.setMemberId(memberId);
            app.setDoctorId(doctorId);
            app.setAppointmentDate(appointmentDate);
            app.setType(type);
            app.setStatus(status);
            app.setChiefComplaint(chiefComplaint);
            app.setCost(cost);
            app.setPaymentStatus(paymentStatus);
            
            return appointmentDAO.insert(app) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }
    
    public boolean updateAppointment(Integer appointmentId, Integer memberId, Integer doctorId, String appointmentDateStr,
                                     Appointment.AppointmentType type, Appointment.AppointmentStatus status,
                                     String chiefComplaint, String costStr, Appointment.PaymentStatus paymentStatus) {
        try {
            LocalDateTime appointmentDate = LocalDateTime.parse(appointmentDateStr, formatter);
            BigDecimal cost = new BigDecimal(costStr);

            if (appointmentId <= 0 || memberId <= 0 || doctorId <= 0) return false;

            Appointment app = new Appointment();
            app.setAppointmentId(appointmentId); 
            app.setMemberId(memberId);
            app.setDoctorId(doctorId);
            app.setAppointmentDate(appointmentDate);
            app.setType(type);
            app.setStatus(status);
            app.setChiefComplaint(chiefComplaint);
            app.setCost(cost);
            app.setPaymentStatus(paymentStatus);

            return appointmentDAO.update(app) > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteAppointment(int id) {
        if (id <= 0) return false;
        Appointment app = new Appointment();
        app.setAppointmentId(id);
        return appointmentDAO.delete(app) > 0;
    }
}


/**
 * LỚP 4: DAO (MOCK)
 */
class AppointmentDAOMock {

    private ArrayList<Appointment> appointments = new ArrayList<>();
    private AtomicInteger counter = new AtomicInteger(0);

    private static AppointmentDAOMock instance;

    private AppointmentDAOMock() {
        Appointment a1 = new Appointment();
        a1.setMemberId(1);
        a1.setDoctorId(1);
        a1.setAppointmentDate(LocalDateTime.now().plusDays(1));
        a1.setType(Appointment.AppointmentType.KHAM_TONG_QUAT);
        a1.setStatus(Appointment.AppointmentStatus.DA_DAT);
        a1.setChiefComplaint("Đau đầu, chóng mặt");
        a1.setCost(new BigDecimal("150000.00"));
        a1.setPaymentStatus(Appointment.PaymentStatus.CHUA_THANH_TOAN);
        insert(a1);

        Appointment a2 = new Appointment();
        a2.setMemberId(2);
        a2.setDoctorId(1);
        a2.setAppointmentDate(LocalDateTime.now().plusDays(2));
        a2.setType(Appointment.AppointmentType.TAI_KHAM);
        a2.setStatus(Appointment.AppointmentStatus.DA_DAT);
        a2.setChiefComplaint("Tái khám sau phẫu thuật");
        a2.setCost(new BigDecimal("100000.00"));
        a2.setPaymentStatus(Appointment.PaymentStatus.BAO_HIEM);
        insert(a2);
    }

    public static AppointmentDAOMock getInstance() {
        if (instance == null) {
            instance = new AppointmentDAOMock();
        }
        return instance;
    }

    public int insert(Appointment t) {
        t.setAppointmentId(counter.incrementAndGet()); 
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());
        appointments.add(t);
        return 1;
    }

    public int update(Appointment t) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(t.getAppointmentId())) {
                t.setUpdatedAt(LocalDateTime.now());
                t.setCreatedAt(appointments.get(i).getCreatedAt()); 
                appointments.set(i, t);
                return 1;
            }
        }
        return 0;
    }

    public int delete(Appointment t) {
        boolean removed = appointments.removeIf(a -> a.getAppointmentId().equals(t.getAppointmentId()));
        return removed ? 1 : 0;
    }

    public Appointment selectById(Appointment t) {
        return appointments.stream()
                .filter(a -> a.getAppointmentId().equals(t.getAppointmentId()))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<Appointment> selectAll() {
        return new ArrayList<>(appointments); 
    }
}


/**
 * LỚP 5: MODEL
 */
class Appointment {
    public enum AppointmentStatus {
        DA_DAT("Đã đặt"),
        HOAN_THANH("Hoàn thành"),
        HUY_BO("Hủy bỏ");

        private final String displayName;
        AppointmentStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum AppointmentType {
        KHAM_TONG_QUAT("Khám tổng quát"),
        TAI_KHAM("Tái khám");

        private final String displayName;
        AppointmentType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum PaymentStatus {
        CHUA_THANH_TOAN("Chưa thanh toán"),
        DA_THANH_TOAN("Đã thanh toán"),
        BAO_HIEM("Bảo hiểm chi trả");

        private final String displayName;
        PaymentStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    // Các trường dữ liệu (private)
    private Integer appointmentId;
    private Integer memberId;
    private Integer doctorId;
    private Integer hospitalId;
    private Integer departmentId;
    private LocalDateTime appointmentDate;
    private AppointmentType type;
    private AppointmentStatus status;
    private String chiefComplaint;
    private String diagnosis;
    private String treatmentNotes;
    private LocalDate followUpDate;
    private BigDecimal cost;
    private PaymentStatus paymentStatus;
    private String roomNumber;
    private Integer queueNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment() {
    }

    // Getters and Setters
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public Integer getHospitalId() { return hospitalId; }
    public void setHospitalId(Integer hospitalId) { this.hospitalId = hospitalId; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatmentNotes() { return treatmentNotes; }
    public void setTreatmentNotes(String treatmentNotes) { this.treatmentNotes = treatmentNotes; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Integer getQueueNumber() { return queueNumber; }
    public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", memberId=" + memberId +
                ", appointmentDate=" + appointmentDate +
                ", status=" + status +
                '}';
    }
}

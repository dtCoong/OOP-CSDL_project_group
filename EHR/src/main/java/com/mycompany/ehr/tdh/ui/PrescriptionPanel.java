package com.mycompany.ehr.tdh.ui;

import com.mycompany.ehr.tdh.dao.PrescriptionDAO;
import com.mycompany.ehr.tdh.dao.PrescriptionDetailDAO;
import com.mycompany.ehr.tdh.dao.MedicationScheduleDAO;
import com.mycompany.ehr.tdh.model.Prescription;
import com.mycompany.ehr.tdh.model.PrescriptionDetail;
import com.mycompany.ehr.tdh.model.MedicationSchedule;

import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.dtc.model.FamilyMembers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrescriptionPanel extends JPanel {

    private final PrescriptionDAO prescriptionDAO;
    private final PrescriptionDetailDAO detailDAO;
    private final MedicationScheduleDAO scheduleDAO;

    private User currentUser;
    private FamilyMembers selectedMember;

    private JPanel mainDisplayPanel;
    private JScrollPane mainScrollPane;
    
    private final String[] prescriptionColumnNames = {
        "Mã đơn thuốc", "Mã người nhà", "Ngày kê đơn",
        "Chẩn đoán", "Ghi chú", "Chi phí", "Trạng thái",
        "Ngày tạo", "Ngày cập nhật"
    };
    
    private JTable detailTable;
    private DefaultTableModel detailTableModel;
    private final String[] detailColumnNames = {
        "Mã đơn thuốc chi tiết", "Mã thuốc", "Liều lượng", "Tần suất", "Số ngày", "Tổng SL",
        "Hướng dẫn", "Bắt đầu", "Kết thúc", "Trạng thái"
    };

    private JTable scheduleTable;
    private DefaultTableModel scheduleTableModel;
    private final String[] scheduleColumnNames = {
        "Mã đơn thuốc chi tiết", "Giờ uống", "Liều lượng/ngày", "Hoạt động"
    };

    public PrescriptionPanel(User user, FamilyMembers member) {
        this.prescriptionDAO = new PrescriptionDAO();
        this.detailDAO = new PrescriptionDetailDAO();
        this.scheduleDAO = new MedicationScheduleDAO();
        this.currentUser = user;
        this.selectedMember = member;
        
        setLayout(new BorderLayout()); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadTableData(); 
    }

    private void initComponents() {

        mainDisplayPanel = new JPanel();
        mainDisplayPanel.setLayout(new BoxLayout(mainDisplayPanel, BoxLayout.Y_AXIS));
        mainDisplayPanel.setBackground(Color.WHITE); 
        mainScrollPane = new JScrollPane(mainDisplayPanel);
        mainScrollPane.setBorder(BorderFactory.createTitledBorder("Đơn thuốc của: " + selectedMember.getName() + " (Nhấp vào để xem chi tiết)"));
        mainScrollPane.setPreferredSize(new Dimension(800, 250)); 
        

        detailTableModel = new DefaultTableModel(detailColumnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        detailTable = new JTable(detailTableModel);
        setupDetailTableColumns(detailTable);
        JScrollPane detailScrollPane = new JScrollPane(detailTable);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("1. Chi tiết thuốc kê đơn"));

        scheduleTableModel = new DefaultTableModel(scheduleColumnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        scheduleTable = new JTable(scheduleTableModel);
        setupScheduleTableColumns(scheduleTable);
        JScrollPane scheduleScrollPane = new JScrollPane(scheduleTable);
        scheduleScrollPane.setBorder(BorderFactory.createTitledBorder("2. Lịch uống thuốc"));

        JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, detailScrollPane, scheduleScrollPane);
        bottomSplitPane.setResizeWeight(0.6); 

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainScrollPane, bottomSplitPane);
        mainSplitPane.setResizeWeight(0.5); 

        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private void addPrescriptionToTable(Prescription p, DefaultTableModel model) {
        Object[] row = { p.getPrescriptionId(), p.getMemberId(), p.getPrescriptionDate(), 
                         p.getDiagnosis(), p.getNotes(), p.getTotalCost(), p.getStatus(), 
                         p.getCreatedAt(), p.getUpdatedAt() };
        model.addRow(row);
    }
    private void setupPrescriptionTableColumns(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  
        table.getColumnModel().getColumn(2).setPreferredWidth(100); 
        table.getColumnModel().getColumn(3).setPreferredWidth(200); 
        table.getColumnModel().getColumn(4).setPreferredWidth(200); 
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  
        table.getColumnModel().getColumn(6).setPreferredWidth(100); 
        table.getColumnModel().getColumn(7).setPreferredWidth(140); 
        table.getColumnModel().getColumn(8).setPreferredWidth(140); 
    }
    private void addDetailToTable(PrescriptionDetail d, DefaultTableModel model) {
        Object[] row = {
            d.getDetailId(),
            d.getMedicationId(), 
            d.getDosage(), d.getFrequency(), d.getDurationDays(),    
            d.getTotalQuantity(), d.getInstructions(), d.getStartDate(),
            d.getEndDate(), d.getStatus()     
        };
        model.addRow(row);
    }
    private void setupDetailTableColumns(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(70);  
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  
        table.getColumnModel().getColumn(2).setPreferredWidth(100); 
        table.getColumnModel().getColumn(3).setPreferredWidth(100); 
        table.getColumnModel().getColumn(4).setPreferredWidth(60);  
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  
        table.getColumnModel().getColumn(6).setPreferredWidth(200); 
        table.getColumnModel().getColumn(7).setPreferredWidth(100); 
        table.getColumnModel().getColumn(8).setPreferredWidth(100); 
        table.getColumnModel().getColumn(9).setPreferredWidth(100); 
    }
    private void addScheduleToTable(MedicationSchedule s, DefaultTableModel model) {
        Object[] row = {
            s.getDetailId(), s.getScheduledTime(), 
            s.getDailyDosage(), s.isActive()
        };
        model.addRow(row);
    }
    private void setupScheduleTableColumns(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  
        table.getColumnModel().getColumn(1).setPreferredWidth(100); 
        table.getColumnModel().getColumn(2).setPreferredWidth(120); 
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  
    }
    private MouseListener createTableMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable)evt.getSource();
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0) {
                    int prescriptionId = (int)table.getModel().getValueAt(
                            table.convertRowIndexToModel(row), 0);
                    
                    loadDetailTable(prescriptionId);
                    loadScheduleTable(prescriptionId);
                }
            }
        };
    }
    private void loadTableData() {
        mainDisplayPanel.removeAll();
        detailTableModel.setRowCount(0); 
        scheduleTableModel.setRowCount(0); 
        
        try {
            String condition = "member_id = " + selectedMember.getMemberId();
            ArrayList<Prescription> list = prescriptionDAO.selectByCondition(condition);

            if (list == null || list.isEmpty()) {
                JLabel emptyLabel = new JLabel("Không tìm thấy đơn thuốc nào cho " + selectedMember.getName());
                emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                mainDisplayPanel.add(emptyLabel);
                mainDisplayPanel.revalidate();
                mainDisplayPanel.repaint();
                return;
            }
            
            Map<LocalDate, List<Prescription>> groupedByDate = list.stream()
                .collect(Collectors.groupingBy(Prescription::getPrescriptionDate));
            List<LocalDate> sortedDates = groupedByDate.keySet().stream().sorted().collect(Collectors.toList());

            for (LocalDate date : sortedDates) {
                List<Prescription> dailyPrescriptions = groupedByDate.get(date);
                JLabel dateLabel = new JLabel("Ngày kê đơn: " + date.toString());
                dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                mainDisplayPanel.add(dateLabel);

                DefaultTableModel model = new DefaultTableModel(prescriptionColumnNames, 0) {
                    @Override public boolean isCellEditable(int row, int column) { return false; }
                };
                JTable table = new JTable(model);
                setupPrescriptionTableColumns(table);
                table.addMouseListener(createTableMouseListener()); 

                for (Prescription p : dailyPrescriptions) {
                    addPrescriptionToTable(p, model);
                }

                table.getTableHeader().setAlignmentX(Component.LEFT_ALIGNMENT);
                table.setAlignmentX(Component.LEFT_ALIGNMENT);
                mainDisplayPanel.add(table.getTableHeader());
                mainDisplayPanel.add(table);
                mainDisplayPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
            }

            mainDisplayPanel.revalidate();
            mainDisplayPanel.repaint();
            SwingUtilities.invokeLater(() -> mainScrollPane.getVerticalScrollBar().setValue(0));
            
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Lỗi khi tải đơn thuốc: " + e.getMessage(), "Lỗi SQL/DAO", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }

    private void addListeners() {
    }

    private void loadDetailTable(int prescriptionId) {
        detailTableModel.setRowCount(0);
        try {
            String condition = "prescription_id = " + prescriptionId;
            ArrayList<PrescriptionDetail> list = detailDAO.selectByCondition(condition);
            if (list != null) {
                for (PrescriptionDetail d : list) {
                    addDetailToTable(d, detailTableModel);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải chi tiết đơn thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadScheduleTable(int prescriptionId) {
        scheduleTableModel.setRowCount(0);
        try {
            String condition = "detail_id IN (SELECT detail_id FROM Prescription_Details WHERE prescription_id = " + prescriptionId + ")";
            ArrayList<MedicationSchedule> list = scheduleDAO.selectByCondition(condition);
            if (list != null) {
                for (MedicationSchedule s : list) {
                    addScheduleToTable(s, scheduleTableModel);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch uống thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
package com.mycompany.ehr;

import com.mycompany.ehr.dao.VaccinationRecordsDao;
import com.mycompany.ehr.dao.VaccinationRecordsDaoImpl;
import com.mycompany.ehr.dao.VaccineTemplatesDao;
import com.mycompany.ehr.dao.VaccineTemplatesDaoImpl;
import com.mycompany.ehr.ui.MainApplicationFrame; 

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            VaccineTemplatesDao templateDao = new VaccineTemplatesDaoImpl();
            VaccinationRecordsDao recordDao = new VaccinationRecordsDaoImpl();

            MainApplicationFrame mainFrame = new MainApplicationFrame(templateDao, recordDao);

            mainFrame.setVisible(true);
        });
    }
}
package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.VaccinationRecords;

import java.util.ArrayList;

public interface VaccinationRecordsDao {

    int insert(VaccinationRecords t);

    VaccinationRecords selectById(VaccinationRecords t);

    int update(VaccinationRecords t);

    int delete(VaccinationRecords t);

    ArrayList<VaccinationRecords> selectAll();

    ArrayList<VaccinationRecords> selectByCondition(String condition);

    boolean exists(String condition);
}
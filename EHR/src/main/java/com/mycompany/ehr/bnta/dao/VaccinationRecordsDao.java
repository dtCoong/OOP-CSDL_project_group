package com.mycompany.ehr.bnta.dao;

import com.mycompany.ehr.bnta.model.VaccinationRecords;

import java.util.ArrayList;

public interface VaccinationRecordsDao {

    VaccinationRecords selectById(VaccinationRecords t);

    ArrayList<VaccinationRecords> selectAll();

    ArrayList<VaccinationRecords> selectByMemberId(int memberId);
}

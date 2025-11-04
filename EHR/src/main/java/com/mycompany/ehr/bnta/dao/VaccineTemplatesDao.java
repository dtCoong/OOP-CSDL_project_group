package com.mycompany.ehr.bnta.dao;

import com.mycompany.ehr.bnta.model.VaccineTemplates;

import java.util.ArrayList;

public interface VaccineTemplatesDao {

    int insert(VaccineTemplates t);

    VaccineTemplates selectById(VaccineTemplates t);

    int update(VaccineTemplates t);

    int delete(VaccineTemplates t);

    ArrayList<VaccineTemplates> selectAll(); 

    ArrayList<VaccineTemplates> selectByCondition(String condition);

    boolean exists(String condition);
}
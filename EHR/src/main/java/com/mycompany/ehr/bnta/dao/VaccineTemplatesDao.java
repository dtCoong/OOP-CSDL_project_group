package com.mycompany.ehr.bnta.dao;

import com.mycompany.ehr.bnta.model.VaccineTemplates;

import java.util.ArrayList;

public interface VaccineTemplatesDao {

    VaccineTemplates selectById(VaccineTemplates t);

    ArrayList<VaccineTemplates> selectAll(); 
}

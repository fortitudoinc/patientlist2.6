/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public interface SpecialtyTypeItemDao {
	
	public SpecialtyTypeItem saveSpecialtyTypeItem(SpecialtyTypeItem item) throws APIException;
	
	public SpecialtyTypeItem getSpecialtyTypeItem(Integer id);
	
	public List<SpecialtyTypeItem> getAllSpecialtyTypeItem();
}

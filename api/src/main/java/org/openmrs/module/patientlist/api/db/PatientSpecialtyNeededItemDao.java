/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItemConfig;
import org.springframework.transaction.annotation.Transactional;

public interface PatientSpecialtyNeededItemDao {
	
	public PatientSpecialtyNeededItem savePatientSpecialtyNeededItem(PatientSpecialtyNeededItem item);
	
	public PatientSpecialtyNeededItem getPatientSpecialtyNeededItem(Integer id);
	
	public List<PatientSpecialtyNeededItem> getAllPatientSpecialtyNeededItem();
	
	public List<PatientSpecialtyNeededItem> getPatientSpecialtyNeededItemForPatient(Integer patientId);
	
}

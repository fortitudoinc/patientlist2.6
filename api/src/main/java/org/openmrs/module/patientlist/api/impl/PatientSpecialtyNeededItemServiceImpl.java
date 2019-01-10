/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.patientlist.api.db.PatientSpecialtyNeededItemDao;
import org.openmrs.module.patientlist.api.db.SpecialtyTypeItemDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public class PatientSpecialtyNeededItemServiceImpl extends BaseOpenmrsService implements PatientSpecialtyNeededItemService {
	
	PatientSpecialtyNeededItemDao dao;
	
	private static final Log log = LogFactory.getLog(SpecialtyTypeItemServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(PatientSpecialtyNeededItemDao dao) {
		this.dao = dao;
	}
	
	public PatientSpecialtyNeededItemDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public PatientSpecialtyNeededItem savePatientSpecialtyNeededItem(PatientSpecialtyNeededItem item) throws APIException {
		return dao.savePatientSpecialtyNeededItem(item);
	}
	
	@Transactional(readOnly = true)
	public PatientSpecialtyNeededItem getPatientSpecialtyNeededItem(Integer id) {
		return dao.getPatientSpecialtyNeededItem(id);
	}
	
	@Transactional(readOnly = true)
	public List<PatientSpecialtyNeededItem> getAllPatientSpecialtyNeededItem() {
		return dao.getAllPatientSpecialtyNeededItem();
	}
	
	@Transactional(readOnly = true)
	public List<PatientSpecialtyNeededItem> getPatientSpecialtyNeededItemForPatient(Integer patientId) {
		return dao.getPatientSpecialtyNeededItemForPatient(patientId);
	}
}

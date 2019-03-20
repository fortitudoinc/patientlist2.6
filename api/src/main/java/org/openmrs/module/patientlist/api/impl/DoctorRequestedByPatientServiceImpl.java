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
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.openmrs.module.patientlist.api.DoctorRequestedByPatientService;
import org.openmrs.module.patientlist.api.db.DoctorRequestedByPatientDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public class DoctorRequestedByPatientServiceImpl extends BaseOpenmrsService implements DoctorRequestedByPatientService {
	
	DoctorRequestedByPatientDao dao;
	
	private static final Log log = LogFactory.getLog(DoctorRequestedByPatientServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(DoctorRequestedByPatientDao dao) {
		this.dao = dao;
	}
	
	public DoctorRequestedByPatientDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public DoctorRequestedByPatient saveDoctorRequestedByPatient(DoctorRequestedByPatient item) throws APIException {
		return dao.saveDoctorRequestedByPatient(item);
	}
	
	@Transactional(readOnly = true)
	public DoctorRequestedByPatient getDoctorRequestedByPatient(Integer id) {
		return dao.getDoctorRequestedByPatient(id);
	}
	
	@Transactional(readOnly = true)
	public List<DoctorRequestedByPatient> getAllDoctorRequestedByPatient() {
		return dao.getAllDoctorRequestedByPatient();
	}
	
	@Transactional(readOnly = true)
	public List<DoctorRequestedByPatient> getDoctorRequestedByPatientForPatient(Integer patientId) {
		return dao.getDoctorRequestedByPatientForPatient(patientId);
	}
}

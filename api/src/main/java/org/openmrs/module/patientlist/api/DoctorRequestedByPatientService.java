/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.openmrs.module.patientlist.DoctorRequestedByPatientConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public interface DoctorRequestedByPatientService extends OpenmrsService {
	
	@Authorized(DoctorRequestedByPatientConfig.MODULE_PRIVILEGE)
	@Transactional
	public DoctorRequestedByPatient saveDoctorRequestedByPatient(DoctorRequestedByPatient item) throws APIException;
	
	/**
	 * Get a {@link SpecialtyTypeItem} object by primary key id.
	 * 
	 * @param id the primary key integer id to look up on
	 * @return the found SpecialtyTypeItem object which matches the row with the given id. If no row
	 *         with the given id exists, null is returned.
	 */
	@Authorized()
	@Transactional(readOnly = true)
	public DoctorRequestedByPatient getDoctorRequestedByPatient(Integer id);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<DoctorRequestedByPatient> getAllDoctorRequestedByPatient();
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<DoctorRequestedByPatient> getDoctorRequestedByPatientForPatient(Integer patientId);
}

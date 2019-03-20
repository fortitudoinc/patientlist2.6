/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public interface DoctorRequestedByPatientDao {
	
	public DoctorRequestedByPatient saveDoctorRequestedByPatient(DoctorRequestedByPatient item) throws APIException;
	
	public DoctorRequestedByPatient getDoctorRequestedByPatient(Integer id);
	
	public List<DoctorRequestedByPatient> getAllDoctorRequestedByPatient();
	
	public List<DoctorRequestedByPatient> getDoctorRequestedByPatientForPatient(Integer patientId);
}

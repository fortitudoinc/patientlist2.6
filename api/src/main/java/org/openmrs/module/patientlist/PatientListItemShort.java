package org.openmrs.module.patientlist;

import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PatientListItemShort extends BaseOpenmrsData {
	
	private String patientId;
	
	private String isOnActivePatientList;
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	
	public String getIsOnActivePatientList() {
		return isOnActivePatientList;
	}
	
	public void setIsOnActivePatientList(String isOnActivePatientList) {
		this.isOnActivePatientList = isOnActivePatientList;
	}
	
	@Override
	public Integer getId() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public void setId(Integer intgr) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}

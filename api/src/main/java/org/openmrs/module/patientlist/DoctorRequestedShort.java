package org.openmrs.module.patientlist;

import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class DoctorRequestedShort extends BaseOpenmrsData {
	
	private String doctorUserId;
	
	private String patientUUID;
	
	public String getDoctorUserId() {
		return doctorUserId;
	}
	
	public void setDoctorUserId(String doctorUserId) {
		this.doctorUserId = doctorUserId;
	}
	
	public String getPatientUUID() {
		return patientUUID;
	}
	
	public void setPatientUUID(String patientUUID) {
		this.patientUUID = patientUUID;
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

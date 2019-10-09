package org.openmrs.module.patientlist;

import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class DoctorRequestedShort extends BaseOpenmrsData {
	
	private String doctorUserUUID;
	
	private String patientId;
	
	public String getDoctorUserUUID() {
		return doctorUserUUID;
	}
	
	public void setDoctorUserUUID(String doctorUserUUID) {
		this.doctorUserUUID = doctorUserUUID;
	}
	
	public String getPatientId() {
		return patientId;
	}
	
	public void setPatientId(String patientId) {
		this.patientId = patientId;
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

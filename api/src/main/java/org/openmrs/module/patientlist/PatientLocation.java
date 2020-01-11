package org.openmrs.module.patientlist;

import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PatientLocation extends BaseOpenmrsData {
	
	private String patientLocation;
	
	private String patientUUID;
	
	public String getPatientLocation() {
		return patientLocation;
	}
	
	public void setPatientLocation(String patientLocation) {
		this.patientLocation = patientLocation;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist;

import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PatientSpecialtyNeededShort extends BaseOpenmrsData {
	
	private String specialtyId;
	
	private String patientUUID;
	
	public String getSpecialtyId() {
		return specialtyId;
	}
	
	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
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

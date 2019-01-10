/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PatientSpecialtyNeededItem extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private int specialtyTypeId;
	
	private int patientId;
	
	private Date dateCreated;
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public int getSpecialtyTypeId() {
		return specialtyTypeId;
	}
	
	public void setSpecialtyTypeId(int specialtyTypeId) {
		this.specialtyTypeId = specialtyTypeId;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
}

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
public class SpecialtyTypeItem extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private String name;
	
	private Date dateCreated;
	
	/**
	 * The primary key for this PatientListItem. Auto generated by the database.
	 * 
	 * @see org.openmrs.OpenmrsObject#getId()
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PersonCountry extends BaseOpenmrsData implements Serializable {
	
	private Integer id;
	
	private Integer personId;
	
	private Integer countryId;
	
	private int isVoid;
	
	public int getIsVoid() {
		return isVoid;
	}
	
	public void setIsVoid(int isVoid) {
		this.isVoid = isVoid;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getPersonId() {
		return personId;
	}
	
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	public Integer getCountryId() {
		return countryId;
	}
	
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
}

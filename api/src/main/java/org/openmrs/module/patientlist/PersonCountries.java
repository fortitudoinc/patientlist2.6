package org.openmrs.module.patientlist;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class PersonCountries extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private int personId;
	
	private String countries;
	
	private Date dateCreated;
	
	public int getPersonId() {
		return personId;
	}
	
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
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
	
	public String getCountries() {
		return countries;
	}
	
	public void setCountries(String countries) {
		this.countries = countries;
	}
	
}

package org.openmrs.module.patientlist;

import java.io.Serializable;
import java.util.Date;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class Country extends BaseOpenmrsData implements Serializable {
	
	private int id;
	
	private String name;
	
	private String countryCode;
	
	private Date dateCreated;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
}

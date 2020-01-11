package org.openmrs.module.patientlist;

import java.io.Serializable;
import org.openmrs.BaseOpenmrsData;

/**
 * @author levine
 */
public class DoctorItem extends BaseOpenmrsData implements Serializable {
	
	private int userId;
	
	private String givenName;
	
	private String familyName;
	
	public DoctorItem() {
		
	}
	
	public DoctorItem(String givenName, String familyName, int userId) {
		this.givenName = givenName;
		this.familyName = familyName;
		this.userId = userId;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public void setGivenName(String name) {
		this.givenName = name;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
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

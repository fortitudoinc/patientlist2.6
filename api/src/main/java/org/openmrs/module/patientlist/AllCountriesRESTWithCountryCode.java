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
public class AllCountriesRESTWithCountryCode extends BaseOpenmrsData {
	
	String countriesWithCountryCode; // return comma list as: NIGERIA/+234,UGANDA/+235, ...
	
	public String getCountriesWithCountryCode() {
		return countriesWithCountryCode;
	}
	
	public void setCountriesWithCountryCode(String countriesWithCountryCode) {
		this.countriesWithCountryCode = countriesWithCountryCode;
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

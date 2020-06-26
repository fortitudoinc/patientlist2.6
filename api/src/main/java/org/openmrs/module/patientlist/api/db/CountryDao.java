/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.Country;

/**
 * @author levine
 */
public interface CountryDao {
	
	Country saveCountry(Country item) throws APIException;
	
	public Country getCountry(Integer id);
	
	public List<Country> getAllCountry();
}

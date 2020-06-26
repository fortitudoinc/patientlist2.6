/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.PersonCountry;

/**
 * @author levine
 */
public interface PersonCountryDao {
	
	PersonCountry savePersonCountry(PersonCountry item) throws APIException;
	
	public PersonCountry getPersonCountry(Integer id);
	
	public List<PersonCountry> getAllPersonCountryForPerson(Integer personId);
	
	public List<PersonCountry> getAllPersonCountry();
}

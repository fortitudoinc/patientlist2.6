/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.PersonCountryConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
@Transactional
public interface PersonCountryService extends OpenmrsService {
	
	@Authorized(PersonCountryConfig.MODULE_PRIVILEGE)
	@Transactional
	PersonCountry savePersonCountry(PersonCountry item) throws APIException;
	
	/**
	 * Get a {@link PersonCountry} object by primary key id.
	 * 
	 * @param id the primary key integer id to look up on
	 * @return the found PersonCountry object which matches the row with the given id. If no row
	 *         with the given id exists, null is returned.
	 */
	@Authorized()
	@Transactional(readOnly = true)
	public PersonCountry getPersonCountry(Integer id);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<PersonCountry> getAllPersonCountryForPerson(Integer personId);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<PersonCountry> getAllPersonCountry();
}

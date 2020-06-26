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
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.CountryConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */

@Transactional
public interface CountryService extends OpenmrsService {
	
	/**
	 * Saves a patient list item. Sets the owner to superuser, if it is not set. It can be called by
	 * users with this module's privilege. It is executed in a transaction.
	 * 
	 * @param item
	 * @return
	 * @throws APIException
	 */
	@Authorized(CountryConfig.MODULE_PRIVILEGE)
	@Transactional
	Country saveCountry(Country item) throws APIException;
	
	/**
	 * Get a {@link Country} object by primary key id.
	 * 
	 * @param id the primary key integer id to look up on
	 * @return the found Country object which matches the row with the given id. If no row with the
	 *         given id exists, null is returned.
	 */
	@Authorized()
	@Transactional(readOnly = true)
	public Country getCountry(Integer id);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<Country> getAllCountry();
	
}

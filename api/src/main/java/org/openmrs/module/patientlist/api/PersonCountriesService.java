package org.openmrs.module.patientlist.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.PersonCountriesConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
@Transactional
public interface PersonCountriesService extends OpenmrsService {
	
	@Authorized(PersonCountriesConfig.MODULE_PRIVILEGE)
	@Transactional
	public PersonCountries savePersonCountries(PersonCountries item) throws APIException;
	
	@Authorized()
	@Transactional(readOnly = true)
	public PersonCountries getPersonCountries(Integer id);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<PersonCountries> getAllPersonCountries();
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<PersonCountries> getPersonCountriesForPerson(Integer personId);
	
}

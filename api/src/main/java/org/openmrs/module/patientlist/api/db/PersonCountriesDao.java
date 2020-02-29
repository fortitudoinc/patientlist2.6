package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.PersonCountriesConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public interface PersonCountriesDao {
	
	public PersonCountries savePersonCountries(PersonCountries item) throws APIException;
	
	public PersonCountries getPersonCountries(Integer id);
	
	public List<PersonCountries> getAllPersonCountries();
	
	public List<PersonCountries> getPersonCountriesForPerson(Integer personId);
	
}

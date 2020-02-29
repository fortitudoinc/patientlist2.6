package org.openmrs.module.patientlist.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.api.PersonCountriesService;
import org.openmrs.module.patientlist.api.db.PersonCountriesDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public class PersonCountriesServiceImpl extends BaseOpenmrsService implements PersonCountriesService {
	
	PersonCountriesDao dao;
	
	private static final Log log = LogFactory.getLog(PersonCountriesServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(PersonCountriesDao dao) {
		this.dao = dao;
	}
	
	public PersonCountriesDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public PersonCountries savePersonCountries(PersonCountries item) throws APIException {
		return dao.savePersonCountries(item);
	}
	
	@Transactional(readOnly = true)
	public PersonCountries getPersonCountries(Integer id) {
		return dao.getPersonCountries(id);
	}
	
	@Transactional(readOnly = true)
	public List<PersonCountries> getAllPersonCountries() {
		return dao.getAllPersonCountries();
	}
	
	@Transactional(readOnly = true)
	public List<PersonCountries> getPersonCountriesForPerson(Integer personId) {
		return dao.getPersonCountriesForPerson(personId);
	}
}

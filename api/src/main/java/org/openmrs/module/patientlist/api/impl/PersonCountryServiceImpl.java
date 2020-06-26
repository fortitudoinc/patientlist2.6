/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.api.PersonCountryService;
import org.openmrs.module.patientlist.api.db.PersonCountryDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public class PersonCountryServiceImpl extends BaseOpenmrsService implements PersonCountryService {
	
	PersonCountryDao dao;
	
	private static final Log log = LogFactory.getLog(PersonCountryServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(PersonCountryDao dao) {
		this.dao = dao;
	}
	
	public PersonCountryDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public PersonCountry savePersonCountry(PersonCountry item) throws APIException {
		return dao.savePersonCountry(item);
	}
	
	@Transactional(readOnly = true)
	public PersonCountry getPersonCountry(Integer id) {
		return dao.getPersonCountry(id);
	}
	
	@Transactional(readOnly = true)
	public List<PersonCountry> getAllPersonCountryForPerson(Integer personId) {
		return dao.getAllPersonCountryForPerson(personId);
	}
	
	@Transactional(readOnly = true)
	public List<PersonCountry> getAllPersonCountry() {
		return dao.getAllPersonCountry();
	}
}

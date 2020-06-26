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
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.api.CountryService;
import org.openmrs.module.patientlist.api.db.CountryDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author levine
 */
public class CountryServiceImpl extends BaseOpenmrsService implements CountryService {
	
	CountryDao dao;
	
	private static final Log log = LogFactory.getLog(CountryServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(CountryDao dao) {
		this.dao = dao;
	}
	
	public CountryDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public Country saveCountry(Country item) throws APIException {
		return dao.saveCountry(item);
	}
	
	@Transactional(readOnly = true)
	public Country getCountry(Integer id) {
		return dao.getCountry(id);
	}
	
	@Transactional(readOnly = true)
	public List<Country> getAllCountry() {
		return dao.getAllCountry();
	}
}

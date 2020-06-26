/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PersonCountry;
import org.openmrs.module.patientlist.api.db.PersonCountryDao;

/**
 * @author levine
 */
public class HibernatePersonCountryDAO implements PersonCountryDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public PersonCountry savePersonCountry(PersonCountry item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public PersonCountry getPersonCountry(Integer id) {
		return (PersonCountry) sessionFactory.getCurrentSession().get(PersonCountry.class, id);
	}
	
	@Override
	public List<PersonCountry> getAllPersonCountryForPerson(Integer personId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PersonCountry.class);
		crit.add(Restrictions.eq("personId", personId));
		crit.add(Restrictions.eq("isVoid", 0));
		return crit.list();
	}
	
	@Override
	public List<PersonCountry> getAllPersonCountry() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PersonCountry.class);
		return crit.list();
	}
}

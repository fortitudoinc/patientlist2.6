package org.openmrs.module.patientlist.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.db.PersonCountriesDao;

/**
 * @author levine
 */
public class HibernatePersonCountriesDAO implements PersonCountriesDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public PersonCountries savePersonCountries(PersonCountries item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public PersonCountries getPersonCountries(Integer id) {
		return (PersonCountries) sessionFactory.getCurrentSession().get(PersonCountries.class, id);
	}
	
	@Override
	public List<PersonCountries> getAllPersonCountries() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PersonCountries.class);
		return crit.list();
	}
	
	@Override
	public List<PersonCountries> getPersonCountriesForPerson(Integer personId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PersonCountries.class);
		crit.add(Restrictions.eq("personId", personId));
		return crit.list();
	}
}

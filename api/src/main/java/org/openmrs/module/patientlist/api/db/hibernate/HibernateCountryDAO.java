package org.openmrs.module.patientlist.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.api.db.CountryDao;

/**
 * @author levine
 */
public class HibernateCountryDAO implements CountryDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Country saveCountry(Country item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public Country getCountry(Integer id) {
		return (Country) sessionFactory.getCurrentSession().get(Country.class, id);
	}
	
	@Override
	public List<Country> getAllCountry() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Country.class);
		return crit.list();
	}
}

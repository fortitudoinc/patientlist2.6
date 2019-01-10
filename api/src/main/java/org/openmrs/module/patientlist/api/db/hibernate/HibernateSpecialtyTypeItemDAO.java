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
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.db.PatientSpecialtyNeededItemDao;
import org.openmrs.module.patientlist.api.db.SpecialtyTypeItemDao;

/**
 * @author levine
 */
public class HibernateSpecialtyTypeItemDAO implements SpecialtyTypeItemDao {
	
	private DbSessionFactory sessionFactory;
	
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public SpecialtyTypeItem saveSpecialtyTypeItem(SpecialtyTypeItem item) throws APIException {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public SpecialtyTypeItem getSpecialtyTypeItem(Integer id) {
		return (SpecialtyTypeItem) sessionFactory.getCurrentSession().get(SpecialtyTypeItem.class, id);
	}
	
	@Override
	public List<SpecialtyTypeItem> getAllSpecialtyTypeItem() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SpecialtyTypeItem.class);
		return crit.list();
	}
}

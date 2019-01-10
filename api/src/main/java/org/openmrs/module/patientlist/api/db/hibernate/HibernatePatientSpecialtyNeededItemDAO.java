/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.api.db.PatientSpecialtyNeededItemDao;

public class HibernatePatientSpecialtyNeededItemDAO implements PatientSpecialtyNeededItemDao {
	
	private DbSessionFactory sessionFactory;
	
	/**
	 * This is a Hibernate object. It gives us metadata about the currently connected database, the
	 * current session, the current db user, etc. To save and get objects, calls should go through
	 * sessionFactory.getCurrentSession() <br/>
	 * <br/>
	 * This is called by Spring. See the /metadata/moduleApplicationContext.xml for the
	 * "sessionFactory" setting. See the applicationContext-service.xml file in CORE openmrs for
	 * where the actual "sessionFactory" object is first defined.
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public PatientSpecialtyNeededItem savePatientSpecialtyNeededItem(PatientSpecialtyNeededItem item) {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public PatientSpecialtyNeededItem getPatientSpecialtyNeededItem(Integer id) {
		return (PatientSpecialtyNeededItem) sessionFactory.getCurrentSession().get(PatientSpecialtyNeededItem.class, id);
	}
	
	@Override
	public List<PatientSpecialtyNeededItem> getPatientSpecialtyNeededItemForPatient(Integer patientId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientSpecialtyNeededItem.class);
		crit.add(Restrictions.eq("patientId", patientId));
		return crit.list();
	}
	
	@Override
	public List<PatientSpecialtyNeededItem> getAllPatientSpecialtyNeededItem() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientSpecialtyNeededItem.class);
		return crit.list();
	}
	
}

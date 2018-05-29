/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.api.db.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.api.db.PatientListItemDao;

/**
 * @author levine
 */
public class HibernatePatientListItemDAO implements PatientListItemDao {
	
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
	public PatientListItem savePatientListItem(PatientListItem item) {
		sessionFactory.getCurrentSession().saveOrUpdate(item);
		return item;
	}
	
	@Override
	public PatientListItem getPatientListItem(Integer id) {
		
		return (PatientListItem) sessionFactory.getCurrentSession().get(PatientListItem.class, id);
		/*
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientListItem.class);
		crit.add(Restrictions.eq("id", id));
		return crit.list();
		 */
	}
	
	@Override
	public List<PatientListItem> getActivePatientListItemForPatient(Integer patientId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientListItem.class);
		crit.add(Restrictions.eq("patientId", patientId));
		crit.add(Restrictions.eq("hasBeenCalled", 0));
		return crit.list();
	}
	
	@Override
	public List<PatientListItem> getAllPatientListItems() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientListItem.class);
		return crit.list();
	}
	
	@Override
	public List<PatientListItem> getActivePatientListItems() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientListItem.class);
		crit.add(Restrictions.eq("hasBeenCalled", 0));
		return crit.list();
	}
	
	@Override
	public List<PatientListItem> getOldPatientListItems() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PatientListItem.class);
		crit.add(Restrictions.eq("hasBeenCalled", 1));
		return crit.list();
	}
	
}

/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientlist.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientlist.PatientListItem;
import org.springframework.transaction.annotation.Transactional;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.db.PatientListItemDao;

public class PatientListItemServiceImpl extends BaseOpenmrsService implements PatientListItemService {
	
	PatientListItemDao dao;
	
	private static final Log log = LogFactory.getLog(PatientListItemServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(PatientListItemDao dao) {
		this.dao = dao;
	}
	
	public PatientListItemDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public PatientListItem savePatientListItem(PatientListItem item) throws APIException {
		return dao.savePatientListItem(item);
	}
	
	@Transactional(readOnly = true)
	public PatientListItem getPatientListItem(Integer id) {
		return dao.getPatientListItem(id);
	}
	
	@Transactional(readOnly = true)
	public List<PatientListItem> getActivePatientListItemForPatient(Integer patientId) {
		return dao.getActivePatientListItemForPatient(patientId);
	}
	
	@Transactional(readOnly = true)
	public List<PatientListItem> getAllPatientListItems() {
		return dao.getAllPatientListItems();
	}
	
	@Transactional(readOnly = true)
	public List<PatientListItem> getActivePatientListItems() {
		return dao.getActivePatientListItems();
	}
	
	@Transactional(readOnly = true)
	public List<PatientListItem> getOldPatientListItems() {
		return dao.getOldPatientListItems();
	}
	
}

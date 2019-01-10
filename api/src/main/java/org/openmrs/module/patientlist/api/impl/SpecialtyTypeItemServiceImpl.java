package org.openmrs.module.patientlist.api.impl;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.patientlist.api.db.SpecialtyTypeItemDao;
import org.springframework.transaction.annotation.Transactional;

public class SpecialtyTypeItemServiceImpl extends BaseOpenmrsService implements SpecialtyTypeItemService {
	
	SpecialtyTypeItemDao dao;
	
	private static final Log log = LogFactory.getLog(SpecialtyTypeItemServiceImpl.class);
	
	/**
	 * Injected in moduleApplicationContext.xml
	 */
	public void setDao(SpecialtyTypeItemDao dao) {
		this.dao = dao;
	}
	
	public SpecialtyTypeItemDao getDao() {
		return dao;
	}
	
	public static Log getLog() {
		return log;
	}
	
	@Transactional
	public SpecialtyTypeItem saveSpecialtyTypeItem(SpecialtyTypeItem item) throws APIException {
		return dao.saveSpecialtyTypeItem(item);
	}
	
	@Transactional(readOnly = true)
	public SpecialtyTypeItem getSpecialtyTypeItem(Integer id) {
		return dao.getSpecialtyTypeItem(id);
	}
	
	@Transactional(readOnly = true)
	public List<SpecialtyTypeItem> getAllSpecialtyTypeItem() {
		return dao.getAllSpecialtyTypeItem();
	}
	
}

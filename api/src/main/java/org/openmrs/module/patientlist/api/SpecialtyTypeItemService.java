package org.openmrs.module.patientlist.api;

import java.util.List;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.SpecialtyTypeItemConfig;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface SpecialtyTypeItemService extends OpenmrsService {
	
	/**
	 * Saves a patient list item. Sets the owner to superuser, if it is not set. It can be called by
	 * users with this module's privilege. It is executed in a transaction.
	 * 
	 * @param item
	 * @return
	 * @throws APIException
	 */
	@Authorized(SpecialtyTypeItemConfig.MODULE_PRIVILEGE)
	@Transactional
	SpecialtyTypeItem saveSpecialtyTypeItem(SpecialtyTypeItem item) throws APIException;
	
	/**
	 * Get a {@link SpecialtyTypeItem} object by primary key id.
	 * 
	 * @param id the primary key integer id to look up on
	 * @return the found SpecialtyTypeItem object which matches the row with the given id. If no row
	 *         with the given id exists, null is returned.
	 */
	@Authorized()
	@Transactional(readOnly = true)
	public SpecialtyTypeItem getSpecialtyTypeItem(Integer id);
	
	@Authorized()
	@Transactional(readOnly = true)
	public List<SpecialtyTypeItem> getAllSpecialtyTypeItem();
	
}

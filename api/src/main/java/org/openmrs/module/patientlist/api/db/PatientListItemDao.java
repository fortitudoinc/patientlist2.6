/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientlist.api.db;

import java.util.List;
import org.openmrs.module.patientlist.PatientListItem;

/**
 * This is the DAO interface. This is never used by the developer, but instead the
 * {@link PatientListItemService} calls it (and developers call the NoteService)
 */
public interface PatientListItemDao {
	
	public PatientListItem savePatientListItem(PatientListItem item);
	
	public PatientListItem getPatientListItem(Integer id);
	
	public List<PatientListItem> getActivePatientListItemForPatient(Integer patientId);
	
	public List<PatientListItem> getAllPatientListItems();
	
	public List<PatientListItem> getActivePatientListItems();
	
	public List<PatientListItem> getOldPatientListItems();
	
}

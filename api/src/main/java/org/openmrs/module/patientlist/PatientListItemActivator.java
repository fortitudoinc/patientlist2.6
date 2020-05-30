/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.patientlist;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.appframework.service.AppFrameworkService;

import java.util.Date;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class PatientListItemActivator extends BaseModuleActivator {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see #started()
	 */
	public void started() {
		log.info("Started Patient List");
		AppFrameworkService service = Context.getService(AppFrameworkService.class);
		service.disableExtension("referenceapplication.realTime.vitals");
		service.disableExtension("org.openmrs.module.coreapps.mostRecentVitals.clinicianDashboardSecondColumn");
		service.disableExtension("coreapps.mostRecentVitals.clinicianDashboardFirstColumn");
		service.disableExtension("org.openmrs.module.coreapps.deletePatient");
		service.disableExtension("org.openmrs.module.coreapps.markPatientDead");
		
		service.disableExtension("appointmentschedulingui.tab");
		service.disableExtension("org.openmrs.module.appointmentschedulingui.firstColumnFragments.patientDashboard.patientAppointments");
		service.disableExtension("appointmentschedulingui.homeAppLink");
		service.disableExtension("appointmentschedulingui.schedulingAppointmentDashboardLink");
		service.disableExtension("appointmentschedulingui.requestAppointmentDashboardLink");
		service.disableExtension("referenceapplication.realTime.simpleAdmission");
		service.disableExtension("referenceapplication.realTime.simpleDischarge");
		service.disableExtension("referenceapplication.realTime.simpleTransfer");
		service.disableExtension("org.openmrs.module.appointmentschedulingui.firstColumnFragments.patientDashboard.patientAppointments");
		createAttribute("audioOrVideoCall");
	}
	
	void createAttribute(String name) {
		if (isAttTypeAlreadyDefined(name)) {
			return;
		}
		PersonAttributeType att = new PersonAttributeType();
		att.setName(name);
		att.setDescription("Indicate patient's request for either audio or whatsapp");
		att.setCreator(Context.getAuthenticatedUser());
		att.setFormat("java.lang.String");
		att.setDateCreated(new Date());
		att.setSearchable(Boolean.TRUE);
		att.setRetired(Boolean.FALSE);
		Context.getPersonService().savePersonAttributeType(att);
	}
	
	private boolean isAttTypeAlreadyDefined(String name) {
		List<PersonAttributeType> atts = Context.getPersonService().getAllPersonAttributeTypes();
		if (atts == null) {
			return false;
		}
		for (PersonAttributeType att : atts) {
			if (att.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown Patient List");
	}
	
}

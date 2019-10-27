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
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.Extension;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;

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
		
	}
	
	/**
	 * @see #shutdown()
	 */
	public void shutdown() {
		log.info("Shutdown Patient List");
	}
	
}

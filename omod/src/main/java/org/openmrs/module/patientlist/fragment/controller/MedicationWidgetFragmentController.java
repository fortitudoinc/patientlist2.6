/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.fragment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.ui.framework.page.PageModel;

/**
 * @author levine
 */
public class MedicationWidgetFragmentController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		AppFrameworkService service = Context.getService(AppFrameworkService.class);
		service.disableExtension("org.openmrs.module.coreapps.deletePatient");
	}
}

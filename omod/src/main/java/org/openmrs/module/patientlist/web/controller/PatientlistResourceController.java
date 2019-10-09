package org.openmrs.module.patientlist.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;

/**
 * @author levine
 */
@Controller
@RequestMapping("/rest/" + RestConstants.VERSION_1 + "/patientlist")
public class PatientlistResourceController extends MainResourceController {
	
	/**
	 * @see org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return "v1/patientlist";
	}
}

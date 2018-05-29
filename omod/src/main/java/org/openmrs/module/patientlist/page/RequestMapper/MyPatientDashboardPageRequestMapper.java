package org.openmrs.module.patientlist.page.RequestMapper;

import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.page.PageRequestMapper;
import org.springframework.stereotype.Component;

/**
 * @author levine
 */
@Component
public class MyPatientDashboardPageRequestMapper implements PageRequestMapper {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Implementations should call {@link PageRequest#setProviderNameOverride(String)} and
	 * {@link PageRequest#setPageNameOverride(String)}, and return true if they want to remap a
	 * request, or return false if they didn't remap it.
	 * 
	 * @param request may have its providerNameOverride and pageNameOverride set
	 * @return true if this page was mapped (by overriding the provider and/or page), false
	 *         otherwise
	 */
	public boolean mapRequest(PageRequest request) {
		
		String clerk;
		boolean isUserClerk = false;
		
		clerk = Context.getAdministrationService().getGlobalProperty("patientlist.clerkrole");
		
		System.out.println("*****************OVERRIDING provider: " + request.getProviderName() + " Page: "
		        + request.getPageName() + "  Clerk Property: " + clerk);
		
		User user = Context.getAuthenticatedUser();
		if (user == null) { // need this check to avoid issues when logging out
			return false;
		}
		Set<Role> userRoles = user.getAllRoles();
		for (Role userRole : userRoles) {
			if (userRole.getName().equals(clerk)) {
				isUserClerk = true;
				break;
			}
		}
		
		if (!isUserClerk) {
			return false;
		}
		//Clerk should not be able to view patient dashboard
		if (request.getProviderName().equals("coreapps")) {
			if (request.getPageName().equals("clinicianfacing/patient")) {
				request.setProviderNameOverride("patientlist");
				request.setPageNameOverride("patientList");
				log.info(request.toString());
				return true;
			}
		}
		// no override happened
		return false;
	}
	
}

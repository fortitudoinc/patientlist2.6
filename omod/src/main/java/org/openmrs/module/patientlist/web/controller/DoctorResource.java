/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.DoctorItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/doctors", supportedClass = DoctorItem.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class DoctorResource extends DataDelegatingCrudResource<DoctorItem> {
	
	public NeedsPaging<DoctorItem> doGetAll(RequestContext context) {
		System.out.println("****************doGetAll: " + context);
		List<User> users = Context.getUserService().getAllUsers();
		List<DoctorItem> doctors = new ArrayList<DoctorItem>();
		String drRole = Context.getAdministrationService().getGlobalProperty("patientlist.drrole");
		Set<Role> userRoles;
		
		for (User user : users) {
			userRoles = user.getAllRoles();
			for (Role role : userRoles) {
				if (role.getName().equalsIgnoreCase(drRole)) {
					DoctorItem drItem = new DoctorItem(user.getGivenName(), user.getFamilyName(), user.getUserId());
					doctors.add(drItem);
					System.out.println("Doctor: " + drItem);
					break;
				}
			}
		}
		return new NeedsPaging<DoctorItem>(doctors, context);
	}
	
	public DoctorItem getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	protected void delete(DoctorItem t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void purge(DoctorItem t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("givenName");
			description.addProperty("familyName");
			description.addProperty("userId");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("givenName");
			description.addProperty("familyName");
			description.addProperty("userId");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	public DoctorItem newDelegate() {
		System.out.println("****************newDelegate: ");
		return new DoctorItem();
	}
	
	public DoctorItem save(DoctorItem t) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
	
	@PropertyGetter("display")
	public String getDisplayString(DoctorItem doctor) {
		return doctor.getGivenName() + "/" + doctor.getFamilyName() + "/" + doctor.getUserId();
	}
	
	/*	
		@Override
		public DelegatingResourceDescription getCreatableProperties() {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			
			return description;
		}
	 */
}

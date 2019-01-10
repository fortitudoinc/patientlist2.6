/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.fragment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class PatientListFragFragmentController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******PatientListFragFragmentController");
		
		List<SpecialtyTypeItem> items;
		items = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		
		String url = (request.getRequestURI()).trim();
		int i = url.indexOf("coreapps");
		if (i < 0) {
			i = url.indexOf("patientlist");
		}
		url = url.substring(0, i);
		i = url.lastIndexOf("/");
		url = url.substring(0, i);
		url = url + "/coreapps/clinicianfacing/patient.page?patientId=";
		
		String userRole = "";
		ArrayList<String> globalPropertyRoles = new ArrayList<String>();
		String clerk, dr, admin;
		clerk = Context.getAdministrationService().getGlobalProperty("patientlist.clerkrole");
		dr = Context.getAdministrationService().getGlobalProperty("patientlist.drrole");
		admin = Context.getAdministrationService().getGlobalProperty("patientlist.adminrole");
		globalPropertyRoles.add(clerk);
		globalPropertyRoles.add(dr);
		globalPropertyRoles.add(admin);
		
		User user = Context.getAuthenticatedUser();
		Set<Role> userRoles = user.getAllRoles();
		for (Role role : userRoles) {
			String roleName = role.getName();
			System.out.println("User Role: " + roleName);
			if (globalPropertyRoles.contains(roleName)) {
				userRole = roleName;
				break;
			}
		}
		System.out.println("USER ROLE: " + userRole);
		
		List<PatientListItem> oldItems = new ArrayList<PatientListItem>();
		List<PatientListItem> activeItems = new ArrayList<PatientListItem>();
		ArrayList<PatientListItemLocal> activePatientListItems = new ArrayList<PatientListItemLocal>();
		ArrayList<PatientListItemLocal> oldPatientListItems = new ArrayList<PatientListItemLocal>();
		
		if ((userRole.equals(clerk)) || (userRole.equals(admin))) {
			oldItems = Context.getService(PatientListItemService.class).getOldPatientListItems();
			for (PatientListItem item : oldItems) {
				oldPatientListItems.add(new PatientListItemLocal(item, 0));
			}
			Collections.sort(oldPatientListItems, new Comparator<PatientListItemLocal>() {
				
				public int compare(PatientListItemLocal o1, PatientListItemLocal o2) {
					return o1.getPatientCallDate().compareTo(o2.getPatientCallDate());
				}
			});
			if (userRole.equals(clerk)) {
				userRole = "clerk";
			} else {
				userRole = "admin";
			}
		} else {
			userRole = "dr";
		}
		
		/*
		Note, when a clerk wishes to add an old patient to the active list she/he must
		be sure the patient has not already been added to the active list; therefore,
		we need to get the active list so the view can do the check
		 */
		int patientId, specId;
		activeItems = Context.getService(PatientListItemService.class).getActivePatientListItems();
		for (PatientListItem item : activeItems) {
			//activePatientListItems.add(new PatientListItemLocal(item, 0));
			patientId = item.getPatientId();
			specId = getMostRecentSpecialtyForPatient(patientId);
			activePatientListItems.add(new PatientListItemLocal(item, specId));
		}
		
		Collections.sort(activePatientListItems, new Comparator<PatientListItemLocal>() {
			
			public int compare(PatientListItemLocal o1, PatientListItemLocal o2) {
				return o1.patientCallDate.compareTo(o2.patientCallDate);
			}
		});
		
		model.addAttribute("activePatientListItems", activePatientListItems);
		model.addAttribute("oldPatientListItems", oldPatientListItems);
		model.addAttribute("url", url);
		model.addAttribute("role", userRole);
		model.addAttribute("items", items);
		
	}
	
	int getMostRecentSpecialtyForPatient(int patientId) {
		List<PatientSpecialtyNeededItem> patientSpecialties;
		patientSpecialties = Context.getService(PatientSpecialtyNeededItemService.class)
		        .getPatientSpecialtyNeededItemForPatient(patientId);
		if ((patientSpecialties == null) || (patientSpecialties.size() == 0)) {
			return 0;
		}
		PatientSpecialtyNeededItem newItem = patientSpecialties.get(0);
		for (PatientSpecialtyNeededItem item : patientSpecialties) {
			if (item.getDateCreated().after(newItem.getDateCreated())) {
				newItem = item;
			}
		}
		return newItem.getSpecialtyTypeId();
	}
	
	public SimpleObject updateSpecialty(@RequestParam(value = "specId", required = false) String specId,
	        @RequestParam(value = "patientId", required = false) String patientId, UiUtils ui) {
		PatientSpecialtyNeededItem ps = new PatientSpecialtyNeededItem();
		ps.setDateCreated(new Date());
		ps.setPatientId(Integer.parseInt(patientId));
		ps.setSpecialtyTypeId(Integer.parseInt(specId));
		Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(ps);
		String[] properties = new String[] { "dateCreated", "patientId" };
		System.out.println("SPECIALTY SAVED, SPECIALTY: " + specId + " patientId: " + patientId);
		return SimpleObject.fromObject(ps, ui, "dateCreated", "patientId");
	}
	
}

class PatientListItemLocal {
	
	int id, patientId, contactAttempts, hasBeenCalled;
	
	Date patientCallDate, lastContactAttemptDate;
	
	String voidedReason;
	
	String patientPhone, patientName;
	
	SpecialtyTypeItem specItem;
	
	public String getPatientPhone() {
		return patientPhone;
	}
	
	public String getPatientName() {
		return patientName;
	}
	
	public PatientListItemLocal(org.openmrs.module.patientlist.PatientListItem item, int specId) {
		System.out.println("**********************Patient id: " + item.getPatientId() + " Spec id: " + specId);
		if (specId == 0) {
			specItem = null;
		} else {
			specItem = Context.getService(SpecialtyTypeItemService.class).getSpecialtyTypeItem(specId);
		}
		id = item.getId();
		patientId = item.getPatientId();
		contactAttempts = item.getContactAttempts();
		hasBeenCalled = item.getHasBeenCalled();
		patientCallDate = item.getPatientCallDate();
		lastContactAttemptDate = item.getLastContactAttemptDate();
		voidedReason = item.getVoidedReason();
		Person person = Context.getPersonService().getPerson(item.getPatientId());
		//System.out.println(person.getFamilyName());
		//patientPhone = "xxxxxx";
		
		try {
			patientPhone = person.getAttribute("Telephone Number").getValue();
		}
		catch (NullPointerException e) {
			System.out.println("[PATIENTLIST] Null Pointer trying to fetch patient phone, filling in with empty string");
			patientPhone = "";
		}
		
		try {
			patientName = person.getGivenName() + " " + person.getFamilyName();
		}
		catch (NullPointerException e) {
			System.out.println("[PATIENTLIST] Null pointer trying to fetch patient name, filling in with empty string");
			patientName = "";
		}
		
	}
	
	public SpecialtyTypeItem getSpecItem() {
		return specItem;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public int getContactAttempts() {
		return contactAttempts;
	}
	
	public int getHasBeenCalled() {
		return hasBeenCalled;
	}
	
	public Date getPatientCallDate() {
		return patientCallDate;
	}
	
	public Date getLastContactAttemptDate() {
		return lastContactAttemptDate;
	}
	
	public String getVoidedReason() {
		return voidedReason;
	}
	
}

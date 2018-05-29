/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.page.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.springframework.web.bind.annotation.RequestParam;

import org.openmrs.ui.framework.annotation.SpringBean;

/**
 * @author levine
 */
public class PatientListPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******PatientListPageController");
		
		String url = (request.getRequestURL().toString()).trim();
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
				oldPatientListItems.add(new PatientListItemLocal(item));
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
		activeItems = Context.getService(PatientListItemService.class).getActivePatientListItems();
		for (PatientListItem item : activeItems) {
			activePatientListItems.add(new PatientListItemLocal(item));
			Collections.sort(activePatientListItems, new Comparator<PatientListItemLocal>() {
				
				public int compare(PatientListItemLocal o1, PatientListItemLocal o2) {
					return o1.patientCallDate.compareTo(o2.patientCallDate);
				}
			});
		}
		
		model.addAttribute("activePatientListItems", activePatientListItems);
		model.addAttribute("oldPatientListItems", oldPatientListItems);
		model.addAttribute("url", url);
		model.addAttribute("role", userRole);
		
	}
	
	/*
	void checkList(ArrayList<PatientListItem> items, String whichList) {
	System.out.println("*********************" + whichList);
	for (PatientListItemLocal item : items) {
		System.out.println("Patient id: " + item.getPatientId() + " patientCallDate: "
		        + item.getFirstContactAttemptDate() + " Name: " + item.getPatientName());
	}
	}
	 */
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "patientIdd", required = false) int patientId,
	        @RequestParam(value = "itemId", required = false) int itemId,
	        @RequestParam(value = "redirectUrl", required = false) String redirectUrl,
	        @RequestParam(value = "postType", required = false) String postType) {
		System.out.println("POST ------------ patiend id: " + patientId);
		
		Patient patient = (Patient) Context.getService(PatientService.class).getPatient(patientId);
		PatientListItem patientListItem = null;
		if (postType.equals("addToList")) {
			System.out.println("Saving new patient: " + patient.getFamilyName());
			User user = Context.getAuthenticatedUser();
			patientListItem = new PatientListItem();
			patientListItem.setPatientCallDate(new Date());
			patientListItem.setLastContactAttemptDate(new Date());
			//patientListItem.setLastContactAttemptDate(null);
			patientListItem.setContactAttempts(0);
			patientListItem.setHasBeenCalled(0);
			patientListItem.setVoidedReason("not voidedd");
			patientListItem.setPatientId(patientId);
			patientListItem.setDrPersonId(user.getPerson().getPersonId()); //needs a person id so default to clerk
		} else {
			System.out.println("Unsuccessful attempt to call patient, item id: " + itemId);
			patientListItem = Context.getService(PatientListItemService.class).getPatientListItem(itemId);
			patientListItem.setContactAttempts(patientListItem.getContactAttempts() + 1);
			patientListItem.setLastContactAttemptDate(new Date());
		}
		
		patientListItem = Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
		InfoErrorMessageUtil.flashInfoMessage(session, "Patient List Updated");
		
		return "redirect:" + redirectUrl;
	}
	
	/*        
	        void listExtensions(AppFrameworkService service) {

	            List<Extension> extensions = service.getAllEnabledExtensions();
	            for (Extension extension : extensions) {
	                System.out.println("Extension: " + extension.getExtensionPointId());
	                
	            }
	            
	        }

	 */
}

class PatientListItemLocal {
	
	int id, patientId, contactAttempts, hasBeenCalled;
	
	Date patientCallDate, lastContactAttemptDate;
	
	String voidedReason;
	
	String patientPhone, patientName;
	
	public String getPatientPhone() {
		return patientPhone;
	}
	
	public String getPatientName() {
		return patientName;
	}
	
	public PatientListItemLocal(org.openmrs.module.patientlist.PatientListItem item) {
		System.out.println("**********************Patient id: " + item.getPatientId());
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
		patientPhone = person.getAttribute("Telephone Number").getValue();
		patientName = person.getGivenName() + " " + person.getFamilyName();
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

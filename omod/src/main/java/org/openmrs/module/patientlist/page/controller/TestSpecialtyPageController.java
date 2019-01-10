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
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class TestSpecialtyPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******TestSpecialtyPageController");
		/*
		List<SpecialtyTypeItem> items;
		items = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		for (SpecialtyTypeItem item : items) {
			System.out.println("ID: " + item.getId() + " NAME: " + item.getName());
		}
		
		System.out.println("*******PatientListPageController");
		
		List<PatientListItem> activeItems = new ArrayList<PatientListItem>();
		ArrayList<PatientListItemLocal1> activePatientListItems = new ArrayList<PatientListItemLocal1>();
		
		activeItems = Context.getService(PatientListItemService.class).getActivePatientListItems();
		int patientId, specId;
		for (PatientListItem item : activeItems) {
			patientId = item.getPatientId();
			specId = getMostRecentSpecialtyForPatient(patientId);
			activePatientListItems.add(new PatientListItemLocal1(item, specId));
			
		}
		
		Collections.sort(activePatientListItems, new Comparator<PatientListItemLocal1>() {
			
			public int compare(PatientListItemLocal1 o1, PatientListItemLocal1 o2) {
				return o1.patientCallDate.compareTo(o2.patientCallDate);
			}
		});
		
		model.addAttribute("activePatientListItems", activePatientListItems);
		model.addAttribute("items", items);
		*/
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "specialtyTypeName", required = false) String specialtyTypeName,
	        @RequestParam(value = "patientIdd", required = false) String patientIdd,
	        @RequestParam(value = "itemId", required = false) String itemId) {
		
		int patientId = Integer.parseInt(patientIdd);
		int specId = Integer.parseInt(itemId);
		
		System.out.println("POST ------------ specialtyTypeName: " + specialtyTypeName + " patId: " + patientId
		        + " specId: " + specId);
		PatientSpecialtyNeededItem ps = new PatientSpecialtyNeededItem();
		ps.setDateCreated(new Date());
		ps.setPatientId(patientId);
		ps.setSpecialtyTypeId(specId);
		Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(ps);
		
		SpecialtyTypeItem item = new SpecialtyTypeItem();
		if (!specialtyTypeName.equals("")) {
			item.setName(specialtyTypeName);
			item.setDateCreated(new Date());
			Context.getService(SpecialtyTypeItemService.class).saveSpecialtyTypeItem(item);
		}
		return "redirect:" + "patientlist/testSpecialty.page";
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
}

class PatientListItemLocal1 {
	
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
	
	public PatientListItemLocal1(org.openmrs.module.patientlist.PatientListItem item, int specId) {
		System.out.println("**********************Patient id: " + item.getPatientId());
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

package org.openmrs.module.patientlist.page.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class PatientListPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		System.out.println("*******PatientListPageController");
		
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
			patientListItem.setClerkPersonId(user.getPerson().getPersonId());
			patientListItem.setDrPersonId(user.getPerson().getPersonId()); //needs a person id so default to clerk
			
			PatientSpecialtyNeededItem specialtyItemNeeded = new PatientSpecialtyNeededItem();
			specialtyItemNeeded.setDateCreated(new Date());
			specialtyItemNeeded.setPatientId(patientId);
			
			PatientSpecialtyNeededItem mostCurrentSpecialtyNeeded = null;
			List<PatientSpecialtyNeededItem> specialtiesNeeded = Context.getService(PatientSpecialtyNeededItemService.class)
			        .getPatientSpecialtyNeededItemForPatient(patientId);
			if ((specialtiesNeeded == null) || (specialtiesNeeded.size() == 0)) {
				// default to Medicine - this check is for patients
				// who never used a specialty; handles cases before we added specialties
				int medicineId;
				List<SpecialtyTypeItem> specialties = Context.getService(SpecialtyTypeItemService.class)
				        .getAllSpecialtyTypeItem();
				for (SpecialtyTypeItem specItem : specialties) {
					if (specItem.getName().equalsIgnoreCase("Medicine")) {
						specialtyItemNeeded.setSpecialtyTypeId(specItem.getId());
						break;
					}
				}
				
			} else {
				mostCurrentSpecialtyNeeded = specialtiesNeeded.get(0);
				for (PatientSpecialtyNeededItem item : specialtiesNeeded) {
					if (item.getDateCreated().after(mostCurrentSpecialtyNeeded.getDateCreated())) {
						mostCurrentSpecialtyNeeded = item;
					}
				}
				specialtyItemNeeded.setSpecialtyTypeId(mostCurrentSpecialtyNeeded.getSpecialtyTypeId());
				System.out.println("*****Old patient with specialty added to list, specialty: "
				        + specialtyItemNeeded.getSpecialtyTypeId());
			}
			Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(specialtyItemNeeded);
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

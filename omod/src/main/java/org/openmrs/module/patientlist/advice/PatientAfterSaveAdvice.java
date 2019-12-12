package org.openmrs.module.patientlist.advice;

/**
 *
 * @author levine
 */
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.springframework.aop.AfterReturningAdvice;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;

public class PatientAfterSaveAdvice implements AfterReturningAdvice {
	
	/*
	THIS IS OLD - IT WILL NOT BE INVOKED SINCE IT'S NOT MENTIONED IN THE CONFIG.XML FILE
	*/
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (!method.getName().equals("savePatient")) {
			return;
		}
		User user = Context.getAuthenticatedUser();
		Patient patient = (Patient) args[0];
		System.out.println("\n\nSaving new patient: " + patient.getFamilyName() + "  id: " + patient.getPatientId()
		        + " user person_id: " + user.getPerson().getPersonId());
		log.debug("Method: " + method.getName() + ". After advice called ");
		/*
		if (patient.getChangedBy() != null) {
		System.out.println("Updating patient demographic data - not added to patient list");
		return;
		}
		 */
		/*
		Patient oldPatient = getOldPatientIdOfSamePatient(patient);
		if (oldPatient != null) {
		    voidNewPatient(patient, user);
		    patient = oldPatient;
		}
		*/
		if (isPatientInPatientList(patient.getPatientId())) {
			System.out.println("Updating patient demographic data - not added to patient list");
			return;
		}
		System.out.println("New Patient - added to patient list");
		PatientListItem patientListItem = new PatientListItem();
		
		patientListItem.setPatientCallDate(new Date());
		patientListItem.setLastContactAttemptDate(new Date());
		//patientListItem.setLastContactAttemptDate(null);
		patientListItem.setContactAttempts(0);
		patientListItem.setHasBeenCalled(0);
		patientListItem.setVoidedReason("not voidedd");
		patientListItem.setClerkPersonId(user.getPerson().getPersonId()); //needs a person id so default to clerk
		patientListItem.setDrPersonId(user.getPerson().getPersonId());
		System.out.println("DrPersonId: " + patientListItem.getDrPersonId());
		patientListItem.setPatientId(patient.getPatientId());
		PatientListItem item = Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
		
		PatientSpecialtyNeededItem specialtyItemNeeded = new PatientSpecialtyNeededItem();
		specialtyItemNeeded.setDateCreated(new Date());
		specialtyItemNeeded.setPatientId(patient.getPatientId());
		
		List<SpecialtyTypeItem> specItems;
		int medicineSpecItemId = 1;
		specItems = Context.getService(SpecialtyTypeItemService.class).getAllSpecialtyTypeItem();
		for (SpecialtyTypeItem specItem : specItems) {
			if (specItem.getName().equalsIgnoreCase("Medicine")) {
				medicineSpecItemId = specItem.getId();
			}
		}
		specialtyItemNeeded.setSpecialtyTypeId(medicineSpecItemId);
		System.out.println("New patient with specialty: " + specialtyItemNeeded);
		
		Context.getService(PatientSpecialtyNeededItemService.class).savePatientSpecialtyNeededItem(specialtyItemNeeded);
		
	}
	
	private boolean isPatientInPatientList(Integer patientId) {
		List<PatientListItem> items = Context.getService(PatientListItemService.class).getAllPatientListItems();
		if (items == null) {
			return false;
		}
		for (PatientListItem item : items) {
			if (item.getPatientId() == patientId) {
				return true;
			}
		}
		return false;
	}
	
	private Patient getOldPatientIdOfSamePatient(Patient patient) {
		if (patient.getVoided() == Boolean.TRUE) {
			return patient;
		}
		List<Patient> patients = Context.getPatientService().getAllPatients();
		System.out.println("PatientAfterSaveAdvice: " + patient.getGivenName() + " " + patient.getFamilyName() + " "
		        + patient.getGender() + " " + patient.getAge() + " " + patient.getAttribute("Telephone Number").getValue());
		for (Patient oldPatient : patients) {
			if (oldPatient.getPatientId() == patient.getPatientId()) { // don't check patient against her/himself; just updating demographic data
				continue;
			}
			if ((oldPatient.getGender().equals(patient.getGender()))
			        && (oldPatient.getAge() == patient.getAge())
			        && (oldPatient.getGivenName().equals(patient.getGivenName()))
			        && (oldPatient.getFamilyName().equals(patient.getFamilyName()))
			        && (oldPatient.getAttribute("Telephone Number").getValue().equals(patient.getAttribute(
			            "Telephone Number").getValue()))) {
				System.out.println("PATIENT MATCH!!!!");
				return oldPatient;
			}
		}
		return null;
	}
	
	private void voidNewPatient(Patient patient, User user) {
		patient.setVoided(Boolean.TRUE);
		patient.setVoidedBy(user);
		patient.setVoidReason("Patient is already registered");
	}
}

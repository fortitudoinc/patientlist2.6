/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.PatientSpecialtyNeededItem;
import org.openmrs.module.patientlist.SpecialtyTypeItem;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.patientlist.api.PatientSpecialtyNeededItemService;
import org.openmrs.module.patientlist.api.SpecialtyTypeItemService;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

public class PatientAroundSaveAdvise extends StaticMethodMatcherPointcutAdvisor implements Advisor {
	
	private static final long serialVersionUID = 3333L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Patient oldPatient = null;
	
	public boolean matches(Method method, Class targetClass) {
		if (method.getName().equals("savePatient")) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public Advice getAdvice() {
		log.debug("Getting new around advice");
		return new PatientSaveAroundAdvice();
	}
	
	private class PatientSaveAroundAdvice implements MethodInterceptor {
		
		public Object invoke(MethodInvocation invocation) throws Throwable {
			Object o = null;
			System.out.println("AROUND ADVICE");
			log.debug("Before " + invocation.getMethod().getName() + ".");
			System.out.println("Before " + invocation.getMethod().getName() + ".");
			Object args[] = invocation.getArguments();
			for (int i = 0; i < args.length; i++) {
				//System.out.println(i + ": " + args[i].getClass());
			}
			Patient patient = (Patient) args[0];
			if (isEditingCurrentPatient(patient)) {
				System.out.println("EDITING CURRENT PATIENT");
				return invocation.proceed();
			}
			
			/*
			Here are the phone formats:

			1. 08033201866 (11 digit format recognized by AWS
			2. +2348033201866 (13 digit format with +234 being country code followed by the phone number after the initial 0 is removed). 

			Allow them to enter in either format but then the number is posted on the patientlist in the first format, 
			*/
			PersonAttribute att = patient.getAttribute("Telephone Number");
			String telNo = att.getValue();
			if ((telNo.startsWith("+")) || (telNo.startsWith("234"))) {
				if (telNo.startsWith("+")) {
					telNo = "0" + telNo.substring(4);
				} else {
					telNo = "0" + telNo.substring(3);
				}
				patient.removeAttribute(att);
				att.setValue(telNo);
				patient.addAttribute(att);
			}
			
			System.out.println("AROUND******* telNo: " + patient.getAttribute("Telephone Number").getValue());
			
			if (isPatientAlreadyRegistered(patient)) {
				System.out.println("PATIENT ALREADY REGISTERED");
				if (!isOldPatientInActiveList()) {
					System.out.println("PATIENT ADDING OLD PATIENT TO PATIENT LIST");
					addPatientToActiveList(oldPatient);
				}
				return oldPatient;
			}
			
			// the proceed() method does not have to be called
			Patient newPatient = (Patient) invocation.proceed();
			System.out.println("AFTER SAVE - ADDING NEW PATIENT TO PATIENTLIST: " + newPatient.getGivenName() + " "
			        + newPatient.getFamilyName());
			
			addPatientToActiveList(patient);
			log.debug("After " + invocation.getMethod().getName() + ".");
			//System.out.println("After " + invocation.getMethod().getName() + ".");
			
			return newPatient;
		}
		
		private boolean isEditingCurrentPatient(Patient patient) {
			if (patient.getPatientId() == null) {
				return false;
			}
			return true;
		}
		
		private boolean isOldPatientInActiveList() {
			List<PatientListItem> itemList = Context.getService(PatientListItemService.class).getActivePatientListItems();
			for (PatientListItem item : itemList) {
				if (item.getPatientId() == oldPatient.getPatientId()) {
					return true;
				}
			}
			return false;
		}
		
		private boolean isPatientAlreadyRegistered(Patient patient) {
			List<Patient> patients = Context.getPatientService().getAllPatients();
			System.out.println("in method patientAlreadyRegistered: " + patient.getGivenName() + " "
			        + patient.getFamilyName() + " " + patient.getGender() + " " + patient.getAge() + " "
			        + patient.getAttribute("Telephone Number").getValue());
			for (Patient oldPat : patients) {
				if ((oldPat.getGender().equals(patient.getGender()))
				        && (oldPat.getAge() == patient.getAge())
				        && (oldPat.getGivenName().equals(patient.getGivenName()))
				        && (oldPat.getFamilyName().equals(patient.getFamilyName()))
				        && (oldPat.getAttribute("Telephone Number").getValue().equals(patient.getAttribute(
				            "Telephone Number").getValue()))) {
					System.out.println("PATIENT MATCH!!!!");
					oldPatient = oldPat;
					return true;
				}
			}
			return false;
		}
		
		private void addPatientToActiveList(Patient patient) {
			PatientListItem patientListItem = new PatientListItem();
			User user = Context.getAuthenticatedUser();
			patientListItem.setPatientCallDate(new Date());
			patientListItem.setLastContactAttemptDate(new Date());
			//patientListItem.setLastContactAttemptDate(null);
			patientListItem.setContactAttempts(0);
			patientListItem.setHasBeenCalled(0);
			patientListItem.setVoidedReason("not voidedd");
			if (user != null) {
				patientListItem.setClerkPersonId(user.getPerson().getPersonId()); //needs a person id so default to clerk
				patientListItem.setDrPersonId(user.getPerson().getPersonId());
				System.out.println("DrPersonId: " + patientListItem.getDrPersonId());
			} else { // if registering patient via mobile then there might not be an active user
				     // so set the clerk/dr to super user - person id is 1
				patientListItem.setClerkPersonId(1);
				patientListItem.setDrPersonId(1);
			}
			
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
		
	}
	
}

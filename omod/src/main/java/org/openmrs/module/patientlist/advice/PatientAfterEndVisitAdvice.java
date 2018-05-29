/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.springframework.aop.AfterReturningAdvice;

/**
 * @author levine
 */
public class PatientAfterEndVisitAdvice implements AfterReturningAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (!method.getName().equals("endVisit")) {
			return;
		}
		Visit visit = (Visit) args[0];
		Patient patient = visit.getPatient();
		User user = Context.getAuthenticatedUser();
		System.out.println("*********ENDING VISIT REMOVE FROM LIST: " + patient.getFamilyName() + " Patient Id: "
		        + patient.getPatientId());
		log.debug("Method: " + method.getName() + ". After advice called ");
		
		int patientId = patient.getPatientId();
		PatientListItem patientListItem = null;
		
		List<PatientListItem> items = Context.getService(PatientListItemService.class).getActivePatientListItemForPatient(
		    patientId);
		if (items.size() != 1) {
			System.out.println("AFTER RETURNING - NO PATIENT MATCH FOR ID: " + patientId);
			return;
		}
		patientListItem = items.get(0);
		
		System.out.println("*********ENDING VISIT REMOVE FROM LIST item id: " + patientListItem.getId());
		patientListItem.setLastContactAttemptDate(new Date());
		patientListItem.setHasBeenCalled(1);
		patientListItem.setDrPersonId(user.getPerson().getPersonId());
		Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
	}
}

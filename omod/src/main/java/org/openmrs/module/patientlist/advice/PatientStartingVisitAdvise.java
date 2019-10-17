/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.openmrs.module.patientlist.api.PatientListItemService;
import org.springframework.aop.MethodBeforeAdvice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author levine
 */
public class PatientStartingVisitAdvise implements MethodBeforeAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public void before(Method method, Object[] args, Object o) throws Throwable {
		if (!method.getName().equals("saveVisit")) {
			return;
		}
		o.getClass().getName();
		System.out.println("o.getClass().getName(): " + o.getClass().getName() + "\nargs[0].getName(): "
		        + args[0].getClass().getName());
		Visit visit = (Visit) args[0];
		Patient patient = visit.getPatient();
		User user = Context.getAuthenticatedUser();
		System.out.println("*********Starting VISIT REMOVE FROM LIST: " + patient.getFamilyName() + " Patient Id: "
		        + patient.getPatientId());
		log.debug("Method: " + method.getName() + ". Before advice called ");
		
		int patientId = patient.getPatientId();
		PatientListItem patientListItem = null;
		
		List<PatientListItem> items = Context.getService(PatientListItemService.class).getActivePatientListItemForPatient(
		    patientId);
		if (items.size() != 1) {
			System.out.println("BEFORE RETURNING - NO ACTIVE PATIENT MATCH FOR ID: " + patientId);
			return;
		}
		patientListItem = items.get(0);
		
		System.out.println("*********Starting VISIT REMOVE FROM LIST item id: " + patientListItem.getId());
		patientListItem.setLastContactAttemptDate(new Date());
		patientListItem.setHasBeenCalled(1);
		patientListItem.setDrPersonId(user.getPerson().getPersonId());
		Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
	}
	
}

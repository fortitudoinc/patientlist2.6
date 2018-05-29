package org.openmrs.module.patientlist.advice;

/**
 *
 * @author levine
 */
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientListItem;
import org.springframework.aop.AfterReturningAdvice;
import org.openmrs.module.patientlist.api.PatientListItemService;

public class PatientAfterSaveAdvice implements AfterReturningAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (!method.getName().equals("savePatient")) {
			return;
		}
		User user = Context.getAuthenticatedUser();
		Patient patient = (Patient) args[0];
		System.out.println("Saving new patient: " + patient.getFamilyName());
		log.debug("Method: " + method.getName() + ". After advice called ");
		if (patient.getChangedBy() != null) {
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
		patientListItem.setDrPersonId(user.getPerson().getPersonId()); //needs a person id so default to clerk
		patientListItem.setPatientId(patient.getPatientId());
		PatientListItem item = Context.getService(PatientListItemService.class).savePatientListItem(patientListItem);
		
	}
}

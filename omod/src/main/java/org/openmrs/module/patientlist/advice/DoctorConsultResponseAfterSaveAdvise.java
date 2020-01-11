/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
//import org.openmrs.module.internlmsgs.InternlMessage;
//import org.openmrs.module.internlmsgs.api.InternlMessageService;
import org.springframework.aop.AfterReturningAdvice;

/**
 * @author levine
 */
public class DoctorConsultResponseAfterSaveAdvise implements AfterReturningAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		/*
		if (!method.getName().equals("saveEncounter")) {
		return;
		}
		Encounter encounter = (Encounter) args[0];
		String encTypeName = encounter.getEncounterType().getName();
		//if (!encTypeName.equals("Telemedicine Consult")) {
		if (!encTypeName.equals("Drug order")) {
		
		return;
		}
		System.out.println("SAVE ENCOUNTER: " + encTypeName);
		
		String subject = "Physician consult request response is posted";
		Visit visit = encounter.getVisit();
		Patient patient = encounter.getPatient();
		User requestingDoctor = visit.getCreator();
		User msgSenderUser = Context.getUserService().getUserByUsername("omgbemena");
		String body = "The consult request regarding patient: " + patient.getGivenName() + " " + patient.getFamilyName()
		    + " has been provided.\n";
		sendMessage(msgSenderUser, requestingDoctor, patient, subject, body);
		    */
		return;
	}
	
	void sendMessage(User msgSenderUser, User requestingDoctor, Patient patient, String subject, String body) {
		
		System.out.println("************CONSULT RESPONSE: msgSenderUserUser: " + msgSenderUser.getId()
		        + " requestingDoctor: " + requestingDoctor + " patient: " + patient.getGivenName() + " "
		        + patient.getFamilyName() + " subject: " + subject + " body: " + body);
		
		String recipientUserIds = requestingDoctor.getUserId() + ",";
		
		/*
		InternlMessage msg = new InternlMessage();

		msg.setSenderUserId(msgSenderUser.getId());
		msg.setIsDeleted(0);
		msg.setIsDraft(0);
		msg.setMsgTag("sent-" + msgSenderUser.getId());

		msg.setMsgBody("%" + body);
		msg.setMsgDate(new Date());
		msg.setMsgSubject(subject);
		msg.setMsgPriority(0);
		msg.setIsTrashed(0);
		msg.setMsgRecipients(recipientUserIds);
		msg.setMsgHasBeenRead(0);
		System.out.println("BEFORE MESSAGE SAVED\n" + msg.stringRep());
		InternlMessage m = Context.getService(InternlMessageService.class).saveInternlMessage(msg);
		System.out.println("MESSAGE SAVED SUCCESSFULLY\n" + msg.stringRep());
		m = msg.copy();
		m.setMsgTag("recip-" + requestingDoctor.getUserId());
		Context.getService(InternlMessageService.class).saveInternlMessage(m);
		System.out.println("********NEW MESSAGE\n " + m.stringRep());
		        */
		
	}
}

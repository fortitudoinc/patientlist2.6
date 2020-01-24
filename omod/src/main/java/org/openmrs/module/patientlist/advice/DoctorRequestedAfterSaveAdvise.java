package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.PersonAttribute;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.springframework.aop.AfterReturningAdvice;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import java.util.Random;
import org.apache.commons.codec.binary.Hex;

/**
 * @author levine
 */
public class DoctorRequestedAfterSaveAdvise implements AfterReturningAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		System.out.println("DoctorRequestedAfterSaveAdvise: " + method.getName());
		if (!method.getName().equals("saveDoctorRequestedByPatient")) {
			return;
		}
		DoctorRequestedByPatient docRequested = (DoctorRequestedByPatient) args[0];
		int docId = docRequested.getDoctorId();
		System.out.println("DoctorRequestedAfterSaveAdvise, docId: " + docId);
		User docUser = Context.getUserService().getUser(docId);
		System.out.println("\n\nSending Text to Doctor: " + docUser.getUsername());
		log.debug("Method: " + method.getName() + ". After advice called ");
		PersonAttribute att = docUser.getPerson().getAttribute("Telephone Number");
		if (att == null) {
			return;
		}
		String telNo = att.getValue();
		String message = "A Fortitudo patient has just requested your assistance";
		
		try {
			sendAwsSms(telNo, message);
		}
		catch (Exception e) {
			System.out.println("There was an error sending the sms message:");
			e.printStackTrace();
		}
		
	}
	
	private void sendAwsSms(String phoneNumber, String msg) {
		System.out.println("Sending message to physician, Tel No.: " + phoneNumber);
		
		// Generate a random topic name to avoid collisions
		byte[] b = new byte[20];
		new Random().nextBytes(b);
		String tmpTopicName = "physician-message-tmp-" + Hex.encodeHexString(b);
		// Initialize AWS client
		AmazonSNSClient snsClient = new AmazonSNSClient();
		snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		
		// Create the topic
		CreateTopicResult createTopicResult = snsClient.createTopic(new CreateTopicRequest(tmpTopicName));
		snsClient.subscribe(createTopicResult.getTopicArn(), "SMS", phoneNumber);
		snsClient.publish(createTopicResult.getTopicArn(), msg);
		
		// Cleanup
		snsClient.deleteTopic(createTopicResult.getTopicArn());
		
	}
	
}

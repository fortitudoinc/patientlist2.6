/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import java.util.Set;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PersonAttribute;
import org.openmrs.api.context.Context;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author levine
 */
public class SendSMSLabsDrugsAdvice implements MethodBeforeAdvice {
	
	/*
	Need to get encounter type so we only send SMS when it's either a drug or lab order
	encounter
	config file is modified for the advise; also, the forms and encounters are added
	 */
	@Override
	public void before(Method method, Object[] args, Object o) throws Throwable {
		//System.out.println("\n\n***************SendSMSLabsDrugsAdvice: " + method.getName());
		if (!method.getName().equals("saveEncounter")) {
			return;
		}
		System.out.println("********Method: " + method.getName() + ". Before advice called \n" + "O: " + o);
		for (Object obj : args) {
			if (obj == null) {
				continue;
			}
			System.out.println("Arg class: " + obj.getClass() + "   Arg: " + obj);
			if (obj.getClass() == Encounter.class) {
				Encounter enc = (Encounter) obj;
				//System.out.println("********** DRUG MESSAGE: " + obs.getValueText());
				//Encounter enc = obs.getEncounter();
				if (enc != null) {
					System.out.println("ENCOUNTER: " + enc.getEncounterType().getName());
				} else {
					System.out.println("ENCOUNTER: NULL");
					return;
				}
				String encTypeName = enc.getEncounterType().getName();
				
				if (!((encTypeName.equals("Drug order")) || (encTypeName.equals("Lab order")))) {
					return;
				}
				
				Set<Obs> obss = enc.getAllObs();
				Obs obs = (Obs) obss.toArray()[0];
				System.out.println("********** DRUG/LAB MESSAGE: " + obs.getValueText());
				
				PersonAttribute att = obs.getPerson().getAttribute("Telephone Number");
				if (att != null) {
					String patientTelNo = att.getValue();
					//strip off leading 0 and add country code
					patientTelNo = "+234" + patientTelNo.substring(1);
					System.out.println("Tel no: " + patientTelNo);
				} else {
					System.out.println("Tel no attribute is null");
				}
				
			}
		}
		
	}
	/*    
		@Override"
		public void before(Method method, Object[] args, Object o) throws Throwable {
			System.out.println("\n\n***************SendSMSLabsDrugsAdvice: " + method.getName());
			if (!method.getName().equals("saveObs")) {
				return;
			}
			System.out.println("********Method: " + method.getName() + ". Before advice called \n" + "O: " + o);
			for (Object obj : args) {
				if (obj == null) {
					continue;
				}
				System.out.println("Arg class: " + obj.getClass() + "   Arg: " + obj);
				if (obj.getClass() == Obs.class) {
					Obs obs = (Obs) obj;
					System.out.println("********** DRUG MESSAGE: " + obs.getValueText());
					Encounter enc = obs.getEncounter();
					if (enc != null) {
						System.out.println("ENCOUNTER: " + enc.getEncounterType().getName());
					} else {
						System.out.println("ENCOUNTER: NULL");
					}
					PersonAttribute att = obs.getPerson().getAttribute("Telephone Number");
					if (att != null) {
						String patientTelNo = att.getValue();
						System.out.println("Tel no: " + patientTelNo);
					} else {
						System.out.println("Tel no attribute is null");
					}
					
				}
			}
			
		}
		*/
}

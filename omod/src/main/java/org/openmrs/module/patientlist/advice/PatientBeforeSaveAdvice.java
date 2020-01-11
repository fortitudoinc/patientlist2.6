/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.patientlist.advice;

import java.lang.reflect.Method;
import org.openmrs.Patient;
import org.springframework.aop.MethodBeforeAdvice;

/**
 * @author levine
 */
public class PatientBeforeSaveAdvice implements MethodBeforeAdvice {
	
	/*
	    THIS IS NOT USED - NOT MENTIONED IN THE CONFIG FILE
	*/
	@Override
	public void before(Method method, Object[] os, Object o) throws Throwable {
		if (!method.getName().equals("savePatient")) {
			return;
		}
		System.out.println("PatientBeforeSaveAdvice***************");
		for (int i = 0; i < os.length; i++) {
			System.out.println(i + ": " + os[i].getClass());
		}
		if (o != null) {
			System.out.println("o: " + o.getClass());
		}
		Patient patient = (Patient) os[0];
		System.out.println("Telephone: " + patient.getAttribute("Telephone Number").getValue());
	}
	
}

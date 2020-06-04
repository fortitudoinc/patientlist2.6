package org.openmrs.module.patientlist.web.controller;

import java.util.Date;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.DoctorRequestedByPatient;
import org.openmrs.module.patientlist.DoctorRequestedShort;
import org.openmrs.module.patientlist.api.DoctorRequestedByPatientService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/doctorrequested", supportedClass = DoctorRequestedShort.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class DoctorRequestedResource extends DataDelegatingCrudResource<DoctorRequestedShort> {
	
	@Override
	public NeedsPaging<User> doGetAll(RequestContext context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DoctorRequestedShort getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	protected void delete(DoctorRequestedShort t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public void purge(DoctorRequestedShort t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientUUID");
			description.addProperty("doctorUserId");
			description.addLink("default", ".?v=" + RestConstants.REPRESENTATION_DEFAULT);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientUUID");
			description.addProperty("doctorUserId");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@Override
	public DoctorRequestedShort newDelegate() {
		System.out.println("****************newDelegate: ");
		return new DoctorRequestedShort();
	}
	
	@Override
	public DoctorRequestedShort save(DoctorRequestedShort t) {
		System.out.println("****************SAVE: " + t.getDoctorUserId() + " " + t.getPatientUUID());
		DoctorRequestedByPatient doctorRequestedByPatient = new DoctorRequestedByPatient();
		doctorRequestedByPatient.setDateCreated(new Date());
		Patient patient = Context.getPatientService().getPatientByUuid(t.getPatientUUID());
		doctorRequestedByPatient.setPatientId(patient.getPatientId());
		doctorRequestedByPatient.setDoctorId(Integer.valueOf(t.getDoctorUserId()));
		Context.getService(DoctorRequestedByPatientService.class).saveDoctorRequestedByPatient(doctorRequestedByPatient);
		return t;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(DoctorRequestedShort item) {
		return item.getPatientUUID();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patientUUID");
		description.addProperty("doctorUserId");
		return description;
	}
}

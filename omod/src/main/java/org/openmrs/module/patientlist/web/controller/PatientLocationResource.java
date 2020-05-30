package org.openmrs.module.patientlist.web.controller;

import java.util.Date;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PatientLocation;
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
@Resource(name = RestConstants.VERSION_1 + "/patientlocation", supportedClass = PatientLocation.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class PatientLocationResource extends DataDelegatingCrudResource<PatientLocation> {
	
	@Override
	public NeedsPaging<PatientLocation> doGetAll(RequestContext context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public PatientLocation getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	protected void delete(PatientLocation t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void purge(PatientLocation t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientUUID");
			description.addProperty("patientLocation");
			description.addLink("default", ".?v=" + RestConstants.REPRESENTATION_DEFAULT);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("patientUUID");
			description.addProperty("patientLocation");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@Override
	public PatientLocation newDelegate() {
		System.out.println("****************newDelegate: ");
		return new PatientLocation();
	}
	
	@Override
	public PatientLocation save(PatientLocation t) {
		System.out.println("****************SAVE: " + t.getPatientUUID() + " location: " + t.getPatientLocation());
		Person person = Context.getPersonService().getPersonByUuid(t.getPatientUUID());
		Patient patient = Context.getPatientService().getPatientByUuid(t.getPatientUUID());
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Address");
		PersonAttribute p = new PersonAttribute();
		p.setPerson(person);
		p.setDateCreated(new Date());
		p.setAttributeType(attType);
		p.setValue(t.getPatientLocation());
		patient.addAttribute(p);
		// patient UUID is taken from person UUID
		
		Context.getPatientService().savePatient(patient);
		return t;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(PatientLocation item) {
		return item.getPatientUUID();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("patientUUID");
		description.addProperty("patientLocation");
		return description;
	}
}

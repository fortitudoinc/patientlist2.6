package org.openmrs.module.patientlist.web.controller;

/**
 *
 * @author levine
 */
import java.util.Date;
import java.util.List;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.PersonCountries;
import org.openmrs.module.patientlist.PersonCountriesShort;
import org.openmrs.module.patientlist.api.PersonCountriesService;
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
@Resource(name = RestConstants.VERSION_1 + "/personcountries", supportedClass = PersonCountriesShort.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class PersonCountriesResource extends DataDelegatingCrudResource<PersonCountriesShort> {
	
	/*
	Use this as:
	curl -u admin:Admin123 -X POST "http://localhost:8081/openmrs/ws/rest/v1/personcountries" -H  "accept: application/json" -H  "content-type: application/json" -d "{  \"personId\": \"9\",  \"countries\": \"NIGERIA,GHANA\"}"
	RESULT:
	{"personId":"9","countries":"NIGERIA,GHANA","links":[{"rel":"default","uri":"http://localhost:8081/openmrs/ws/rest/v1/personcountries/be2f4175-8d0c-4eba-aaf3-d35383d5d221?v=default"},{"rel":"self","uri":"http://localhost:8081/openmrs/ws/rest/v1/personcountries/be2f4175-8d0c-4eba-aaf3-d35383d5d221"}],"resourceVersion":"1.8"}
	
	
	 */
	@Override
	public NeedsPaging<PersonCountriesShort> doGetAll(RequestContext context) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public PersonCountriesShort getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	protected void delete(PersonCountriesShort t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public void purge(PersonCountriesShort t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("personUUID");
			description.addProperty("countries");
			description.addLink("default", ".?v=" + RestConstants.REPRESENTATION_DEFAULT);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("personUUID");
			description.addProperty("countries");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	@Override
	public PersonCountriesShort newDelegate() {
		System.out.println("****************newDelegate: ");
		return new PersonCountriesShort();
	}
	
	@Override
	public PersonCountriesShort save(PersonCountriesShort t) {
		System.out.println("****************SAVE: " + t.getPersonUUID() + " countries: " + t.getCountries());
		Person person = Context.getPersonService().getPersonByUuid(t.getPersonUUID());
		PersonCountries personCountries;
		int personId = person.getPersonId();
		List<PersonCountries> pp = Context.getService(PersonCountriesService.class).getPersonCountriesForPerson(personId);
		if ((pp == null) || (pp.size() == 0)) {
			personCountries = new PersonCountries();
			personCountries.setCountries(t.getCountries());
			personCountries.setDateCreated(new Date());
			personCountries.setPersonId(personId);
		} else {
			personCountries = pp.get(0);
			personCountries.setCountries(t.getCountries());
			personCountries.setDateCreated(new Date());
		}
		Context.getService(PersonCountriesService.class).savePersonCountries(personCountries);
		return t;
	}
	
	@PropertyGetter("display")
	public String getDisplayString(PersonCountriesShort item) {
		return item.getCountries() + item.getPersonUUID();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("personUUID");
		description.addProperty("countries");
		return description;
	}
}

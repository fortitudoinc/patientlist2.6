package org.openmrs.module.patientlist.web.controller;

/**
 *
 * @author levine
 */
import org.openmrs.api.context.Context;
import org.openmrs.module.patientlist.AllCountriesRESTWithCountryCode;
import org.openmrs.module.patientlist.Country;
import org.openmrs.module.patientlist.api.CountryService;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.openmrs.module.webservices.rest.web.resource.impl.DataDelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;

/**
 * @author levine
 */
@Resource(name = RestConstants.VERSION_1 + "/allcountrieswithcountrycode", supportedClass = Country.class, supportedOpenmrsVersions = {
        "2.0.*", "2.1.*", "2.2.*", "2.3.*", "2.4.*" })
public class CountryResource extends DataDelegatingCrudResource<Country> {
	
	public NeedsPaging<Country> doGetAll(RequestContext context) {
		System.out.println("****************doGetAll: " + context);
		return new NeedsPaging<Country>(Context.getService(CountryService.class).getAllCountry(), context);
		
	}
	
	public Country getByUniqueId(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	protected void delete(Country t, String string, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public void purge(Country t, RequestContext rc) throws ResponseException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	public DelegatingResourceDescription getRepresentationDescription(Representation r) {
		if (r instanceof DefaultRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			description.addProperty("countryCode");
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
			description.addSelfLink();
			return description;
		} else if (r instanceof FullRepresentation) {
			DelegatingResourceDescription description = new DelegatingResourceDescription();
			description.addProperty("name");
			description.addProperty("countryCode");
			description.addSelfLink();
			return description;
		}
		return null;
	}
	
	public Country newDelegate() {
		System.out.println("****************newDelegate: ");
		return new Country();
	}
	
	@PropertyGetter("display")
	public String getDisplayString(Country item) {
		return item.getName() + "/" + item.getCountryCode();
	}
	
	@Override
	public Country save(Country t) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}

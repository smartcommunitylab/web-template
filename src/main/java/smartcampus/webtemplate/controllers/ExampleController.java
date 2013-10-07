/**
 *    Copyright 2012-2013 Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package smartcampus.webtemplate.controllers;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;
import it.sayservice.platform.smartplanner.data.message.otpbeans.Route;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.aac.model.TokenData;
import eu.trentorise.smartcampus.communicator.CommunicatorConnector;
import eu.trentorise.smartcampus.communicator.CommunicatorConnectorException;
import eu.trentorise.smartcampus.communicator.model.Notification;
import eu.trentorise.smartcampus.filestorage.client.Filestorage;
import eu.trentorise.smartcampus.filestorage.client.FilestorageException;
import eu.trentorise.smartcampus.filestorage.client.model.Account;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.mobilityservice.MobilityDataService;
import eu.trentorise.smartcampus.mobilityservice.MobilityPlannerService;
import eu.trentorise.smartcampus.mobilityservice.MobilityServiceException;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import eu.trentorise.smartcampus.social.model.Group;
import eu.trentorise.smartcampus.socialservice.SocialService;
import eu.trentorise.smartcampus.socialservice.SocialServiceException;
import eu.trentorise.smartcampus.territoryservice.TerritoryService;
import eu.trentorise.smartcampus.territoryservice.TerritoryServiceException;
import eu.trentorise.smartcampus.territoryservice.model.EventObject;
import eu.trentorise.smartcampus.territoryservice.model.ObjectFilter;

@Controller
public class ExampleController {

	private static final String client_secret = "8fa7819d-9fa1-4fbd-8fef-e3873df7dd85";
	private static final String client_id = "a5d402a1-8fcc-46b0-993e-bd97e37eef9c";
	private BasicProfileService profileService = new BasicProfileService(
			"https://vas-dev.smartcampuslab.it/aac");
	private MobilityDataService mobilityDataService = new MobilityDataService(
			"https://vas-dev.smartcampuslab.it/core.mobility");
	private MobilityPlannerService mobilityPlannerService = new MobilityPlannerService(
			"https://vas-dev.smartcampuslab.it/core.mobility");
	private TerritoryService territoryService = new TerritoryService(
			"https://vas-dev.smartcampuslab.it/core.territory");
	private CommunicatorConnector communicatorConnector = null;
	private Filestorage filestorage = new Filestorage(
			"https://vas-dev.smartcampuslab.it/core.filestorage", "rk");
	private SocialService socialService = new SocialService(
			"https://vas-dev.smartcampuslab.it/core.social");
	private AACService aacService = new AACService(
			"https://vas-dev.smartcampuslab.it/aac",
			client_id,
			client_secret);
	
	
	private String userToken="";

	public ExampleController() {
		super();
		try {
			communicatorConnector = new CommunicatorConnector(
					"https://vas-dev.smartcampuslab.it/core.communicator", "web-template");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index(HttpServletRequest request) {

		return "index";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/exit")
	public String exit(HttpServletRequest request) {

		return "exit";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/secure")
	public ModelAndView x(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		Map<String,Object> model = new HashMap<String, Object>();	
		if(userToken!=null && userToken.compareTo("")!=0){
			
			model.put("token", userToken);	
			BasicProfile bp=profileService.getBasicProfile(userToken);
			model.put("name", bp.getName());	
			model.put("surname", bp.getSurname());	
		}
		 return new ModelAndView("secure",model);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request,@RequestParam(required=false) String code) throws SecurityException, AACException {
		
		
		String redirectUri = "http://localhost:8080/web-template/check";	

		
		userToken= aacService.exchngeCodeForToken(code, redirectUri).getAccess_token();		

		return new ModelAndView("redirect:/secure");		
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {
		userToken=null;
		String redirectUri = "http://localhost:8080/web-template/check";

		return new ModelAndView("redirect:"
				+ aacService.generateAuthorizationURIForCodeFlow(redirectUri,
					
						null, "smartcampus.profile.basicprofile.me", null));
	}
	
	
//	
//	@RequestMapping(method = RequestMethod.GET, value = "/implicit")
//	public ModelAndView implicit(HttpServletRequest request,@RequestParam(required=false) String token) throws AACException {
//		
//		userToken=token;
//        
//		return new ModelAndView("redirect:/secure");
//	}
	

	/*
	 * Example to get the profile of the authenticated user.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/profile")
	public @ResponseBody
	BasicProfile getProfile(HttpServletRequest request)
			throws SecurityException, ProfileServiceException {
		BasicProfile profile = profileService
				.getBasicProfile(getToken(request));
		return profile;
	}

	/*
	 * Request all the routes for Trentino Trasporti (agencyId = "12")
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/routes")
	public @ResponseBody
	List<Route> getRoutes(HttpServletRequest request)
			throws MobilityServiceException {
		List<Route> routes = mobilityDataService.getRoutes("12",
				getToken(request));
		return routes;
	}

	/*
	 * Plan a single journey
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/plansinglejourney")
	public @ResponseBody
	List<Itinerary> planSingleJourney(HttpServletRequest request)
			throws MobilityServiceException {
		SingleJourney req = new SingleJourney();
		req.setDate("03/28/2013");
		req.setDepartureTime("10:25");
		Position from = new Position();
		from.setLat("46.062005");
		from.setLon("11.129169");
		Position to = new Position();
		to.setLat("46.068854");
		to.setLon("11.151184");
		req.setFrom(from);
		req.setTo(to);
		TType[] tt = new TType[] { TType.TRANSIT };
		req.setTransportTypes(tt);
		req.setResultsNumber(1);
		req.setRouteType(RType.fastest);

		List<Itinerary> itineraries = mobilityPlannerService.planSingleJourney(
				req, getToken(request));
		return itineraries;
	}

	/*
	 * Get all the events whose category is "Concerts"
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/concerts")
	public @ResponseBody
	List<EventObject> getConterts(HttpServletRequest request)
			throws TerritoryServiceException {
		ObjectFilter filter = new ObjectFilter();
		filter.setTypes(Collections.singletonList("Concerts"));
		filter.setFromTime(System.currentTimeMillis());
		return territoryService.getEvents(filter, getToken(request));
	}

	/*
	 * Get all notifications
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/notification")
	public @ResponseBody
	List<Notification> getNotifications(HttpServletRequest request)
			throws CommunicatorConnectorException {
		List<Notification> result = communicatorConnector
				.getNotificationsByUser(0L, 0, -1, getToken(request))
				.getNotifications();
		return (List<Notification>) result;
	}

	/*
	 * Example to get all storage user accounts owned by the authenticated user
	 * bound to an application.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/useraccount")
	public @ResponseBody
	Account getUserAccounts(HttpServletRequest request)
			throws FilestorageException {
		return filestorage.getAccountByUser(getToken(request));
	}

	/*
	 * Example to get the information about a stored resource.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/metadata")
	public @ResponseBody
	Metadata getResourceMetadata(HttpServletRequest request)
			throws FilestorageException {
		List<Metadata> list = filestorage.getAllResourceMetadataByApp(
				getToken(request), 0, 1);
		if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}

	/*
	 * Example to get some social information about a user. Example shows how
	 * retrieve the group created by the user
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/rest/group")
	public @ResponseBody
	List<Group> getUsergroups(HttpServletRequest request)
			throws SecurityException, SocialServiceException {
		return socialService.getUserGroups(getToken(request)).getContent();
	}

	/**
	 * Here we assume that the access token is placed in the current security
	 * context by the PRE_AUTH_FILTER
	 * 
	 * @return
	 */
	private String getToken(HttpServletRequest request) {
		return (String) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

}

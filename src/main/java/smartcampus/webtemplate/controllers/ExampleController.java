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
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	private BasicProfileService profileService = new BasicProfileService("https://vas-dev.smartcampuslab.it/aac");
	private MobilityDataService mobilityDataService = new MobilityDataService("https://vas-dev.smartcampuslab.it/core.mobility");
	private MobilityPlannerService mobilityPlannerService = new MobilityPlannerService("https://vas-dev.smartcampuslab.it/core.mobility");
	private TerritoryService territoryService = new TerritoryService("https://vas-dev.smartcampuslab.it/core.territory");
	private CommunicatorConnector communicatorConnector = null;
	private Filestorage filestorage = new Filestorage("https://vas-dev.smartcampuslab.it/core.filestorage", "rk");
	private SocialService socialService = new SocialService("https://vas-dev.smartcampuslab.it/core.social");

	public ExampleController() {
		super();
		try {
			communicatorConnector = new CommunicatorConnector("https://vas-dev.smartcampuslab.it/core.communicator", "rk");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Example to get the profile of the authenticated user.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/profile")
	public @ResponseBody
	BasicProfile getProfile(HttpServletRequest request) throws SecurityException, ProfileServiceException {
		BasicProfile profile = profileService.getBasicProfile(getToken(request));
		return profile;
	}

	/*
	 * Request all the routes for Trentino Trasporti (agencyId = "12")
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/routes")
	public @ResponseBody
	List<Route> getRoutes(HttpServletRequest request)
			throws MobilityServiceException {
		List<Route> routes = mobilityDataService.getRoutes("12", getToken(request));
		return routes;
	}

	/*
	 * Plan a single journey
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/plansinglejourney")
	public @ResponseBody
	List<Itinerary> planSingleJourney(HttpServletRequest request) throws MobilityServiceException {
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

		List<Itinerary> itineraries = mobilityPlannerService.planSingleJourney(req, getToken(request));
		return itineraries;
	}

	/*
	 * Get all the events whose category is "Concerts"
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/concerts")
	public @ResponseBody
	List<EventObject> getConterts(HttpServletRequest request) throws TerritoryServiceException {
		ObjectFilter filter = new ObjectFilter();
		filter.setTypes(Collections.singletonList("Concerts"));
		filter.setFromTime(System.currentTimeMillis());
		return territoryService.getEvents(filter, getToken(request));
	}

	/*
	 * Get all notifications
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/notification")
	public @ResponseBody
	List<Notification> getNotifications(HttpServletRequest request) throws CommunicatorConnectorException {
		List<Notification> result = communicatorConnector.getNotificationsByUser(0L, 0, -1, getToken(request)).getNotifications();
		return (List<Notification>) result;
	}

	/*
	 * Example to get all storage user accounts owned by the authenticated user
	 * bound to an application.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/useraccount")
	public @ResponseBody
	Account getUserAccounts(HttpServletRequest request) throws FilestorageException {
		return filestorage.getAccountByUser(getToken(request));
	}

	/*
	 * Example to get the information about a stored resource. 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/metadata")
	public @ResponseBody
	Metadata getResourceMetadata(HttpServletRequest request)
			throws FilestorageException {
		List<Metadata> list = filestorage.getAllResourceMetadataByApp(getToken(request), 0, 1);
		if (list != null && ! list.isEmpty()) return list.get(0);
		return null;
	}



	/*
	 * Example to get some social information about a user. Example shows how
	 * retrieve the group created by the user
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/group")
	public @ResponseBody
	List<Group> getUsergroups(HttpServletRequest request) throws SecurityException, SocialServiceException {
		return socialService.getUserGroups(getToken(request)).getContent();
	}

	/**
	 * Here we assume that the access token is placed in the current security context by the PRE_AUTH_FILTER
	 * @return
	 */
	private String getToken(HttpServletRequest request) {
		return (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}


}

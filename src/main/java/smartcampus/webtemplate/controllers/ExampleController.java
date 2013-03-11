package smartcampus.webtemplate.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.ac.provider.AcService;
import eu.trentorise.smartcampus.ac.provider.filters.AcProviderFilter;
import eu.trentorise.smartcampus.filestorage.client.Filestorage;
import eu.trentorise.smartcampus.filestorage.client.FilestorageException;
import eu.trentorise.smartcampus.filestorage.client.model.AppAccount;
import eu.trentorise.smartcampus.filestorage.client.model.Metadata;
import eu.trentorise.smartcampus.filestorage.client.model.UserAccount;
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller("exampleController")
public class ExampleController {

	private static final Logger logger = Logger
			.getLogger(ExampleController.class);
	@Autowired
	private AcService acService;

	/*
	 * the base url of the service. Configure it in webtemplate.properties
	 */
	@Autowired
	@Value("${services.server}")
	private String serverAddress;

	/*
	 * Example to get the profile of the authenticated user.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/getprofile")
	public @ResponseBody
	BasicProfile getProfile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(
					serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			return profile;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	/*
	 * Example to get all storage application accounts binded to a specific
	 * application. Example uses tutorial application account named 'hackathon'.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/appaccount")
	public @ResponseBody
	List<AppAccount> getAppAccounts(HttpServletRequest request)
			throws FilestorageException {
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");
		return filestorage.getAppAccounts();
	}

	/*
	 * Example to get all storage user accounts owned by the authenticated user
	 * binded to an application . Example uses tutorial application account
	 * named 'hackathon'
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/useraccount")
	public @ResponseBody
	List<UserAccount> getuserAccounts(HttpServletRequest request)
			throws FilestorageException {
		String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");

		return filestorage.getUserAccounts(token);
	}

	/*
	 * Example to get the information about a stored resource. Example uses the
	 * tutorial application account named 'hackathon' and a default resource
	 * already stored (resourceId = 513da746975aa4412a383769
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/metadata")
	public @ResponseBody
	Metadata getResourceMetadata(HttpServletRequest request)
			throws FilestorageException {
		String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
		Filestorage filestorage = new Filestorage(serverAddress, "hackathon");
		return filestorage.getResourceMetadata(token,
				"513da746975aa4412a383769");
	}

}

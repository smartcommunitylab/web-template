package smartcampus.webtemplate.controllers;

import java.io.IOException;

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
import eu.trentorise.smartcampus.profileservice.ProfileConnector;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;


@Controller("exampleController")
public class ExampleController {

	private static final Logger logger = Logger.getLogger(ExampleController.class);
	@Autowired
	private AcService acService;	
	
	@Autowired
	@Value("${services.server}")
	private String serverAddress;
	
	@RequestMapping(method = RequestMethod.GET, value = "/getprofile")
	public @ResponseBody
	BasicProfile getProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			String token = request.getHeader(AcProviderFilter.TOKEN_HEADER);
			ProfileConnector profileConnector = new ProfileConnector(serverAddress);
			BasicProfile profile = profileConnector.getBasicProfile(token);
			return profile;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return null;
	}		

	
}

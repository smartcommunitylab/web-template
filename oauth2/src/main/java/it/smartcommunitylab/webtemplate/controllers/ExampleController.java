/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
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

package it.smartcommunitylab.webtemplate.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
public class ExampleController {
	/**
	 * INSERT A VALID CLIENT_ID AND CLIENT_SECRET CODES FROM SMARTCOMMUNITY
	 * PERMISSION PROVIDER
	 */
	private static final String CLIENT_ID = "18f6db6f-398c-4ee3-b341-662c4b58786d";
	private static final String CLIENT_SECRET = "4796ea88-0506-4727-a7ad-f8766d5231fb";

	private static final String PROFILE_SERVICE_ENDPOINT = "https://dev.smartcommunitylab.it/aac";
	private static final String AAC_SERVICE_ENDPOINT = "https://dev.smartcommunitylab.it/aac";
	private static final String REDIRECT_URI = "http://localhost:8080/webtemplate.oauth2/check";

	private BasicProfileService profileService = new BasicProfileService(
			PROFILE_SERVICE_ENDPOINT);

	private AACService aacService = new AACService(AAC_SERVICE_ENDPOINT,
			CLIENT_ID, CLIENT_SECRET);

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String index(HttpServletRequest request) {

		return "index";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/secure")
	public ModelAndView x(HttpServletRequest request) throws SecurityException,
			ProfileServiceException {
		Map<String, Object> model = new HashMap<String, Object>();
		String userToken = (String) request.getSession().getAttribute(
				"accessToken");
		if (userToken != null && userToken.compareTo("") != 0) {

			model.put("token", userToken);
			BasicProfile bp = profileService.getBasicProfile(userToken);
			model.put("name", bp.getName());
			model.put("surname", bp.getSurname());
		}
		return new ModelAndView("secure", model);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/check")
	public ModelAndView securePage(HttpServletRequest request,
			@RequestParam(required = false) String code)
			throws SecurityException, AACException {

		String userToken = aacService.exchngeCodeForToken(code, REDIRECT_URI)
				.getAccess_token();
		request.getSession().setAttribute("accessToken", userToken);
		return new ModelAndView("redirect:/secure");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login")
	public ModelAndView secure(HttpServletRequest request) {

		return new ModelAndView("redirect:"
				+ aacService.generateAuthorizationURIForCodeFlow(REDIRECT_URI,
						null, "profile.basicprofile.me", null));
	}

}

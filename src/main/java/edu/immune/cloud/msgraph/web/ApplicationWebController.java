package edu.immune.cloud.msgraph.web;

import java.io.IOException;
import java.util.Properties;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.immune.cloud.msgraph.helper.InMemoryCache;
import edu.immune.cloud.msgraph.helper.MicrosoftGraphEndPoint;

/**
 * End point for all such calls that interact through browser and have a web
 * page of their own
 * 
 * @author Lalit Mehra
 *
 */
@Controller
@Validated
public class ApplicationWebController {

	@Autowired
	private Properties properties;

	@Autowired
	private InMemoryCache cache;

	@Autowired
	private MicrosoftGraphEndPoint msEndPoint;

	private static final String AMP = "&";

	private String prepareRequestParam(String state) {
		String clientId = properties.getProperty("client.id");
		String redirectUri = properties.getProperty("redirect_uri");

		StringBuilder builder = new StringBuilder().append("client_id=").append(clientId).append(AMP).append(
				"scope=offline_access User.Read Calendars.Read Calendars.ReadWrite&response_type=code&response_mode=query&")
				.append("redirect_uri=").append(redirectUri).append("&state=").append(state);

		return builder.toString();
	}

	/**
	 * Default page renderer
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String index(Model model) {
		return "index";
	}

	/**
	 * Authorize call initiator end-point
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@PostMapping("authorize")
	public String authorize(@RequestParam(name = "user", required = true) @NotBlank String user, Model model) {
		String authUrl = msEndPoint.getAuthorizationUrl();
		String redirectUrl;

		try {
			redirectUrl = "redirect:" + authUrl + prepareRequestParam(user);
		} catch (NullPointerException ne) {
			redirectUrl = "missingprop";
			ne.printStackTrace();
		}

		return redirectUrl;
	}

	/**
	 * Redirect URI end-point configured in Azure portal 
	 * 
	 * @param code
	 * @param state
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "authorize/response")
	public String authResponse(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state)
			throws IOException {
		cache.setCode(state, code);
		return "code";
	}
}

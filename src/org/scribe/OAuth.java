/**
 * @author Fernando José Iglesias García, April 2012 based on 
 * TwitterExample.java from scribe-java
 */

package org.scribe;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OAuth {

	/**
	 * Some of the credentials of the application registered in twitter 
	 * developers necessary to do requests with OAuth privileges
	 */
	
	private static final String CONSUMER_KEY = "cWq25sJqFfkkYbJn76kYVw";
	
	// According to twitter developers web, this shouldn't be stored in human
	// readable format but ....
	private static final String CONSUMER_SECRET 
					= "m7uZxK1LvEbGyfjHjbeQn7N2XxH43Ka7p9UDVE7Y";
	
	private static Token accessToken;
	
	private static OAuthService service;
	
	public static void authenticate() {
	
		service = new ServiceBuilder()
					.provider(TwitterApi.class)
					.apiKey(CONSUMER_KEY)
					.apiSecret(CONSUMER_SECRET)
					.build();
		Scanner in = new Scanner(System.in);
		
		System.out.println("=== Twitter's OAuth Workflow ===");
		System.out.println();
		
		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println();
		
		System.out.println("Now go and authorize the application here:");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();
		
		// Trade the Request Token and Verifier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		setAccessToken(service.getAccessToken(requestToken, verifier));
		System.out.println("Got the Access Token!");
		
		System.out.println();
	}

	public static void signRequest(OAuthRequest request) {
		service.signRequest(accessToken, request);
	}
	
	public static Token getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(Token accessToken) {
		OAuth.accessToken = accessToken;
	}
	
}

package AuthAwsProject.AuthAwsProject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.opensdk.config.ConnectionConfiguration;
import com.amazonaws.opensdk.config.TimeoutConfiguration;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.cognitoidentity.model.Credentials;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetCredentialsForIdentityResult;
import com.amazonaws.services.cognitoidentity.model.GetIdRequest;
import com.amazonaws.services.cognitoidentity.model.GetIdResult;

import api.gw.stage.demo.apiGoogle;
import api.gw.stage.demo.apiGoogleClientBuilder;
import api.gw.stage.demo.model.PostGoogleRequest;
import api.gw.stage.demo.model.PostGoogleResult;

public class App {

	final static String identityPoolId = "us-east-2:fce76517-ab1b-4998-88db-ee7fe0d5ae63";
	static String idpJwtToken = "";

	/***
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			// Enter your valid JWT token from a federated identity
			System.out.println("enter id");
			Scanner sc = new Scanner(System.in);
			idpJwtToken = sc.nextLine();
			System.out.println("started");
			
			// create a Cognito client instance for authenticating the request
			AmazonCognitoIdentityClient client = new AmazonCognitoIdentityClient();
			client.setRegion(Region.getRegion(Regions.US_EAST_2));

			// create a request object for the current invokation
			GetIdRequest request = new GetIdRequest();
			request.setIdentityPoolId(identityPoolId);

			// set the login options to define against which provider the JWT token should be validated on
			// the name must equal to the provider configured in AWS IAM (Federation)
			Map<String, String> logins = new HashMap<String, String>();
			logins.put("raptestidp.auth0.com", idpJwtToken);
			request.setLogins(logins);

			// create the result object
			GetIdResult result = client.getId(request);
			
			// invoke the clients get identity method. The result is an Id that represents your identity in the Cognito identity pool
			String cognitoIdentityId = result.getIdentityId();

			System.out.println("cognitoIdentityId is : " + cognitoIdentityId);
			
			// get the current AWS credentials based on the determined Cognito identity ID
			GetCredetialsForIdentity(cognitoIdentityId);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	/***
	 * 
	 * @param cognitoIdentityId
	 */
	private static void GetCredetialsForIdentity(String cognitoIdentityId) {

		// see above
		AmazonCognitoIdentityClient client = new AmazonCognitoIdentityClient();
		client.setRegion(Region.getRegion(Regions.US_EAST_2));

		// create the request object to get temporally AWS credentials
		GetCredentialsForIdentityRequest request = new GetCredentialsForIdentityRequest();
		request.setIdentityId(cognitoIdentityId);

		// set login provider (see above)
		Map<String, String> logins = new HashMap<String, String>();
		logins.put("raptestidp.auth0.com", idpJwtToken);
		request.setLogins(logins);

		// invoke method
		GetCredentialsForIdentityResult result = client.getCredentialsForIdentity(request);

		// get a temporally valid AWS credential pair for invoking the API on the API-Gateway
		Credentials awsTempCredentials = result.getCredentials();

		// Invoke a API-Gateway function with the determined credentials
		InvokeTestApiWithIdentity(awsTempCredentials);
	}

	/***
	 * 
	 * @param awsTempCredentials
	 */
	private static void InvokeTestApiWithIdentity(Credentials awsTempCredentials) {
		System.out.println("AWS AccessKeyId : " + awsTempCredentials.getAccessKeyId());
		
		// create a credential object for the request
		// set the previously determined temporal keys
		BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
				awsTempCredentials.getAccessKeyId(), awsTempCredentials.getSecretKey(),
				awsTempCredentials.getSessionToken());
		
		// create a builder object for the client to invoke the API
		apiGoogleClientBuilder builder =apiGoogle.builder();
		// set credentials 
		builder.iamCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials));
		// create an API client for invoking the service
		apiGoogle client = builder.connectionConfiguration(
	                      new ConnectionConfiguration()
	                            .maxConnections(100)
	                            .connectionMaxIdleMillis(1000))
	                  .timeoutConfiguration(
	                      new TimeoutConfiguration()
	                            .httpRequestTimeout(3000)
	                            .totalExecutionTimeout(10000)
	                            .socketTimeout(2000))
	                  .build();
		// create request object for the API 
		// set API parameter here if necessary
		PostGoogleRequest request = new PostGoogleRequest();
		
		// invoke service and get response object
		PostGoogleResult result = client.postGoogle(request);
		System.out.println("successful");
		
		// parse response object and extract parameters if necessary
		System.out.println(result.toString());	    
	}
}

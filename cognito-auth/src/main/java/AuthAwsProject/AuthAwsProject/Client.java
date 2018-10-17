package AuthAwsProject.AuthAwsProject;

import com.gigya.socialize.GSResponseListener;

public class Client {
	public void initRegistration() {
		
		GSResponseListener resListener = new InitGSResponseListener();
		// Step 3 - Sending the request
		String methodName = "accounts.initRegistration";
		GSAPI.getInstance().sendRequest(methodName, null, resListener, null);
	}
}

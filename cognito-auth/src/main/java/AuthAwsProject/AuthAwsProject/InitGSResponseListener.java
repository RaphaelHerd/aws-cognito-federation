package AuthAwsProject.AuthAwsProject;

import com.gigya.socialize.GSResponse;
import com.gigya.socialize.GSResponseListener;

public class InitGSResponseListener implements GSResponseListener {
	
	/***
	 * 
	 */
	private String init_token = new String();
	
	/***
	 * 
	 * @return
	 */
	public String getInitToken() {
		return init_token;
	}
	
	/***
	 * 
	 */
	@Override
	public void onGSResponse(String method, GSResponse response, Object context) {
		try {
			System.out.println(response.toString());
			if (response.getErrorCode() == 0) {

				init_token = response.getData().getString("regToken");

			} else { // Error
				System.out.println("Got error on setStatus: " + response.getLog());
				System.out.println(response.getErrorMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

package org.openmrs.module.patientlist.advice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author levine
 */
public class RestPostGet {
	
	public String doPostRestCall(String url, String action) {
		System.out.println("DOPOSTRESTCALL: " + url + "  " + action);
		String response = null;
		
		try {
			URL urlForPost = new URL(url);
			
			HttpURLConnection postConnJSON = (HttpURLConnection) urlForPost.openConnection();
			String encoded = Base64.getEncoder().encodeToString(
			    ("restuser" + ":" + "Jx59Ums413p").getBytes(StandardCharsets.UTF_8)); //Java 8
			postConnJSON.setRequestProperty("Authorization", "Basic " + encoded);
			postConnJSON.setDoOutput(true);
			postConnJSON.setRequestMethod("POST");
			postConnJSON.setRequestProperty("Content-Type", "application/json");
			postConnJSON.setRequestProperty("Accept", "application/json");
			OutputStream os = postConnJSON.getOutputStream();
			byte[] input = action.getBytes("utf-8");
			os.write(input, 0, input.length);
			//System.out.println("POST Response Message : " + postConnJSON.getResponseMessage());
			
			response = getRestResponse(postConnJSON);
		}
		catch (Exception ex) {
			System.out.println("****************** POST ISSUE: " + ex);
			
		}
		return response;
	}
	
	public String doGetRestCall(String url) throws IOException, ProtocolException {
		URL urlForGET = null;
		try {
			urlForGET = new URL(url);
		}
		catch (Exception ex) {
			System.out.println("****************** GET ISSUE: " + ex);
			//System.exit(1);
			
		}
		HttpURLConnection getConnJSON = (HttpURLConnection) urlForGET.openConnection();
		String encoded = Base64.getEncoder().encodeToString(("admin" + ":" + "Admin123").getBytes(StandardCharsets.UTF_8)); // Java 8
		
		getConnJSON.setRequestProperty("Authorization", "Basic " + encoded);
		getConnJSON.setDoOutput(true);
		getConnJSON.setRequestMethod("GET");
		getConnJSON.setRequestProperty("Content-Type", "application/json");
		getConnJSON.getInputStream();
		
		int jSonResponse = getConnJSON.getResponseCode();
		return getRestResponse(getConnJSON);
	}
	
	private String getRestResponse(HttpURLConnection connectionToURL) throws IOException {
		int code = connectionToURL.getResponseCode();
		if (connectionToURL.getResponseCode() >= 300) {
			throw new RuntimeException("Failed : HTTP error code : " + connectionToURL.getResponseCode());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader((connectionToURL.getInputStream())));
		String output, msg = "";
		//System.out.println("Output from Server .... \n");
		//System.out.println("Response Code: " + connectionToURL.getResponseCode());
		while ((output = br.readLine()) != null) {
			msg += output;
			//System.out.println(output);
		}
		connectionToURL.disconnect();
		return msg;
	}
}

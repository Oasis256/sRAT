package client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {
	
	public interface Callback {
		public void run(String response);
	}
	
	public static void sendAsync(String targetURL, String request, Callback callback) {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				String response = send(targetURL, request);
				callback.run(response);
			}
			
		}).start();
		
	}
	
	public static String send(String targetURL, String request) {
		
		try {
			// Setting up connection
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			/*conn.setRequestProperty(
					"Content-Length",
					Integer.toString(request.getBytes().length));
			conn.setUseCaches(false);*/
			conn.setDoOutput(true);
			
			// Sending request
			DataOutputStream stream =
					new DataOutputStream(conn.getOutputStream());
			stream.writeBytes(request);
			stream.close();
			
			// Getting response
			InputStream is = conn.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			reader.close();
			return response.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

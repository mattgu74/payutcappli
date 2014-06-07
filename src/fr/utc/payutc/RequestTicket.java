package fr.utc.payutc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class RequestTicket extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		// On cr�� un client http
				HttpClient httpclient = new DefaultHttpClient();

				// On cr�� notre ent�te
				HttpPost httppost = new HttpPost("https://cas.utc.fr/cas/v1/tickets/" + params[0]); 
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);  
				
				nameValuePairs.add(new BasicNameValuePair("service", params[1]));  
		        
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
			        HttpResponse response;
					response = httpclient.execute(httppost);
					
				// get the tbt
					String rep =  EntityUtils.toString(response.getEntity());
//					Log.d("REP", rep);
					
					return rep;
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
		
	}

}

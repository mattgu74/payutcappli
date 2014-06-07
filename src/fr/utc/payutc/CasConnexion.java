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

public class CasConnexion extends AsyncTask<String, Integer, String> {


	@Override
	protected String doInBackground(String... params) {
		// On cr�� un client http
		HttpClient httpclient = new DefaultHttpClient();

		// On cr�� notre ent�te
		HttpPost httppost = new HttpPost("https://cas.utc.fr/cas/v1/tickets");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
		nameValuePairs.add(new BasicNameValuePair("username", params[0].toString()));  
		nameValuePairs.add(new BasicNameValuePair("password", params[1].toString()));  
		
        
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
			
	        HttpResponse response;
			response = httpclient.execute(httppost);
			
		// get the tbt
			String rep =  EntityUtils.toString(response.getEntity());
			int index = rep.indexOf("/cas/v1/tickets/");
			
			rep.charAt(index+"/cas/v1/tickets/".length() -1);
			String tbt = rep.substring(index+"/cas/v1/tickets/".length());
			tbt = tbt.split("\"")[0];
			
			return tbt;
			
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

package fr.utc.payutc;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.net.wifi.WifiConfiguration.Protocol;
import android.os.AsyncTask;
import android.util.Log;

public class LoginCas extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		// On créé un client http
				HttpClient httpclient = new DefaultHttpClient();

				// On créé notre entête
				HttpPost httppost = new HttpPost("https://assos.utc.fr/buckutt/MYACCOUNT/logincas");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);  
				nameValuePairs.add(new BasicNameValuePair("ticket", params[0].toString()));  
				nameValuePairs.add(new BasicNameValuePair("service", params[1].toString()));  
				
			    try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
					
			        HttpResponse response;
					response = httpclient.execute(httppost);
					
				// get the tbt
					String rep =  EntityUtils.toString(response.getEntity());
					Log.d("rep", rep);
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

package fr.utc.payutc;

import java.security.KeyStore;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button connectingbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//peer authentification
		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//
//		DefaultHttpClient client = new DefaultHttpClient();
//
//		SchemeRegistry registry = new SchemeRegistry();
//		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//		registry.register(new Scheme("https", socketFactory, 443));
//		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//
//		// Set verifier     
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//		HttpsURLConnection.setDefaultSSLSocketFactory(createAdditionalCertsSSLSocketFactory());
		HostnameVerifier v = new HostnameVerifier() {
        	public boolean verify(String hostname, SSLSession session) {
        		return true;
        	}
        };
        HttpsURLConnection.setDefaultHostnameVerifier(v);
		
		connectingbutton = (Button) findViewById(R.id.connect);
		connectingbutton.setOnClickListener(new View.OnClickListener() {
	  			
	    	@Override
	    	public void onClick(View v) {
	    		EditText user = (EditText) findViewById(R.id.user_email);
	    		EditText pass = (EditText) findViewById(R.id.user_password);
	    		
	    		CasConnexion casConnexion = new CasConnexion();
	    		try {
	    			String service ="http://localhost";
					String tbt = casConnexion.execute(user.getText().toString(), pass.getText().toString()).get();
//					Log.d("TBT", tbt);
					
					if (tbt.startsWith("TGT")){
						RequestTicket rt = new RequestTicket();
						String ticket = rt.execute(tbt, service).get();
						
//						ticket = ticket.split("-cas.utc.fr")[0];
						if (ticket.startsWith("ST")){
					//well connected
							LoginCas lc = new LoginCas();
							String ma = lc.execute(getResources().getText(R.string.url).toString(),
													ticket, service).get();
							Log.d("ma", ma);
							
							LoginApp la = new LoginApp();
							
							String lapp = la.execute(getResources().getText(R.string.url).toString(), 
													getResources().getText(R.string.key).toString()).get();
							Log.d("lapp", lapp);
							
							Historique histo = new Historique();
							String his = histo.execute(getResources().getText(R.string.url).toString()).get();
							Log.d("his", his);
							
						}else{
							Toast.makeText(MainActivity.this, "Erreur login", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(MainActivity.this, "Erreur login", Toast.LENGTH_SHORT).show();
					}
					
	    		} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	    	}
	    	
		});
	}
	
//	 protected SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
//	        try {
////	            final KeyStore ks = KeyStore.getInstance("BKS");
////
////	            // the bks file we generated above
////	            final InputStream in = getApplicationContext().getResources().openRawResource( R.raw.mystore);  
////	            try {
////	                // don't forget to put the password used above in strings.xml/mystore_password
////	                ks.load(in, "pauline".toCharArray());
////	            } finally {
////	                in.close();
////	            }
//	        	  try {
//	        	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//	        	        trustStore.load(null, null);
//
//	        	        SSLSocketFactory sf = new AdditionalKeyStoresSSLSocketFactory(trustStore);
//	        	        sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//	        	        HttpParams params = new BasicHttpParams();
//	        	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//	        	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//	        	        SchemeRegistry registry = new SchemeRegistry();
//	        	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//	        	        registry.register(new Scheme("https", (SocketFactory) sf, 443));
//
//	        	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//	        	        return new DefaultHttpClient(ccm, params);
//	        	    } catch (Exception e) {
//	        	        return new DefaultHttpClient();
//	        	    }
//	            
//	            /*TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//	            tmf.init(ks);
//	            SSLContext context = SSLContext.getInstance("SSL");
//	            context.init(null, tmf.getTrustManagers(), null);
//	            return context.getSocketFactory();*/
//
//	        } catch( Exception e ) {
//	            throw new RuntimeException(e);
//	        }
//	        
//	        
//	    }
	 
	
}
	

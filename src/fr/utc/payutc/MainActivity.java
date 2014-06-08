package fr.utc.payutc;

import static fr.utc.payutc.CommonUtilities.SENDER_ID;
import static fr.utc.payutc.CommonUtilities.TAG;

import java.io.IOException;
import java.io.InputStream;import java.security.KeyStore;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.apache.http.protocol.HTTP;import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	private Button connectingbutton;
	
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    //String SENDER_ID = "292387549777";

    /**
     * Tag used on log messages.
     */
    //static final String TAG = "GCMDemo";
    
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;

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
		context = getApplicationContext();
		// Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
        	System.out.println("herer");
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
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
	 
	
	 
	 /**
	  * Check the device to make sure it has the Google Play Services APK. If
	  * it doesn't, display a dialog that allows users to download the APK from
	  * the Google Play Store or enable it in the device's system settings.
	  */
	 private boolean checkPlayServices() {
	     int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	     if (resultCode != ConnectionResult.SUCCESS) {
	         if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	             GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                     PLAY_SERVICES_RESOLUTION_REQUEST).show();
	         } else {
	             Log.i(TAG, "This device is not supported.");
	             finish();
	         }
	         return false;
	     }
	     return true;
	 }
	 
	 /**
	  * Gets the current registration ID for application on GCM service.
	  * <p>
	  * If result is empty, the app needs to register.
	  *
	  * @return registration ID, or empty string if there is no existing
	  *         registration ID.
	  */
	 private String getRegistrationId(Context context) {
	     final SharedPreferences prefs = getGCMPreferences(context);
	     String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	     if (registrationId.isEmpty()) {
	         Log.i(TAG, "Registration not found.");
	         return "";
	     }
	     // Check if app was updated; if so, it must clear the registration ID
	     // since the existing regID is not guaranteed to work with the new
	     // app version.
	     int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	     int currentVersion = getAppVersion(context);
	     if (registeredVersion != currentVersion) {
	         Log.i(TAG, "App version changed.");
	         return "";
	     }
	     return registrationId;
	 }
	 
	 /**
	  * @return Application's {@code SharedPreferences}.
	  */
	 private SharedPreferences getGCMPreferences(Context context) {
	     // This sample app persists the registration ID in shared preferences, but
	     // how you store the regID in your app is up to you.
	     return getSharedPreferences(MainActivity.class.getSimpleName(),
	             Context.MODE_PRIVATE);
	 }
	 
	 /**
	  * @return Application's version code from the {@code PackageManager}.
	  */
	 private static int getAppVersion(Context context) {
	     try {
	         PackageInfo packageInfo = context.getPackageManager()
	                 .getPackageInfo(context.getPackageName(), 0);
	         return packageInfo.versionCode;
	     } catch (NameNotFoundException e) {
	         // should never happen
	         throw new RuntimeException("Could not get package name: " + e);
	     }
	 }
	 
	 /**
	  * Registers the application with GCM servers asynchronously.
	  * <p>
	  * Stores the registration ID and app versionCode in the application's
	  * shared preferences.
	  */
	 private void registerInBackground() {
	     AsyncTask<Void, String, String> register_task = new AsyncTask<Void, String, String>() {
	    	 @Override
	    	 protected String doInBackground(Void... params) {
	             String msg = "";
                 Log.i(TAG, "getting gcm");
	             try {
	                 if (gcm == null) {
	                     gcm = GoogleCloudMessaging.getInstance(context);
	                 }
	                 Log.i(TAG, "gcm got");
	                 regid = gcm.register(SENDER_ID);
	                 msg = "Device registered, registration ID=" + regid;
	                 Log.i(TAG, msg);

	                 // You should send the registration ID to your server over HTTP,
	                 // so it can use GCM/HTTP or CCS to send messages to your app.
	                 // The request to your server should be authenticated if your app
	                 // is using accounts.
	                 sendRegistrationIdToBackend();

	                 // For this demo: we don't need to send it because the device
	                 // will send upstream messages to a server that echo back the
	                 // message using the 'from' address in the message.

	                 // Persist the regID - no need to register again.
	                 storeRegistrationId(context, regid);
	             } catch (IOException ex) {
	                 msg = "Error :" + ex.getMessage();
	                 // If there is an error, don't just keep trying to register.
	                 // Require the user to click a button again, or perform
	                 // exponential back-off.
	             }
	             return msg;
	         }
			
	        protected void onPostExecute(String msg) {
	            mDisplay.append(msg + "\n");
	        }
		};
		register_task.execute(null, null, null);
	 }
	 
	 /**
	  * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
	  * or CCS to send messages to your app. Not needed for this demo since the
	  * device sends upstream messages to a server that echoes back the message
	  * using the 'from' address in the message.
	  */
	 private void sendRegistrationIdToBackend() {
	     // Your implementation here.
	 }
	 
	 /**
	  * Stores the registration ID and app versionCode in the application's
	  * {@code SharedPreferences}.
	  *
	  * @param context application's context.
	  * @param regId registration ID
	  */
	 private void storeRegistrationId(Context context, String regId) {
	     final SharedPreferences prefs = getGCMPreferences(context);
	     int appVersion = getAppVersion(context);
	     Log.i(TAG, "Saving regId on app version " + appVersion);
	     SharedPreferences.Editor editor = prefs.edit();
	     editor.putString(PROPERTY_REG_ID, regId);
	     editor.putInt(PROPERTY_APP_VERSION, appVersion);
	     editor.commit();
	 }
}
	

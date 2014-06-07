package fr.utc.payutc;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

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
		HttpsURLConnection.setDefaultSSLSocketFactory(createAdditionalCertsSSLSocketFactory());
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
						
						ticket = ticket.split("-cas.utc.fr")[0];
						if (ticket.startsWith("ST")){
					//well connected
							LoginCas lc = new LoginCas();
							String ma = lc.execute(ticket, service).get();
							Log.d("ma", ma);
							
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
	
	 protected SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
	        try {
	            final KeyStore ks = KeyStore.getInstance("BKS");

	            // the bks file we generated above
	            final InputStream in = getApplicationContext().getResources().openRawResource( R.raw.mystore);  
	            try {
	                // don't forget to put the password used above in strings.xml/mystore_password
	                ks.load(in, "pauline".toCharArray());
	            } finally {
	                in.close();
	            }

	            return new AdditionalKeyStoresSSLSocketFactory(ks);
	            
	            /*TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	            tmf.init(ks);
	            SSLContext context = SSLContext.getInstance("SSL");
	            context.init(null, tmf.getTrustManagers(), null);
	            return context.getSocketFactory();*/

	        } catch( Exception e ) {
	            throw new RuntimeException(e);
	        }
	        
	        
	    }
}
	

package fr.utc.payutc;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabBar extends TabActivity implements OnTabChangeListener{
	 
    /** Called when the activity is first created. */
      TabHost tabHost;
       
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_tab_bar);
          
          // Get TabHost Refference
          tabHost = getTabHost();
           
          // Set TabChangeListener called when tab changed
          tabHost.setOnTabChangedListener(this);
       
          TabHost.TabSpec spec;
          Intent intent;
     
           /************* TAB Home ************/
          // Create  Intents to launch an Activity for the tab (to be reused)
          intent = new Intent().setClass(this, HomeTab.class);
          spec = tabHost.newTabSpec("First").setIndicator("")
                        .setContent(intent);
           
          //Add intent to tab
          tabHost.addTab(spec);
     
          /************* TAB Invite ************/
          intent = new Intent().setClass(this, RechargementTab.class);
          spec = tabHost.newTabSpec("Second").setIndicator("")
                        .setContent(intent);  
          tabHost.addTab(spec);
     
          /************* TAB To DO List ************/
          intent = new Intent().setClass(this, HistoriqueTab.class);
          spec = tabHost.newTabSpec("Third").setIndicator("")
                        .setContent(intent);
          tabHost.addTab(spec);
          
          /************* TAB Course ************/
          intent = new Intent().setClass(this, HistoriqueTab.class);
          spec = tabHost.newTabSpec("Four").setIndicator("")
                        .setContent(intent);
          tabHost.addTab(spec);
          
          
          /************* TAB Chat ************/
          intent = new Intent().setClass(this, EtatIvresseTab.class);
          spec = tabHost.newTabSpec("Five").setIndicator("")
                        .setContent(intent);
          tabHost.addTab(spec);
          
     
          // Set drawable images to tab
          tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#ececea"));
          tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.ic_action_person);
          tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.ic_action_go_to_today);
          tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.ic_action_paste);
          tabHost.getTabWidget().getChildAt(4).setBackgroundResource(R.drawable.ic_action_chat);
             
          // Set Tab1 as Default tab and change image   
          tabHost.getTabWidget().setCurrentTab(0);
          tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.ic_action_cloud_clicked);
           
     
       }
 
    @Override
    public void onTabChanged(String tabId) {
         
        /************ Called when tab changed *************/
         
        //********* Check current selected tab and change according images *******/
         
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            if(i==0)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_action_cloud);
            else if(i==1)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_action_person);
            else if(i==2)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_action_go_to_today);
            else if(i==3)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_action_paste);
            else if(i==4)
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_action_chat);

        }
         
         
        Log.i("tabs", "CurrentTab: "+tabHost.getCurrentTab());
         
    if(tabHost.getCurrentTab()==0)
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_action_cloud_clicked);
    else if(tabHost.getCurrentTab()==1)
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_action_person_clicked);
    else if(tabHost.getCurrentTab()==2)
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_action_go_to_today_clicked);
    else if(tabHost.getCurrentTab()==3)
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_action_paste_clicked);
    else if(tabHost.getCurrentTab()==4)
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_action_chat_clicked);   
    }
    
}
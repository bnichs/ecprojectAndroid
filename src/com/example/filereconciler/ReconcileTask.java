package com.example.filereconciler;

import java.io.File;
import java.net.InetAddress;

import ec504project.application.CoreReconciler;
import android.os.AsyncTask;

class ReconcileTask extends AsyncTask<Object, Void, String> {

   /// private Exception exception;
private CoreReconciler crc;

    protected String doInBackground(Object... stuff) {
      
        	File fil = (File) stuff[0];
        	InetAddress ip = (InetAddress)stuff[1];
        	CoreReconciler crc = new CoreReconciler(fil,ip);
        	
            return "";
       
    }

    protected void onPostExecute() {
        // TODO: check this.exception 
        // TODO: do something with the feed
    }
}
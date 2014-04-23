package com.example.filereconciler;

import java.io.File;
import java.net.InetAddress;

import ec504project.application.CoreReconciler;
import ec504project.application.CoreReconciler.Metrics;
import android.os.AsyncTask;

class ReconcileTask extends AsyncTask < Object, Void, Metrics > {

	/// private Exception exception;
	private CoreReconciler	crc;

	protected Metrics doInBackground(Object... stuff) {

		File fil = (File) stuff[0];
		InetAddress ip = (InetAddress) stuff[1];
		CoreReconciler crc = new CoreReconciler(fil, ip);
		Metrics m = crc.getPerformanceMetrics();
		return m;

	}

	protected void onPostExecute() {
		// TODO: check this.exception 
		// TODO: do something with the feed
	}
}
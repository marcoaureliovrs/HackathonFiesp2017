package com.example.eizesazake.fiesp.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.example.eizesazake.fiesp.locationdata.LocationData;
import com.example.eizesazake.fiesp.connections.CheckConnectivity;

//There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
// Therefore, it should be fixed in order to get location updates correctly

public class LatLngAppService extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private Service serviceContext = this;
	
	// There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
	// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
	// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
	// Therefore, it should be fixed in order to get location updates correctly		
	private LocationData locationData;	
	
	
	private Long paramMotoLoginId;

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("LatLngAppServiceStartArguments",
				android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startLatLngAppId) {
		
//		if (intent != null) {
//			Bundle params = intent.getExtras();
//			if (params != null) {
//				paramMotoLoginId = params.getLong("motoLoginId");
//			}
//		}
		
		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startLatLngAppId;
		mServiceHandler.sendMessage(msg);
		
		

		return START_STICKY;
//		return START_REDELIVER_INTENT;
	}

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// Normally we would do some work here, like download a file.
			// For our sample, we just sleep for 5 seconds.
//				long endTime = System.currentTimeMillis() + 5 * 1000;
//			while (infinite) {
				synchronized (this) {
					try {
						Boolean conn = CheckConnectivity.checkNow(serviceContext);
						if (conn == true) {
							
							// There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
							// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
							// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
							// Therefore, it should be fixed in order to get location updates correctly	
							locationData = new LocationData(serviceContext, 0);
							
							// There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
							// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
							// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
							// Therefore, it should be fixed in order to get location updates correctly							
							locationData.getmLocationClient().connect();
						}
				    	
				    	
					}catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//			}
			// Stop the service using the startLatLngAppId, so that we don't stop
			// the service in the middle of handling another job
//			stopSelf(msg.arg1);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {
		
		// There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
		// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
		// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
		// Therefore, it should be fixed in order to get location updates correctly			
		if (locationData != null){
			locationData.stopUpdates();
		}
	}
}
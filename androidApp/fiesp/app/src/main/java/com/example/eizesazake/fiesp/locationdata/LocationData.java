package com.example.eizesazake.fiesp.locationdata;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.eizesazake.fiesp.connections.CheckConnectivity;
import com.example.eizesazake.fiesp.jsonclass.StringResultJson;
import com.example.eizesazake.fiesp.model.Denuncia;
import com.example.eizesazake.fiesp.service.InternalDBService;
import com.example.eizesazake.fiesp.service.StringUtilsService;
import com.example.eizesazake.fiesp.sqlite.InternalDBSqlite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// There is a problem with LocationData class. GooglePlayServices has been updated, and LocationClient class
// has been deprecated as a result. LocationClient has been replaced by GoogleApiClient according to this:
// http://stackoverflow.com/questions/24611977/android-locationclient-class-is-deprecated-but-used-in-documentation.
// Therefore, it should be fixed in order to get location updates correctly

public class LocationData implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Activity activity;
    private Context context;
    private Long paramMotoboyId;
    private Location location;
    private InternalDBSqlite internalDBSqlite = new InternalDBSqlite(context);
    private StringResultJson result;
	
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
//    private LocationClient mLocationClient;
    private GoogleApiClient mGoogleApiClient;

    /*
     * Initialize the Activity
     */
    public LocationData (Context context, long paramMotoboyId){

        this.context = context;
        this.paramMotoboyId = paramMotoboyId;
        // Create a new global location parameters object
        mLocationRequest = new LocationRequest();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy - in order to work, must set android.permission.ACCESS_FINE_LOCATION
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        /**
         * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
         * LocationServices API.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
//            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
//                errorFragment.show(context.getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }

    public void stopUpdates() {

//        if (servicesConnected()) {
            stopPeriodicUpdates();
//        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {

        startPeriodicUpdates();

    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                   activity,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

        // Log the error
        e.printStackTrace();
    }
} else {

//        // If no resolution is available, display a dialog to the user with the error.
//        showErrorDialog(connectionResult.getErrorCode());
        }
        }

/**
 * Report location updates to the UI.
 *
 * @param location The updated location.
 */
@Override
public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
//        mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
        String latlong = LocationUtils.getLatLng(context, location);

//        Toast toast = Toast.makeText(context, "Sua geolocalização: " + latlong, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.TOP, 0, 200);
//        toast.show();

//        InternalDBService.sqliteAddUserLogin(internalDBSqlite, latlong);
        this.location = location;
        LocationDataAsyncTask locationDataAsyncTask = 
        		new LocationDataAsyncTask();
        Boolean conn = CheckConnectivity.checkNow(context);
		if (conn == true) {
			locationDataAsyncTask.execute(latlong);
		}
//		else {
//			UserMsgService.showDialog(context, R.string.no_connectivity_title,
//					R.string.no_connectivity_login);
//		}
        
        
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

//    	if (servicesConnected()) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
//    	}
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
    	
    	// If the client is connected
        if (mGoogleApiClient.isConnected()) {
        	LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        // After disconnect() is called, the client is considered "dead".
        mGoogleApiClient.disconnect();
    	
    }

//    /**
//     * Show a dialog returned by Google Play services for the
//     * connection error code
//     *
//     * @param errorCode An error code returned from onConnectionFailed
//     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode,
            activity,
            LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
//            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
	public class LocationDataAsyncTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... latlng) {
			StringResultJson toString = new StringResultJson();
			
			try {
				toString.setResult(paramMotoboyId.toString().concat(",").concat(latlng[0]));
				String gson = new Gson().toJson(toString);

                Denuncia denuncia = new Denuncia();

                String latlong = LocationUtils.getLatLng(context, location);

                denuncia.setLat(StringUtilsService.formatLatLng(String.valueOf(location.getLatitude())));
                denuncia.setLng(StringUtilsService.formatLatLng(String.valueOf(location.getLongitude())));

                String json = new Gson().toJson(denuncia);
                System.out.println(json);

                String url_ = "https://hackathonfiesp2017-marcogorak.c9users.io/geolocation";

                URL url = new URL(url_);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                OutputStream os = urlConnection.getOutputStream();
                os.write(json.getBytes());
                os.flush();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader =
                        new InputStreamReader(in);

                result = new Gson().fromJson(reader,
                        StringResultJson.class);
                String test = "test";
			} catch (Exception exception) {
				exception.printStackTrace(); // show exception details
				return false;
			}
			
			return null;
		}

        @Override
        protected void onPostExecute(final Boolean success) {
            InternalDBSqlite internalDBSqlite = new InternalDBSqlite(context.getApplicationContext());

            String latlong = LocationUtils.getLatLng(context, location);

            InternalDBService internalDBService;
            internalDBService = new InternalDBService(context.getApplicationContext());
            internalDBService.getmEditor().putString(InternalDBService.LATLONG, latlong);
            if (result != null && result.getResult() != null){
                internalDBService.getmEditor().putString(InternalDBService.ALARM, result.getResult());
                internalDBService.getmEditor().putString(InternalDBService.ADDRESS, result.getAddress());
                Intent intent = new Intent("alarm");
                context.getApplicationContext().sendBroadcast(intent);
            }
            internalDBService.getmEditor().commit();
            String paramPassword = internalDBService.getmPrefs().getString(InternalDBService.LATLONG, null);
            String test = "";
        }
		
	}
	
	public GoogleApiClient getmLocationClient() {
		return mGoogleApiClient;
	}

	@Override
	public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
	}
	
}

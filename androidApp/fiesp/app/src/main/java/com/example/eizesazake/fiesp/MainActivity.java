package com.example.eizesazake.fiesp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.eizesazake.fiesp.appservice.LatLngAppService;
import com.example.eizesazake.fiesp.jsonclass.StringResultJson;
import com.example.eizesazake.fiesp.model.Denuncia;
import com.example.eizesazake.fiesp.service.InternalDBService;
import com.example.eizesazake.fiesp.service.StaticVars;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        DenunciaFragment.OnFragmentInteractionListener{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Intent intentLatLngAppServ;

    private Activity activity = this;

    private int position;

    private String descDenuncia;

    private String latlong;

    private Ringtone r;
    Uri alert;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            InternalDBService internalDBService;
            internalDBService = new InternalDBService(activity);

            String address = internalDBService.getmPrefs().getString(InternalDBService.ADDRESS, null);


//            if(alert == null){
                // alert is null, using backup
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (r == null) {
                r = RingtoneManager.getRingtone(getApplicationContext(), alert);
                r.play();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alerta Fiscal Cidação");
                builder.setMessage("Confirme a situação da obra localizada em: " + address).show();
                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                r.stop();
                            }
                        }).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
        this.position = position;
        GeoTask geoTask = new GeoTask();
        geoTask.execute();

    }

    public void initDenunciaFragment(int position) {
        // Create new fragment and transaction
        Fragment newFragment = new DenunciaFragment();

        initFragment(position, newFragment);
    }

    public void initFragment(int position, Fragment newFragment) {

        Bundle args = new Bundle();
//        args.putInt("section_number", position + 1);
//        if (loginStringJson != null){
//            args.putString("loginStringJson", loginStringJson);
//        }
//        args.putString("listJsonString", listJsonString);

        newFragment.setArguments(args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, newFragment);

        // Commit the transaction
        transaction.commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_enviar) {

            DenunciaFragment denunciaFragment;
            denunciaFragment = (DenunciaFragment) getFragmentManager().findFragmentById((R.id.container));
            descDenuncia = denunciaFragment.getDescricao();
            EnviarDenunciaTask enviarDenunciaTask = new EnviarDenunciaTask();
            enviarDenunciaTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public class GeoTask extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... gcmRegId) {

            try {
                intentLatLngAppServ = new Intent(activity, LatLngAppService.class);
                Bundle params = new Bundle();
                params.putLong("id", 0);
                intentLatLngAppServ.putExtras(params);

                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        ){//Can add more as per requirement

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            123);
                }else if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ){
                    startService(intentLatLngAppServ);
                }



            }catch (Exception ex){

            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (position == StaticVars.TITLE_SECTION_DENUNCIA - 1) {
                initDenunciaFragment(position);
            }
        }

    }

    public class EnviarDenunciaTask extends AsyncTask<String, Void, Boolean> {

        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String... gcmRegId) {

            try {
                Denuncia denuncia = new Denuncia();

                denuncia.setDescription(descDenuncia);
                String json = new Gson().toJson(denuncia);
                System.out.println(json);

                String url_ = "https://hackathonfiesp2017-marcogorak.c9users.io/suspect";

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

                StringResultJson result = new Gson().fromJson(reader,
                        StringResultJson.class);
                String test = "test";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (position == StaticVars.TITLE_SECTION_DENUNCIA - 1) {
                initDenunciaFragment(position);
            }
        }

    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver, new IntentFilter("alarm"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(broadcastReceiver);
        r.stop();
    }
}

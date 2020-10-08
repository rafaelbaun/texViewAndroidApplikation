package de.lingen.hsosna.texview;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

import de.lingen.hsosna.texview.Login.Login;
import de.lingen.hsosna.texview.database.TableArtikelkombination;
import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerbestand_Summe;
import de.lingen.hsosna.texview.database.TableLagerplaetze;
import de.lingen.hsosna.texview.database.TableTimestamp;
import de.lingen.hsosna.texview.fragments.SettingsFragment;
import de.lingen.hsosna.texview.fragments.FilterFragment;
import de.lingen.hsosna.texview.fragments.HomeFragment;
import de.lingen.hsosna.texview.fragments.InfoFragment;
import de.lingen.hsosna.texview.fragments.KPIFragment;
import de.lingen.hsosna.texview.fragments.RegalfrontFragment;
import de.lingen.hsosna.texview.fragments.SearchFragment;
import de.lingen.hsosna.texview.fragments.ShelffrontTemporaryFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static de.lingen.hsosna.texview.fragments.RegalfrontFragment.ARG_SHELVESTOMARKRED;

/**
 * Die MainActivity ist die gesamte Zeit der Applikationslaufzeit aktiv, da sie über Fragmente und
 * Fragmentswitcher Ihren Inhalt wechselt.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   SearchFragment.SearchFragmentListener, RegalfrontFragment.ShelfFrontFragmentListener,
                   ShelffrontTemporaryFragment.ShelfFrontFragmentListener {

    private static final String TAG = "MainActivity";
    public static boolean colorSwitchState;
    public static long timestampServerGlobal;

    private DrawerLayout drawer;
    private View decorView;
    private BottomSheetBehavior mBottomSheetBehaviour;

    public static ArrayList<Lagerplatz> freeShelveList;

    private DatabaseHelper dbHelper;
    public SQLiteDatabase mDatabase;


    private boolean isConnected = false;
    private ImageView connectedImage;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SERVER_URL_SOCKET);
            isConnected = true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getReadableDatabase();
        freeShelveList = getFreeShelves();

        connectedImage = findViewById(R.id.toolbar_imageConnected);
        connectedImage.setVisibility(View.VISIBLE);
        changeConnectedIcon();

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange (int visibility) {
                        if (visibility == 0) {
                            decorView.setSystemUiVisibility(hideSystemBars());
                        }
                    }
                });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            //anything else that doesnt update ui
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment(), "HOME_FRAGMENT").commit();
            navigationView.setCheckedItem(R.id.nav_home);

            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.on("timestamp", onTimestamp);
            mSocket.on("dbOperation", onOperation);
            mSocket.connect();
        }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  ANDROID OVERWRITES                 /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Das angeklickte Menüitem in dem Navigationsdrawer wird unterschieden und das jeweilige
     * Fragment aufgerufen.
     * Der Navigationsdrawer wird nach der Auswahl geschlossen.
     *
     * @param item Das angeklickte Item im Navigationsmenu
     * @return Es wird true returned um das ausgewählte Item als angewähltes Item zu markieren.
     */
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment(), "HOME_FRAGMENT").addToBackStack("HOME_FRAGMENT").commit();
                break;
            case R.id.nav_filter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FilterFragment(), "FILTER_FRAGMENT").addToBackStack("FILTER_FRAGMENT").commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment(), "SEARCH_FRAGMENT").addToBackStack("SEARCH_FRAGMENT").commit();
                break;
            case R.id.nav_kpi:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new KPIFragment(), "KPI_FRAGMENT").addToBackStack("KPI_FRAGMENT").commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InfoFragment(), "INFO_FRAGMENT").addToBackStack("INFO_FRAGMENT").commit();
                break;
            // TODO nav_settings
            case R.id.nav_dbcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment(), "DB_FRAGMENT").addToBackStack("DB_FRAGMENT").commit();
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }



    /**
     * Methode um den Fullscreen Modus beizubehalten, wenn die App minimiert oder das Gerät gedreht wird.
     *
     * @param hasFocus Booleanvariable ob die Applikation im Fokus ist
     */
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }



    /**
     * Wenn der Zurück-Knopf gedrückt wird, soll der Navigationsdrawer einfahren, wenn er offen ist.
     * Ansonsten soll die Standardfunktion gewährleistet sein.
     */
    @Override
    public void onBackPressed () {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        // switch color
        MenuItem itemSwitch = menu.findItem(R.id.switchColor);
        itemSwitch.setActionView(R.layout.switch_color);
        Switch sw = (Switch) menu.findItem(R.id.switchColor).getActionView().findViewById(R.id.switchColorAction);

        if (colorSwitchState) {
            sw.setChecked(true);
        }

        /* color switch changes */
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             *
             * @param buttonView
             * @param isChecked
             */
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    colorSwitchState = true;

                    //HOMEFRAGMENT
                    HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager()
                            .findFragmentByTag("HOME_FRAGMENT");
                    if (homeFragment != null && homeFragment.isVisible()) {
                        homeFragment.markFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //FILTERFRAGMENT
                    FilterFragment filterFragment = (FilterFragment)getSupportFragmentManager()
                            .findFragmentByTag("FILTER_FRAGMENT");
                    if (filterFragment != null && filterFragment.isVisible()) {
                        filterFragment.markFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //REGALFRONTFRAGMENT
                    RegalfrontFragment regalfrontFragment = (RegalfrontFragment)getSupportFragmentManager()
                            .findFragmentByTag("REGALFRONT_FRAGMENT");
                    if (regalfrontFragment != null && regalfrontFragment.isVisible()) {
                        regalfrontFragment.markFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                } else {
                    colorSwitchState = false;
                    //HOMEFRAGMENT
                    HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager()
                            .findFragmentByTag("HOME_FRAGMENT");
                    if (homeFragment != null && homeFragment.isVisible()) {
                        homeFragment.unmarkFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //FILTERFRAGMENT
                    FilterFragment filterFragment = (FilterFragment)getSupportFragmentManager()
                            .findFragmentByTag("FILTER_FRAGMENT");
                    if (filterFragment != null && filterFragment.isVisible()) {
                        filterFragment.unmarkFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //REGALFRONTFRAGMENT
                    RegalfrontFragment regalfrontFragment = (RegalfrontFragment)getSupportFragmentManager()
                            .findFragmentByTag("REGALFRONT_FRAGMENT");
                    if (regalfrontFragment != null && regalfrontFragment.isVisible()) {
                        regalfrontFragment.unmarkFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                }
            }
        });

        return true;
    }



    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchButton:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment(), "SEARCH_FRAGMENT").addToBackStack("SEARCH_FRAGMENT").commit();
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_search);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     *
     */
    @Override
    protected void onDestroy () {
        super.onDestroy();

        mSocket.off("dbOperation", onOperation);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("timestamp", onTimestamp);
        mSocket.disconnect();

    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  UI METHODS                 /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Methode um die Applikation in den Fullscreen zu versetzen und die Navigationsbar zu verstecken.
     *
     * @return Mittels des inklusiven OR's werden verschiedene Operationen auf dem View getätigt, um
     * den Rückgabewert für den Methodenaufruf von setSystemUiVisibility(); zu verwenden.
     */
    private int hideSystemBars () {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
               | View.SYSTEM_UI_FLAG_FULLSCREEN
               | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
               | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
               | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
               | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    }



    /**
     *
     * @param context
     * @param view
     */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    /**
     *
     * @param view
     */
    public void returnHome (View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(), "HOME_FRAGMENT").addToBackStack("HOME_FRAGMENT").commit();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
    }



    /**
     *
     */
    public void changeConnectedIcon(){
        if (isConnected) {
            connectedImage.setImageResource(R.drawable.ic_cloud_checkmark);
            ImageViewCompat.setImageTintList(connectedImage, ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(), R.color.colorGreenBright)));
        } else {
            connectedImage.setImageResource(R.drawable.ic_cloud_crossed);
            ImageViewCompat.setImageTintList(connectedImage, ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(), R.color.colorGrayMediumLight)));
        }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  HOME FRAGMENT                 //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Die Methode wird aufgerufen, nachdem eines der Regale in der Übersicht angeklickt wurde.
     * Daraufhin wird die Regalansicht aufgerufen und entsprechende Daten des angewählten Regales
     * übermittelt.
     *
     * @param view Das geklickte Regal wird als Parameter der Klasse "View" übergeben.
     */
    public void getClickedRegal (View view) {
        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager()
                .findFragmentByTag("HOME_FRAGMENT");
        if (homeFragment != null && homeFragment.isVisible()) {
            Bundle b = homeFragment.getArguments();
            RegalfrontFragment fragment;
            if (b!= null) {
                fragment = RegalfrontFragment.newInstance(
                        b.<Lagerplatz>getParcelableArrayList(ARG_SHELVESTOMARKRED),
                        view.getContentDescription());
            } else {
                fragment = RegalfrontFragment.newInstance(new ArrayList<Lagerplatz>(),
                        view.getContentDescription());
            }

            if (view.getContentDescription().equals("606200")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ShelffrontTemporaryFragment.newInstance(view.getContentDescription()), "REGALFRONT_TEMP_FRAGMENT")
                        .addToBackStack("REGALFRONT_TEMP_FRAGMENT").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment, "REGALFRONT_FRAGMENT").addToBackStack("REGALFRONT_FRAGMENT").commit();
            }
        }

        FilterFragment filterFragment = (FilterFragment) getSupportFragmentManager()
                .findFragmentByTag("FILTER_FRAGMENT");
        if (filterFragment != null && filterFragment.isVisible()) {
            RegalfrontFragment fragment;
            if (filterFragment.getShelvesToMarkRed() != null) {
                fragment = RegalfrontFragment.newInstance(filterFragment.getShelvesToMarkRed(),
                        view.getContentDescription());
            } else {
                fragment = RegalfrontFragment.newInstance(new ArrayList<Lagerplatz>(),
                        view.getContentDescription());
            }

            if (view.getContentDescription().equals("606200")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        ShelffrontTemporaryFragment.newInstance(view.getContentDescription()), "")
                        .addToBackStack("REGALFRONT_TEMP_FRAGMENT").commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        fragment, "REGALFRONT_FRAGMENT").addToBackStack("REGALFRONT_FRAGMENT").commit();
            }
        }
    }


    /**
     *
     * @return
     */
    public ArrayList<Lagerplatz> getFreeShelves (){
        ArrayList<Lagerplatz> freeShelves = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery(
                "SELECT " + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + ", "
                        + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT
                        + " FROM " + TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME + ""
                        + " LEFT OUTER JOIN " + TableLagerbestand.LagerbestandEntry.TABLE_NAME + ""
                        + " ON " + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ + " = "
                        + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ
                        + " WHERE " + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ + " IS NULL;"
                , null);
        try {
            while (cursor.moveToNext()) {
                int lagerplatz = cursor.getInt(cursor.getColumnIndex(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ));
                int lagerort = cursor.getInt(cursor.getColumnIndex(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERORT));
                Lagerplatz lagerplatzObject = new Lagerplatz(lagerort, lagerplatz);
                freeShelves.add(lagerplatzObject);
            }
        } finally {
            cursor.close();
        }

        return freeShelves;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  SHELF FRONTAL                 //////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Die Methode wird aufgerufen, nachdem eines der Regalfächer in der Übersicht angeklickt wurde.
     * Es wird das entsprechende BottomSheet expandiert.
     *
     * @param view Das geklickte Regalfach wird als Parameter der Klasse "View" übergeben.
     */
    public void getClickedRegalFach (View view) {
        ArrayList<View> bottomSheets = getSlideUpPaneArrayList();
        /////////////////////////

        //RegalfrontFragment.fachID = view.getContentDescription();
        if (view.getContentDescription().toString().equals("1")) {
            hideSlideUpPanes();
            focusShelfCompartment(1);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(0));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("2")) {
            hideSlideUpPanes();
            focusShelfCompartment(2);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(1));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("3")) {
            hideSlideUpPanes();
            focusShelfCompartment(3);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(2));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("4")) {
            hideSlideUpPanes();
            focusShelfCompartment(4);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(3));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("5")) {
            hideSlideUpPanes();
            focusShelfCompartment(5);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(4));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("6")) {
            hideSlideUpPanes();
            focusShelfCompartment(6);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(5));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("7")) {
            hideSlideUpPanes();
            focusShelfCompartment(7);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(6));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    /**
     *
     */
    public void hideSlideUpPanes (){
        for (View v : getSlideUpPaneArrayList()) {
            mBottomSheetBehaviour = BottomSheetBehavior.from(v);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        for (View w : getFocusShelfCompartmentArrayList()) {
            w.setVisibility(View.INVISIBLE);
        }
    }


    /**
     *
     * @return
     */
    public ArrayList<View> getSlideUpPaneArrayList(){
        ArrayList<View> slideUpPanesOfCompartments = new ArrayList<>();
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment01));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment02));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment03));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment04));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment05));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment06));
        slideUpPanesOfCompartments.add(findViewById(R.id.slideUpPane_shelf_compartment07));
        return slideUpPanesOfCompartments;
    }


    /**
     *
     * @return
     */
    public ArrayList<View> getFocusShelfCompartmentArrayList(){
        ArrayList<View> compartmentsToBeFocused = new ArrayList<>();
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06_focused));
        compartmentsToBeFocused.add(findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07_focused));
        return compartmentsToBeFocused;
    }


    /**
     *
     * @param compartmentToFocus
     */
    public void focusShelfCompartment (int compartmentToFocus){
        ImageView compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment07_focused);
        switch (compartmentToFocus){
            case 1:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment01_focused);
                break;
            case 2:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment02_focused);
                break;
            case 3:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment03_focused);
                break;
            case 4:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment04_focused);
                break;
            case 5:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment05_focused);
                break;
            case 6:
                compartmentToBeFocused = findViewById(R.id.fragment_shelf_frontal_shelfonly_imageView_compartment06_focused);
                break;
        }
        if(compartmentToBeFocused != null){
            compartmentToBeFocused.setVisibility(View.VISIBLE);
        }
    }


    /**
     *
     * @param input
     */
    @Override
    public void onShelfFrontInputSent (Lagerplatz input) {
        onSearchInputSent(input);
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  SEARCH                 /////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Daten des Suchformulares werden verarbeitet
     *
     * @param input Daten die übergeben wurden aus den Eingabefeldern des Fragmentes.
     */
    @Override
    public void onSearchInputSent (Lagerplatz input) {
        ArrayList<Lagerplatz> shelvesToMarkRed = new ArrayList<>();
        shelvesToMarkRed.add(input);
        HomeFragment fragment = HomeFragment.newInstance(shelvesToMarkRed);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, "HOME_FRAGMENT")
                .addToBackStack("HOME_FRAGMENT")
                .commit();
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  SOCKET IO                 //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private Emitter.Listener onOperation = new Emitter.Listener() {
        /**
         *
         * @param args
         */
        @Override
        public void call (Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                TriggerEvent triggerEvent = new TriggerEvent(data.getString("type"),
                        data.getString("schema"),
                        data.getString("table"),
                        data.getJSONArray("affectedRows"),
                        data.getJSONArray("affectedColumns"),
                        data.getLong("timestamp"),
                        data.getInt("nextPosition"));
                switch (triggerEvent.getType()) {
                    case "UPDATE":
                        handleUpdateOperation(triggerEvent);
                        updateTimestamp(triggerEvent.getTimestamp());
                        break;
                    case "INSERT":
                        handleInsertOperation(triggerEvent);
                        updateTimestamp(triggerEvent.getTimestamp());
                        break;
                    case "DELETE":
                        handleDeleteOperation(triggerEvent);
                        updateTimestamp(triggerEvent.getTimestamp());
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     *
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        /**
         *
         * @param args
         */
        @Override
        public void call (Object... args) {
            if (!isConnected) {
                isConnected = true;
                changeConnectedIcon();
            }
            Log.d(TAG, "---------------------------------------------SOCKET.IO IS CONNECTED");
        }
    };


    /**
     *
     */
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        /**
         *
         * @param args
         */
        @Override
        public void call (Object... args) {
            isConnected = false;
            changeConnectedIcon();
            Log.d(TAG, "---------------------------------------------SOCKET.IO IS DISCONNECTED");
        }
    };


    /**
     *
     */
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        /**
         *
         * @param args
         */
        @Override
        public void call(Object... args) {
            isConnected = false;
            changeConnectedIcon();
            Log.d(TAG, "---------------------------------------------ERROR CONNECTINGSOCKET.IO");
        }
    };


    /**
     *
     */
    private Emitter.Listener onTimestamp = new Emitter.Listener() {
        /**
         *
         * @param args
         */
        @Override
        public void call (Object... args) {
            long timestampDatabase = 0;
            try (Cursor cursor = mDatabase.rawQuery(
                    "SELECT " + TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP
                    + " FROM " + TableTimestamp.TimestampEntry.TABLE_NAME, null)) {
                while (cursor.moveToNext()) {
                    timestampDatabase = cursor.getLong(
                            cursor.getColumnIndex(TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP));
                }
            }

            String timestampString = (String) args[0].toString();
            StringBuilder str = new StringBuilder(timestampString);
            timestampString = (String) str.subSequence(0, str.length());
            final long timestampServer = Long.parseLong(timestampString);
            timestampServerGlobal = timestampServer;
            if (timestampDatabase != timestampServer) {
                Log.d(TAG, "-----------------------run: ALERT DIALOG SOLLTE ANGEZEIGT WERDEN");
                //BITTE DB UPDATE
                final HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager()
                        .findFragmentByTag("HOME_FRAGMENT");
                    if (homeFragment != null && homeFragment.isVisible()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                homeFragment.showDBUpdate();
                            }
                        });
                    }
            }
            Log.d(TAG, "-------------------------------------OnConnect: TIMESTAMP " + args[0]);
        }
    };


    /**
     *
     * @param triggerEvent
     * @throws JSONException
     */
    public void handleUpdateOperation (TriggerEvent triggerEvent) throws JSONException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        JSONObject afterObject = triggerEvent.getAfterObject();
        JSONObject beforeObject = triggerEvent.getBeforeObject();

        switch (triggerEvent.getTable()) {
            // ARTIKELKOMBINATIONEN
            case TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME:
                for (int i = 0 ; i < triggerEvent.getAffectedColumns().length(); i++) {
                    if (!afterObject.isNull(triggerEvent.getAffectedColumns().getString(i))) {
                        if (triggerEvent.getAffectedColumns().getString(i).equals(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID)
                            || triggerEvent.getAffectedColumns().getString(i).equals(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID)
                            || triggerEvent.getAffectedColumns().getString(i).equals(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID)
                            || triggerEvent.getAffectedColumns().getString(i).equals(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_VARIANTEN_ID)
                        ) {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getInt(triggerEvent.getAffectedColumns().getString(i)));
                        } else {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getString(triggerEvent.getAffectedColumns().getString(i)));
                        }
                    }
                }

                db.update(triggerEvent.getTable(),
                        values,
                        "" + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID      +  " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG + " = '"
                                + beforeObject.getString(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID         + " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID            + " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID) + "'",
                        null);
                break;

            // KPI
            case "kpi":
                break;

            // LAGERBESTAND
            case TableLagerbestand.LagerbestandEntry.TABLE_NAME:
                for (int i=0 ; i < triggerEvent.getAffectedColumns().length(); i++) {
                    if (!afterObject.isNull(triggerEvent.getAffectedColumns().getString(i))) {
                        if (triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand.LagerbestandEntry.COLUMN_FERTIGUNGSZUSTAND)
                           || triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand.LagerbestandEntry.COLUMN_MENGE)
                           || triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand.LagerbestandEntry.COLUMN_MENGENEINHEIT)
                        ) {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getString(triggerEvent.getAffectedColumns().getString(i)));
                        } else {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getInt(triggerEvent.getAffectedColumns().getString(i)));
                        }
                    }
                }

                db.update(triggerEvent.getTable(),
                        values,
                        "" + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER   + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG  + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG) + "'",
                        null);
                break;

            // LAGERBESTAND_SUMME
            case TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME:
                for (int i = 0 ; i < triggerEvent.getAffectedColumns().length(); i++) {
                    if (!afterObject.isNull(triggerEvent.getAffectedColumns().getString(i))) {
                        if (triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FERTIGUNGSZUSTAND)
                            || triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGE)
                            || triggerEvent.getAffectedColumns().getString(i).equals(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_MENGENEINHEIT)
                        ) {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getString(triggerEvent.getAffectedColumns().getString(i)));
                        } else {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getInt(triggerEvent.getAffectedColumns().getString(i)));
                        }
                    }
                }

                db.update(triggerEvent.getTable(),
                        values,
                        "" + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ +  " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID     + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID    + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID       + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID) + "'",
                        null);
                break;

            // LAGERPLAETZE
            case TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME:
                for (int i = 0 ; i < triggerEvent.getAffectedColumns().length(); i++) {
                    if (!afterObject.isNull(triggerEvent.getAffectedColumns().getString(i))) {
                        if (triggerEvent.getAffectedColumns().getString(i).equals(TableLagerplaetze.LagerplaetzeEntry.COLUMN_BESCHREIBUNG)) {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getString(triggerEvent.getAffectedColumns().getString(i)));
                        } else {
                            values.put(triggerEvent.getAffectedColumns().getString(i),
                                    afterObject.getInt(triggerEvent.getAffectedColumns().getString(i)));
                        }
                    }
                }

                db.update(triggerEvent.getTable(),
                        values,
                        "" + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ +  " = '"
                                + beforeObject.getInt(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ) + "'",
                        null);
                break;
        }
    }


    /**
     *
     * @param triggerEvent
     * @throws JSONException
     */
    public void handleInsertOperation(TriggerEvent triggerEvent) throws JSONException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        JSONObject afterObject = triggerEvent.getAfterObject();

        Iterator<String> keys = afterObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            if (!afterObject.isNull(key)) {
                Object aObj = afterObject.get(key);
                if (aObj instanceof Integer) {
                    values.put(key, afterObject.getInt(key));
                } else if (aObj instanceof String) {
                    values.put(key, afterObject.getString(key));
                }
            }
        }

        db.insert(triggerEvent.getTable(), null, values);
    }


    /**
     *
     * @param triggerEvent
     * @throws JSONException
     */
    public void handleDeleteOperation(TriggerEvent triggerEvent) throws JSONException {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        JSONObject beforeObject = triggerEvent.getBeforeObject();

        switch (triggerEvent.getTable()) {
            // ARTIKELKOMBINATIONEN
            case TableArtikelkombination.ArtikelkombinationenEntry.TABLE_NAME:
                db.delete(triggerEvent.getTable(),
                        "" + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID      +  " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_ID)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG + " = '"
                                + beforeObject.getString(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_ARTIKEL_BEZEICHNUNG)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID         + " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_GROESSEN_ID)
                        + "' AND " + TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID            + " = '"
                                + beforeObject.getInt(TableArtikelkombination.ArtikelkombinationenEntry.COLUMN_FARBE_ID) + "'",
                        null);
                break;

            // KPI
            case "kpi":
                break;

            // LAGERBESTAND
            case TableLagerbestand.LagerbestandEntry.TABLE_NAME:
                db.delete(triggerEvent.getTable(),
                        "" + TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ +  " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_LAGERPLATZ)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER   + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKNUMMER)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG  + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_STUECKTEILUNG)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID     + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_ARTIKEL_ID)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID    + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_GROESSEN_ID)
                        + "' AND " + TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID       + " = '"
                                + beforeObject.getInt(TableLagerbestand.LagerbestandEntry.COLUMN_FARBE_ID) + "'",
                        null);
                break;

            // LAGERBESTAND_SUMME
            case TableLagerbestand_Summe.Lagerbestand_SummeEntry.TABLE_NAME:
                db.delete(triggerEvent.getTable(),
                        "" + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ +  " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_LAGERPLATZ)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID     + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_ARTIKEL_ID)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID    + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_GROESSEN_ID)
                        + "' AND " + TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID       + " = '"
                                + beforeObject.getInt(TableLagerbestand_Summe.Lagerbestand_SummeEntry.COLUMN_FARBE_ID) + "'",
                        null);
                break;

            // LAGERPLAETZE
            case TableLagerplaetze.LagerplaetzeEntry.TABLE_NAME:
                db.delete(triggerEvent.getTable(),
                        "" + TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ +  " = '"
                                + beforeObject.getInt(TableLagerplaetze.LagerplaetzeEntry.COLUMN_LAGERPLATZ) + "'",
                        null);
                break;
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    //  DATABASE                  //////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param timestampServer
     */
    private void updateTimestamp (long timestampServer) {
        mDatabase.execSQL("DELETE FROM " + TableTimestamp.TimestampEntry.TABLE_NAME + ";");

        ContentValues contentValues = new ContentValues();
        contentValues.put(TableTimestamp.TimestampEntry.COLUMN_TIMESTAMP, timestampServer);

        mDatabase.insert(TableTimestamp.TimestampEntry.TABLE_NAME, null, contentValues);
        timestampServerGlobal = timestampServer;
    }
}
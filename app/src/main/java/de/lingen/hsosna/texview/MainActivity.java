package de.lingen.hsosna.texview;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import de.lingen.hsosna.texview.database.TableLagerbestand;
import de.lingen.hsosna.texview.database.TableLagerplaetze;
import de.lingen.hsosna.texview.fragments.DatabaseFragment;
import de.lingen.hsosna.texview.fragments.FilterFragment;
import de.lingen.hsosna.texview.fragments.HomeFragment;
import de.lingen.hsosna.texview.fragments.RegalfrontFragment;
import de.lingen.hsosna.texview.fragments.SearchFragment;
import de.lingen.hsosna.texview.layouts.ZoomLinearLayout;

import static de.lingen.hsosna.texview.fragments.RegalfrontFragment.ARG_SHELVESTOMARKRED;

/**
 * Die MainActivity ist die gesamte Zeit der Applikationslaufzeit aktiv, da sie über Fragmente und
 * Fragmentswitcher Ihren Inhalt wechselt.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchFragment.SearchFragmentListener {
    private DrawerLayout drawer;
    private View decorView;
    private BottomSheetBehavior mBottomSheetBehaviour;

    public static ArrayList<Lagerplatz> freeShelveList;
    public ArrayList<CharSequence> freeShelveBoxList;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDatabase;

    public static boolean colorSwitchState;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.fragment_home_lagerort_60);


        dbHelper = new DatabaseHelper(this);
        mDatabase = dbHelper.getReadableDatabase();
        freeShelveList = getFreeShelves();


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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment(), "HOME_FRAGMENT").commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }



    }

    /**
     * Die Methode wird aufgerufen, nachdem eines der Regalfächer in der Übersicht angeklickt wurde.
     * Es wird das entsprechende BottomSheet expandiert.
     *
     * @param view Das geklickte Regalfach wird als Parameter der Klasse "View" übergeben.
     */
    public void getClickedRegalFach (View view) {
        displayToast((String) view.getContentDescription());

        //muss wahrscheinlich aus der methode
        ArrayList<View> bottomSheets = new ArrayList<>();
        bottomSheets.add(findViewById(R.id.slideUpPaneFach01));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach02));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach03));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach04));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach05));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach06));
        bottomSheets.add(findViewById(R.id.slideUpPaneFach07));
        /////////////////////////



        //RegalfrontFragment.fachID = view.getContentDescription();
        if (view.getContentDescription().toString().equals("1")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(0));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("2")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(1));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("3")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(2));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("4")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(3));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("5")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(4));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("6")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(5));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("7")) {
            hideSlideUpPanes(bottomSheets);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheets.get(6));
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void hideSlideUpPanes (ArrayList<View> bottomSheets){
        for (View v : bottomSheets) {
            mBottomSheetBehaviour = BottomSheetBehavior.from(v);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HIDDEN);
        }

    }

    /**
     * Die Methode wird aufgerufen, nachdem eines der Regale in der Übersicht angeklickt wurde.
     * Daraufhin wird die Regalansicht aufgerufen und entsprechende Daten des angewählten Regales
     * übermittelt.
     *
     * @param view Das geklickte Regal wird als Parameter der Klasse "View" übergeben.
     */
    public void getClickedRegal (View view) {
        displayToast((String) view.getContentDescription());
        // neues fragment mit values


        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if(homeFragment != null && homeFragment.isVisible()) {
            Bundle b = homeFragment.getArguments();

            RegalfrontFragment fragment;
            if(b!= null) {
                fragment = RegalfrontFragment.newInstance(b.<Lagerplatz>getParcelableArrayList(ARG_SHELVESTOMARKRED), view.getContentDescription());
            }
            else {
                fragment = RegalfrontFragment.newInstance(new ArrayList<Lagerplatz>(), view.getContentDescription());
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment, "REGALFRONT_FRAGMENT").commit();
        }
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
                        new HomeFragment(), "HOME_FRAGMENT").commit();
                break;
            case R.id.nav_filter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FilterFragment(), "FILTER_FRAGMENT").commit();
                break;
            case R.id.nav_dbcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DatabaseFragment(), "DB_FRAGMENT").commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment(), "SEARCH_FRAGMENT").commit();
                break;
            case R.id.nav_dbcon2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DatabaseFragment(), "DB_FRAGMENT").commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);

        MenuItem itemSwitch = menu.findItem(R.id.switchColor);
        itemSwitch.setActionView(R.layout.switch_color);
        Switch sw = (Switch) menu.findItem(R.id.switchColor).getActionView().findViewById(R.id.switchColorAction);


        //color switch changes
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    colorSwitchState = true;
                    //HOMEFRAGMENT
                    HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
                    if(homeFragment != null){
                        homeFragment.markFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //REGALFRONTFRAGMENT
                    RegalfrontFragment regalfrontFragment = (RegalfrontFragment)getSupportFragmentManager().findFragmentByTag("REGALFRONT_FRAGMENT");
                    if(regalfrontFragment != null){
                        regalfrontFragment.markFreeShelves(findViewById(android.R.id.content).getRootView());
                    }
                    //markFreeShelves();
                    displayToastLong("checked movement");
                }
                else{
                    colorSwitchState = false;
                    //HOMEFRAGMENT
                    HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
                    if(homeFragment != null) {
                        homeFragment.unmarkFreeShelves(
                                findViewById(android.R.id.content).getRootView());
                    }
                    //REGALFRONTFRAGMENT
                    RegalfrontFragment regalfrontFragment = (RegalfrontFragment)getSupportFragmentManager().findFragmentByTag("REGALFRONT_FRAGMENT");
                    if(regalfrontFragment != null){
                        regalfrontFragment.unmarkFreeShelves(findViewById(android.R.id.content).getRootView());
                    }

                    displayToastLong("unchecked movmement");
                    //unmarkFreeShelves();
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchButton:
                Toast.makeText(this, "Suche ausgewählt", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment(), "SEARCH_FRAGMENT").commit();
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_search);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Eine Nachricht wird als Toast dargestellt.
     *
     * @param message Nachricht die dargestellt werden soll
     */
    public void displayToast (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void displayToastLong (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

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
        //fragment wird gesetzt
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "HOME_FRAGMENT")//_AFTER_SEARCH")
                .commit();
    }

    public void returnHome (View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment(), "HOME_FRAGMENT").commit();

    }

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

}
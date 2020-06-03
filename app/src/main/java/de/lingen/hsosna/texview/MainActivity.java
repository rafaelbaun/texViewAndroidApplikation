package de.lingen.hsosna.texview;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Die MainActivity ist die gesamte Zeit der Applikationslaufzeit aktiv, da sie über Fragmente und
 * Fragmentswitcher Ihren Inhalt wechselt.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchFragment.SearchFragmentListener {
    private DrawerLayout drawer;
    private SearchFragment searchFragment;
    private View decorView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private TextView mTextViewState;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    new HomeFragment()).commit();
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
        RegalfrontFragment.fachID = view.getContentDescription();
        if (view.getContentDescription().toString().equals("Regal Fach 01")) {
            View bottomSheet = findViewById(R.id.slideUpPaneFach01);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 02")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach02);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 03")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach03);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 04")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach04);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 05")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach05);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 06")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach06);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else if (view.getContentDescription().toString().equals("Regal Fach 07")) {
            View bottomSheet2 = findViewById(R.id.slideUpPaneFach07);
            mBottomSheetBehaviour = BottomSheetBehavior.from(bottomSheet2);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
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
        RegalfrontFragment fragment = RegalfrontFragment.newInstance(view.getContentDescription(),
                "00");
        //fragment wird gesetzt
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .commit();
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
                        new HomeFragment()).commit();
                break;
            case R.id.nav_filter:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FilterFragment()).commit();
                break;
            case R.id.nav_dbcon:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DatabaseFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
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
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchButton:
                Toast.makeText(this, "Suche ausgewählt", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
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
    public void onSearchInputSent (CharSequence input) {
        displayToastLong(input.toString());
    }
}

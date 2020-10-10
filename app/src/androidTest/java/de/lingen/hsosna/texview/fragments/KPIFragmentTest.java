package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.view.Gravity;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Für das KPI-Fragment werden Tests durchgeführt.
 */
public class KPIFragmentTest {

    private MainActivity mainActivity = null;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);



    /**
     * Es wird ein Intent initialisiert, um den Test vorzubereiten.
     */
    @Before
    public void setUp() throws Exception {
        Intents.init();
    }



    /**
     * Überprüft, ob der richtige Context der App ausgewählt worden ist
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Das KPI-Fragment wird über das Menü aufgerufen
     */
    @Test
    public void open_kpi () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(500);
        onView(withId(R.id.nav_kpi))
                .perform(click());
        Thread.sleep(500);
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
        Intents.release();
    }
}
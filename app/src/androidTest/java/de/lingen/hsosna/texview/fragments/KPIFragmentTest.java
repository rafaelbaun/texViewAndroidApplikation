package de.lingen.hsosna.texview.fragments;

import android.content.Context;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.*;

public class KPIFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mainActivity = null;


    /**
     * Es wird ein neues KPIFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new KPIFragment()).commit();
    }



    /**
     * Überprüft, ob der richtige Context der App ausgewählt worden ist
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Das KPIFragment wird aufgerufen.
     */
    @Test
    public void open_settings () throws Exception {
        //onView(withContentDescription(R.string.app_description)).perform(DrawerActions.open());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(allOf(withId(R.id.nav_kpi))).perform(DrawerActions.open());
        //assertTrue(mActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof KPIFragment);
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}
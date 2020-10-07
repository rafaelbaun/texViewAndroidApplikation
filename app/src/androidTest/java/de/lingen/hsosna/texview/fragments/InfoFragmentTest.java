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
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class InfoFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity = null;


    /**
     * Es wird ein neues InfoFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new InfoFragment()).commit();
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
     * Das InfoFragment wird aufgerufen.
     */
    @Test
    public void open_settings () throws Exception {
        onView(withContentDescription(R.string.app_description))
                .perform(DrawerActions.open());
        onView(allOf(withId(R.id.nav_kpi)))
                .perform(click())
                .check(matches(not(isDisplayed())));
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

}
package de.lingen.hsosna.texview.fragments;

import android.content.Context;

import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

public class ShelffrontTemporaryFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public MainActivity mainActivity = null;
    //Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SearchFragment.class.getName(), null, false);


    /**
     * Es wird ein neues ShelffrontTemporaryFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ShelffrontTemporaryFragment()).commit();
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
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}
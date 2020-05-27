package de.lingen.hsosna.texview;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void init(){
        activityRule.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SearchFragment()).commit();
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }

    @Test
    public void user_can_enter_first_name(){
        onView(withId(R.id.editText)).perform(typeText("Daniel"));
    }

    @Test
    public void auswaehlenRegalUndFach() {
        onView(withId(R.id.regal_0101)).perform(click());
        onView(withId(R.id.fach01)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


        //    @Test
//    public void user_can_enter_a_number(){
//        onView(withId(R.id.editText2)).perform(typeText("5"));
//    }
}

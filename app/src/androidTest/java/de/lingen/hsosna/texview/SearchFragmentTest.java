package de.lingen.hsosna.texview;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.editText)).perform(typeText("Daniel"));
    }

    @Test
    public void user_can_enter_a_number(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.editText2)).perform(typeText("5"));
    }

    @Test
    public void sent_data_is_displayed_as_toast(){
        user_can_enter_first_name();
        user_can_enter_a_number();
        onView(withId(R.id.submitButton)).perform(click());
        onView(withText(R.string.testFall)).inRoot(new ToastMatcher()).check(matches(withText("Daniel")));
    }

    @Test
    public void fill_search_form(){
        onView(withId(R.id.editTextArtikelNr)).perform(replaceText("79987")); //Artikel Nr
        onView(withId(R.id.editTextArtikelkurzbez)).perform(replaceText("Sheldon")); //Kurzbez
        onView(withId(R.id.editTextFarbID)).perform(replaceText("100935")); //FarbID
        onView(withId(R.id.editTextFarbbezeichnung)).perform(replaceText("Punkte, 2,5cm, erika, rose")); //Farbbez
        onView(withId(R.id.editTextGroesse)).perform(replaceText("140")); //Groeße
        onView(withId(R.id.editTextFertigungszustand)).perform(replaceText("FW")); //Fertzustand
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

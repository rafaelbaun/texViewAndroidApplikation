package de.lingen.hsosna.texview;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lingen.hsosna.texview.fragments.SearchFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTestALT {

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
    public void testFillSearchForm(){
        onView(withId(R.id.searchFragment_editText_articleId)).perform(replaceText("79987")); //Artikel Nr
        onView(withId(R.id.searchFragment_editText_articleShortDesc)).perform(replaceText("Sheldon")); //Kurzbez
        onView(withId(R.id.searchFragment_editText_colorId)).perform(replaceText("100935")); //FarbID
        onView(withId(R.id.searchFragment_editText_colorDescription)).perform(replaceText("Punkte, 2,5cm, erika, rose")); //Farbbez
        onView(withId(R.id.searchFragment_editText_size)).perform(replaceText("140")); //Groeße
        onView(withId(R.id.searchFragment_editText_manufacturingState)).perform(replaceText("FW")); //Fertzustand
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //Leere Suchformulare
    @Test
    public void testEmptySearchForm(){
        onView(withId(R.id.searchButton)).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Additionen werden ignoriert bei ArtikelNr
    @Test
    public void testSearchArticleIdAddition(){
        onView(withId(R.id.searchFragment_editText_articleId)).perform(replaceText("12+25"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // negative ArtikelNr wird ignoriert
    @Test
    public void testSearchArticleIdNegative(){
        onView(withId(R.id.searchFragment_editText_articleId)).perform(replaceText(String.valueOf(-61000)));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // ArtikelNr & Artikelbezeichnung
    @Test
    public void testSearchArticleConny(){
        onView(withId(R.id.searchFragment_editText_articleId)).perform(replaceText(String.valueOf(61000)));
        onView(withId(R.id.searchFragment_editText_articleShortDesc)).perform(replaceText("Conny"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Artikelbezeichnung mit whitespaces
    @Test
    public void testSearchArticleIdWhitespaces(){
        onView(withId(R.id.searchFragment_editText_articleShortDesc)).perform(replaceText("   Conny   "));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // FarbId & Farbbezeichnung
    @Test
    public void testSearchColorIdAndColordescription(){
        onView(withId(R.id.searchFragment_editText_colorId)).perform(replaceText(String.valueOf(173)));
        onView(withId(R.id.searchFragment_editText_colorDescription)).perform(replaceText("beige"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //FarbId mit führenden Stellen
    @Test
    public void testSearchColorIdWithUnfinishedNumber(){
        onView(withId(R.id.searchFragment_editText_colorId)).perform(replaceText(String.valueOf(17)));
        onView(withId(R.id.searchFragment_editText_colorDescription)).perform(replaceText("beige"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Größe & Fertigungszustand
    @Test
    public void testSearchSizeAndManufacturingstatus(){
        onView(withId(R.id.searchFragment_editText_size)).perform(replaceText(String.valueOf(155)));
        onView(withId(R.id.searchFragment_editText_manufacturingState)).perform(replaceText("FW"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Fertigungszustand sind nur Buchstaben erlaubt. Zahlen werden ignoriert
    @Test
    public void testSearchManufacturingstatusIsANumber(){
        onView(withId(R.id.searchButton)).perform(click());
        onView(withId(R.id.searchFragment_editText_size)).perform(replaceText(String.valueOf(155)));
        onView(withId(R.id.searchFragment_editText_manufacturingState)).perform(replaceText("0"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.searchFragment_button_submit)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;

import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.lingen.hsosna.texview.DatabaseHelper;
import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.pressMenuKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    public MainActivity mainActivity = null;
    //Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SearchFragment.class.getName(), null, false);


    //@Rule
    //public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    /*
    @Rule
    public IdlingResource idlingResource = new IdlingResource() {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isIdleNow() {
            return false;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {

        }
    };
    */


    /**
     * Es wird ein neues SearchFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SearchFragment()).commit();
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
     * Die Suche wid über das Such-Icon in der Toolbar aufgerufen
     */
    @Test
    public void open_search_toolbar () throws Exception{
        onView(withId(R.id.searchButton))
                .perform(click())
                .check(matches(isDisplayed()));

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Die Suche wird über das Menü aufgerufen
     */
    @Test
    public void open_search_menu () throws Exception {
        onView(withId(R.id.mainActivity))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_search))
                .perform(click())
                .check(matches(not(isDisplayed())));

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Ein Text wird in  die Artikel-Kurzbeschreibung eingegeben. Im Anschluss wird wiederum in
     * das Textfeld hineingegangen und der Inhalt gelöscht.
     */
    @Test
    public void edit_articleDesc_clearText () throws Exception {
        Thread.sleep(500);

        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("Conny"), closeSoftKeyboard(), scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(clearText(), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(1000);
    }


    /**
     * Das gesamte Suchformular wird examplarisch mit Werten gefüllt.
     */
    @Test
    public void fill_complete_searchForm () throws Exception {
        Thread.sleep(1000);
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(1000);

        // Artikel Nr.
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Artikel Kurzbez.
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Stücknummer
        onView(withId(R.id.searchFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Stückteilung
        onView(withId(R.id.searchFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Farb ID
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Farbbezeichnung
        onView(withId(R.id.searchFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Größe
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("51,3"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // Fertzstd
        onView(withId(R.id.searchFragment_editText_manufacturingState))
                .perform(replaceText("FW"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // button submit
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));
        Thread.sleep(1000);
    }


    /**
     * Die Suche wird aufgerufen. Das Formular wird nicht mit Werten gefüllt.
     * Es wird lediglich der Such-Button zur Ausführung der Suche gedrückt.
     * Im Anschluss wird der AlertDialog durch Klicken auf "OK" bestätigt
     */
    @Test
    public void emptySearchForm_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());

        // AlertDialog bestätigen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }

    }


    /**
     * Das Textfeld "Artikel-Id" wird mit einer Rechenoperation gefüllt.
     * Im Anschluss wird der AlertDialog durch Klicken auf "OK" bestätigt.
     */
    @Test
    public void edit_articleId_addition_should_alert () throws Exception {
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText("12 + 15"), pressImeActionButton());
        Thread.sleep(500);

        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Bei dem Textfeld "Artikel-Id" wird eine negative Zahl eingetragen.
     * Es erscheint ein AlertDialog, welcher durch die Zurück-Taste bestätigt wird.
     */
    @Test
    public void edit_articleId_isNegative_should_alert () throws Exception {
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(-61000)))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(500);

        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            mDevice.pressBack();
        }
    }


    /**
     * Es wird eine Eingabe der Artikel-Kurzbeschreibung vorgenommen. Anstatt der korrekten
     * Kurzbeschreibung wird nicht auf die Groß- bzw. Kleinschreibung geachtet ("coNny")
     */
    @Test
    public void edit_articleDesc_coNny () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("coNny"))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
    }


    /**
     * Es wird bei der Suche eine Artikel-Kurzbeschreibung eingegeben, welche auch Leerzeichen
     * enthält.
     */
    @Test
    public void edit_articleDesc_with_whitespaces () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("   Conny   "), closeSoftKeyboard() , scrollTo())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                        .perform(click());
    }


    /**
     * Bei der Suche wird im Textfeld Farbbezeichnung ", beige" eingetragen.
     */
    @Test
    public void edit_colorDesc_beige () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_colorDescription))
                .perform(replaceText(", beige"))
                .perform(pressImeActionButton());
        Thread.sleep(500);
    }


    /**
     * Bei der Suche wird bei dem Textfeld Farb-ID 179 eingegeben. Diese Farb-ID ist nicht
     * vollständig. Dementsprechend werden alle Artikel angezeigt, deren Farb-ID mit 179 beginnt.
     */
    @Test
    public void edit_colorId_withUnfinishedNumber () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)))
                .perform(pressImeActionButton());
    }


    /**
     * Bei der Eingabe der Farb-Id wird anstatt einer Zahl ein Text eingegeben. Es erscheit ein
     * AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_colorId_isText_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        // AlertDialog bestätigen
        Thread.sleep(500);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Das Textfeld "Größe" wird anstatt einer Zahl mit einem Text gefüllt. Es erscheint ein
     * AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_size_isText_should_alert () throws Exception {
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        // AlertDialog
        Thread.sleep(500);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Es wird nach dem Fertigungszustand gesucht. Hierbei wird anstatt eines Textes eine Zahl
     * eingegeben.vEs erscheint ein AlertDialog, welcher durch die Zurück-Taste bestätigt wird.
     */
    @Test
    public void edit_manufacturingState_isNumber_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        onView(withId(R.id.searchFragment_editText_manufacturingState))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        // AlertDialog bestätigen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            mDevice.pressBack();
        }
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.mainActivity = null;
    }

}
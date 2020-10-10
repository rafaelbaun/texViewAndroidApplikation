package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
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

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * In dem SearchFragment werden UI-Tests durchgeführt.
 */
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    public MainActivity mainActivity = null;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);



    /**
     * Es wird ein Intent initialisiert, um den Test vorzubereiten.
     */
    @Before
    public void setUp () throws Exception {
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
     * Die Suche wid über das Such-Icon in der Toolbar aufgerufen.
     */
    @Test
    public void open_search_toolbar () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(1000);
    }


    /**
     * Die Suche wird über das Menü aufgerufen
     */
    @Test
    public void open_search_menu () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Ein Text wird in die Artikel-Kurzbeschreibung eingegeben. Im Anschluss wird wiederum in das
     * Textfeld hineingegangen und der Inhalt gelöscht.
     */
    @Test
    public void edit_articleDesc_clearText () throws Exception {
        Thread.sleep(500);

        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("Conny"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(clearText())
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(1000);
    }


    /**
     * Die Suche wird über das Menü aufgerufen. Das gesamte Suchformular wird exemplarisch mit
     * Werten gefüllt und die Suche wird gestartet.
     */
    @Test
    public void fill_complete_searchForm () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);

        // Artikel Nr.
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Artikel Kurzbez.
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Stücknummer
        onView(withId(R.id.searchFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Stückteilung
        onView(withId(R.id.searchFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Farb ID
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Farbbezeichnung
        onView(withId(R.id.searchFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Größe
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("51,3"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Fertzstd
        onView(withId(R.id.searchFragment_editText_manufacturingState))
                .perform(replaceText("FW"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(1000);

        // button submit
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Die Suche wird über das Menü aufgerufen. Alle Input-Textfelder des Suchformulares werden
     * exemplarisch mit Werten gefüllt. Es erfolgt jedoch keine Suche, sondern es wird auf den
     * Hauptbildschirm durch Klicken auf die Titelfläche gewechselt.
     */
    @Test
    public void fill_searchForm_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);

        // Artikel Nr.
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Artikel Kurzbez.
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Stücknummer
        onView(withId(R.id.searchFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Stückteilung
        onView(withId(R.id.searchFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Farb ID
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Farbbezeichnung
        onView(withId(R.id.searchFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Größe
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("51,3"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);

        // Fertzstd
        onView(withId(R.id.searchFragment_editText_manufacturingState))
                .perform(replaceText("FW"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(1000);

        // show home
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Das Suchformular wird nicht mit Werten gefüllt. Es wird lediglich auf den Button "Suche" zur
     * Ausführung gedrückt. Die Suche schlägt fehl. Im Anschluss wird der AlertDialog durch Klicken
     * auf "OK" bestätigt.
     */
    @Test
    public void emptySearchForm_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);

        // AlertDialog bestätigen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Die Suche wird über das Menü aufgerufen. Das Textfeld "Artikel-Id" wird mit einer
     * Rechenoperation gefüllt. Die Suche schlägt fehl. Im Anschluss wird der AlertDialog durch
     * Klicken auf "OK" bestätigt.
     */
    @Test
    public void edit_articleId_addition_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText("12 + 15"))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);

        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Bei dem Textfeld "Artikel-Id" wird anstatt einer positven Zahl eine negative Zahl eingetragen.
     * Die Suche schlägt fehl. Es erscheint ein AlertDialog, welcher durch die Zurück-Taste
     * bestätigt wird.
     */
    @Test
    public void edit_articleId_isNegative_should_alert () throws Exception {
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(-61000)))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(100);

        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            mDevice.pressBack();
        }
    }


    /**
     * Es wird eine Eingabe der Artikel-Kurzbeschreibung vorgenommen. Anstatt der korrekten
     * Kurzbeschreibung wird nicht auf die Groß- bzw. Kleinschreibung geachtet ("coNny"). Die Suche
     * wird über die Enter-Taste (Suche) gestartet
     */
    @Test
    public void edit_articleDesc_coNny () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("coNny"))
                .perform(pressImeActionButton());
        Thread.sleep(1000);
    }


    /**
     * Es wird bei der Suche eine Artikel-Kurzbeschreibung eingegeben, welche auch Leerzeichen
     * enthält ("   Conny   ").
     */
    @Test
    public void edit_articleDesc_with_whitespaces () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_articleShortDesc))
                .perform(replaceText("   Conny   "))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird nach der Farbbezeichnung ", beige" gesucht. Die Bestätigung erfolgt durch die
     * Enter-Taste (Suche).
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
        Thread.sleep(100);
    }


    /**
     * Bei der Suche wird bei dem Textfeld Farb-ID der Wert 179 eingegeben. Diese Farb-ID ist nicht
     * vollständig. Die Bestätigung erfolgt durch die Enter-Taste. Es werden alle Artikel angezeigt,
     * deren Farb-ID mit 179 beginnt.
     */
    @Test
    public void edit_colorId_withUnfinishedNumber () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)))
                .perform(pressImeActionButton());
        Thread.sleep(1000);
    }


    /**
     * Die Suche wird über das Menü aufgerufen. Bei der Eingabe der Farb-Id wird anstatt einer Zahl
     * ein Text eingegeben. Die Suche schlägt fehl. Es erscheit ein AlertDialog, welcher durch
     * Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_colorId_isText_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_search))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());

        // AlertDialog bestätigen
        Thread.sleep(1000);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Das Textfeld "Größe" wird anstatt einer Zahl mit einem Text gefüllt. Die Bestätigung erfolgt
     * über die Enter-Taste. Die Suche schlägt fehl. Es erscheint ein AlertDialog, welcher durch
     * Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_size_isText_should_alert () throws Exception {
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(pressKey(KeyEvent.KEYCODE_SEARCH));

        // AlertDialog bestätigen
        Thread.sleep(1000);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Es wird nach dem Fertigungszustand gefilter. Hierbei wird eine Zahl eingegeben. Die Suche
     * schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_manufacturingState_isNumber_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.searchButton))
                .perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.searchFragment_editText_manufacturingState))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);

        // AlertDialog bestätigen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.mainActivity = null;
        Intents.release();
    }

}
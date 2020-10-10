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
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

/**
 * Im FilterFragment werden UI-Tests durchgeführt
 */
@RunWith(AndroidJUnit4.class)
public class FilterFragmentTest {

    public MainActivity mainActivity = null;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);



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
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Der Filter wird über das Menü aufgerufen.
     */
    @Test
    public void open_filter () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Alle Input-Textfelder des Filterformulares werden exemplarisch mit Werten gefüllt und das
     * Filtern wird gestartet.
     */
    @Test
    public void fill_complete_filterForm () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        // ArtikelNr
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)))
                .perform(scrollTo());

        // Artikel Kurzbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"))
                .perform(scrollTo());

        // Stücknummer
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)))
                .perform(scrollTo());

        // Stückteilung
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)))
                .perform(scrollTo());

        // Farb ID
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)))
                .perform(scrollTo());

        // Farbbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"))
                .perform(scrollTo());

        // Größe
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("51,3"))
                .perform(scrollTo());

        // Fert. Zustand
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_manufacturingState))
                .perform(replaceText("FW"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        // button filter
        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());

        Thread.sleep(1000);
    }


    /**
     * Alle Input-Textfelder des Filterformulares werden exemplarisch mit Werten gefüllt. Es erfolgt
     * jedoch keine Filterung, sondern es wird auf den Hauptbildschirm durch Klicken auf die
     * Titelfläche gewechselt.
     */
    @Test
    public void fill_filterForm_show_home () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        // ArtikelNr
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)))
                .perform(scrollTo());

        // Artikel Kurzbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"))
                .perform(scrollTo());

        // Stücknummer
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)))
                .perform(scrollTo());

        // Stückteilung
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)))
                .perform(scrollTo());

        // Farb ID
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)))
                .perform(scrollTo());

        // Farbbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"))
                .perform(scrollTo());

        // Größe
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("51,3"))
                .perform(scrollTo());

        // Fert. Zustand
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_manufacturingState))
                .perform(replaceText("FW"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        // show home
        Thread.sleep(2000);
        onView(withId(R.id.textView))
                .perform(click());
        Thread.sleep(2000);
    }


    /**
     * Das Filterformular wird nicht mit Werten gefüllt. Es wird lediglich auf den Button "Filtern"
     * zur Ausführung gedrückt. Die Filterung schlägt fehl. Im Anschluss wird der AlertDialog durch
     * Klicken auf "OK" bestätigt.
     */
    @Test
    public void empty_filterForm_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        Thread.sleep(2000);

        // AlertDialog bestätigen
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Das Textfeld "Artikel-Id" wird mit einer Rechenoperation gefüllt. Die Filterung schlägt fehl.
     * Im Anschluss wird der AlertDialog durch Klicken auf "OK" bestätigt.
     */
    @Test
    public void edit_articleId_addition_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText("12 + 15"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_button_submit))
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
     * Bei dem Textfeld "Artikel-Id" wird anstatt einer positiven eine negative Zahl eingetragen.
     * Die Filterung schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK"
     * bestätigt wird.
     */
    @Test
    public void edit_articleId_isNegative_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText(String.valueOf(-61000)));

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
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
     * Bei der Eingabe der Artikel-Kurzbeschreibung wird nicht explizit auf die Klein- bzw.
     * Großschreibung geachtet ("coNny"). Das Filtern wird über die Enter-Taste gestartet.
     */
    @Test
    public void edit_articleDesc_coNny () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("coNny"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Thread.sleep(500);
    }


    /**
     * Ein Text wird in  die Artikel-Kurzbeschreibung eingegeben. Im Anschluss wird wiederum in
     * das Textfeld hineingegangen und der Inhalt gelöscht.
     */
    @Test
    public void edit_articleDesc_clearText () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("Conny"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(clearText())
                .perform(closeSoftKeyboard())
                .perform(scrollTo());
        Thread.sleep(1000);
    }


    /**
     * Es wird beim Filtern eine Artikel-Kurzbeschreibung eingegeben, welche auch Leerzeichen
     * enthält.
     */
    @Test
    public void edit_articleDesc_with_whitespaces () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("   Conny   "))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Es wird nach der Farbbezeichnung ", beige" gefiltert.
     */
    @Test
    public void edit_colorDesc_beige () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText(", beige"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo());

        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        Thread.sleep(1000);
    }


    /**
     * Beim Filtern wird bei dem Textfeld Farb-ID der Wert 179 eingegeben. Diese Farb-ID ist nicht
     * vollständig. Die Bestätigung erfolgt durch die Enter-Taste. Es werden alle Artikel angezeigt,
     * deren Farb-ID mit 179 beginnt.
     */
    @Test
    public void edit_colorId_withUnfinishedNumber () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Thread.sleep(500);
    }


    /**
     * Bei der Eingabe der Farb-Id wird anstatt einer Zahl ein Text eingegeben. Die Filterung
     * schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_colorId_isText_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard());

        Thread.sleep(1000);
        onView(withId(R.id.filterFragment_button_submit))
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
     * über die Enter-Taste. Die Filterung schlägt fehl. Es erscheint ein AlertDialog, welcher durch
     * Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_size_isText_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));

        // AlertDialog bestätigen
        Thread.sleep(1000);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Es wird nach dem Fertigungszustand gefiltert. Hierbei wird eine Zahl eingegeben. Das Filtern
     * schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_manufacturingState_isNumber_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open())
                .check(matches(isOpen()));
        Thread.sleep(1000);
        onView(withId(R.id.nav_filter))
                .perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.filterFragment_editText_manufacturingState))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard());

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
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
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
        Intents.release();
    }
}
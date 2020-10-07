package de.lingen.hsosna.texview.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.pressMenuKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FilterFragmentTest {


    public MainActivity mainActivity = null;
    //Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SearchFragment.class.getName(), null, false);

    /* https://github.com/android/testing-samples/blob/master/ui/uiautomator/BasicSample/app/src/androidTest/java/com/example/android/testing/uiautomator/BasicSample/ChangeTextBehaviorTest.java */
    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /*
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);
    */

    /**
     * Es wird ein neues FilterFragment erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FilterFragment()).commit();
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

    /*
    @Before
    public void startMainActivityFromHomeScreen () {
        mDevice = UiDevice.getInstance(getInstrumentation());
        mDevice.pressHome();
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000);

        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage("de.lingen.hsosna.texview.FilterFragment");
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        mDevice.wait(Until.hasObject(By.pkg(
                "de.lingen.hsosna.texview").depth(0)), 5000);
    }
    */



    @Test
    public void filterHeader_swipeDown_swipeUp () throws Exception {
        Thread.sleep(500);
        onView(allOf(withId(R.id.nav_filter)))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        onView(allOf(withId(R.id.filterFragment_filterHeader), withParent(withId(R.id.filterFragment_searchSlideUpPane))))
                .perform(swipeDown())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(allOf(withId(R.id.filterFragment_filterHeader), withParent(withId(R.id.filterFragment_searchSlideUpPane))))
                .perform()
                /*https://androidresearch.wordpress.com/2015/04/04/an-introduction-to-espresso/*/
                .check(matches(isDisplayed()));
        Thread.sleep(500);
    }

    /**
     * onCreateView
     * Checkt, ob der Alert Dialog ausgeführt wird
     */
    // TODO alert Dialog
    @Test
    public void alertDialogAfterQuery () throws Exception {
        onView(withText("Filteranfrage fehlgeschlagen"))
                .inRoot(isDialog())
                .check((ViewAssertion) isDialog())
                .check(matches(withText("Filteranfrage fehlgeschlagen")))
                .check((ViewAssertion) isDisplayed());
        onView(withText("Bitte füllen Sie mindestents ein Feld aus!"))
                .inRoot(isDialog())
                .check((ViewAssertion) isDialog())
                .check(matches(withText("Bitte füllen Sie mindestens ein Feld aus!")))
                .check((ViewAssertion) isDisplayed());
        onView(withText("OK"))
                .inRoot(isDialog())
                .check((ViewAssertion) isDialog())
                .check(matches(withText("OK")))
                .check((ViewAssertion) isDisplayed())
                .perform(click());

        // AlertDialog bestätigen
        Thread.sleep(TimeUnit.SECONDS.toMillis(1000));
        UiDevice device = UiDevice.getInstance(getInstrumentation());
        UiObject object = device.findObject(new UiSelector().textContains("OK").clickable(true));
        object.click();
    }



    @Test
    public void open_filter () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter))
                .perform(click())
                .check(matches(not(isDisplayed())));
    }


    /**
     * Das Filterformular wird mit Werten gefüllt
     */
    @Test
    public void fill_complete_filterForm () throws Exception {
        Thread.sleep(500);
        //onView(withId(R.layout.activity_main)).perform(swipeRight().perform(new UiController() {});

        Thread.sleep(500);
        //onView(allOf(withResourceName(String.valueOf(R.layout.activity_main)), isRoot()));
        onView(allOf(withId(R.id.nav_view))).perform(click()).check(matches(isDisplayed()));
        //onView(withId(R.layout.activity_main)).perform(swipeRight());
        Thread.sleep(500);
        onView(withId(R.id.nav_filter)).perform(click());

        // ArtikelNr
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText(String.valueOf(79992)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Artikel Kurzbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("Marvin"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Stücknummer
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceId))
                .perform(replaceText(String.valueOf(34345118)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Stückteilung
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_pieceDivision))
                .perform(replaceText(String.valueOf(0)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Farb ID
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorId))
                .perform(replaceText(String.valueOf(1842)), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Farbbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorDescription))
                .perform(replaceText("meliert, türkis"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Größe
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("51,3"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // Fert. Zustand
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_manufacturingState))
                .perform(replaceText("FW"), closeSoftKeyboard(), scrollTo())
                .check(matches(isDisplayed()));

        // button filter
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));

        Thread.sleep(500);
    }


    /**
     * Das Filterformular wird nicht ausgefüllt und es wird auf den Button "Filtern" geglickt.
     */
    @Test
    public void empty_filterForm_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
                //.check(matches(isDisplayed()));
        //assertThat("OK", );
        // AlertDialog
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
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleId))
                .perform(replaceText("12 + 15"))
                .perform(closeSoftKeyboard());
        //.check(matches(isDisplayed()));
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        //.check(matches(isCompletelyDisplayed()));
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
     * Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_articleId_isNegative_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.searchFragment_editText_articleId))
                .perform(replaceText(String.valueOf(-61000)))
                .perform(closeSoftKeyboard());
        //.check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.searchFragment_button_submit))
                .perform(click());
        //.check(matches(isDisplayed()));
        Thread.sleep(500);
        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            mDevice.pressBack();
        }
        //onView(withText("OK")).inRoot(isDialog()).check(matches(isDisplayed())).perform(pressBack());

    }


    /**
     * SearchFragment: 338
     * Artikel Kurzbez. "coNny"
     * Bei der Eingabe der Artikel-Kurzbeschreibung wird nicht explizit auf die Klein- bzw.
     * Großschreibung geachtet.
     * Es werden trotzdem Artikel gefunden, da durch die match()-Methode
     * die Klein- und Großschreibung ignoriert wird.
     *
     */
    @Test
    public void edit_articleDesc_coNny () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("coNny"))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));
    }


    /**
     * Artikelbezeichnung mit whitespaces
     * Sollte funktionieren, weil die Zeichen in der Kurzbeschreibung enthalten sind
     * und die Leerzeichen eleminiert werden --> .trim()
     * SearchFragment:341
     * Es wird bei der Suche eine Artikel-Kurzbeschreibung eingegeben, welche auch Leerzeichen enthält.
     *
     */
    @Test
    public void edit_articleDesc_with_whitespaces () throws Exception {
        Thread.sleep(500);
        onView(allOf(withId(R.layout.fragment_home_lagerort_60))).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());

        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        // Artikelkurzbez.
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText("   Conny   "), closeSoftKeyboard() , scrollTo())
                .check(matches(isDisplayed()));
        // button submit
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));
    }


    /**
     * Bei der Suche wird im Textfeld Farbbezeichnung ", beige" eingetragen.
     * @throws Exception
     */
    @Test
    public void edit_colorDesc_beige () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_articleShortDesc))
                .perform(replaceText(", beige"))
                .perform(closeSoftKeyboard())
                .perform(scrollTo())
                .check(matches(isDisplayed()));
        // buttonSubmit
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));
        Thread.sleep(500);
    }


    /**
     * Bei der Suche wird bei dem Textfeld Farb-ID 179 eingegeben.
     */
    @Test
    public void edit_colorId_withUnfinishedNumber () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        onView(withId(R.id.nav_filter)).perform(click());

        Thread.sleep(500);
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_colorId))
                .perform(replaceText(String.valueOf(179)));
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click())
                .check(matches(isDisplayed()));
    }


    /**
     * Der Aufruf erfolgt durch das Hauptmenü auf der linken Seite
     * Bei der Eingabe der Farb-Id wird ein Text eingegeben. Es erscheit ein AlertDialog, welcher
     * durch Klicken auf "OK" bestätigt wird.
     */
    //TODO Aufruf vom Menü
    @Test
    public void edit_colorId_isText_should_alert () throws Exception {
        /*
        // vom Hauptmenu aus aufrufen
        onView(withId(R.id.nav_home)).perform(click());
        onView(withId(R.id.nav_search)).perform(click());
        */
        Thread.sleep(500);
        //onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        //onView(withId(R.id.nav_filter)).perform(click());

        // open drawer to click navigation
        onView(withId(R.id.mainActivity)).check(matches(isClosed(Gravity.START))).perform(DrawerActions.open());
        // start screen of filter-activity
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_filter));

        onView(withId(R.id.filterFragment))
                .check(matches(isDisplayed()))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).check(matches(not(isDisplayed())));

        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard());
        //.check(matches(isDisplayed()));

        // buttonSubmit
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        //.check(matches(isCompletelyDisplayed()));

        // AlertDialog
        Thread.sleep(500);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }



    /**
     * Größe wird nicht als Zahl, sondern als Text eingegeben
     * Es können nur postive Integer eingegeben werden (Tastatur nur mit Zahlen)
     * Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void edit_size_isText_should_alert () throws Exception {
        Thread.sleep(500);
        //onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        //onView(withId(R.id.nav_filter)).perform(click());

        // open drawer to click navigation
        onView(withId(R.id.mainActivity)).check(matches(isClosed(Gravity.LEFT))).perform(DrawerActions.open());
        // start screen of filter-activity
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_filter));


        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_size))
                .perform(replaceText("Text"))
                .perform(closeSoftKeyboard());
        //.check(matches(isDisplayed()));

        // buttonSubmit
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        //.check(matches(isCompletelyDisplayed()));

        // AlertDialog
        Thread.sleep(500);
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    /**
     * Fertigungszustand sind nur Buchstaben erlaubt.
     * Es wird nach dem Fertigungszustand gesucht. Hierbei wird eine Zahl eingegeben.
     * Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    // TODO vom Hauptmenü aufrufen
    @Test
    public void edit_manufacturingState_isNumber_should_alert () throws Exception {
        /*
        // vom Hauptmenü aus aufrufen
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withId(R.id.nav_view))).perform(click());
        //onView(withId(R.id.nav_view_test)).perform(click());
        onView(withText("Search")).perform(click());
        //onView(withId(R.id.nav_search)).perform(click());
        */
        Thread.sleep(500);
        //onView(withId(R.id.mainActivity)).perform(pressKey(KeyEvent.KEYCODE_ENTER), pressMenuKey());
        //onView(withId(R.id.nav_filter)).perform(click());



        Thread.sleep(500);
        onView(withId(R.id.filterFragment_editText_manufacturingState))
                .perform(replaceText(String.valueOf(0)))
                .perform(closeSoftKeyboard());
        //.check(matches(isDisplayed()));
        // buttonSubmit
        Thread.sleep(500);
        onView(withId(R.id.filterFragment_button_submit))
                .perform(click());
        //.check(matches(isCompletelyDisplayed()));
        // AlertDialog
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObject.exists()) {
            uiObject.click();
        }
    }


    @Test
    public void checkPreconditions () {
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void testChangeText_sameActivity () {
        mDevice.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "onCreateView"))
                .setText("UiAutomator");
        mDevice.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "performFilter"))
                .setText("UiAutomator");
        UiObject2 changedText = mDevice.wait(Until.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "getShelvesToMarkRed")),
                500);
        assertThat(changedText.getText(), is(equalTo("UiAutomator")));
    }

    @Test
    public void testChangeText_newActivity () {
        mDevice.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "onCreateView"))
                .setText("UiAutomator");
        mDevice.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "button"))
                .click();
        UiObject2 changedText = mDevice.wait(Until.findObject(By.res(
                "de.lingen.hsosna.texview.fragments.FilterFragment", "markRegale")),
                500);
        assertThat(changedText.getText(), is(equalTo("UiAutomator")));
    }

    private String getLauncherPackageName () {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }


    /**
     * Die Aktivität wird terminiert.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }
}
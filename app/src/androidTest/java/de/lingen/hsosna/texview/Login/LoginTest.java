package de.lingen.hsosna.texview.Login;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import de.lingen.hsosna.texview.MainActivity;
import de.lingen.hsosna.texview.R;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * LoginTest
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    /*
    @Rule
    public IntentsTestRule<Login> mIntentsTestRule = new IntentsTestRule<Login>(Login.class);
    */

    @Rule
    public ActivityTestRule<Login> mLoginTestRule = new ActivityTestRule<Login>(Login.class) {
        @Override
        protected void beforeActivityLaunched() {
            Intents.init();
            //mIntentsTestRule.getActivity();
            super.beforeActivityLaunched();
        }
        @Override
        protected void afterActivityFinished() {
            super.afterActivityFinished();
            //mIntentsTestRule.finishActivity();
            Intents.release();
        }
    };

    UiDevice mDevice;
    public Login loginActivity = null;


    /**
     * Es wird eine neue LoginActivity erstellt, um darauf zu testen.
     */
    @Before
    public void setUp() throws Exception {
        //Intents.init();
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    /**
     *
     */
    @Test
    @UiThreadTest
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Beim Login wird lediglich auf den Login-Button geglickt. Es erscheint ein AlertDialog,
     * welcher durch Klicken auf "OK" bestätitgt wird.
     */
    @Test
    public void login_without_username_password_should_alert () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);

        // AlertDialog bestätigen
        mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        UiObject uiObjectTitel = mDevice.findObject((UiSelector) withText("Login fehlgeschlagen"));
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Bitte füllen Sie alle Felder aus"));
        assertEquals(isDisplayed(), uiObjectTitel);
        assertEquals("Bitte füllen Sie alle Felder aus", uiObject);
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Der Login ist fehlgeschlagen, da weder Benutzername noch Passwort eingegegben worden sind.
     * Der AlertDialog soll über die Zurück-Taste bestätigt werden.
     */
    @Test
    public void login_fail_alert_cancel_pressBack () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        // AlertDialog bestätigen
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Bitte füllen Sie alle Felder aus"));
        assertEquals("Bitte füllen Sie alle Felder aus", uiObject);
        mDevice.pressBack();
        //Espresso.pressBack();
        onView(isRoot()).perform(ViewActions.pressBack());
    }


    /**
     * Der Login ist fehlgeschlagen, da weder Benutzername noch Passwort eingegegben worden sind.
     * Der AlertDialog soll durch Klicken außerhalb von "OK" bestätigt werden
     */
    @Test
    public void login_fail_alert_cancel_outside () throws Exception {
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Bitte füllen Sie alle Felder aus"));
        assertEquals("Bitte füllen Sie alle Felder aus", uiObject);
        // AlertDialog bestätigen
        onView(not(withText("OK)"))).perform(click());
    }


    /**
     * Es wird beim Login ein falscher Benutzname eingegeben. Das Passwort ist korrekt. Es erscheint
     * ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectUsername_should_alert () throws Exception {
        String username = "username";
        String password = "hochschuleosnabrueck";
        onView(withId(R.id.username))
                .perform(replaceText(username), closeSoftKeyboard())
                .check(matches(isDisplayed()));
        onView(withId(R.id.password))
                .perform(replaceText(password), pressKey(KeyEvent.KEYCODE_ENTER))
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        // AlertDialog bestätigen
        mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Benutzername oder Passwort ungültig"));
        assertEquals("Benutzername oder Passwort ungültig", uiObject);
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird beim Login ein falsches Passwort eingegeben. Der Bentzername ist korrekt. Es erscheint
     * ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectPassword_should_alert () throws Exception {
        String username = "sep_demo";
        String password = "password";
        onView(withId(R.id.username))
                .perform(replaceText(username), closeSoftKeyboard())
                .check(matches(isDisplayed()));
        onView(withId(R.id.password))
                .perform(replaceText(password), closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);
        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Benutzername oder Passwort ungültig"));
        assertEquals("Benutzername oder Passwort ungültig", uiObject);
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird beim Login ein falscher Benutzername und ein falsches Passwort eingegeben. Es
     * erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectUsername_incorrectPassword_should_alert () throws Exception {
        String username = "username";
        String password = "password";
        onView(withId(R.id.username))
                .perform(replaceText(username), closeSoftKeyboard())
                .check(matches(isDisplayed()));
        onView(withId(R.id.password))
                .perform(replaceText(password), closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);
        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObject = mDevice.findObject(new UiSelector().text("Benutzername oder Passwort ungültig"));
        assertEquals("Benutzername oder Passwort ungültig", uiObject);
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird überprüft, ob nach erfolreichem Login die neue Aktivität geladen wird und man somit
     * zur MainActivity gelangt.
     */
    @Test
    public void click_loginButton_forwards_to_mainActivity () throws Exception {
        String username = "sep_demo";
        String password = "hochschuleosnabrueck";
        Thread.sleep(500);
        onView(withId(R.id.username))
                .perform(replaceText(username))
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.password))
                .perform(replaceText(password))
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);

        Intent resultData = new Intent();
        resultData.putExtra("MainActivtity", "Login");
        ActivityResult result = new ActivityResult(Activity.RESULT_OK, resultData);

        intended(hasComponent(new ComponentName(getTargetContext(), MainActivity.class)));
        intending(toPackage(MainActivity.class.getName())).respondWith(result);
        intending(hasComponent(MainActivity.class.getName()))
                .respondWith(new Instrumentation.ActivityResult(0, null));
        //intended(hasComponent(MainActivity.class.getName()));
    }


    /// MOCKITO ///
    @Mock
    private Login login;

    private static MockWebServer server;

    @Test
    public void login_success_should_lead_to_mainactivity_and_display_correct_string ()
            throws Exception {
        MockResponse usernameResonse = new MockResponse().setResponseCode(200);
        MockResponse passwordResponse = new MockResponse().setResponseCode(200)
                .setBody("{\"isAndroidTestingFunny\": true]");
        server.enqueue(usernameResonse);
        server.enqueue(passwordResponse);

        ViewInteraction editTextUsername = onView(withId(R.id.username))
                .perform(scrollTo(), replaceText("rafael"), closeSoftKeyboard());
        ViewInteraction editTextPassword = onView(withId(R.id.password))
                .perform(scrollTo(), replaceText("rafael"), closeSoftKeyboard());
        ViewInteraction loginButton = onView(withId(R.id.buttonLogin))
                .perform(scrollTo(), click());
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.loginActivity = null;
    }

}

package de.lingen.hsosna.texview.Login;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
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

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Für den Login werden UI-Tests geschrieben
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    public Login loginActivity = null;

    @Rule
    public ActivityTestRule<Login> mLoginTestRule
            = new ActivityTestRule<Login>(Login.class, true, true);



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
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("de.lingen.hsosna.texview", appContext.getPackageName());
    }


    /**
     * Beim Login werden weder Benutzername noch Passwort eingegeben. Es wird lediglich auf den
     * Login-Button geklickt. Der Login schlägt fehl. Es erscheint ein AlertDialog, welcher durch
     * Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void login_without_username_password_should_alert () throws Exception {
        Thread.sleep(1000);
        onView(withId(R.id.username))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);

        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Der Login ist fehlgeschlagen, da weder Benutzername noch Passwort eingegeben worden sind.
     * Der AlertDialog soll über die Zurück-Taste bestätigt werden.
     */
    @Test
    public void login_fail_alert_cancel_pressBack () throws Exception {
        Thread.sleep(1000);
        String username = "username";
        String password = "password";
        onView(withId(R.id.username))
                .perform(replaceText(username))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(replaceText(password))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);

        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            onView(withText("OK"))
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()))
                    .perform(pressBack());
        }
    }


    /**
     * Es wird beim Login ein falscher Benutzername eingegeben. Das Passwort ist korrekt. Die Eingabe
     * erfolgt über die Enter-Taste. Der Login schlägt fehl. Es erscheint ein AlertDialog, welcher
     * durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectUsername_should_alert () throws Exception {
        Thread.sleep(1000);
        String username = "username";
        String password = "hochschuleosnabrueck";

        onView(withId(R.id.username))
                .perform(replaceText(username))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.password))
                .perform(replaceText(password))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));
        Thread.sleep(500);

        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird beim Login ein falsches Passwort eingegeben. Der Benutzername ist korrekt. Der
     * Benutzer drückt die Weiter-Taste, um zur Eingabe des Passworts zu gelangen. Der Login
     * schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectPassword_should_alert () throws Exception {
        Thread.sleep(1000);
        String username = "sep_demo";
        String password = "password";

        onView(withId(R.id.username))
                .perform(replaceText(username))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.password))
                .perform(replaceText(password))
                .perform(closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);

        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird beim Login ein falscher Benutzername und ein falsches Passwort eingegeben. Der Login
     * schlägt fehl. Es erscheint ein AlertDialog, welcher durch Klicken auf "OK" bestätigt wird.
     */
    @Test
    public void incorrectUsername_incorrectPassword_should_alert () throws Exception {
        Thread.sleep(1000);
        String username = "username";
        String password = "password";
        onView(withId(R.id.username))
                .perform(replaceText(username))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.password))
                .perform(replaceText(password))
                .perform(closeSoftKeyboard())
                .check(matches(isDisplayed()));
        Thread.sleep(500);
        onView(withId(R.id.buttonLogin))
                .perform(click());
        Thread.sleep(500);

        // AlertDialog bestätigen
        UiDevice mDevice  = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject uiObjectOK = mDevice.findObject(new UiSelector().text("OK"));
        if (uiObjectOK.exists()) {
            uiObjectOK.click();
        }
    }


    /**
     * Es wird überprüft, ob nach erfolgreichem Login die neue Aktivität geladen wird und man zur
     * MainActivity gelangt.
     */
    @Test
    public void click_loginButton_forwards_to_mainActivity () throws Exception {
        Thread.sleep(1000);
        String username = "sep_demo";
        String password = "hochschuleosnabrueck";

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
    }



    /**
     * Die Aktivität wird terminiert.
     */
    @After
    public void tearDown() throws Exception {
        this.loginActivity = null;
        Intents.release();
    }

}

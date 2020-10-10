package de.lingen.hsosna.texview;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.lingen.hsosna.texview.Login.LoginTest;
import de.lingen.hsosna.texview.fragments.FilterFragmentTest;
import de.lingen.hsosna.texview.fragments.HomeFragmentTest;
import de.lingen.hsosna.texview.fragments.InfoFragmentTest;
import de.lingen.hsosna.texview.fragments.KPIFragmentTest;
import de.lingen.hsosna.texview.fragments.SearchFragmentTest;
import de.lingen.hsosna.texview.fragments.SettingsFragmentTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        // fragments
        FilterFragmentTest.class,
        HomeFragmentTest.class,
        InfoFragmentTest.class,
        KPIFragmentTest.class,
        SearchFragmentTest.class,
        SettingsFragmentTest.class,
        // login
        LoginTest.class,

        MainActivityTest.class
})
public class AllTestsRun {
}

package de.lingen.hsosna.texview;

import org.junit.Test;

import static org.junit.Assert.*;

public class SearchFragmentUnitTest {
    @Test
    public void getSqlWhereQuery () {
        try {
            String ArtikelNr = "ARTIKEL9999";
            String ArtikelBez = "";
            String FarbId = "keine Strings erlaubt";
            String FarbBez = "";
            String Groesse = "        whitespace";
            String Fertigungszustand = "";
            String expected = "";
            SearchFragmentTestClass sftc = new SearchFragmentTestClass(ArtikelNr, ArtikelBez,
                    FarbId, FarbBez, Groesse, Fertigungszustand);
            String output = sftc.getSqlWhereQuery();
            assertEquals(expected, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
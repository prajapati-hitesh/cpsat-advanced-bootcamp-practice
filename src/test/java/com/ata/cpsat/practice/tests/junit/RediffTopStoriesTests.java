package com.ata.cpsat.practice.tests.junit;

import com.ata.cpsat.framework.utility.DateUtility;
import com.ata.cpsat.practice.pages.RediffHomePage;
import com.ata.cpsat.runner.JUnitSuiteRunner;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * a. Open the website https://www.rediff.com/ (0 mark)
 * b. Find out href of all the TopStories on the Rediff.com page using findElements (3 marks)
 * c. Store all the href’s in either a Text file, or an Excel file (You have to submit the file created) (3 marks)
 */
public class RediffTopStoriesTests extends JUnitSuiteRunner {

    @Test
    public void getAndSaveAllTopStoriesHREFTest() throws IOException {
        RediffHomePage rediffHomePage = new RediffHomePage();

        // Load website
        List<String> allHref = rediffHomePage.loadWebsite().getAllTopStoriesHREFs();

        FileUtils.writeLines(new File(FILE_DIR.concat("rediff-top-stories-hrefs-").concat(DateUtility.getCurrentTimeStamp()).concat(".txt")), allHref);
    }
}

package com.philwin.finance.optionsingest.util;

import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.assertTrue;

public class DataWriterUtilTest {

    @Test
    public void testStoreData_SimpleFileCreationCurrentDir() {
        File fileOfInterest =   new File(System.getProperty("user.dir") + File.separator + "RandomFile.txt");
        if (fileOfInterest.exists()) {
            fileOfInterest.delete();
        }
        DataWriterUtil.storeData(null, fileOfInterest.getAbsolutePath());

        assertTrue(fileOfInterest.exists());
        fileOfInterest.delete();
    }

    @Test
    public void testStoreData_SimpleFileCreationNextDir() {
        File fileOfInterest =   new File(System.getProperty("user.dir") + File.separator + "RandomFolder" + File.separator + "RandomFile.txt");
        if (fileOfInterest.getParentFile().exists()) {
            fileOfInterest.delete();
        }
        DataWriterUtil.storeData("This is amazing!!!!", fileOfInterest.getAbsolutePath());

        assertTrue(fileOfInterest.exists());
        fileOfInterest.getParentFile().delete();
    }
}
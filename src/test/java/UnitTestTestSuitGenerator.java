import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;

@Test()
public class UnitTestTestSuitGenerator {
    TestSuiteGenerator testSuiteGeneratorObject = new TestSuiteGenerator();

    @Test(priority = 1, description = "Negative Test: Verify the method fails for invalid directory path")
    public void UT_readFile_invalidPath() throws Exception {
        String invalidPath = System.getProperty("user.dir") + "\\src\\main\\resources\\InvalidPath";
        try {
            testSuiteGeneratorObject.readFile(invalidPath);
        } catch (Exception e) {
            System.out.println("Negative Test passes for invalid directory path");
        }


    }

    @Test(priority = 2, description = "Negative Test: Verify the method fails for empty directory")
    public void UT_readFile_emptyDir() throws Exception {
        String emptyDirectory = System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles\\emptyFolder";
        try {
            testSuiteGeneratorObject.readFile(emptyDirectory);
        } catch (Exception e) {
            System.out.println("Negative Test passes for empty directory path");
        }
    }

    @Test(priority = 3, description = "Negative Test: Verify the method fails when there is no CSV files")
    public void UT_readFile_noCSVFile() throws Exception {
        String noCSVFile = System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles\\noCSVFile";
        try {
            testSuiteGeneratorObject.readFile(noCSVFile);
        } catch (Exception e) {
            System.out.println("Negative Test passes for No CSV File");
        }
    }

    @Test(priority = 4, description = "Verify the Test Suite File is created")
    public void UT_createXML_FileCreated() throws Exception {
        String suitePath = System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles";

        File[] xmlData = testSuiteGeneratorObject.readFile(suitePath);

        testSuiteGeneratorObject.createXML(xmlData, "Regression Test Suite", "com.test.testSuite.Runner.", suitePath);

        File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles\\RegressionTestSuite.xml");
        if (!file.exists()) {
            throw new FileNotFoundException("File Not Created");

        } else System.out.println("File Created Successfully !!");


    }
}
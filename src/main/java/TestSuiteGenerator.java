import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * The script is developed to read the file names from a directory and generate a Test Suite xml.
 *
 * @author Azkhan Hassan
 * @version 1.0
 * @since 2020-02-05
 */
public class TestSuiteGenerator {

    private static String FILE_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles";

    //Provide the following details
    private static String SUITE_NAME = "Regression Test Suite";
    private static String RUNNER_CLASS = "com.test.testSuite.Runner.";


    public static void main(String args[]) throws Exception {

        File[] xmlData = readFile(FILE_LOCATION);
        createXML(xmlData, SUITE_NAME, RUNNER_CLASS, FILE_LOCATION);

    }


    //Reads the file names from the location provided in FILE_LOCATION
    static File[] readFile(String path) throws Exception {

        String filePath = path.replace("\\", "/");
        File folder = new File(filePath);

        //File Error handling
        if (!(isDirectoryExists(filePath) && isDirectoryEmpty(filePath))) {
            throw new Exception("Invalid Directory Path or Directory Empty !");
        }

        //Filter only for CSV Files
        File[] listOfFiles = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                if (s.toLowerCase().endsWith(".csv")) {
                    return true;
                } else return false;
            }
        });
        if (listOfFiles.length == 0) {

            throw new FileNotFoundException("No CSV Files found ! ");
        } else {
            System.out.println("Number of CSV files found : " + listOfFiles.length);
            return listOfFiles;
        }

    }

    //Create XML file
    static void createXML(File[] data, String suiteName, String runnerClass, String path) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //rootElement : suiteTag
            Element suiteTag = doc.createElement("suite");
            doc.appendChild(suiteTag);
            suiteTag.setAttribute("name", suiteName);
            suiteTag.setAttribute("preserve-order", "true");
            suiteTag.setAttribute("parallel", "classes");
            suiteTag.setAttribute("thread-count", "1");

            for (int i = 0; i < data.length; i++) {

                //get the extension of the files
                String fileName = data[i].getName();
                String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                //test tag
                Element testTag = doc.createElement("test");
                suiteTag.appendChild(testTag);
                testTag.setAttribute("name", data[i].getName().replace("." + extension, "") + "_" + extension);

                //classes tag
                Element classesTag = doc.createElement("classes");
                testTag.appendChild(classesTag);

                //class tag
                Element classTag = doc.createElement("class");
                classesTag.appendChild(classTag);
                classTag.setAttribute("name", runnerClass);

                //parameter tag
                Element parameterTag = doc.createElement("parameter");
                classTag.appendChild(parameterTag);
                parameterTag.setAttribute("name", "scriptPath");
                parameterTag.setAttribute("value", path.replace("\\", "/") + "/" + data[i].getName());

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles\\" + suiteName.replace(" ", "") + ".xml"));

            transformer.transform(source, result);

            System.out.println(suiteName.replace(" ", "") + ".xml is saved on drive " + System.getProperty("user.dir") + "\\src\\main\\resources\\TestFiles\\");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    public static boolean isDirectoryExists(String directoryPath) {
        if (!Paths.get(directoryPath).toFile().isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean isDirectoryEmpty(String directoryPath) {
        File file = new File(directoryPath);
        if (file.list().length > 0) {
            return true;
        }
        return false;
    }

}



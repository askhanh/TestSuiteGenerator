import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * The script is developed to read the file names from a directory and generate a Test Suite xml.
 *
 * @author Azkhan Hassan
 * @version 1.0
 * @since 2020-02-05
 */
public class TestSuiteGenerator {

    //Provide the following details
    private static String FILE_LOCATION = "C:\\Regression Test Suite\\Test Suite"; //copy paste the location
    private static String SUITE_NAME = "Regression Test Suite";
    private static String RUNNER_CLASS = "com.test.testSuite.Runner.";


    public static void main(String args[]) {

        File[] xmlData = readFile();
        createXML(xmlData);

    }


    //Reads the file names from the location provided in FILE_LOCATION
    static File[] readFile() {

        String filePath = FILE_LOCATION.replace("\\", "/");
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        for (File i : listOfFiles) {

            System.out.println(i.getName());
        }
        System.out.println("Number of files found : " + listOfFiles.length);

        return listOfFiles;

    }

    //Create XML file
    static void createXML(File[] data) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            //rootElement : suiteTag
            Element suiteTag = doc.createElement("suite");
            doc.appendChild(suiteTag);
            suiteTag.setAttribute("name", SUITE_NAME);
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
                classTag.setAttribute("name", RUNNER_CLASS);

                //parameter tag
                Element parameterTag = doc.createElement("parameter");
                classTag.appendChild(parameterTag);
                parameterTag.setAttribute("name", "scriptPath");
                parameterTag.setAttribute("value", FILE_LOCATION.replace("\\", "/")+"/" + data[i].getName());

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("D:\\" + SUITE_NAME.replace(" ", "") + ".xml"));

            transformer.transform(source, result);

            System.out.println(SUITE_NAME.replace(" ", "") + ".xml is saved on drive D:");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

}



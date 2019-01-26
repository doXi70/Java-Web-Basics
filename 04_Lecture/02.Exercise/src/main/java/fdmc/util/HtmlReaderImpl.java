package fdmc.util;

import java.io.*;

public class HtmlReaderImpl implements HtmlReader {

    @Override
    public String readHtmlFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(filePath))));

        StringBuilder htmlFileContent = new StringBuilder();
        String htmlFileLine;
        while ((htmlFileLine = reader.readLine()) != null) {
            htmlFileContent.append(htmlFileLine).append(System.lineSeparator());
        }

        return htmlFileContent.toString();
    }
}

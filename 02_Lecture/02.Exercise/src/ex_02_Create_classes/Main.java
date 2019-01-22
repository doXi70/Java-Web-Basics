package ex_02_Create_classes;

import ex_02_Create_classes.http.HttpRequest;
import ex_02_Create_classes.http.HttpRequestImpl;
import ex_02_Create_classes.http.HttpResponse;
import ex_02_Create_classes.http.HttpResponseImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // IMPORTANT: By TASK REQUIREMENTS DATE: header should not be included if its not in the request
        // EVEN that in 3 and 4 there is date in the response, I accepted that this is mistake
        // IN the TASK
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder fullRequest = new StringBuilder();

        String line;
        while (!"".equals(line = reader.readLine())) {
            fullRequest.append(line).append(System.lineSeparator());
        }

        fullRequest.append(" ").append(System.lineSeparator());

        line = reader.readLine();
        if (line.isEmpty()) {
            fullRequest.append(" ");
        } else {
            fullRequest.append(line);
        }

        // I don't think that this belongs as inside filed of any of the 2 classes
        List<String> validUrls = Arrays.stream(fullRequest.toString()
                .split(System.lineSeparator())[0].split(" "))
                .collect(Collectors.toList());

        HttpRequest request = new HttpRequestImpl(fullRequest.toString());
        HttpResponse response = new HttpResponseImpl(request, validUrls);


        // IMPORTANT: By TASK REQUIREMENTS DATE: header should not be included if its not in the request
        // EVEN that in 3 and 4 there is date in the response, I accepted that this is mistake
        // IN the TASK
        System.out.print(new String(response.getBytes(), StandardCharsets.UTF_8));
    }
}
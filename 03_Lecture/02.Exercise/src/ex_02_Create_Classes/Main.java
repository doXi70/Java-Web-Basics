package ex_02_Create_Classes;

import ex_02_Create_Classes.http.HttpRequest;
import ex_02_Create_Classes.http.HttpRequestImpl;
import ex_02_Create_Classes.http.HttpResponse;
import ex_02_Create_Classes.http.HttpResponseImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
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

//        List<String> validUrls = Arrays.stream(fullRequest.toString()
//                .split(System.lineSeparator())[0].split(" "))
//                .collect(Collectors.toList());

        HttpRequest request = new HttpRequestImpl(fullRequest.toString());
        HttpResponse response = new HttpResponseImpl(request);

//        System.out.println(new String(response.getBytes(), StandardCharsets.UTF_8));
        request.getCookieContents().forEach(System.out::println);
    }
}
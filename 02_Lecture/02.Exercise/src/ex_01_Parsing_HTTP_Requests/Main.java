package ex_01_Parsing_HTTP_Requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        // IMPORTANT: By TASK REQUIREMENTS DATE: header should not be included if its not in the request
        // EVEN that in 3 and 4 there is date in the response, I accepted that this is mistake
        // IN the TASK
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String requestBody;
        List<String> fullRequest = new ArrayList<>();
        while (true) {
            String line = reader.readLine();

            if (line.equals("")) {
                requestBody = reader.readLine();
                break;
            }

            fullRequest.add(line);
        }

        LinkedHashMap<String, String> requestHeaders = getDateHostContentTypeAuthHeaders(fullRequest);
        List<String> validUrlPaths = Arrays.stream(fullRequest.get(0).split("\\s+")).collect(Collectors.toList());
        String[] methodUrlVersionTokens = fullRequest.get(1).split("\\s+");

        StringBuilder response = new StringBuilder();
        if (!isRequestUrlPathValid(methodUrlVersionTokens[1], validUrlPaths)) {
            response.append(methodUrlVersionTokens[2]).append(" 404 Not Found")
                    .append(System.lineSeparator());

            appendHeaders(requestHeaders, response);

            response.append(System.lineSeparator())
                    .append("The requested functionality was not found.");
        } else if (!requestHeaders.containsKey("Authorization:")) {
            response.append(methodUrlVersionTokens[2]).append(" 401 Unauthorized")
                    .append(System.lineSeparator());

            appendHeaders(requestHeaders, response);

            response.append(System.lineSeparator())
                    .append("You are not authorized to access the requested functionality.");
        } else if (methodUrlVersionTokens[0].equals("POST") && requestBody == null) {
            response.append(methodUrlVersionTokens[2]).append(" 400 Bad Request")
                    .append(System.lineSeparator());

            appendHeaders(requestHeaders, response);

            response.append(System.lineSeparator())
                    .append("There was an error with the requested functionality due to malformed request.");
        } else if (methodUrlVersionTokens[0].equals("GET") && requestBody.trim().isEmpty()) {
            response.append(methodUrlVersionTokens[2]).append(" 200 OK")
                    .append(System.lineSeparator());

            appendHeaders(requestHeaders, response);

            response.append(System.lineSeparator())
                    .append(String.format("Greetings %s!", decodeUsernameFromAuth(requestHeaders.get("Authorization:"))));
        } else {
            response.append(methodUrlVersionTokens[2]).append(" 200 OK")
                    .append(System.lineSeparator());

            appendHeaders(requestHeaders, response);

            String username = decodeUsernameFromAuth(requestHeaders.get("Authorization:"));
            response.append(System.lineSeparator()).append(buildResponseBody(requestBody, username));
        }

        // IMPORTANT: By TASK REQUIREMENTS DATE: header should not be included if its not in the request
        // EVEN that in 3 and 4 there is date in the response, I accepted that this is mistake
        // IN the TASK
        System.out.println(response);
    }

    private static String buildResponseBody(String requestBody, String username) {
        Map<String, String> result = new LinkedHashMap<>();

        List<String> tokens = Arrays.stream(requestBody.split("&"))
                .collect(Collectors.toList());

        tokens.forEach(kvp -> {
            String[] kvpTokens = kvp.split("=");
            result.put(kvpTokens[0], kvpTokens[1]);
        });

        Object[] keys = result.keySet().toArray();
        Object[] values = result.values().toArray();

        return String.format("Greetings %s! " +
                "You have successfully created %s with %s - %s, %s - %s.",
                username, values[0], keys[1], values[1], keys[2], values[2]);
    }


    private static void appendHeaders(Map<String, String> requestHeaders, StringBuilder response) {
        requestHeaders.forEach((name, value) -> {
            switch (name) {
                case "Date:":
                    response.append("Date: ").append(requestHeaders.get("Date:"))
                            .append(System.lineSeparator());
                    break;
                case "Content-Type:":
                    response.append("Content-Type: ").append(requestHeaders.get("Content-Type:"))
                            .append(System.lineSeparator());
                    break;
                case "Host:":
                    response.append("Host: ").append(requestHeaders.get("Host:"))
                            .append(System.lineSeparator());
                    break;
            }
        });
    }

    // IMPORTANT: By TASK REQUIREMENTS DATE: header should not be included if its not in the request
    // EVEN that in 3 and 4 there is date in the response, I accepted that this is mistake
    // IN the TASK
    private static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        return String.format("%02d/%02d/%04d", now.getDayOfMonth(), now.getMonthValue(), now.getYear());
    }

    private static boolean isRequestUrlPathValid(String pathRequested, List<String> validPaths) {
        return validPaths.contains(pathRequested);
    }

    private static LinkedHashMap<String, String> getDateHostContentTypeAuthHeaders(List<String> request) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();

        // start from 2 to skip valid url and request method
        for (int i = 2; i < request.size(); i++) {
            String[] headerNameValue = request.get(i).split("\\s");

            switch (headerNameValue[0]) {
                case "Date:":
                case "Host:":
                case "Content-Type:":
                    result.put(headerNameValue[0], headerNameValue[1]);
                    break;
                case "Authorization:":
                    result.put(headerNameValue[0], headerNameValue[2]);
                    break;
            }
        }

        return result;
    }

    private static String decodeUsernameFromAuth(String encodedBase64) {
        byte[] decode = Base64.getUrlDecoder().decode(encodedBase64);

        StringBuilder sb = new StringBuilder();
        for (byte b : decode) {
            sb.append((char) b);
        }

        return sb.toString();
    }
}

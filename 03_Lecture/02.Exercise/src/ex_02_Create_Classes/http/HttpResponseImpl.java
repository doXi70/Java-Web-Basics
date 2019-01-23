package ex_02_Create_Classes.http;

import ex_02_Create_Classes.utils.Base64Decoder;
import ex_02_Create_Classes.utils.HttpStatusCodes;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseImpl implements HttpResponse {

    private LinkedHashMap<String, String> headers;
    private int statusCode;
    private byte[] content;


    public HttpResponseImpl(HttpRequest httpRequest) {
        this.statusCode = -1;
        this.headers = new LinkedHashMap<>();

        this.parseRequest(httpRequest, List.of(httpRequest.getMethod()));
    }

    public HttpResponseImpl(HttpRequest httpRequest, List<String> validUrls) {
        this.statusCode = -1;
        this.headers = new LinkedHashMap<>();

        this.parseRequest(httpRequest, validUrls);
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public byte[] getBytes() {
        return this.content;
    }

    @Override
    public void addHeader(String header, String value) {
        switch (header) {
            case "Date:":
            case "Host:":
            case "Content-Type:":
                this.headers.put(header, value);
                break;
        }
    }

    private void parseRequest(HttpRequest httpRequest, List<String> validUrls) {
        boolean requestedPathIsValid = validUrls.contains(httpRequest.getRequestUrl());
        if (!requestedPathIsValid) {
            this.statusCode = 404;
            httpRequest.getHeaders().forEach(this::addHeader);
            buildErrorResponse(httpRequest, "The requested functionality was not found.");
        } else if (!httpRequest.getHeaders().containsKey("Authorization:")) {
            this.statusCode = 401;
            httpRequest.getHeaders().forEach(this::addHeader);
            buildErrorResponse(httpRequest, "You are not authorized to access the requested functionality.");
        } else if (httpRequest.getMethod().equals("POST") && httpRequest.getBodyParameters().isEmpty()) {
            this.statusCode = 400;
            httpRequest.getHeaders().forEach(this::addHeader);
            buildErrorResponse(httpRequest, "There was an error with the requested functionality due to malformed request.");
        } else {
            this.statusCode = 200;
            httpRequest.getHeaders().forEach(this::addHeader);
            buildSuccessResponse(httpRequest);
        }
    }

    private void buildSuccessResponse(HttpRequest httpRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %s", httpRequest.getHttpVersion(),
                this.statusCode, HttpStatusCodes.getMessage(this.statusCode)))
                .append(System.lineSeparator());

        for (Map.Entry<String, String> entry : this.getHeaders().entrySet()) {
            sb.append(entry.getKey()).append(" ")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        String username = Base64Decoder.decode(httpRequest.getHeaders().get("Authorization:"));
        if (httpRequest.getMethod().equals("GET")) {
            sb.append(String.format("Greetings %s!", username));
        } else {
            Object[] keys = httpRequest.getBodyParameters().keySet().toArray();
            Object[] values = httpRequest.getBodyParameters().values().toArray();

            sb.append(String.format("Greetings %s!", username))
                    .append(String.format(" You have successfully created %s with %s – %s, %s – %s.",
                            values[0], keys[1], values[1], keys[2], values[2]));
        }

        this.content = sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void buildErrorResponse(HttpRequest httpRequest, String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %d %s", httpRequest.getHttpVersion(),
                this.statusCode, HttpStatusCodes.getMessage(this.statusCode)))
                .append(System.lineSeparator());

        for (Map.Entry<String, String> entry : this.getHeaders().entrySet()) {
            sb.append(entry.getKey()).append(" ")
                    .append(entry.getValue())
                    .append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());
        sb.append(s);
        this.content = sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}

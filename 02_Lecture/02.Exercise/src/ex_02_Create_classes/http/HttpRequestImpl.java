package ex_02_Create_classes.http;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestImpl implements HttpRequest {

    private String method;
    private String requestUrl;
    private String httpVersion;
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, String> bodyParameters;

    public HttpRequestImpl(String fullRequest) {
        this.headers = new LinkedHashMap<>();
        this.bodyParameters = new LinkedHashMap<>();

        this.parseRequest(fullRequest);
    }

    @Override
    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public LinkedHashMap<String, String> getBodyParameters() {
        return bodyParameters;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestUrl() {
        return requestUrl;
    }

    @Override
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public void addBodyParameter(String parameter, String value) {
        bodyParameters.put(parameter, value);
    }

    @Override
    public boolean isResource() {
        // Content-Disposition: attachment
        for (Map.Entry<String, String> headersKVP : headers.entrySet()) {
            String header = headersKVP.getKey();
            if (header.equals("Content-Disposition:")) {
                String info = headersKVP.getValue();
                if (info.contains("attachment")) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String getHttpVersion() {
        return this.httpVersion;
    }

    private void parseRequest(String fullRequest) {
//        /validUrls /spaceSeparator
//        POST /url HTTP/1.1
//        Date: 17/01/2019
//        Host: localhost:8000
//        Content-Type: application/xml
//        Authorization: Basic UGVzaG8=
//
//        name=Yum&quantity=50&price=10
        String[] tokens = fullRequest.split(System.lineSeparator());

        String[] split = tokens[1].split("\\s+");
        this.method = split[0];
        this.requestUrl = split[1];
        this.httpVersion = split[2];

        for (int i = 2; i < tokens.length - 2; i++) {
            String[] moreTokens = tokens[i].split("\\s+");
            if (!moreTokens[0].equals("Authorization:")) {
                this.addHeader(moreTokens[0], moreTokens[1]);
            } else {
                this.addHeader(moreTokens[0], moreTokens[2]);
            }

        }

        boolean bodyExists = !tokens[tokens.length - 1].equals(" ");
        if (bodyExists && this.method.equals("POST")) {
            //name=Yum&quantity=50&price=10
            Arrays.stream(tokens[tokens.length - 1].split("&"))
                    .forEach(x -> {
                        String[] items = x.split("=");
                        this.addBodyParameter(items[0], items[1]);
                    });
        }
    }
}

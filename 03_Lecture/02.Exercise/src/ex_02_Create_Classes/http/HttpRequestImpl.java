package ex_02_Create_Classes.http;

import java.util.*;

public class HttpRequestImpl implements HttpRequest {

    private String method;
    private String requestUrl;
    private String httpVersion;
    private LinkedHashMap<String, String> headers;
    private LinkedHashMap<String, String> bodyParameters;
    private List<HttpCookie> cookieContents;

    public HttpRequestImpl(String fullRequest) {
        this.headers = new LinkedHashMap<>();
        this.bodyParameters = new LinkedHashMap<>();
        this.cookieContents = new ArrayList<>();

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
        if ("Cookie:".equals(header)) {
            parseCookie(value);
        }

        headers.put(header, value);
    }

    @Override
    public void addBodyParameter(String parameter, String value) {
        bodyParameters.put(parameter, value);
    }

    @Override
    public boolean isResource() {
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

    @Override
    public List<HttpCookie> getCookieContents() {
        return this.cookieContents;
    }

    @Override
    public void addCookie(HttpCookie cookie) {
        this.cookieContents.add(cookie);
    }

    private void parseRequest(String fullRequest) {
        String[] tokens = fullRequest.split(System.lineSeparator());

        String[] split = tokens[0].split("\\s+");
        this.method = split[0];
        this.requestUrl = split[1];
        this.httpVersion = split[2];

        for (int i = 1; i < tokens.length - 2; i++) {
            String[] moreTokens = tokens[i].split("\\s+", 2);
            if (!moreTokens[0].equals("Authorization:")) {
                this.addHeader(moreTokens[0], moreTokens[1]);
            } else {
                this.addHeader(moreTokens[0], moreTokens[1].split("\\s+")[1]);
            }

        }

        boolean bodyExists = !tokens[tokens.length - 1].equals(" ");
        if (bodyExists && this.method.equals("POST")) {
            Arrays.stream(tokens[tokens.length - 1].split("&"))
                    .forEach(x -> {
                        String[] items = x.split("=");
                        this.addBodyParameter(items[0], items[1]);
                    });
        }
    }

    private void parseCookie(String cookieContents) {
        if (!cookieContents.isEmpty()) {
            Arrays.stream(cookieContents.split("; "))
                    .forEach(x -> {
                        String[] kvp = x.split("=");
                        this.addCookie(new HttpCookie(kvp[0], kvp[1]));
                    });
        }
    }
}

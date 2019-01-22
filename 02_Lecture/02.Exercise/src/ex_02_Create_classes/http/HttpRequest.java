package ex_02_Create_classes.http;

import java.util.LinkedHashMap;

public interface HttpRequest {

    LinkedHashMap<String, String> getHeaders();

    LinkedHashMap<String, String> getBodyParameters();

    String getMethod();

    void setMethod(String method);

    String getRequestUrl();

    void setRequestUrl(String requestUrl);

    void addHeader(String header, String value);

    void addBodyParameter(String parameter, String value);

    boolean isResource();

    String getHttpVersion();
}

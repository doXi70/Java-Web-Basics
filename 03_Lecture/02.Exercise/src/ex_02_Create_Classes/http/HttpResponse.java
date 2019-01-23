package ex_02_Create_Classes.http;

import java.util.LinkedHashMap;
import java.util.Map;

public interface HttpResponse {

    LinkedHashMap<String, String> getHeaders();

    int getStatusCode();

    void setStatusCode(int statusCode);

    byte[] getContent();

    void setContent(byte[] content);

    byte[] getBytes();

    void addHeader(String header, String value);
}

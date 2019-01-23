package ex_02_Create_Classes.http;

public class HttpCookie {
    private String key;
    private String value;

    public HttpCookie(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s <-> %s", this.key, this.value);
    }
}

package ex_02_Create_classes.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum HttpStatusCodes {

    OK(200),
    BadRequest(400),
    NotFound(404),
    Unauthorized(401);

    private int statusCode;

    HttpStatusCodes(int statusCode) {
        this.statusCode = statusCode;
    }

    public static String getMessage(int statusCode) {

        return Arrays.stream(HttpStatusCodes.values())
                .filter(hsc -> hsc.getStatusCode() == statusCode)
                .findFirst()
                .map(msg -> {
                    if (msg.name().equals("BadRequest") || msg.name().equals("NotFound")) {
                        return msg.name().substring(0, 3) + " " + msg.name().substring(3);
                    } else {
                        return msg.name();
                    }
                })
                .orElseThrow(NoSuchElementException::new);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}

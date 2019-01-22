package ex_01_Extracting_Cookies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String line;
        //INFO: to be valid input according to the task
        // if there is empty cookie header it will be after it with space
        // not just "Cookie:", but it will be "Cookie ".
        while (!"".equals(line = reader.readLine())) {
            if (line.split("\\s+")[0].equals("Cookie:")) {
                String cookieContents = line.split("\\s+", 2)[1];
                if (!cookieContents.isEmpty()) {
                    Arrays.stream(cookieContents.split("; "))
                            .forEach(x -> {
                                String[] kvp = x.split("=");
                                System.out.println(String.format("%s <-> %s", kvp[0], kvp[1]));
                            });
                } else {
                    System.out.println();
                }
                break;
            }
        }
    }
}

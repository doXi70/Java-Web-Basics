package fdmc.web.servlets;

import fdmc.util.HtmlReader;
import fdmc.web.models.entities.Cat;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@SuppressWarnings("unchecked")
@WebServlet("/cats/all")
public class AllCatsServlet extends HttpServlet {

    public static final String ALL_CATS_HTML_PATH =
            "D:\\SoftUni\\JavaWeb\\Java-Web-Basics\\04_Lecture\\02.Exercise\\src\\main\\resources\\views\\cat-all.html";

    private final HtmlReader reader;

    @Inject
    public AllCatsServlet(HtmlReader reader) {
        this.reader = reader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();

        List<Cat> cats = (List<Cat>) req.getSession().getAttribute("cats");
        String htmlContents;
        if (cats == null || cats.isEmpty()) {
            htmlContents = reader.readHtmlFile(ALL_CATS_HTML_PATH)
                    .replace("{{cats}}", "")
                    .replace("{{createSome}}", "There are no cats.<a href=\"/cats/create\">Create Some!</a>");

            writer.println(htmlContents);
        } else {
            htmlContents = reader.readHtmlFile(ALL_CATS_HTML_PATH)
                    .replace("{{cats}}", getAllCats(cats))
                    .replace("{{createSome}}", "");

            writer.println(htmlContents);
        }
    }

    private String getAllCats(List<Cat> cats) {
        StringBuilder sb = new StringBuilder();
        for (Cat cat : cats) {
            sb.append(String.format("<a href=\"/cats/profile?catName=%s\">%s</a>", cat.getName(), cat.getName()));
            sb.append("<br/>");
        }

        return sb.toString();
    }
}

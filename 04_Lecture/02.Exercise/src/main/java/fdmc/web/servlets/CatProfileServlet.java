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
@WebServlet("/cats/profile")
public class CatProfileServlet extends HttpServlet {

    public static final String CATS_PROFILE_HTML_PATH =
            "D:\\SoftUni\\JavaWeb\\Java-Web-Basics\\04_Lecture\\02.Exercise\\src\\main\\resources\\views\\cat-profile.html";

    private final HtmlReader htmlReader;

    @Inject
    public CatProfileServlet(HtmlReader htmlReader) {
        this.htmlReader = htmlReader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Cat cat = null;
        String catName = req.getParameter("catName");
        List<Cat> cats = (List<Cat>) req.getSession().getAttribute("cats");

        for (Cat kitty : cats) {
            if (kitty.getName().equals(catName)) {
                cat = kitty;
                break;
            }
        }

        PrintWriter writer = resp.getWriter();
        if (cat != null) {
            String htmlFileContent = this.htmlReader
                    .readHtmlFile(CATS_PROFILE_HTML_PATH)
                    .replace("{{catName}}", cat.getName())
                    .replace("{{catBreed}}", cat.getBreed())
                    .replace("{{catColor}}", cat.getColor())
                    .replace("{{catLegs}}", cat.getNumberOfLegs().toString());

            writer.println(htmlFileContent);
        } else {
            String backToCatsAll = "/cats/all";

            writer.println(String.format(
                    "<h1>Cat, with name - %s was not found.</h1><br><a href=\"%s\">Back</a>",
                    catName, backToCatsAll));
        }
    }
}

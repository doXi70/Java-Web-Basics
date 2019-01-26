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
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@WebServlet("/cats/create")
public class CreateCatServlet extends HttpServlet {

    public static final String CAT_CREATE_HTML_FILE_PATH =
            "D:\\SoftUni\\JavaWeb\\Java-Web-Basics\\04_Lecture\\02.Exercise\\src\\main\\resources\\views\\cat-create.html";

    private HtmlReader htmlReader;

    @Inject
    public CreateCatServlet(HtmlReader htmlReader) {
        this.htmlReader = htmlReader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        writer.println(this.htmlReader.readHtmlFile(CAT_CREATE_HTML_FILE_PATH));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String catName = req.getParameter("name");
        String catBreed = req.getParameter("breed");
        String catColor = req.getParameter("color");
        String catLegsNumber = req.getParameter("legs");

        Cat cat = new Cat(catName, catBreed, catColor, Integer.parseInt(catLegsNumber));

        if (req.getSession().getAttribute("cats") == null) {
            req.getSession().setAttribute("cats", new ArrayList<Cat>());
        }

        ((List<Cat>) req.getSession().getAttribute("cats")).add(cat);

        resp.sendRedirect(String.format("/cats/profile?catName=%s", catName));
    }
}

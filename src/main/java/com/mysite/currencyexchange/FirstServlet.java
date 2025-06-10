package com.mysite.currencyexchange;

import com.google.gson.Gson;
import com.mysite.currencyexchange.test.ConnectDB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {

    static
    {
        ConnectDB connectDB = new ConnectDB();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        var persons = List.of(
                new Person(1, "Anna"),
                new Person(2, "Max"),
                new Person(1, "Lisa")
        );

        String json = new Gson().toJson(persons);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

//        super.doGet(req, resp);
    }
}

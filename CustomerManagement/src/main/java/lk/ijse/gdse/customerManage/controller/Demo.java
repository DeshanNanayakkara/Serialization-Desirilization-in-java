package lk.ijse.gdse.customerManage.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.customerManage.dto.CustomerDTO;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/demo")
public class Demo extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println(getServletContext().getInitParameter("myparam"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Jsonb jsonb = JsonbBuilder.create();
        ArrayList<CustomerDTO> customerList = jsonb.fromJson(req.getReader(),
                new ArrayList<CustomerDTO>() {}.getClass().getGenericSuperclass());

        customerList.forEach(System.out::println);
    }
}

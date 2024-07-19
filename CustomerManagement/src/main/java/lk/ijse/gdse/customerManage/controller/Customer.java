package lk.ijse.gdse.customerManage.controller;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse.customerManage.dto.CustomerDTO;
import lk.ijse.gdse.customerManage.util.Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet(urlPatterns = "/customer")
public class Customer extends HttpServlet {

    Connection connection;
    public static String SAVE_CUSTOMER = "Insert into customers (id,name,email,address) values(?,?,?,?)";
    public static String GET_CUSTOMER = "Select * from customers where id = ?";
    public static String GET_ALL = "Select * from customers";
    public static String UPDATE_CUSTOMER = "Update customers set name=?, email=?, address=? where id=?";
    public static String DELETE_CUSTOMER = "Delete from customers where id=?";

    @Override
    public void init() throws ServletException {
        try {
            var dbClass = getServletContext().getInitParameter("db-class");
            var dbUrl = getServletContext().getInitParameter("dburl");
            var dbUsername = getServletContext().getInitParameter("db-username");
            var dbPassword = getServletContext().getInitParameter("db-password");
            Class.forName(dbClass);
            this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            customer.setId(Util.idGenerator());
            System.out.println(customer);

            var ps = connection.prepareStatement(SAVE_CUSTOMER);
            ps.setString(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            if (ps.executeUpdate() != 0) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("Customer saved successfully!");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("Failed to save customer!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            var customerDto = new CustomerDTO();
            Jsonb jsonb = JsonbBuilder.create();
            var customerId = req.getParameter("customerId");
            var ps = connection.prepareStatement(GET_CUSTOMER);
            ps.setString(1, customerId);
            var resultSet = ps.executeQuery();
            while (resultSet.next()) {
                customerDto.setId(resultSet.getString("id"));
                customerDto.setName(resultSet.getString("name"));
                customerDto.setEmail(resultSet.getString("email"));
                customerDto.setAddress(resultSet.getString("address"));
            }
            resp.setContentType("application/json");
            jsonb.toJson(customerDto, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            var customerId = req.getParameter("customerId");
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customer = jsonb.fromJson(req.getReader(), CustomerDTO.class);
            PreparedStatement ps = connection.prepareStatement(UPDATE_CUSTOMER);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customerId);
            if (ps.executeUpdate() != 0) {
                writer.write("Customer updated successfully!");
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                writer.write("Something went wrong!");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (var writer = resp.getWriter()) {
            var customerId = req.getParameter("customerId");
            PreparedStatement ps = connection.prepareStatement(DELETE_CUSTOMER);
            ps.setString(1, customerId);
            if (ps.executeUpdate() != 0) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

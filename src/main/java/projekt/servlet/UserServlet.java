package projekt.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import projekt.dao.UserDAO;
import projekt.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet
{
    private UserDAO userDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.split("/").length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Wymagany jest identyfikator usera w ścieżce URL.");
            return;
        }

        try {
            int userId = Integer.parseInt(pathInfo.split("/")[1]);
            Users users = objectMapper.readValue(req.getInputStream(), Users.class);
            users.setId(userId);

            boolean updated = userDAO.updateUser(users);
            if (updated) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Pomyślnie zaktualizowano dane usera.");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("User o id " + userId + " nie został znaleziony.");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Błąd serwera: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.split("/").length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Wymagany jest identyfikator użytkownika w ścieżce URL.");
            return;
        }

        try {
            int userId = Integer.parseInt(pathInfo.split("/")[1]);

            // Pobierz użytkownika po ID z DAO
            Users user = userDAO.getUserById(userId);

            if (user != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(objectMapper.writeValueAsString(user));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("User o id " + userId + " nie został znaleziony.");
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Błąd serwera: " + e.getMessage());
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Identyfikator użytkownika musi być liczbą.");
        }
    }
}

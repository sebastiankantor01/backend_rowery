package projekt.servlet;

import projekt.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/api/users/*")
public class UserServlet extends HttpServlet
{
    private UserDAO userDAO;

}

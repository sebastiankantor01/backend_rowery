package projekt.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import projekt.dao.NoticeBoardDAO;
import projekt.entity.Bike;
import projekt.entity.NoticeBoard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/api/noticeboard/*")
public class NoticeBoardServlet extends HttpServlet
{
    private NoticeBoardDAO noticeBoardDAO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {   // Przeglądanie całej tablicy ogłoszeń
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Pobranie całej tablicy ogłoszeń
                List<NoticeBoard> board = noticeBoardDAO.getAllNoticeBoardsSorted();
                resp.setContentType("application/json");
                resp.getWriter().write(objectMapper.writeValueAsString(board));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Błąd serwera: " + e.getMessage());
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NoticeBoard noticeBoard = objectMapper.readValue(req.getInputStream(), NoticeBoard.class); // Odczyt JSON z żądania
            noticeBoardDAO.addNoticeBoard(noticeBoard); // Dodanie ogłoszenia przez DAO
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Ogłoszenie dodane ");
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Błąd serwera: " + e.getMessage());
        }
    }
}

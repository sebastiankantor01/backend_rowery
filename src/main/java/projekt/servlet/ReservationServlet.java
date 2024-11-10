package projekt.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import projekt.dao.ReservationDAO;
import projekt.entity.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/reservation/*")
public class ReservationServlet {
    private ReservationDAO reservationDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            // Deserialize request JSON to Reservation object
            Reservation reservation = objectMapper.readValue(request.getReader(), Reservation.class);
            reservationDAO.addReservation(reservation);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(objectMapper.writeValueAsString("Reservation created successfully"));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString("Error creating reservation: " + e.getMessage()));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.startsWith("/user/")) {
                // Get reservations by user
                int userId = Integer.parseInt(pathInfo.split("/")[2]);
                List<Reservation> reservations = reservationDAO.getReservationByUser(userId);
                response.getWriter().write(objectMapper.writeValueAsString(reservations));
            } else if (pathInfo != null && pathInfo.startsWith("/bike/")) {
                // Get reservation dates by bike
                int bikeId = Integer.parseInt(pathInfo.split("/")[2]);
                List<LocalDate[]> reservationDates = reservationDAO.getReservationDateByBike(bikeId);
                response.getWriter().write(objectMapper.writeValueAsString(reservationDates));
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(objectMapper.writeValueAsString("Invalid request"));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(objectMapper.writeValueAsString("Error retrieving data: " + e.getMessage()));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(objectMapper.writeValueAsString("Invalid ID format"));
        }
    }
}

package projekt.dao;

import projekt.entity.Reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReservationDAO {
    private final Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation (user_id, bike_id, start_date, end_date, total_price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getBikeId());
            stmt.setDate(3, Date.valueOf(reservation.getStartDate()));
            stmt.setDate(4, Date.valueOf(reservation.getEndDate()));
            stmt.setDouble(5, reservation.getTotalPrice());
            stmt.executeUpdate();
        }
    }
}


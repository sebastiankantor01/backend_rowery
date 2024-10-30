package projekt.dao;

import projekt.entity.Bike;
import projekt.entity.BikeSize;
import projekt.entity.BikeType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BikeDAO {
    private final Connection connection;

    public BikeDAO(Connection connection) {
        this.connection = connection;
    }

    public void addBike(Bike bike) throws SQLException {
        String sql = "INSERT INTO bike (name, type, size, available, price_per_day, description, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bike.getName());
            stmt.setString(2, bike.getType().name());
            stmt.setString(3, bike.getSize().name());
            stmt.setBoolean(4, bike.isAvailable());
            stmt.setDouble(5, bike.getPricePerDay());
            stmt.setString(6, bike.getDescription());
            stmt.setString(7, bike.getImageUrl());
            stmt.executeUpdate();
        }
    }

    public Bike getBikeById(int id) throws SQLException {
        String sql = "SELECT * FROM bike WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Bike(
                        rs.getInt("id"),
                        rs.getString("name"),
                        BikeType.valueOf(rs.getString("type")),
                        BikeSize.valueOf(rs.getString("size")),
                        rs.getBoolean("available"),
                        rs.getDouble("price_per_day"),
                        rs.getString("description"),
                        rs.getString("image_url")
                );
            }
        }
        return null;
    }
}

package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoArbitros extends DaoBase {

    public ArrayList<Arbitro> listarArbitros() {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM Arbitro";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()){
                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt(1));
                arbitro.setNombre(rs.getString(2));
                arbitro.setPais(rs.getString(3));
                arbitros.add(arbitro);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public void crearArbitro(Arbitro arbitro) {
        String sql = "INSERT INTO Arbitro (nombre, pais) VALUES (?,?)";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,arbitro.getNombre());
            ps.setString(2,arbitro.getPais());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Arbitro> busquedaPais(String pais) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM Arbitro WHERE pais LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + pais + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                    arbitros.add(arbitro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public ArrayList<Arbitro> busquedaNombre(String nombre) {
        ArrayList<Arbitro> arbitros = new ArrayList<>();
        String sql = "SELECT * FROM Arbitro WHERE nombre LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Arbitro arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                    arbitros.add(arbitro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public Arbitro buscarArbitro(int id) {
        Arbitro arbitro = null;

        String sql = "SELECT * FROM Arbitro WHERE idArbitro = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt(1));
                    arbitro.setPais(rs.getString(2));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return arbitro;
    }

    public Arbitro buscarArbitroPorNombre (String nombre) {
        Arbitro arbitro = null;
        String sql = "SELECT * FROM Arbitro WHERE nombre = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    arbitro = new Arbitro();
                    arbitro.setIdArbitro(rs.getInt("idArbitro"));
                    arbitro.setNombre(rs.getString("nombre"));
                    arbitro.setPais(rs.getString("pais"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitro;
    }

    public void borrarArbitro(int id) {
        String sql = "DELETE FROM Arbitro WHERE idArbitro = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }
}

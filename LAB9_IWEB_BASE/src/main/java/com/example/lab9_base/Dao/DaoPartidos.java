package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase{
    public ArrayList<Partido> listaDePartidos() {

        ArrayList<Partido> partidos = new ArrayList<>();

        String sql = "select p.idPartido, p.fecha, p.numeroJornada, " +
                "sl.nombre AS nombreSeleccionLocal, sv.nombre AS nombreSeleccionVisita, "+
                "e.nombre AS nombreEstadio, a.nombre AS nombreArbitro " +
                "FROM Partido p " +
                "JOIN Seleccion sl ON p.seleccionLocal = sl.idSeleccion " +
                "JOIN Seleccion sv ON p.seleccionVisitante = sv.idSeleccion " +
                "JOIN Estadio e ON sl.estadio_idEstadio = e.idEstadio " +
                "JOIN Arbitro a ON p.arbitro = a.idArbitro";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()){
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setFecha(rs.getString("fecha"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));

                Seleccion selLocal = new Seleccion();
                selLocal.setNombre(rs.getString("nombreSeleccionLocal"));

                Seleccion selVisita = new Seleccion();
                selVisita.setNombre(rs.getString("nombreSeleccionVisita"));

                Estadio estadio = new Estadio();
                estadio.setNombre(rs.getString("nombreEstadio"));
                selLocal.setEstadio(estadio);

                partido.setSeleccionLocal(selLocal);
                partido.setSeleccionVisitante(selVisita);

                Arbitro arbitro = new Arbitro();
                arbitro.setNombre(rs.getString("nombreArbitro"));

                partido.setArbitro(arbitro);

                 partidos.add(partido);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return partidos;
    }

    public void crearPartido(Partido partido) {
        String sql = "INSERT INTO Partido (numeroJornada, fecha, seleccionLocal, seleccionVisitante, arbitro) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establece los valores en el PreparedStatement
            pstmt.setInt(1, partido.getNumeroJornada());

            // Convertir fecha a java.sql.Date si está en formato String
            java.sql.Date fechaSql = java.sql.Date.valueOf(partido.getFecha());
            pstmt.setDate(2, fechaSql);

            pstmt.setInt(3, partido.getSeleccionLocal().getIdSeleccion());
            pstmt.setInt(4, partido.getSeleccionVisitante().getIdSeleccion());
            pstmt.setInt(5, partido.getArbitro().getIdArbitro());

            // Ejecutar la inserción
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Se creará métodos para los comboBox

    public ArrayList<Seleccion> listaSelecciones(){
        ArrayList<Seleccion> selecciones = new ArrayList<>();
        String sql = "SELECT * FROM Seleccion";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()){
                Seleccion seleccion = new Seleccion();
                seleccion.setIdSeleccion(rs.getInt(1));
                seleccion.setNombre(rs.getString(2));
                selecciones.add(seleccion);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return selecciones;
    }

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
                arbitros.add(arbitro);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return arbitros;
    }

    public boolean verSiExistePartido(int idSeleccionLocal, int idSeleccionVisitante) {
        String sql = "SELECT COUNT(*) FROM Partido WHERE seleccionLocal = ? AND seleccionVisitante = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idSeleccionLocal);
            pstmt.setInt(2, idSeleccionVisitante);


            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Devuelve true si ya existe un partido con esas condiciones
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

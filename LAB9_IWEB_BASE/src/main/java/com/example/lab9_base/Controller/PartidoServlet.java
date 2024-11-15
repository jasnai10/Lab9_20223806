package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Dao.DaoPartidos;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Con esto estoy garantizando que se pueda recibir caracteres extraños en los formularios
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        DaoPartidos dao = new DaoPartidos();


        switch (action) {

            case "guardar":
                String jornadaStr = request.getParameter("jornada");
                String fechaStr = request.getParameter("fecha");
                String paisLocalStr = request.getParameter("local");
                String paisVisitaStr = request.getParameter("visitante");
                String arbitroStr = request.getParameter("arbitro");

                // Validamos que todos los campos estén ccompletos
                if (jornadaStr==null || jornadaStr.isEmpty() || fechaStr==null || fechaStr.isEmpty() || paisLocalStr==null ||
                paisLocalStr.isEmpty() || paisVisitaStr==null || paisVisitaStr.isEmpty() || arbitroStr==null || arbitroStr.isEmpty()) {
                    request.setAttribute("error", "Los campos no están completos");
                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }

                // Validamos que los países sean disntintos
                if (paisVisitaStr.equals(paisLocalStr)) {

                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }

                // Conversión de valores y verificación de duplicado
                int numJornada = Integer.parseInt(jornadaStr);
                int idLocal = Integer.parseInt(paisLocalStr);
                int idVisitante = Integer.parseInt(paisVisitaStr);
                int idArbitro = Integer.parseInt(arbitroStr);

                if (dao.verSiExistePartido(idLocal, idVisitante)) {

                    response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=crear");
                    return;
                }

                // Guardar el partido si todo está bien
                Partido partido = new Partido();
                partido.setNumeroJornada(numJornada);
                partido.setFecha(fechaStr);
                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(idLocal);
                partido.setSeleccionLocal(seleccionLocal);
                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(idVisitante);
                partido.setSeleccionVisitante(seleccionVisitante);
                Arbitro arbitroObj = new Arbitro();
                arbitroObj.setIdArbitro(idArbitro);
                partido.setArbitro(arbitroObj);

                dao.crearPartido(partido);

                response.sendRedirect(request.getContextPath() + "/PartidoServlet");

                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        DaoPartidos daoPartido = new DaoPartidos();

        RequestDispatcher view;

        switch (action) {
            case "lista":
                ArrayList<Partido> partidos = daoPartido.listaDePartidos();
                request.setAttribute("partidos", partidos);
                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                ArrayList<Seleccion> selecciones = daoPartido.listaSelecciones();
                ArrayList<Arbitro> arbitros = daoPartido.listarArbitros();
                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);
                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);
                break;
        }

    }
}

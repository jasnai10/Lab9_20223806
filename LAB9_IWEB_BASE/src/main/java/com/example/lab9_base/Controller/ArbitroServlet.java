package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        DaoArbitros dao = new DaoArbitros();


        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        switch (action) {

            case "buscar":
                String tipoBusqueda = request.getParameter("tipo"); // puede ser "nombre" o "pais"
                String valorBusqueda = request.getParameter("buscar");

                ArrayList<Arbitro> resultadosBusqueda = new ArrayList<>();
                if ("nombre".equals(tipoBusqueda)) {
                    resultadosBusqueda = dao.busquedaNombre(valorBusqueda);
                } else if ("pais".equals(tipoBusqueda)) {
                    resultadosBusqueda = dao.busquedaPais(valorBusqueda);
                }

                // Asigna resultados y opciones de búsqueda para que se muestren en el JSP
                request.setAttribute("arbitros", resultadosBusqueda);
                request.setAttribute("opciones", opciones);


                // Reenvía a list.jsp con los resultados de la búsqueda
                RequestDispatcher view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "guardar":
                // Practicando
                // Primero, recopimos los datos extraídos del jsp
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                // Validamos si los datos están completos
                if (nombre==null || nombre.isEmpty() || pais==null || pais.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=crear");
                    return;
                }
                Arbitro arbitroExistente = dao.buscarArbitroPorNombre(nombre);
                if (arbitroExistente == null) {
                    // Si no existe, se crea el árbitro
                    Arbitro nuevoArbitro = new Arbitro();
                    nuevoArbitro.setNombre(nombre);
                    nuevoArbitro.setPais(pais);
                    dao.crearArbitro(nuevoArbitro);

                    // Redirigir a la lista después de crear
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                } else {
                    // Si ya existe, redirigir al formulario
                    response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=crear");
                }
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        DaoArbitros daoArbitros = new DaoArbitros();
        RequestDispatcher view;

        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        switch (action) {
            case "lista":
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();
                request.setAttribute("arbitros", arbitros);
                request.setAttribute("opciones", opciones);
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "crear":
                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;

            case "borrar":
                String idArbitro = request.getParameter("id");
                int ArbID = Integer.parseInt(idArbitro);
                if (daoArbitros.buscarArbitro(ArbID) != null) {
                    daoArbitros.borrarArbitro(ArbID);
                }
                response.sendRedirect(request.getContextPath() + "/ArbitroServlet");
                break;
        }
    }
}

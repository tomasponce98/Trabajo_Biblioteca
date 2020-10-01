package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.GeneroLogic;
import logic.PoliticaPrestamoLogic;
import model.PoliticaPrestamo;

/**
 * Servlet implementation class ABMPoliticaPrestamoServlet
 */
@WebServlet("/ABMPoliticaPrestamoServlet")
public class ABMPoliticaPrestamoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
     * Default constructor. 
     */
    public ABMPoliticaPrestamoServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/*Servlet.VerificarSesionYUsuario(request, response, Usuario.tipoUsuario.Administrador);*/
		GeneroLogic gl = new GeneroLogic();
		try {
			request.setAttribute("ListaPoliticasPrestamos", gl.getAll());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			response.getWriter().println(e.getMessage());
		}
		request.getRequestDispatcher("WEB-INF/ABMPoliticaPrestamo.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("action-type").equals("agregar")) {	
			PoliticaPrestamo pp=new PoliticaPrestamo();
			pp.setCantMaxLibrosPend(Integer.parseInt(request.getParameter("cant_max_libros_pend")));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String dateInString = request.getParameter("fecha_politica_prestamo");
			Date date;
			try {
				date = formatter.parse(dateInString);
				pp.setFechaPoliticaPrestamo(date);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			PoliticaPrestamoLogic ppl=new PoliticaPrestamoLogic();
			try {
				ppl.insert(pp);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.getWriter().println(e.getMessage());
			}
		}
		else if (request.getParameter("action-type").equals("eliminar")) {	
			PoliticaPrestamo pp=new PoliticaPrestamo();
			pp.setId(Integer.parseInt(request.getParameter("id")));
			PoliticaPrestamoLogic ppl=new PoliticaPrestamoLogic();
			try {
				ppl.delete(pp);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.getWriter().println(e.getMessage());
			}
		}
		else if (request.getParameter("action-type").equals("editar")) {	
			PoliticaPrestamo pp=new PoliticaPrestamo();
			pp.setId(Integer.parseInt(request.getParameter("id")));
			pp.setCantMaxLibrosPend(Integer.parseInt(request.getParameter("cant_max_libros_pend")));
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String dateInString = request.getParameter("fecha_politica_prestamo");
			Date date;
			try {
				date = formatter.parse(dateInString);
				pp.setFechaPoliticaPrestamo(date);

			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PoliticaPrestamoLogic ppl=new PoliticaPrestamoLogic();
			try {
				ppl.update(pp);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				response.getWriter().println(e.getMessage());
			}
		}
		this.doGet(request, response);
	}

}

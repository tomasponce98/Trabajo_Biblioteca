package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logic.SocioLogic;
import model.Socio;
import model.Usuario;

/**
 * Servlet implementation class DatosSocioServlet
 */
@WebServlet("/DatosSocioServlet")
public class DatosSocioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatosSocioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Servlet.VerificarSesionYUsuario(request, response, Usuario.tipoUsuario.Socio)) 
		request.getRequestDispatcher("WEB-INF/DatosSocio.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Servlet.VerificarSesionYUsuario(request, response, Usuario.tipoUsuario.Socio)) {
			if (request.getParameter("action-type").equals("editar")) {	
				Socio socio = new Socio();
				socio.setId(Integer.parseInt(request.getParameter("id")));
				socio.setNombre(request.getParameter("nombre"));
				socio.setApellido(request.getParameter("apellido"));
				socio.setEmail(request.getParameter("email"));
				socio.setDni(Integer.parseInt(request.getParameter("dni")));
				socio.setDomicilio(request.getParameter("domicilio"));
				socio.setTelefono(request.getParameter("telefono"));
				socio.setEstado(Boolean.parseBoolean(request.getParameter("estado")));
				SocioLogic sl = new SocioLogic();
				try {
					sl.update(socio);
				} catch (SQLException e) {
					response.getWriter().println(e.getMessage());
				}
			}
			this.doGet(request, response);
		}
	}

}
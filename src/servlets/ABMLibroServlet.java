package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import logic.GeneroLogic;
import logic.LibroLogic;
import model.Libro;
import model.Usuario;

/**
 * Servlet implementation class ABMLibroServlet
 */
@WebServlet("/ABMLibroServlet")
public class ABMLibroServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    private static final String AWS_ACCESS_KEY_ID = "";
    private static final String AWS_SECRET_ACCESS_KEY = "";
    private static final String BUCKET_NAME = "imagenesbibliotecabucket";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ABMLibroServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Servlet.VerificarSesionYUsuario(request, response, Usuario.tipoUsuario.Administrador)) {
			//Las l�neas sobre cache se ponen intentando que las mismas se actualicen en caso de haber cambios en el bucket de S3
			// Set standard HTTP/1.1 no-cache headers.
			response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

			// Set standard HTTP/1.0 no-cache header.
			response.setHeader("Pragma", "no-cache");
			LibroLogic ll = new LibroLogic();
			GeneroLogic gl = new GeneroLogic();
			try {
				request.setAttribute("ListaLibros", ll.getAll());
				request.setAttribute("ListaGeneros", gl.getAll());
			} catch (SQLException e) {
				Servlet.log(Level.SEVERE, e, request);
				request.setAttribute("mensaje", "No se pudieron obtener los libros");
			}
			request.setAttribute("JSP", "ABMLibro");
			request.getRequestDispatcher("WEB-INF/Administrador.jsp").forward(request, response);		
			}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Servlet.VerificarSesionYUsuario(request, response, Usuario.tipoUsuario.Administrador)) {
			try {
			if (request.getParameter("action-type")!=null) {
				if (request.getParameter("action-type").equals("agregar")) {	
					if (ValidarDatos(request)) {
						Libro libro=new Libro();
						libro.setTitulo(request.getParameter("titulo"));
						libro.setAutor(request.getParameter("autor"));
						libro.setFechaEdicion(LocalDate.parse(request.getParameter("fecha-edicion")));
						libro.setNroEdicion(request.getParameter("numero-edicion"));
						libro.setCantEjemplares(Integer.parseInt(request.getParameter("cant-ejemplares")));
						int idGenero = (Integer.parseInt(request.getParameter("genero")));
						GeneroLogic gl = new GeneroLogic();
						try {
							libro.setGenero(gl.getOne(idGenero));
						} catch (SQLException e) {
							Servlet.log(Level.SEVERE, e, request);
							request.setAttribute("mensaje", "No se pudo encontrar el g�nero indicado");
						}
						LibroLogic ll = new LibroLogic();
						if (request.getParameter("mensaje") == null) {
							try {
								ll.insert(libro);
								request.setAttribute("clase-mensaje", "class=\"alert alert-success alert-dismissible fade show\"");
								request.setAttribute("mensaje", "Libro agregado correctamente");
							} catch (SQLException e) {
								Servlet.log(Level.SEVERE, e, request);
								request.setAttribute("mensaje", "No se pudo agregar un libro");
							}
						}
					}
				}
				else if (request.getParameter("action-type").equals("eliminar")) {	
					Libro libro=null;
					LibroLogic ll=new LibroLogic();
					System.setProperty("aws.accessKeyId", AWS_ACCESS_KEY_ID);
		            System.setProperty("aws.secretKey", AWS_SECRET_ACCESS_KEY);
					final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.SA_EAST_1).build();
					try {
						libro = ll.getOne(Integer.parseInt(request.getParameter("id"))); 
						if(libro!=null) {
							String aFileName = new String(request.getParameter("image").getBytes( 
							        "iso8859-1"), "gbk"); 
							try {
							    s3.deleteObject(BUCKET_NAME, aFileName);
							    request.setAttribute("clase-mensaje", "class=\"alert alert-success alert-dismissible fade show\"");
								request.setAttribute("mensaje", "Imagen eliminada.");
							} catch (AmazonServiceException ase) {
								Servlet.log(Level.SEVERE, ase, request);
								System.out.println(aFileName + ":error:" + ase.getMessage());
								request.setAttribute("mensaje", "No se pudo eliminar la imagen del libro.");
							}
							ll.delete(libro);
							request.setAttribute("clase-mensaje1", "class=\"alert alert-success alert-dismissible fade show\"");
							request.setAttribute("mensaje1", "Libro eliminado correctamente");
						}
						else
						{
							request.setAttribute("clase-mensaje", "class=\"alert alert-danger alert-dismissible fade show\"");
							request.setAttribute("mensaje", "Id de libro invalida");
						}
					
					} 
					catch (NumberFormatException e) {
						request.setAttribute("clase-mensaje", "class=\"alert alert-danger alert-dismissible fade show\"");
						request.setAttribute("mensaje", "No se pudo realizar la operaci�n debido a un error en los datos suministrados");
					}
					catch (SQLException e) {
						Servlet.log(Level.SEVERE, e, request);
						request.setAttribute("mensaje", "No se pudo eliminar un libro");
					}
				}
				else if (request.getParameter("action-type").equals("editar")) {	
					if (ValidarDatos(request)) {
						LibroLogic ll=new LibroLogic();
						Libro libro;
						try {
							libro = ll.getOne(Integer.parseInt(request.getParameter("id")));
							if(libro!=null) {
								libro.setId(Integer.parseInt(request.getParameter("id")));
								libro.setTitulo(request.getParameter("titulo"));
								libro.setAutor(request.getParameter("autor"));
								libro.setFechaEdicion(LocalDate.parse(request.getParameter("fecha-edicion")));
								libro.setNroEdicion(request.getParameter("numero-edicion"));
								int idGenero = (Integer.parseInt(request.getParameter("genero")));
								GeneroLogic gl = new GeneroLogic();
								try {
									libro.setGenero(gl.getOne(idGenero));
								} catch (SQLException e) {
									Servlet.log(Level.SEVERE, e, request);
									request.setAttribute("mensaje", "No se pudo encontrar el g�nero indicado");
								}
								ll.update(libro);
								request.setAttribute("clase-mensaje", "class=\"alert alert-success alert-dismissible fade show\"");
								request.setAttribute("mensaje", "Libro actualizado correctamente");
							}
							else {
								request.setAttribute("clase-mensaje", "class=\"alert alert-danger alert-dismissible fade show\"");
								request.setAttribute("mensaje", "Id de libro inv�lida");
							}
						}
						 catch (NumberFormatException e) {
							request.setAttribute("clase-mensaje", "class=\"alert alert-danger alert-dismissible fade show\"");
							request.setAttribute("mensaje", "No se pudo realizar la operaci�n debido a un error en los datos suministrados");
						}
						catch (SQLException e) {
							Servlet.log(Level.SEVERE, e, request);
							request.setAttribute("mensaje", "No se pudo actualizar un libro");
						}
					}
				}
			}
			} catch (Exception e) {
				Servlet.log(Level.SEVERE,e, request);
				request.setAttribute("mensaje", "Ha ocurrido un error durante la ejecuci�n de la operaci�n");
			}
			this.doGet(request, response);
		}
	}
	
	private static Boolean ValidarDatos (HttpServletRequest request) {
		if (Servlet.parameterNotNullOrBlank(request.getParameter("titulo")) && Servlet.parameterNotNullOrBlank(request.getParameter("autor")) && 
				Servlet.parameterNotNullOrBlank(request.getParameter("fecha-edicion")) && Servlet.parameterNotNullOrBlank(request.getParameter("numero-edicion"))&&
				Servlet.parameterNotNullOrBlank(request.getParameter("genero"))&& 
				(Servlet.parameterNotNullOrBlank(request.getParameter("cant-ejemplares")) || (request.getParameter("action-type").equals("editar")))) {
				try {
					Integer.parseInt(request.getParameter("numero-edicion"));
					Integer.parseInt(request.getParameter("genero"));
					LocalDate.parse(request.getParameter("fecha-edicion"));
					if (request.getParameter("action-type").equals("agregar")) {
						Integer.parseInt(request.getParameter("cant-ejemplares"));
					}
					return true;
				}
				catch (NumberFormatException e) {
					request.setAttribute("mensaje", "Por favor, ingrese el nro de edici�n y cantidad de ejemplares en formato numerico, sin puntos ni smbolos y seleccione un genero valido.");
					return false;
				}
				catch (DateTimeParseException e) {
					request.setAttribute("mensaje", "El formato de fecha ingresado es inv�lido");
					return false;
				}
			
		
		}
		else {
			request.setAttribute("mensaje", "Campos incompletos.");
			return false;
		}
	}
	
}
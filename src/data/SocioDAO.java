package data;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Genero;
import model.Libro;
import model.Socio;
import model.Usuario;
public class SocioDAO extends BaseDAO implements IBaseDAO<Socio> {
	
	public Socio mapearSocio(ResultSet rs) throws SQLException {
		Socio s = new Socio();
		s.setId(rs.getInt("id_socio"));
		s.setNombre(rs.getString("nombre"));
		s.setApellido(rs.getString("apellido"));
		s.setEmail(rs.getString("email"));
		s.setDomicilio(rs.getString("domicilio"));
		s.setTelefono(rs.getString("telefono"));
		s.setDni(rs.getInt("dni"));
		s.setEstado(rs.getBoolean("estado"));
		Usuario usuario=new Usuario();
		usuario.setId(rs.getInt("id_usuario"));
		s.setUsuario(usuario);
		
		return s;
	}
	
	public ArrayList<Socio> getAll() throws SQLException {
		ArrayList<Socio> socios = new ArrayList<Socio>();
		Statement stm = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery("SELECT * FROM socios");
			while (rs.next()) {
				socios.add(this.mapearSocio(rs));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			this.closeConnection(stm, rs);
		}
		return socios;
	}
	
	public Socio getOne(int id) throws SQLException {
		Socio soc = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("SELECT * FROM socios WHERE id_socio = ?");
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				soc = this.mapearSocio(rs);
			}
		}
		catch (SQLException e){
			e.printStackTrace();
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
		return soc;
	}
	
	public void insert(Socio soc) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("INSERT INTO socios(id_socio,nombre,apellido,email,domicilio,"
					+ "telefono,dni,estado,id_usuario)"
					+ " VALUES(?,?,?,?,?,?,?,?,?)");
			
			pst.setInt(1, soc.getId());
			pst.setString(2,soc.getNombre());
			pst.setString(3, soc.getApellido());
			pst.setString(4, soc.getEmail());
			pst.setString(5, soc.getDomicilio());
			pst.setString(6, soc.getTelefono());
			pst.setInt(7, soc.getDni());
			pst.setBoolean(8, soc.getEstado());
			pst.setInt(9, soc.getUsuario().getId());
			pst.executeUpdate();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}
	
	public void update(Socio soc) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("UPDATE socios SET nombre= ?,apellido=?,email=?,domicilio=?,"
					+ "telefono=?,dni=?,estado=? "
					+ "WHERE id_socio = ?");
			
			
			pst.setString(1,soc.getNombre());
			pst.setString(2, soc.getApellido());
			pst.setString(3, soc.getEmail());
			pst.setString(4, soc.getDomicilio());
			pst.setString(5, soc.getTelefono());
			pst.setInt(6, soc.getDni());
			pst.setBoolean(7, soc.getEstado());
			pst.setInt(8, soc.getUsuario().getId());
			pst.setInt(9, soc.getId());

			pst.executeUpdate();
		}
		catch (SQLException e){
			e.printStackTrace();
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}

}

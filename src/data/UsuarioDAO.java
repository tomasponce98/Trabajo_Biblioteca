package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Usuario;
import model.Usuario.tipoUsuario;

public class UsuarioDAO extends BaseDAO implements IBaseDAO<Usuario> {

	public Usuario mapearUsuario(ResultSet rs) throws SQLException {
		Usuario u = new Usuario();
		u.setId(rs.getInt("id_usuario"));
		u.setNombreUsuario(rs.getString("nombre_usuario"));
		u.setPassword(rs.getString("password"));
		u.setEstado(rs.getBoolean("estado"));
		switch(rs.getInt("tipo")) {
		case 0:
			u.setTipo(tipoUsuario.Socio);
			break;
		case 1:
			u.setTipo(tipoUsuario.Bibliotecario);
			break;
		case 2:
			u.setTipo(tipoUsuario.Administrador);
		}
		return u;
	}
	
	public ArrayList<Usuario> getAll() throws SQLException {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		Statement stm = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery("SELECT * FROM usuarios");
			while (rs.next()) {
				usuarios.add(this.mapearUsuario(rs));
			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			this.closeConnection(stm, rs);
		}
		return usuarios;
	}
	
	public Usuario getOne(int id) throws SQLException {
		Usuario usu = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("SELECT * FROM usuarios WHERE id_usuario = ?");
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				usu = this.mapearUsuario(rs);
			}
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
		return usu;
	}
	
	public Usuario getOneBySocio(int idSocio) throws SQLException {
		Usuario usu = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("SELECT u.id_usuario,nombre_usuario,password,u.estado,tipo FROM socios s INNER JOIN usuarios u WHERE s.id_usuario=u.id_usuario AND id_socio=?");
			pst.setInt(1, idSocio);
			rs = pst.executeQuery();
			if (rs.next()) {
				usu = this.mapearUsuario(rs);
			}
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
		return usu;
	}
	
	public Usuario login(String username, String password) throws SQLException {
		Usuario usu = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ? AND password = ?");
			pst.setString(1, username);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if (rs.next()) {
				usu = this.mapearUsuario(rs);
			}
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
		return usu;
	}
	
	public void insert(Usuario usu) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("INSERT INTO usuarios(nombre_usuario,password,estado,tipo)"
					+ " VALUES(?,?,?,?)");
			pst.setString(1, usu.getNombreUsuario());
			pst.setString(2, usu.getPassword());
			pst.setBoolean(3, usu.getEstado());
			switch(usu.getTipo())
			{
			case Socio:
				pst.setInt(4, 0);
				break;
			case Bibliotecario:
				pst.setInt(4, 1);
				break;
			case Administrador:
				pst.setInt(4, 2);
				break;
			}
			pst.executeUpdate();
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}
	
	public Usuario insertAndReturn(Usuario usu) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("INSERT INTO usuarios(nombre_usuario,password,estado,tipo)"
					+ " VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, usu.getNombreUsuario());
			pst.setString(2, usu.getPassword());
			pst.setBoolean(3, usu.getEstado());
			switch(usu.getTipo())
			{
			case Socio:
				pst.setInt(4, 0);
				break;
			case Bibliotecario:
				pst.setInt(4, 1);
				break;
			case Administrador:
				pst.setInt(4, 2);
				break;
			}
			pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				usu.setId(rs.getInt(1));
			}
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
		return usu;
	}
	
	public void update(Usuario usu) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("UPDATE usuarios SET nombre_usuario=?,password=?,estado=?,tipo=?"
					+ " WHERE id_usuario = ?");
			
			pst.setString(1, usu.getNombreUsuario());
			pst.setString(2, usu.getPassword());
			pst.setBoolean(3, usu.getEstado());
			switch(usu.getTipo())
			{
			case Socio:
				pst.setInt(4, 0);
				break;
			case Bibliotecario:
				pst.setInt(4, 1);
				break;
			case Administrador:
				pst.setInt(4, 2);
				break;
			}
			pst.setInt(5, usu.getId());
			pst.executeUpdate();
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}
	
	public void delete(Usuario usu) throws SQLException {
		PreparedStatement pst = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?");
			pst.setInt(1, usu.getId());
			pst.executeUpdate();
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst);
		}
	}
	
	public boolean userAlreadyExists(String user) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			this.openConnection();
			pst = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre_usuario = ?");
			pst.setString(1, user);
			rs = pst.executeQuery();
			if (rs.next()) {
				return true;
			}
			else return false;
		}
		catch (SQLException e){
			throw e;
		}
		finally {
			this.closeConnection(pst, rs);
		}
	}
}
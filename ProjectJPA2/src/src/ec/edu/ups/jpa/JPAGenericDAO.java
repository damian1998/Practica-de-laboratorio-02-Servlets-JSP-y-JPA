package src.ec.edu.ups.jpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sound.midi.Soundbank;

import src.ec.edu.ups.dao.DAOFactory;
import src.ec.edu.ups.dao.GenericDAO;
import src.ec.edu.ups.dao.UsuarioDAO;
import src.ec.edu.ups.entidades.Contacto;
import src.ec.edu.ups.entidades.Telefono;
import src.ec.edu.ups.entidades.Usuario;

public class JPAGenericDAO<T, ID> implements GenericDAO<T, ID> {
	private Class<T> persistentClass;
	protected EntityManager em;

	public JPAGenericDAO(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
		this.em = Persistence.createEntityManagerFactory("ProjectJPA2").createEntityManager();
	}

	@Override
	public void create(T entity) {
		// System.out.println("INGRESANDO: /n");
		em.getTransaction().begin();
		try {
			em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println("INGRESANDO: /n");
			System.out.println(">>>> ERROR:JPAGenericDAO:create " + e);
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	@Override
	public T read(ID id) {
		System.out.println("Buscando...");
		return em.find(persistentClass, id);
	}

	@Override
	public void update(T entity) {
		System.out.println("Actualizando.....");
		em.getTransaction().begin();
		try {
			em.merge(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println(">>>> ERROR:JPAGenericDAO:update " + e);
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	// entity debe estar en estado de "Managed"
	@Override
	public void delete(T entity) {
		em.getTransaction().begin();
		try {
			em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println(">>>> ERROR:JPAGenericDAO:delete " + e);
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	@Override
	public void deleteByID(ID id) {
		T entity = this.read(id);
		if (entity != null)
			this.delete(entity);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> findAll() {
		em.getTransaction().begin();
		List<T> lista = null;
		try {
			javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
			cq.select(cq.from(persistentClass));
			lista = em.createQuery(cq).getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;

	}

	@Override
	public void createTable() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<T> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario buscar(String email, String contrasena) {    
		    Query nativeQuery = em.createNativeQuery("SELECT * FROM usuario WHERE correo = ? AND pwd =? ", Usuario.class);
	        nativeQuery.setParameter(1, email);
	        nativeQuery.setParameter(2, contrasena);
	         
	        return (Usuario) nativeQuery.getSingleResult();
	}
 
	@Override
	public List<Contacto> buscarCorreo(String correo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public List<Telefono> buscarCedula(String cedula) {
		System.out.println("Consulta Realizada...");
		Usuario user = new Usuario();
		UsuarioDAO usuario = DAOFactory.getFactory().getUsuarioDAO();
		List<Telefono> list = new ArrayList<Telefono>();
		Query nativeQuery = em.createNativeQuery("SELECT id, numero, operadora, tipo, usuario_cedula FROM usuario, telefono WHERE telefono.usuario_cedula=usuario.cedula and usuario.cedula= ?", Telefono.class);
		 nativeQuery.setParameter(1, cedula);
		//ResultSet rs = (ResultSet) nativeQuery;
		System.out.println("Consulta Realizada...");
		/*
		 * try { user=usuario.read(rs.getString("CEDULA"));
		 * System.out.println("Usuario:...."+user.getNombre()); while (rs.next()) {
		 * list.add(new Telefono(rs.getInt("ID"), rs.getString("NUMERO"),
		 * rs.getString("OPERADORA"),rs.getString("TIPO"))); } } catch (SQLException e)
		 * { System.out.println(">>>WARNING (JDBCTelefonoDAO:find): " + e.getMessage());
		 * }
		 */
		//return list;
		return (List<Telefono>)nativeQuery.getResultList();
	}

	
	
	
}
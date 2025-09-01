package sia;
import java.util.*;

// Contiene publicaciones
public class Proyecto {
	private String idProyecto;
	private String nombre; // nombre del proyecto
	private String descripcion; // breve descripcion
	private String subRamaCarrera;
	private double fondos; 
	private ArrayList<Publicacion> publicaciones;
	private ArrayList<Investigador> investigadores; 

	public Proyecto(String idProyecto, String nombre, String descripcion, double fondos, String subRamaCarrera) {
		this.idProyecto = idProyecto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.subRamaCarrera = subRamaCarrera;
		this.fondos = fondos;
		this.publicaciones = new ArrayList<>();
		this.investigadores = new ArrayList<>();
	}
	
	//Getters y setters
	public String getIdProyecto() {
		return idProyecto;
	}
	public void setIdProyecto(String idProyecto) {
		if(idProyecto != null) {
			this.idProyecto = idProyecto;
		}
		else {
			this.idProyecto = "";
		}
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		if(nombre != null) {
			this.nombre = nombre;
		}
		else {
			this.nombre = "";
		}
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		if(descripcion != null) {
			this.descripcion = descripcion;
		}
		else {
			this.descripcion = "";
		}
	}
	
	public String getSubRamaCarrera() {
		return subRamaCarrera;
	}
	public void setSubRamaCarrera(String subRamaCarrera) {
		if(subRamaCarrera != null) {
			this.subRamaCarrera = subRamaCarrera;
		}
		else {
			this.subRamaCarrera = "";
		}
		
	}
	
	public double getFondos() {
		return fondos;
	}
	public void setFondos(double fondos) {
		if(fondos != 0) {
			this.fondos = fondos;
		}
		else {
			this.fondos = 0;
		}
	}
	
	public ArrayList<Publicacion> getPublicaciones() {
		return publicaciones;
	}
	
	public ArrayList<Investigador> getInvestigadores() {
		return investigadores;
	}
	
	// Metodos para agregar publicaciones
	public boolean agregarPublicacion(Publicacion publicacion) {
		if(publicacion == null) return false;
		// evitar ids duplicados
		for(Publicacion p : this.publicaciones) {
			if(p.getIdPublicacion().equals(publicacion.getIdPublicacion())) {
				System.out.println("La publicación con ID: " + publicacion.getIdPublicacion() + " ya existe en el proyecto " + this.idProyecto);
				return false;
			}
		}
		this.publicaciones.add(publicacion);
		return true;
	}
	
	public boolean agregarPublicacion(String idPublicacion, String titulo, int año, String tipo) {
		Publicacion pub = new Publicacion(idPublicacion, titulo, año, tipo);
		return agregarPublicacion(pub);
	}
	
	//Metodos para agregar investigadores
	public boolean agregarInvestigador(Investigador investigador) {
		if(investigador == null) return false;
		//evita duplicados
		for(Investigador inv : this.investigadores) {
			if(inv.getIdInvestigador().equals(investigador.getIdInvestigador())) {
				System.out.println("El investigador con ID: " + investigador.getIdInvestigador() + " ya está en el proyecto " + this.idProyecto);
				return false;
			}
		}
		this.investigadores.add(investigador);
		return true;
	}
	
	public boolean agregarInvestigador(String idInvestigador, String nombre, String especialidad, String carrera) {
		Investigador inv = new Investigador(idInvestigador, nombre, especialidad, carrera);
		return agregarInvestigador(inv);
	}
	
	// buscar investigador por id
	public Investigador buscarInvestigador(String idInvestigador) {
		for(Investigador inv : this.investigadores) {
			if(inv.getIdInvestigador().equals(idInvestigador)) return inv;
		}
		return null;
	}
	
	// buscar publicacion por id
	public Publicacion buscarPublicacion(String idPublicacion) {
		for(Publicacion p : this.publicaciones) {
			if(p.getIdPublicacion().equals(idPublicacion)) return p;
		}
		return null;
	}
	
	// remover publicacion
	public boolean removerPublicacion(String idPublicacion) {
		Publicacion p = buscarPublicacion(idPublicacion);
		if(p == null) return false;
		return this.publicaciones.remove(p);
	}
	
	// remover investigador
	public boolean removerInvestigador(String idInvestigador) {
		Investigador inv = buscarInvestigador(idInvestigador);
		if(inv == null) return false;
		return this.investigadores.remove(inv);
	}

}

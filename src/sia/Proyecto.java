package sia;
import java.util.*;

// Contiene publicaciones
public class Proyecto {
	private String idProyecto;
	private String nombre; // nombre del proyecto
	private String descripcion; // breve descripcion
	private String subRamaCarrera ;
	private double fondos; 
	private ArrayList<Publicacion> publicaciones;
	private ArrayList<Investigador> investigadores; 

	public Proyecto(String idProyecto, String nombre, String descripcion, double fondos, String subRamaCarrera) {
		this.idProyecto = idProyecto;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.subRamaCarrera = subRamaCarrera ;
		this.fondos = fondos;
		this.publicaciones = new ArrayList<>();
		this.investigadores = new ArrayList<>();
	}
	
	// Manejo de publicaciones
	public void agregarPublicacion(Publicacion p) {
		publicaciones.add(p);
	}
	public ArrayList<Publicacion> getPublicaciones() {
		return publicaciones;
	}
	
	//Manejo de investigadores
	public void agregarInvestigador(Investigador inv) {
		investigadores.add(inv);
	}
	public ArrayList<Investigador> getInvestigadores() {
		return investigadores;
	}
	
	//Getters y setters
	public String getIdproyecto() {
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

}

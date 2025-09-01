package sia;
import java.util.*;

// administra proyectos con publicaciones
public class Sistema {
	private HashMap<String, Proyecto> proyectos;

	public Sistema() {
		this.proyectos = new HashMap<>();
		inicializarSistema();
	}
	
	public HashMap<String, Proyecto> getProyectos() {
		return this.proyectos;
	}
	
	// metodo para inicializar datos de prueba -> privado: No queremos que se tenga acceso a este metodo
	private void inicializarSistema() {
		// Ejemplo 1
		Proyecto p1 = new Proyecto("P-1001", "Sistema Solar Inteligente","Investigación en optimización de paneles solares" , 500000.0, "Energía Renovable");
		Investigador i1 = new Investigador("I-2001", "Juan Pérez", "Paneles Solares", "Ingenieria Eléctrica");
		Publicacion pub1 = new Publicacion("PUB-0019", "Modelado de paneles", 1991, "Artículo");
		
		p1.agregarInvestigador(i1);
		pub1.agregarAutor(i1);
		p1.agregarPublicacion(pub1);
		this.proyectos.put(p1.getIdProyecto(), p1);
		
		// Ejemplo 2
		Proyecto p2 = new Proyecto("P-3002", "Clasificación con Deep Learning","Aplicación de redes neuronales con imágenes médicas" , 7500000.0, "Inteligencia Artificial");
		Investigador i2 = new Investigador("I-6002", "María Gómez", "Deep Learning", "Ingenieria Informática");
		Publicacion pub2 = new Publicacion("PUB-3002", "Redes convolucionales para diagnóstico", 2021, "Tesis");
		
		p2.agregarInvestigador(i2);
		pub2.agregarAutor(i2);
		p2.agregarPublicacion(pub2);
		this.proyectos.put(p2.getIdProyecto(), p2);
	}
	
	// Operaciones sobre proyectos
	
	// Agregar proyecto (objeto)
	public boolean agregarProyecto(Proyecto p) {
		if(p == null) return false;
		String id = p.getIdProyecto();
		if(this.proyectos.containsKey(id)) {
			System.out.println("Ya existe un proyecto con ID: " + id);
			return false;
		}
		this.proyectos.put(id, p);
		return true;
	}
	
	// Agregar proyecto por parametros (sobrecarga)
	public boolean agregarProyecto(String idProyecto, String nombre, String descripcion, double fondos, String subRamaCarrera) {
		if(this.proyectos.containsKey(idProyecto)) {
			System.out.println("Ya existe un proyecto con ID: " + idProyecto);
			return false;
		}
		
		if(fondos <= 0) {
			System.out.println("El monto de fondos debe ser mayor a 0");
			return false;
		}
		
		Proyecto p = new Proyecto(idProyecto, nombre, descripcion, fondos, subRamaCarrera);
		this.proyectos.put(idProyecto, p);
		return true;
	}
	
	// Buscar proyecto por id
	public Proyecto buscarProyecto(String idProyecto) {
		return this.proyectos.get(idProyecto);
	}
	
	// Eliminar proyecto por id
	public boolean eliminarProyecto(String idProyecto) {
		if(!this.proyectos.containsKey(idProyecto)) return false;
		this.proyectos.remove(idProyecto);
		return true;
	}
	
	// Agregar publicacion a un proyecto (objeto)
	public boolean agregarPublicacion(String idProyecto, Publicacion publicacion) {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) {
			System.out.println("No existe el proyecto con ID: " + idProyecto);
			return false;
		}
		return p.agregarPublicacion(publicacion);
	}
	
	// Agregar publicacion a un proyecto por parametros (sobrecarga)
	public boolean agregarPublicacion(String idProyecto, String idPublicacion, String titulo, int año, String tipo) {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) {
			System.out.println("No existe proyecto con ID: " + idProyecto);
			return false;
		}
		Publicacion pub = new Publicacion(idPublicacion, titulo, año, tipo);
		return p.agregarPublicacion(pub);
	}
	
	// Agregar investigador a un proyecto (objeto)
	public boolean agregarInvestigador(String idProyecto, Investigador investigador) {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) {
			System.out.println("No existe el proyecto con ID: " + idProyecto);
			return false;
		}
		return p.agregarInvestigador(investigador);
	}
	
	// Agregar investigador a un proyecto por parametros (sobrecarga)
	public boolean agregarInvestigador(String idProyecto, String idInvestigador, String nombre, String especialidad, String carrera) {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) {
			System.out.println("No existe el proyecto con ID: " + idProyecto);
			return false;
		}
		if(idInvestigador == null || idInvestigador.isBlank() || nombre == null || nombre.isBlank() || 
		   especialidad == null || especialidad.isBlank() || carrera == null || carrera.isBlank()) {
			System.out.println("Todos los campos del investigador son obligatorios");
			return false;
		}
		
		Investigador inv = new Investigador(idInvestigador, nombre, especialidad, carrera);
		return p.agregarInvestigador(inv);
	}
	
	// Tamaño del sistema
	public int cantidadProyectos() {
		return this.proyectos.size();
	}
	
}

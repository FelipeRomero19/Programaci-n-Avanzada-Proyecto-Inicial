package sia;
import java.util.*;

import java.io.*;
import java.util.*;

import sia.persistence.PersistenceCSV;
import sia.exceptions.DuplicatedIdException;
import sia.exceptions.ProyectoNotFoundException;

// administra proyectos con publicaciones
public class Sistema {
	private final HashMap<String, Proyecto> proyectos;

	public Sistema() {
		this.proyectos = new HashMap<>();
		try {
			PersistenceCSV.cargarTodo(this);
			if(this.proyectos.isEmpty()) {
				inicializarSistema();
			}
		}
		catch (Exception e) {
			System.out.println("Aviso: No se pudieron cargar datos desde disco. Se usará el conjunto de prueba. (" + e.getMessage() + ")");
			inicializarSistema();
		}
	}
	
	// Getter seguro para mapa
	public Map<String, Proyecto> getProyectos() {
		return Collections.unmodifiableMap(this.proyectos);
	}
	
	public void reemplazaAll(Map<String, Proyecto> proyectosNuevos) {
		this.proyectos.clear();
		this.proyectos.putAll(proyectosNuevos);
	}
	
	public void guardarSistema() throws IOException {
		PersistenceCSV.guardarTodo(this);
	}
	
	private void inicializarSistema() {
		Proyecto p1 = new Proyecto("P-1001", "Sistema Solar Inteligente","Investigación en optimización de paneles solares" , 500000.0, "Energía Renovable");
		Investigador i1 = new Investigador("I-2001", "Juan Pérez", "Paneles Solares", "Ingenieria Eléctrica");
		Publicacion pub1 = new Publicacion("PUB-0019", "Modelado de paneles", 1991, "Artículo");
		p1.agregarInvestigador(i1);
		pub1.agregarAutor(i1);
		p1.agregarPublicacion(pub1);
		this.proyectos.put(p1.getIdProyecto(), p1);
		
		Proyecto p2 = new Proyecto("P-3002", "Clasificación con Deep Learning","Aplicación de redes neuronales con imágenes médicas" , 7500000.0, "Inteligencia Artificial");
		Investigador i2 = new Investigador("I-6002", "María Gómez", "Deep Learning", "Ingenieria Informática");
		Publicacion pub2 = new Publicacion("PUB-3002", "Redes convolucionales para diagnóstico", 2021, "Tesis");
		p2.agregarInvestigador(i2);
		pub2.agregarAutor(i2);
		p2.agregarPublicacion(pub2);
		this.proyectos.put(p2.getIdProyecto(), p2);
	}
	
	// Operaciones sobre proyectos
	
	public boolean agregarProyecto(Proyecto p) throws DuplicatedIdException {
		if(p == null) return false;
		String id = p.getIdProyecto();
		if(this.proyectos.containsKey(id)) {
			throw new DuplicatedIdException("Ya existe un proyecto con ID: " + id);
		}
		this.proyectos.put(id, p);
		return true;
	}
	
	public boolean agregarProyecto(String idProyecto, String nombre, String descripcion, double fondos, String subRamaCarrera) throws DuplicatedIdException {
		if(idProyecto == null || idProyecto.isBlank()) throw new IllegalArgumentException("id del proyecto es obligatorio");
		if(this.proyectos.containsKey(idProyecto)) {
			throw new DuplicatedIdException("Ya existe un proyecto con ID: " + idProyecto);
		}
		
		if(fondos <= 0) {
			System.out.println("El monto de fondos debe ser mayor a 0");
			return false;
		}
		
		Proyecto p = new Proyecto(idProyecto, nombre, descripcion, fondos, subRamaCarrera);
		this.proyectos.put(idProyecto, p);
		return true;
	}
	
	public Proyecto buscarProyecto(String idProyecto) {
		if(idProyecto == null) return null;
		return this.proyectos.get(idProyecto);
	}
	
	public boolean eliminarProyecto(String idProyecto) throws ProyectoNotFoundException {
		if(!this.proyectos.containsKey(idProyecto)) 
			throw new ProyectoNotFoundException("No existe proyecto con ID: " + idProyecto);;
		this.proyectos.remove(idProyecto);
		return true;
	}
	
	public boolean agregarPublicacion(String idProyecto, Publicacion publicacion) throws ProyectoNotFoundException {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
		return p.agregarPublicacion(publicacion);
	}
	
	public boolean agregarPublicacion(String idProyecto, String idPublicacion, String titulo, int año, String tipo) throws ProyectoNotFoundException {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) throw new ProyectoNotFoundException("No existe proyecto con ID: " + idProyecto);
		Publicacion pub = new Publicacion(idPublicacion, titulo, año, tipo);
		return p.agregarPublicacion(pub);
	}
	
	public boolean agregarInvestigador(String idProyecto, Investigador investigador) throws ProyectoNotFoundException {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
		return p.agregarInvestigador(investigador);
	}
	
	public boolean agregarInvestigador(String idProyecto, String idInvestigador, String nombre, String especialidad, String carrera) throws ProyectoNotFoundException {
		Proyecto p = this.proyectos.get(idProyecto);
		if(p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
		if(idInvestigador == null || idInvestigador.isBlank() || nombre == null || nombre.isBlank() || 
		   especialidad == null || especialidad.isBlank() || carrera == null || carrera.isBlank()) {
			System.out.println("Todos los campos del investigador son obligatorios");
			return false;
		}
		
		Investigador inv = new Investigador(idInvestigador, nombre, especialidad, carrera);
		return p.agregarInvestigador(inv);
	}
	
	public int cantidadProyectos() {
		return this.proyectos.size();
	}
	
	// Filtro / busquedas
    public ArrayList<Proyecto> filtrarProyectosPorSubRama(String subRama) {
        ArrayList<Proyecto> out = new ArrayList<>();
        for (Proyecto p : this.proyectos.values()) {
        	if (p.getSubRamaCarrera() != null && p.getSubRamaCarrera().equalsIgnoreCase(subRama)) out.add(p);
        }
        return out;
    }

    public ArrayList<Publicacion> filtrarPublicacionesPorRangoAños(int desde, int hasta) {
        ArrayList<Publicacion> out = new ArrayList<>();
        for (Proyecto p : this.proyectos.values()) {
            for (Publicacion pub : p.getPublicaciones()) {
                if (pub.getAño() >= desde && pub.getAño() <= hasta) out.add(pub);
            }
        }
        return out;
    }
    
    // busca una publicacion en todos los proyectos; devuelve un Map.Entry donde key=Proyecto, value=Publicacion
    public Map.Entry<Proyecto, Publicacion> buscarPublicacionGlobal(String idPublicacion) {
        if (idPublicacion == null) return null;
        for (Proyecto p : this.proyectos.values()) {
            Publicacion pub = p.buscarPublicacion(idPublicacion);
            if (pub != null) return new AbstractMap.SimpleEntry<>(p, pub); // clase de java que implementa Map.Entry<p, p>
        }
        return null;
    }

    // busca un investigador en todos los proyectos; devuelve Map.Entry<Proyecto, Investigador>
    public Map.Entry<Proyecto, Investigador> buscarInvestigadorGlobal(String idInvestigador) {
        if (idInvestigador == null) return null;
        for (Proyecto p : this.proyectos.values()) {
            Investigador inv = p.buscarInvestigador(idInvestigador);
            if (inv != null) return new AbstractMap.SimpleEntry<>(p, inv); // clase de java que implementa Map.Entry<p, i>
        }
        return null;
    }

    // Edicion
    public boolean editarProyecto(String idProyecto, String nuevoNombre, String nuevaDescripcion, double nuevosFondos, String nuevaSubRama) throws ProyectoNotFoundException {
        Proyecto p = this.proyectos.get(idProyecto);
        if (p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
        p.setNombre(nuevoNombre);
        p.setDescripcion(nuevaDescripcion);
        p.setFondos(nuevosFondos);
        p.setSubRamaCarrera(nuevaSubRama);
        return true;
    }

    public boolean eliminarPublicacion(String idProyecto, String idPublicacion) throws ProyectoNotFoundException {
        Proyecto p = this.proyectos.get(idProyecto);
        if (p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
        return p.removerPublicacion(idPublicacion);
    }

    public boolean eliminarInvestigador(String idProyecto, String idInvestigador) throws ProyectoNotFoundException {
        Proyecto p = this.proyectos.get(idProyecto);
        if (p == null) throw new ProyectoNotFoundException("No existe el proyecto con ID: " + idProyecto);
        return p.removerInvestigador(idInvestigador);
    }
	
}

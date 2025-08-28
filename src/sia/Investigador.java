package sia;

public class Investigador {
	private String idInvestigador;
	private String nombre; // nombre del investigador
	private String especialidad;// ej: especializado en IA o en Energia solar.
	private String carrera ;

	public Investigador(String idInvestigador, String nombre, String especialidad, String carrera) {
		this.idInvestigador = idInvestigador;
		this.nombre = nombre;
		this.especialidad = especialidad;
		this.carrera = carrera ;
	}
	
	// Getters y setters
	public String getIdInvestigador() {
		return idInvestigador;
	}
	public void setIdInvestigador(String idInvestigador) {
		if(idInvestigador != null) {
			this.idInvestigador = idInvestigador;
		}
		else{
			this.idInvestigador = "";
		}
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		if (nombre != null) {
			this.nombre = nombre;
		}
		else {
			this.nombre = "" ;
		}
	}
	
	public String getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(String especialidad) {
		if(especialidad != null) {
			this.especialidad = especialidad;
		}
		else{
			this.especialidad = "" ;
		}
	}
	
	public String getCarrera() {
		return carrera;
	}
	
	public void setCarrera(String carrera) {
		if(carrera != null) {
			this.carrera = carrera ;
		}
		else{
			this.carrera = "" ;
		}
	}
	

}

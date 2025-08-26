package sia;

public class Investigador {
	private String idInvestigador;
	private String nombre; // nombre del investigador
	private String especialidad; // ej: especializado en IA o en Energia solar.

	public Investigador(String idInvestigador, String nombre, String especialidad) {
		this.idInvestigador = idInvestigador;
		this.nombre = nombre;
		this.especialidad = especialidad;
	}
	
	// Getters y setters
	public String getIdInvestigador() {
		return idInvestigador;
	}
	public void setIdInvestigador(String idInvestigador) {
		this.idInvestigador = idInvestigador;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

}

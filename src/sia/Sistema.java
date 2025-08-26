package sia;
import java.util.*;

// administra proyectos con publicaciones
public class Sistema {
	private ArrayList<Proyecto> proyectos;

	public Sistema() {
		proyectos = new ArrayList<>();
	}
	
	public void agregarProyecto(Proyecto p) {
		proyectos.add(p);
	}
	public ArrayList<Proyecto> getProyectos() {
		return proyectos;
	}

}

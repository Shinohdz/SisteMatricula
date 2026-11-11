package modelo;

import java.util.*;

/**
 * @author Jose Luis
 */
public class Estudiante implements Comparable<Estudiante> {
    private String carnet;
    private String nombre;
    private String documento;
    private String correo;
    private boolean activo;
    private Set<String> cursosEnCurso;

    public static final Comparator<Estudiante> POR_NOMBRE = (e1, e2) ->
            e1.getNombre().compareToIgnoreCase(e2.getNombre());

    public Estudiante(String carnet, String nombre, String documento, String correo, boolean activo) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
        this.activo = activo;
        this.cursosEnCurso = new HashSet<>();
    }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public Set<String> getCursosEnCurso() { return cursosEnCurso; }
    public void setCursosEnCurso(Set<String> cursosEnCurso) { this.cursosEnCurso = cursosEnCurso; }

    public boolean agregarCurso(String codCurso) {
        return this.cursosEnCurso.add(codCurso);
    }

    public boolean retirarUnCurso(String codCurso) {
        return this.cursosEnCurso.remove(codCurso);
    }

    @Override
    public int compareTo(Estudiante otro) {
        return this.carnet.compareToIgnoreCase(otro.carnet);
    }

    @Override
    public String toString() {
        return carnet + " - " + nombre;
    }
}

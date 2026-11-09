package modelo;

import java.util.*;

/**
 * @author Jose Luis
 */
public class Estudiante {
    private String carnet;
    private String nombre;
    private String correo;
    private Set<String> cursosEnCurso;
    
    public Estudiante(String carnet, String nombre, String correo) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.correo = correo;
        this.cursosEnCurso = new HashSet<>();
    }

    public String getCarnet() {
        return carnet;
    }

    public String getNombre() {
        return nombre;
    }
    
    public Set<String> getCursosEnCurso(){
        return cursosEnCurso;
    }
    
    public boolean agregarCurso(String idCurso){
        return cursosEnCurso.add(idCurso);
    }
    
    public boolean retirarUnCurso(String cod){
        return cursosEnCurso.remove(cod);
    }
}

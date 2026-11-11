
package modelo;

/**
 * @author Jose Luis
 */
public class Curso {
    private String codCurso;
    private String nombre;
    private String descripcion;
    private String tutor;
    private int cupoMaximo;
    private int matriculadosActivos;

    public Curso(String codCurso, String nombre, String descripcion, String tutor, int cupoMaximo, int matriculadosActivos) {
        this.codCurso = codCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tutor = tutor;
        this.cupoMaximo = cupoMaximo;
        this.matriculadosActivos = matriculadosActivos;
    }

    public String getCodCurso() { return codCurso; }
    public void setCodCurso(String codCurso) { this.codCurso = codCurso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTutor() { return tutor; }
    public void setTutor(String tutor) { this.tutor = tutor; }
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    public int getMatriculadosActivos() { return matriculadosActivos; }
    public void setMatriculadosActivos(int matriculadosActivos) { this.matriculadosActivos = matriculadosActivos; }

    public int getCuposDisponibles() {
        return Math.max(0, cupoMaximo - matriculadosActivos);
    }

    @Override
    public String toString() {
        return codCurso + " - " + nombre + " (Disp: " + getCuposDisponibles() + "/" + cupoMaximo + ")";
    }

}

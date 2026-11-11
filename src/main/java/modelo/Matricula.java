
package modelo;

/**
 * @author Jose Luis
 */
public class Matricula {
    private String idMatricula;
    private Estudiante estudiante;
    private Curso curso;
    private String fechaMatricula;
    private String estado;

    public Matricula(String idMatricula, Estudiante estudiante, Curso curso, String fechaMatricula, String estado) {
        this.idMatricula = idMatricula;
        this.estudiante = estudiante;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
        this.estado = estado;
    }

    public String getIdMatricula() {
        return idMatricula;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public String getFechaMatricula() {
        return fechaMatricula;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdMatricula(String idMatricula) {
        this.idMatricula = idMatricula;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setFechaMatricula(String fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
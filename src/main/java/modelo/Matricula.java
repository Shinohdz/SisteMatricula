
package modelo;

/**
 * @author Jose Luis
 */
public class Matricula {
    private int idMatricula;
    private Estudiante estudiante;
    private Curso curso;
    private String fechaMatricula;

    public Matricula(int idMatricula, Estudiante estudiante, Curso curso, String fechaMatricula) {
        this.idMatricula = idMatricula;
        this.estudiante = estudiante;
        this.curso = curso;
        this.fechaMatricula = fechaMatricula;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Curso getCurso() {
        return curso;
    }
}

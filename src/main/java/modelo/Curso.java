
package modelo;

/**
 * @author Jose Luis
 */
public class Curso {
    private String codCurso;
    private String Curso;
    private String tutor;

    public Curso(String codCurso, String Curso, String tutor) {
        this.codCurso = codCurso;
        this.Curso = Curso;
        this.tutor = tutor;
    }

    public String getCodCurso() {
        return codCurso;
    }
}

package android.example.yourclassroom.model;

public class Classroom {
    private String id;
    private String className;
    private String codeClass;
    private String idTeacher;

    public Classroom() {
    }

    public Classroom(String id, String className, String codeClass, String idTeacher) {
        this.id = id;
        this.className = className;
        this.codeClass = codeClass;
        this.idTeacher = idTeacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCodeClass() {
        return codeClass;
    }

    public void setCodeClass(String codeClass) {
        this.codeClass = codeClass;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }
}

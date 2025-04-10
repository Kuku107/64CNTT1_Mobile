package android.example.yourclassroom.model;

import java.io.Serializable;

/**
 * Lớp đại diện cho một lớp học trong ứng dụng Your Classroom.
 * Thực thi Serializable để có thể truyền đối tượng giữa các Activity.
 */
public class classroom implements Serializable {

    private String idClass;    // Mã ID của lớp học
    private String className;  // Tên lớp học
    private String codeClass;  // Mã code của lớp để học sinh tham gia
    private String idTeacher;  // ID của giáo viên tạo lớp

    /**
     * Constructor mặc định (bắt buộc cho Firebase).
     */
    public classroom() {
    }

    /**
     * Constructor khởi tạo đầy đủ thông tin lớp học.
     * @param idClass   ID lớp học
     * @param className Tên lớp học
     * @param codeClass Mã lớp
     * @param idTeacher ID giáo viên
     * @param idUser    ID người dùng
     * @param roleInClass Vai trò của người dùng trong lớp (Teacher/Student)
     */
    public classroom(String idClass, String className, String codeClass, String idTeacher, String idUser, String roleInClass) {
        this.idClass = idClass;
        this.className = className;
        this.codeClass = codeClass;
        this.idTeacher = idTeacher;
    }

    // Getter và Setter cho các thuộc tính
    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
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
}

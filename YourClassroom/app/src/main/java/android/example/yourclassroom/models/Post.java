package android.example.yourclassroom.models;

import java.io.Serializable;
import java.util.Date;

public class Post {
    private String idPost;
    private String idTeacher;
    private String idClass;
    private String idExercise = null;
    private String author;
    private Date createAt;
    private String content;

    public Post() {
    }

    public Post(String idPost, String idTeacher, String idClass, String idExercise, String author, Date createAt, String content) {
        this.idPost = idPost;
        this.idTeacher = idTeacher;
        this.idClass = idClass;
        this.idExercise = idExercise;
        this.author = author;
        this.createAt = createAt;
        this.content = content;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

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

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

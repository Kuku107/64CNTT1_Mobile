package android.example.yourclassroom.model;

import java.util.Date;

public class Post {
    private String author;
    private Date createdAt;
    private String content;
    private String id;
    private String idClass;
    private String idUser;
    private String idExercise;

    public Post() {
    }

    public Post(String author, Date createdAt, String content, String id, String idClass, String idUser, String idExercise) {
        this.author = author;
        this.createdAt = createdAt;
        this.content = content;
        this.id = id;
        this.idClass = idClass;
        this.idUser = idUser;
        this.idExercise = idExercise;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }
}

package android.example.yourclassroom.model;

import java.io.Serializable;
import java.util.Date;

public class Exercise implements Serializable {
    private String id;
    private String idClass;
    private String title;
    private String instruction;
    private Date createAt;
    private Date expired;
    private String idAuthor;

    public Exercise() {
    }

    public Exercise(String title, Date expired) {
        this.title = title;
        this.expired = expired;
    }

    public Exercise(String id, String idClass, String title, String instruction, Date createAt, Date expired, String idAuthor) {
        this.id = id;
        this.idClass = idClass;
        this.title = title;
        this.instruction = instruction;
        this.createAt = createAt;
        this.expired = expired;
        this.idAuthor = idAuthor;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }
}

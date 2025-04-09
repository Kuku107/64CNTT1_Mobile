package android.example.yourclassroom.model;


public class Attachment {
    private String id;
    private String uri;
    private String filename;
    private String idExercise;
    private String idUser;

    private String idSubmission;
    private String idPost;

    public Attachment() {
    }

    public Attachment(String uri, String filename) {
        this.uri = uri;
        this.filename = filename;
    }

    public Attachment(String id, String uri, String filename, String idExercise, String idUser, String idSubmission, String idPost) {
        this.id = id;
        this.uri = uri;
        this.filename = filename;
        this.idExercise = idExercise;
        this.idUser = idUser;
        this.idSubmission = idSubmission;
        this.idPost = idPost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIdExercise() {
        return idExercise;
    }

    public void setIdExercise(String idExercise) {
        this.idExercise = idExercise;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdSubmission() {
        return idSubmission;
    }

    public void setIdSubmission(String idSubmission) {
        this.idSubmission = idSubmission;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
}

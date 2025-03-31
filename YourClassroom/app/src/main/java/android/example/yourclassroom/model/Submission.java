package android.example.yourclassroom.model;

public class Submission {
    private int score;
    private String idExercise;
    private String idUser;

    public Submission() {
    }

    public Submission(int score, String idExercise, String idUser) {
        this.score = score;
        this.idExercise = idExercise;
        this.idUser = idUser;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
}

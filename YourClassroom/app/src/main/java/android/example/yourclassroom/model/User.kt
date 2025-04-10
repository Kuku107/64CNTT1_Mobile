package android.example.yourclassroom.model

data class User(val email: String, val password: String?, val fullName: String?, val id: String?) {
    constructor() : this("", null, null, null) {
        // Default constructor required for calls to DataSnapshot.getValue(User::class.java)
    }

    constructor(email: String, password: String) : this(email, password, null, null) {
        // Constructor for creating a new user with email and password
    }
}
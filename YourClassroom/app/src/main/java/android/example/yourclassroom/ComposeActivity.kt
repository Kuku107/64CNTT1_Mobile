package android.example.yourclassroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

class ComposeActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val startDestination = intent.getStringExtra("startDestination") ?: "login"

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = startDestination) {
                composable("login") {
                    LoginScreen(navController = navController, onLoginSuccess = { user ->
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", true)
                        editor.putString("userEmail", user?.email)
                        editor.putString("userUID", user?.uid)
                        editor.putString("displayName", user?.displayName)
                        editor.apply()

                        val intent = Intent(this@ComposeActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                }
                composable("registration") {
                    RegistrationScreen(navController = navController)
                }
                composable("forgotPassword") {
                    ForgotPasswordScreen(navController = navController)
                }
            }
        }
    }
}
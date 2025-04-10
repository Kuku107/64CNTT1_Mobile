package android.example.yourclassroom.controller

import android.content.Context
import android.content.Intent
import android.example.yourclassroom.R
import android.example.yourclassroom.model.AuthModel
import android.example.yourclassroom.model.User
import android.example.yourclassroom.view.auth.CustomDialog
import android.example.yourclassroom.view.auth.ForgotPasswordScreen
import android.example.yourclassroom.view.auth.LoginScreen
import android.example.yourclassroom.view.auth.RegistrationScreen
import android.example.yourclassroom.view.classroom.ListClassActivity
import android.example.yourclassroom.view.exercise.ListExerciseActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class ComposeActivity : ComponentActivity() {

    private val authModel = AuthModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)


        if (isLoggedIn) {
            startActivity(Intent(this, ListClassActivity::class.java))
            finish()
            return
        }

        setContent {
            val navController = rememberNavController()
            var dialogMessage by remember { mutableStateOf<String?>(null) }

            Box(modifier = Modifier.fillMaxSize()) {

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        var email by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        var passwordVisible by remember { mutableStateOf(false) }

                        LoginScreen(
                            email = email,
                            password = password,
                            passwordVisible = passwordVisible,
                            onEmailChange = { email = it },
                            onPasswordChange = { password = it },
                            onPasswordVisibilityChange = { passwordVisible = it },
                            errorMessage = null,
                            onLoginClick = {
                                if (email.isEmpty() || password.isEmpty()) {
                                    dialogMessage = "Vui lòng nhập đầy đủ thông tin!"
                                    return@LoginScreen
                                }

                                lifecycleScope.launch {
                                    val result = authModel.login(User(email, password, null, null))
                                    if (result.isSuccess) {
                                        val user = result.getOrNull()
                                        saveLoginToPrefs(user)
                                        startActivity(Intent(this@ComposeActivity, ListClassActivity::class.java))
                                        finish()
                                    } else {
                                        dialogMessage = result.exceptionOrNull()?.message ?: "Đăng nhập thất bại!"
                                    }
                                }
                            },
                            onForgotPasswordClick = {
                                navController.navigate("forgotPassword")
                            },
                            onRegisterClick = {
                                navController.navigate("registration")
                            }
                        )
                    }

                    composable("registration") {
                        var fullName by remember { mutableStateOf("") }
                        var email by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        var confirmPassword by remember { mutableStateOf("") }
                        var passwordVisible by remember { mutableStateOf(false) }
                        var confirmPasswordVisible by remember { mutableStateOf(false) }

                        RegistrationScreen(
                            fullName = fullName,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            onFullNameChange = { fullName = it },
                            onEmailChange = { email = it },
                            onPasswordChange = { password = it },
                            onConfirmPasswordChange = { confirmPassword = it },
                            onPasswordVisibilityChange = { passwordVisible = it },
                            onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it },
                            passwordVisible = passwordVisible,
                            confirmPasswordVisible = confirmPasswordVisible,
                            errorMessage = null,
                            onRegisterClick = {
                                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                                    dialogMessage = "Vui lòng nhập đầy đủ thông tin!"
                                    return@RegistrationScreen
                                }

                                if (password != confirmPassword) {
                                    dialogMessage = "Mật khẩu không khớp!"
                                    return@RegistrationScreen
                                }

                                lifecycleScope.launch {
                                    val result = authModel.register(User(email, password, fullName, null))
                                    if (result.isSuccess) {
                                        val user = result.getOrNull()
                                        if (user != null) {
                                            authModel.saveUserToDatabase(user, User(email, password, fullName, user.uid))
                                        }
                                        dialogMessage = "Đăng ký thành công! Vui lòng xác nhận email!"
                                        navController.popBackStack()
                                    } else {
                                        dialogMessage = result.exceptionOrNull()?.message ?: "Lỗi đăng ký!"
                                    }
                                }
                            }
                        )
                    }

                    composable("forgotPassword") {
                        var email by remember { mutableStateOf("") }

                        ForgotPasswordScreen(
                            email = email,
                            onEmailChange = { email = it },
                            errorMessage = null,
                            onResetPasswordClick = {
                                lifecycleScope.launch {
                                    val result = authModel.forgotPassword(email)
                                    if (result.isSuccess) {
                                        dialogMessage = "Đã gửi email đặt lại mật khẩu!"
                                        navController.popBackStack()
                                    } else {
                                        dialogMessage = result.exceptionOrNull()?.message ?: "Lỗi gửi email!"
                                    }
                                }
                            }
                        )
                    }
                }

                // Hiển thị CustomDialog nếu có thông báo lỗi
                if (dialogMessage == "Đã gửi email đặt lại mật khẩu!" || dialogMessage == "Đăng ký thành công! Vui lòng xác nhận email!") {
                    CustomDialog(
                        message = dialogMessage ?: "",
                        onDismiss = { dialogMessage = null },
                        iconResId = R.drawable.ic_email,
                        buttonText = "OK",
                        onNavigateToLogin = {
                            navController.navigate("login")
                        }
                    )
                } else if (dialogMessage != null) {
                    CustomDialog(
                        message = dialogMessage ?: "",
                        onDismiss = { dialogMessage = null },
                    )
                }
            }
        }
    }

    private fun saveLoginToPrefs(user: FirebaseUser?) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putString("userEmail", user?.email)
            putString("userUID", user?.uid)
            putString("displayName", user?.displayName)
            apply()
        }
    }
}

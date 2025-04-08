package android.example.yourclassroom

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import android.example.yourclassroom.Dialog
import com.google.firebase.auth.UserProfileChangeRequest

@Composable
fun RegistrationScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf<String?>(null) }
    val auth = Firebase.auth
    val context = LocalContext.current

    Box(Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFAF4EF)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFAF4EF))
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register_illustration),
                        contentDescription = "Registration Illustration",
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = 4.dp,
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFFF8F9FA))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Đăng ký",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Họ và tên") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            )
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                                autoCorrect = false
                            )
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mật khẩu") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = "Ẩn/Hiện mật khẩu"
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next,
                            )
                        )

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Xác nhận mật khẩu") },
                            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = "Ẩn/Hiện xác nhận mật khẩu"
                                    )
                                }
                            }
                        )

                        Button(
                            onClick = {
                                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    showMessage = "Email không hợp lệ!"
                                    return@Button
                                }
                                if (password == confirmPassword) {
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser

                                                val profileUpdates = UserProfileChangeRequest.Builder()
                                                    .setDisplayName(fullName)
                                                    .build()
                                                user?.updateProfile(profileUpdates)

                                                user?.sendEmailVerification()
                                                    ?.addOnCompleteListener { verificationTask ->
                                                        if (verificationTask.isSuccessful) {
                                                            showMessage = "Gửi email xác thực thành công!"
                                                        } else {
                                                            showMessage = "Gửi email xác thực thất bại!"
                                                        }
                                                    }
                                            } else {
                                                val exception = task.exception
                                                if (exception is FirebaseAuthUserCollisionException) {
                                                    showMessage = "Email đã được sử dụng!"
                                                } else {
                                                    showMessage = "Đăng ký không thành công!"
                                                }
                                            }
                                        }
                                } else {
                                    showMessage = "Mật khẩu không trùng khớp!"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp)
                        ) {
                            Text("Đăng ký")
                        }
                    }
                }
            }
        }

        if (showMessage == "Gửi email xác thực thành công!") {
            Dialog(
                Message = showMessage!!,
                onDismiss = { showMessage = null },
                iconResourceId = R.drawable.ic_email,
                buttonText = "OK",
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                    showMessage = null
                }
            )
        } else if (showMessage != null) {
            Dialog(
                Message = showMessage!!,
                onDismiss = { showMessage = null }
            )
        }
    }
}
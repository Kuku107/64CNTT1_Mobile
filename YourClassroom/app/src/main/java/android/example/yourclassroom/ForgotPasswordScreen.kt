package android.example.yourclassroom

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf<String?>(null) }
    val auth = FirebaseAuth.getInstance()

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
                        painter = painterResource(id = R.drawable.forgot_password_illustration),
                        contentDescription = "Forgot Password Illustration",
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
                            text = "Quên mật khẩu",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .padding(top = 8.dp)
                                .align(Alignment.Start),
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {email = it},
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                autoCorrect = false
                            ),
                        )

                        Button(
                            onClick = {
                                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    showMessage = "Email không hợp lệ!"
                                    return@Button
                                }
                                auth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            showMessage = "Yêu cầu đặt lại mật khẩu đã được gửi!"
                                        } else {
                                            val exception = task.exception
                                            if (exception != null) {
                                                val log = exception.message
                                                if (log?.contains("no user record") == true) {
                                                    showMessage = "Email không tồn tại trong hệ thống!"
                                                } else {
                                                    showMessage = "Lỗi: $log"
                                                }
                                            }
                                        }
                                    }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 52.dp)
                                .padding(top = 16.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Text("Gửi yêu cầu đặt lại mật khẩu")
                        }
                    }
                }
            }
        }

        if (showMessage == "Yêu cầu đặt lại mật khẩu đã được gửi!") {
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
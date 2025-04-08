package android.example.yourclassroom

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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.navigation.NavController
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import android.example.yourclassroom.Dialog
import com.google.firebase.auth.FirebaseUser

@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: (FirebaseUser?) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
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
                        painter = painterResource(id = R.drawable.mobile_login),
                        contentDescription = "Login Illustration",
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
                            text = "Đăng nhập",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.Start),
                        )

                        OutlinedTextField(
                            value = emailState,
                            onValueChange = { emailState = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next,
                                autoCorrect = false
                            ),
                        )

                        OutlinedTextField(
                            value = passwordState,
                            onValueChange = { passwordState = it },
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
                                        contentDescription = "Ẩn/Hiện mật khẩu",
                                    )
                                }
                            }
                        )

                        TextButton(
                            onClick = { navController.navigate("forgotPassword")},
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(top = 4.dp)
                        ) {
                            Text("Quên mật khẩu?")
                        }

                        Button(
                            onClick = {
                                val email = emailState
                                val password = passwordState
                                if (email.isNotEmpty() && password.isNotEmpty()) {
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val user = auth.currentUser
                                                if (user?.isEmailVerified == true) {
                                                    val sharedPreferences = context.getSharedPreferences(
                                                        "MyAppPrefs",
                                                        Context.MODE_PRIVATE
                                                    )
                                                    val editor = sharedPreferences.edit()
                                                    editor.putBoolean("isLoggedIn", true)
                                                    editor.apply()
                                                    onLoginSuccess(user)
                                                } else {
                                                    showMessage = "Vui lòng xác nhận email của bạn!"
                                                }
                                            } else {
                                                showMessage = "Thông tin đăng nhập không chính xác!"
                                            }
                                        }
                                } else {
                                    showMessage = "Vui lòng nhập đầy đủ thông tin!"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 52.dp)
                                .padding(top = 16.dp)
                        ) {
                            Text("Đăng nhập")
                        }

                        OutlinedButton(
                            onClick = { navController.navigate("registration") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 48.dp)
                                .padding(top = 8.dp)
                        ) {
                            Text("Đăng ký tài khoản mới")
                        }
                    }
                }
            }
        }

        if (showMessage != null) {
            Dialog(
                Message = showMessage!!,
                onDismiss = {showMessage = null}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController, onLoginSuccess = {})
}
package android.example.yourclassroom.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.example.yourclassroom.R
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomDialog(
    message: String,
    onDismiss: () -> Unit,
    iconResId: Int = R.drawable.ic_error_x,
    buttonText: String = "Thử lại",
    onNavigateToLogin: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(86.dp)
                )
                Text(
                    message,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        if (buttonText == "OK" && onNavigateToLogin != null) {
                            onNavigateToLogin()
                        }
                        onDismiss()
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}

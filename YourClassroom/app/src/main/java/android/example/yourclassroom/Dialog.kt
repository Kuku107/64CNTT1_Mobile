package android.example.yourclassroom

import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Dialog(
    Message: String,
    onDismiss: () -> Unit,
    iconResourceId: Int = R.drawable.ic_error_x,
    buttonText: String = "Thử lại",
    onNavigateToLogin: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = "Error Icon",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(86.dp)
                )
                Text(
                    Message,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        if (buttonText == "OK" && onNavigateToLogin != null) {
                            onNavigateToLogin()
                        } else {
                            onDismiss()
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}
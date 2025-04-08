package android.example.yourclassroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            val intent = Intent(this, ComposeActivity::class.java)
            intent.putExtra("startDestination", "login")
            startActivity(intent)
            finish()
        }

        val getSharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val displayName = getSharedPreferences.getString("displayName", "")
        text = findViewById(R.id.welcome)
        text.text = "Xin chao, $displayName!"

    }
}
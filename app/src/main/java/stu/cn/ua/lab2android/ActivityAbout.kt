package stu.cn.ua.lab2android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ActivityAbout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val backToMenuButton: Button = findViewById(R.id.btnBackToMenu)
        backToMenuButton.setOnClickListener {
            finish() // Завершуємо поточну Activity і повертаємось до MainActivity
        }
    }
}

package stu.cn.ua.lab2android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val startGameButton: Button = findViewById(R.id.btnStartGame)
        val aboutButton: Button = findViewById(R.id.btnAbout)
        val exitButton: Button = findViewById(R.id.btnExit)
        val difficultySpinner: Spinner = findViewById(R.id.spinnerDifficulty)

        startGameButton.setOnClickListener {
            val difficulty = difficultySpinner.selectedItem.toString()
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("difficulty", difficulty)
            startActivity(intent)
        }

        aboutButton.setOnClickListener {
            val intent = Intent(this, ActivityAbout::class.java)
            startActivity(intent)
        }

        exitButton.setOnClickListener {
            finish()
        }
    }
}

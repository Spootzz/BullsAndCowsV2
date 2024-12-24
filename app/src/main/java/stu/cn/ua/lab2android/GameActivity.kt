package stu.cn.ua.lab2android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class GameActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var checkButton: Button
    private lateinit var attemptsHistory: TextView
    private lateinit var backButton: Button
    private var secretNumber: String = ""

    private var difficulty: String = "Easy"
    private var attemptNumber: Int = 1 // Лічильник спроб

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        inputField = findViewById(R.id.inputNumber)
        checkButton = findViewById(R.id.btnCheck)
        attemptsHistory = findViewById(R.id.attemptsHistory)
        backButton = findViewById(R.id.btnBack)

        // Отримання складності та генерація секретного числа
        difficulty = intent.getStringExtra("difficulty") ?: "Easy"
        secretNumber = generateSecretNumber(difficulty)

        // Реєстрація BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(
            resultReceiver,
            IntentFilter("stu.cn.ua.lab2android.RESULT_BROADCAST")
        )

        checkButton.setOnClickListener {
            val input = inputField.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Enter a number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (input.length != secretNumber.length) {
                Toast.makeText(
                    this,
                    "Enter a ${secretNumber.length}-digit number",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Надсилання числа для перевірки
            startService(Intent(this, GameService::class.java).apply {
                action = "stu.cn.ua.lab2android.ACTION_CHECK"
                putExtra("input", input)
                putExtra("secretNumber", secretNumber)
            })
        }

        backButton.setOnClickListener {
            finish() // Повернення до головного меню
        }
    }

    // Отримання результату через BroadcastReceiver
    private val resultReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val input = intent?.getStringExtra("input") ?: ""
            val result = intent?.getStringExtra("result") ?: ""

            // Форматуємо запис у вигляді: Attempt 1: 4321 -> Bulls: 2, Cows: 1
            attemptsHistory.append("Attempt $attemptNumber: $input -> $result\n")
            attemptNumber++ // Збільшуємо номер спроби

            // Перевірка виграшу
            if (result.contains("Bulls: ${secretNumber.length}")) {
                showWinDialog()
            }
        }
    }

    // Генерація секретного числа на основі складності
    private fun generateSecretNumber(difficulty: String): String {
        val digits = (0..9).shuffled()
        val length = when (difficulty) {
            "EASY" -> 4
            "MEDIUM" -> 5
            "HARD" -> 6
            else -> 4 // Значення за замовчуванням
        }
        return digits.take(length).joinToString("")
    }

    // Діалог виграшу
    private fun showWinDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("You won!")
            .setMessage("Start a new game or return to the main menu?")
            .setPositiveButton("New Game") { _, _ ->
                recreate() // Почати нову гру з тією самою складністю
            }
            .setNegativeButton("Main Menu") { _, _ ->
                finish() // Повернення до головного меню
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(resultReceiver)
    }
}


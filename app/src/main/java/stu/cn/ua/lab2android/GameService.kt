package stu.cn.ua.lab2android

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class GameService : IntentService("GameService") {

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            "stu.cn.ua.lab2android.ACTION_CHECK" -> {
                val input = intent.getStringExtra("input") ?: ""
                val secret = intent.getStringExtra("secretNumber") ?: ""
                val result = checkNumber(input, secret)
                sendResult(result, input)
            }
        }
    }

    private fun checkNumber(input: String, secret: String): String {
        var bulls = 0
        var cows = 0
        input.forEachIndexed { index, char ->
            if (char in secret) {
                if (index < secret.length && secret[index] == char) {
                    bulls++
                } else {
                    cows++
                }
            }
        }
        return "Bulls: $bulls, Cows: $cows" // Повертаємо лише результати
    }

    private fun sendResult(result: String, input: String) {
        val broadcastIntent = Intent("stu.cn.ua.lab2android.RESULT_BROADCAST").apply {
            putExtra("result", result)
            putExtra("input", input)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

}

package com.example.nameage

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs: SharedPreferences = getSharedPreferences("User", 0)

            buttonWrite.setOnClickListener {
                val NomeInput = Name.text.toString()
                val IdadeInput = Age.text.toString().toInt()

                val editor = prefs.edit()
                editor.apply {
                    putString("user_name", NomeInput)
                    putInt("user_age", IdadeInput)
                    apply()

                }
            buttonRead.setOnClickListener {
                val NomeOutput = prefs.getString("user_name", null)
                val IdadeOutput = prefs.getInt("user_age", 0)
                User.setText("O seu nome é $NomeOutput e sua idade é $IdadeOutput")

            }
        }
    }
}
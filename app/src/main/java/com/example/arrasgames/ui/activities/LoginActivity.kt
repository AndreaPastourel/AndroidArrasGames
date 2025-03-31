package com.example.arrasgames.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arrasgames.R
import com.example.arrasgames.repository.UserRepository

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UserRepository.init(this)

        val btnLogin = findViewById<Button>(R.id.buttonLogin)
        val editUsername = findViewById<EditText>(R.id.editTextUsername)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if(username.isNotEmpty() && password.isNotEmpty()){
                UserRepository.loginUser(username,password){success,token->
                    if (success){
                        Toast.makeText(this,"Connexion réussie !", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this,"Echec de la connexion",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Méthode d'authentification en test
    private fun authenticate(username: String, password: String): Boolean {
        return username == "test" && password == "test"
    }
}
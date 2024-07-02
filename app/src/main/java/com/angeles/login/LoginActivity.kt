package com.angeles.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var ingresar: Button
    private lateinit var registrar: Button
    private lateinit var inicioGoogle: ImageButton

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var recuperar: TextView

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //botones
        ingresar = findViewById(R.id.ingresarButton)
        registrar= findViewById(R.id.registrarButton)
        inicioGoogle= findViewById(R.id.gmailButton)
        //edittext
        email = findViewById(R.id.emailTxt)
        password = findViewById(R.id.passwordTxt)
        recuperar= findViewById(R.id.RecuperarPassword)
        //eventos
        registrar.setOnClickListener {onRegistrarButtonClicked(it)}
        ingresar.setOnClickListener {onLoginButtonClicked(it)}
        recuperar.setOnClickListener {onRecuperarButtonClicked(it)}

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

    }
    //click Recuperar
    fun onRecuperarButtonClicked(view: View) {
        val intent = Intent(this, valida::class.java)
        // Inicia la nueva actividad
        startActivity(intent)
    }


    //click Registrar
    fun onRegistrarButtonClicked(view: View) {
        val intent = Intent(this, RegistroActivity::class.java)
        // Inicia la nueva actividad
        startActivity(intent)
    }
    //click Ingresar
    fun onLoginButtonClicked(view: View) {
        val email = email.text.toString() //
        val password = password.text.toString() //

        if (email.isBlank() || password.isBlank()) {
            AlertDialog.Builder(this)
                .setTitle("Campos incompletos")
                .setMessage("Por favor, completa todos los campos antes de continuar.")
                .setPositiveButton("Aceptar", null) // No es necesario hacer nada al presionar "Aceptar"
                .show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, pantalla de bienvenida
                    val user = auth.currentUser
                    // Redirige al usuario a la pantalla principal
                    AlertDialog.Builder(this)
                        .setTitle("ingresaste")
                        .setPositiveButton("Aceptar", null)
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    // Inicia la nueva actividad
                    startActivity(intent)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Contraseñas o usuario incorrectos")
                        .setMessage("Por favor, vuelva a intentarlo.")
                        .setPositiveButton("Aceptar", null)
                        .show()
                }
            }

    }



    }


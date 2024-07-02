package com.angeles.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.firestore
import java.util.Date

class RegistroActivity : AppCompatActivity(){
    data class Usuario(
        val uid: String = "",
        val correo: String = "",
        val usuario: String = "",
        val sexo: String = "",
        val fechaNacimiento: String = "")

    private lateinit var auth: FirebaseAuth
    private lateinit var nombreUsuario: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordEditText2: EditText
    private lateinit var registrarButton: Button
    private lateinit var cerrar:ImageButton
    private lateinit var fecha:EditText
    private lateinit var sexo:EditText




    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registro)

        auth = FirebaseAuth.getInstance()
        // Inicializar vistas
        emailEditText = findViewById(R.id.emailTxt)
        passwordEditText = findViewById(R.id.passwordTxt)
        passwordEditText2 = findViewById(R.id.passwordTxt2)
        nombreUsuario= findViewById(R.id.usuarioTxt)
        fecha=findViewById(R.id.fechaTxt)
        sexo=findViewById(R.id.sexoTxt)

        registrarButton = findViewById(R.id.registrarButton)
        cerrar=findViewById(R.id.cerrarButton)
        //evento del click
        registrarButton.setOnClickListener {
            registrarUsuario()
        }
        cerrar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        fecha.setOnClickListener{mostrarCalendario()}
    }
    private fun mostrarCalendario(){

        val fechaPicker=fecha{day,month,year->seleccionarFecha(day,month,year)}
        fechaPicker.show(supportFragmentManager,"fechaPicker")

    }
    fun seleccionarFecha(day:Int,month:Int,year:Int){
        fecha.setText("$day/$month/$year")
    }
    private fun registrarUsuario() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val password2= passwordEditText2.text.toString()
        val nombreUsuario = nombreUsuario.text.toString()
        val fNacimiento = fecha.text.toString()
        val sexo = sexo.text.toString()

        if (email.isBlank() || password.isBlank() || password2.isBlank() || nombreUsuario.isBlank()) {
            AlertDialog.Builder(this)
                .setTitle("Campos incompletos")
                .setMessage("Por favor, completa todos los campos antes de continuar.")
                .setPositiveButton("Aceptar", null) // No es necesario hacer nada al presionar "Aceptar"
                .show()

            return
        }
        else if (password != password2) {
            AlertDialog.Builder(this)
                .setTitle("Contraseñas no coinciden")
                .setMessage("Por favor, asegúrese de que las contraseñas coinciden.")
                .setPositiveButton("Aceptar", null) // No es necesario hacer nada al presionar "Aceptar"
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Almacenar datos del usuario registrado
                    val firebaseUser = auth.currentUser

                    val usuario = Usuario(
                        uid = firebaseUser!!.uid,
                        correo = email,
                        usuario=nombreUsuario,
                        sexo=sexo,
                        fechaNacimiento=fNacimiento

                    )

                    val db = Firebase.firestore
                    val usuariosRef = db.collection("Usuarios")
                    usuariosRef.document(usuario.uid).set(usuario)
                        .addOnSuccessListener {
                            // Éxito al guardar los datos del usuario
                            Log.d("Firestore", "Documento guardado con ID: ${usuario.uid}")
                            // Aquí puedes realizar acciones adicionales después de guardar el usuario, como mostrar un mensaje de éxito o navegar a otra pantalla.
                        }
                        .addOnFailureListener { e ->
                            // Error al guardar los datos del usuario
                            Log.w("Firestore", "Error al guardar el documento", e)
                            // Aquí puedes manejar el error, como mostrar un mensaje de error al usuario.
                        }

                    AlertDialog.Builder(this)
                        .setTitle("Usuario registrado")
                        .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, _ ->
                            // Inicia LoginActivity
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

                            // Cierra la actividad actual (opcional)
                            finish()
                        })
                        .show()
                } else {
                    // Maneja el error de registro
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // La dirección de correo electrónico ya está en uso
                        Toast.makeText(this, "La dirección de correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
                        // Aquí puedes redirigir al usuario a la pantalla de inicio de sesión o proporcionar una opción para recuperar la contraseña.
                    } else {
                        // Otro error de registro
                        Toast.makeText(this, "Error al registrar el usuario: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


}




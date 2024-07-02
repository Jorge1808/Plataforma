package com.angeles.login

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class valida: AppCompatActivity()  {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var cerrar: ImageButton
    private lateinit var validar: Button
    private lateinit var correo: EditText
    private lateinit var fecha: EditText
    //recupera el correo para cambiar la contraseña

    companion object {
        var emailRecibido: String? = null // Variable para almacenar el email

        fun procesarEmail(email: String) {
            emailRecibido = email // Almacena el email en la variable
            // ... realiza otras acciones necesarias con el email}
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.validacorreo)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        cerrar=findViewById(R.id.cerrarButton)
        validar=findViewById(R.id.validarButton)
        correo=findViewById(R.id.emailTxt)
        fecha=findViewById(R.id.fechaTxt)

        cerrar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        validar.setOnClickListener {
            // Obténer los valores del correo y la fecha de nacimiento desde los campos de texto en tu layout

            val fechaNacimiento = fecha.text.toString()
            val email = correo.text.toString()

            validarCorreoYFechaNacimiento(email, fechaNacimiento)

        }
        fecha.setOnClickListener{mostrarCalendario()}
    }
    //Función para mostrar el calendario
    private fun mostrarCalendario(){

        val fechaPicker=fecha{day,month,year->seleccionarFecha(day,month,year)}
        fechaPicker.show(supportFragmentManager,"fechaPicker")

    }
    fun seleccionarFecha(day:Int,month:Int,year:Int){
        fecha.setText("$day/$month/$year")
    }
    //Función para validar el correo y la fecha de nacimiento
    private fun validarCorreoYFechaNacimiento(email: String, fechaNacimiento: String) {

        procesarEmail(email)

        val usuariosRef = db.collection("Usuarios")
        usuariosRef.whereEqualTo("correo", email)
            .whereEqualTo("fechaNacimiento", fechaNacimiento)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // No se encontró ningún usuario con el correo y la fecha de nacimiento especificados
                    Toast.makeText(this, "El correo o la fecha de nacimiento son incorrectos", Toast.LENGTH_SHORT).show()
                } else {

                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                AlertDialog.Builder(this)
                                    .setTitle("Se envio un correo electrónico para restablecer la contraseña")
                                    .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, _ ->
                                        // Inicia LoginActivity
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)

                                        // Cierra la actividad actual (opcional)
                                        finish()
                                    })
                                    .show()
                               
                            } else {
                                // Ocurrió un error al enviar el correo electrónico de restablecimiento de contraseña
                                val exception = task.exception
                                Toast.makeText(this, "Error al enviar el correo electrónico de restablecimiento: ${exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Error al realizar la consulta
                Toast.makeText(this, "Error al validar los datos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

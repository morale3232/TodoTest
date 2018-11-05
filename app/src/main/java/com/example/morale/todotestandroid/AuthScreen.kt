package com.example.morale.todotestandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth_screen.*


class AuthScreen : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var mAuth: FirebaseAuth
    private val reference = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_screen)

        mAuth = FirebaseAuth.getInstance()

        facebook_login_button.setReadPermissions(listOf("email", "public_profile"))

        callbackManager = CallbackManager.Factory.create()

        val contextReference = this
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        // TODO move to home screen
                        val intent = Intent(contextReference, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        Log.d("ARTHUR", "onSuccess from Facebook login")
                    }

                    override fun onCancel() {
                        // TODO dismiss?
                        Log.d("ARTHUR", "Cancel from Facebook login")
                    }

                    override fun onError(exception: FacebookException) {
                        // TODO present error message
                        Log.d("ARTHUR", "Error from Facebook login")
                    }
                })

        email_login_button.setOnClickListener {
            mAuth.signInWithEmailAndPassword(email_text_box.text.toString(), password_text_box.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("ARTHUR", "signInWithEmail: SUCCESS")
                            val user = mAuth.currentUser
                            val userObject = User(user?.displayName?: "None", user?.email?: "No email")
                            reference.collection("users").add(userObject)
                            val intent = Intent(contextReference, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("ARTHUR", "loginUserWithEmail: FAILURE")
                            Log.d("ARTHUR", it.exception.toString())
                            Toast.makeText(contextReference, "Authentication Failed.",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }

        sign_up_button.setOnClickListener {
            mAuth.createUserWithEmailAndPassword(email_text_box.text.toString(), password_text_box.text.toString())
                    .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("ARTHUR", "createEmailWithPassword: SUCCESS")
                    val intent = Intent(contextReference, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.d("ARTHUR", "createUserWithEmail: FAILURE")
                    Log.d("ARTHUR", it.exception.toString())
                    Toast.makeText(contextReference, "Authentication Failed.",
                            Toast.LENGTH_SHORT).show()

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

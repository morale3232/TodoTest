package com.example.morale.todotestandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            Log.d("ARTHUR", "Replacing root layout")
            supportFragmentManager.beginTransaction().add(R.id.root_layout,
                    ContentFragment.newInstance(), "ContentFragment").commit()
        }
    }

    fun logoutButtonPressed(v: View) {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        val intent = Intent(this, AuthScreen::class.java)
        startActivity(intent)
        finish()
    }
}

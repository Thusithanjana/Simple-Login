package com.example.simplelogin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.simplelogin.data.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //UI initialization
        val tvTitle:TextView = findViewById(R.id.tvTitle)
        val edtUsername:EditText = findViewById(R.id.edtUsername)
        val edtPassword:EditText = findViewById(R.id.edtPassword)
        val btnLogin:Button = findViewById(R.id.btnLogin)
        val btnRegister:Button = findViewById(R.id.btnRegister)
        val btnDeleteUser:Button = findViewById(R.id.btnDeleteUser)

        //reading the initial state
        val userState = readPref()

        //Adjusting the UI according to the selection
        if(userState == 0){
            tvTitle.text = "Register"
            btnLogin.visibility = View.GONE
            btnRegister.visibility = View.VISIBLE
        }else{
            tvTitle.text = "Login"
            btnLogin.visibility = View.VISIBLE
            btnRegister.visibility = View.GONE
        }

        btnLogin.setOnClickListener {
            //usage of the elvis ?: operator
            val user = makeUser(edtUsername,edtPassword) ?: toastMe("Enter details")
            val userHash = user.hashCode()

            if(userState == userHash){
                toastMe("Login Success")
                recreate() // refresh the screen. this will recall the onCreate() method
            }else{
                toastMe("Wrong Credentials")
            }

        }

        btnRegister.setOnClickListener {
            val user = makeUser(edtUsername,edtPassword) ?: toastMe("Enter details")
            saveToSharedPref(user.hashCode())
            if (readPref() !=0){
                recreate()
            }

        }

        btnDeleteUser.setOnClickListener {
            val user = makeUser(edtUsername,edtPassword) ?: toastMe("Enter details")

            if(userState == user.hashCode()){
                saveToSharedPref(0)
                recreate()
            }else{
                toastMe("No User found")
            }

        }

    }

    //Make a user object. if the user haven't entered user name or password, it will return null
    fun makeUser(userName:EditText, password:EditText):User?{
        val userName = userName.text.toString()
        val password = password.text.toString()

        return if (userName.isEmpty() && password.isEmpty()) {
            null
        } else {
            User(userName, password)
        }

    }


//Toast Message generator
    fun toastMe(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    //Save to Shared Preferences
    fun saveToSharedPref(hash:Int){
        val sharedPref = this.getSharedPreferences("Credentials",Context.MODE_PRIVATE)

        val editor = sharedPref?.edit()
        editor?.putInt("user",hash)
        editor?.apply()
    }

    //read from shared preferences
    fun readPref():Int{
        val sharedPref = this.getSharedPreferences("Credentials", Context.MODE_PRIVATE)
        return sharedPref.getInt("user",0)
    }


}
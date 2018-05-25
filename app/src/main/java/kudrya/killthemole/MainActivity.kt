package kudrya.killthemole

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kudrya.killthemole.core.CreatorRequest
import android.app.ActivityManager




class MainActivity : AppCompatActivity() {


    companion object {
        private val APP_PREFERENCES = "authorization"
        private val APP_PREFERENCES_AUTHORIZATION = "getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)"
    }

    var intentService: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (mSettings.contains(APP_PREFERENCES_AUTHORIZATION)) {
            val line = mSettings.getString(APP_PREFERENCES_AUTHORIZATION, "login password").split(" ")
            val login = line[0]
            val password = line[1]
            authorization(login, password)
        }
    }

    @SuppressLint("ShowToast")
    fun clickLogin(v: View?) {
        val editTextLogin: EditText = findViewById(R.id.editTextLogin)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        authorization(editTextLogin.text.toString(), editTextPassword.text.toString())
    }

    private fun authorization(login: String, password: String) {
        val parameters = HashMap<String, String>()
        parameters["login"] = login
        parameters["password"] = password
        val url = CreatorRequest("check", parameters).result
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    if (response == "success") {
                        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = mSettings.edit()
                        editor?.clear()
                        editor?.putString(APP_PREFERENCES_AUTHORIZATION, "$login $password")
                        editor?.apply()
                        val intent = Intent(this@MainActivity, ListActivity::class.java)
                        intent.putExtra("admin", true)
                        intent.putExtra("login", login)
                        intent.putExtra("password", password)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                })
        queue.add(stringRequest)
    }

    fun clickSend(v: View?) {
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        startActivity(intent)
    }

    fun clickNext(v: View?) {
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        startActivity(intent)
    }
}

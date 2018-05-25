package kudrya.killthemole

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kudrya.killthemole.core.CreatorRequest
import kudrya.killthemole.view.ItemPerson
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AlertDialog
import android.widget.EditText



class ListActivity : AppCompatActivity() {

    private var login: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        if (intent.getBooleanExtra("admin", false)) {
            val buttonAdmin: Button = findViewById(R.id.buttonAdmin)
            buttonAdmin.visibility = View.VISIBLE
            login = intent.getStringExtra("login")
            password = intent.getStringExtra("password")
            val parameters = HashMap<String, String>()
            parameters["login"] = login
            parameters["password"] = password
            val url = CreatorRequest("message", parameters).result
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                        if (response != "error") {
                            val message = gson.fromJson(response, Message::class.java)
                            if (message.message == 1) {
                                Toast.makeText(this@ListActivity, "You are being sought. Update your position", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    Response.ErrorListener {
                    })
            queue.add(stringRequest)

        }
        val pane = findViewById<LinearLayout>(R.id.pane)
        val url = CreatorRequest("").result
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    val objs = gson.fromJson(response, ListPerson::class.java)
                    objs.forEach {
                        val item = ItemPerson(this@ListActivity, it.login, it.name)
                        pane.addView(item.view)
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this@ListActivity, "Error", Toast.LENGTH_SHORT).show()
                })
        queue.add(stringRequest)
    }

    override fun onBackPressed() {

    }

    fun clickUpdate(v: View?) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Update information")
        alert.setMessage("Where are you?")
        val input = EditText(this)
        alert.setView(input)
        alert.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, whichButton ->
            val place = input.text.toString()
            val map = HashMap<String, String>()
            map["login"] = login
            map["password"] = password
            map["place"] = place
            val url = CreatorRequest("update", map).result
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response ->
                        if (response == "success") {
                            Toast.makeText(this@ListActivity, "Succes", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ListActivity, "Error", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this@ListActivity, "Error", Toast.LENGTH_SHORT).show()
                    })
            queue.add(stringRequest)

        })
        alert.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
        })
        alert.show()
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private class ListPerson: ArrayList<Person>()
        private class Person {
            lateinit var name: String
            lateinit var login: String
        }
        private class Message {
            lateinit var name: String
            var message: Int = 0
        }
    }
}

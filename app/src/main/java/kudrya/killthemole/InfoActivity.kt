
package kudrya.killthemole

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.gson.GsonBuilder
import kudrya.killthemole.core.CreatorRequest
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import com.android.volley.toolbox.Volley


class InfoActivity : AppCompatActivity() {

    private var login = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        login = intent!!.getStringExtra("login")
        val map = HashMap<String, String>()
        map["login"] = login
        val url = CreatorRequest("info", map).result
        val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
        val textViewName = findViewById<TextView>(R.id.textViewName)
        val textViewPlace = findViewById<TextView>(R.id.textViewPlace)
        val textViewCurrent = findViewById<TextView>(R.id.textViewCurrent)
        val textViewTime = findViewById<TextView>(R.id.textViewTime)
        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    val person = gson.fromJson(response, Person::class.java)
                    if (person.image != null) {
                        val decodedString = Base64.decode(person.image, Base64.DEFAULT)
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        imageViewPhoto.setImageBitmap(decodedByte)
                    }
                    textViewName.text = person.name
                    textViewPlace.text = "Place: " + person.place
                    textViewCurrent.text = "Current place: " +person.current
                    textViewTime.text = "Update time: " + person.time
                },
                Response.ErrorListener {
                    super.onBackPressed()
                })
        queue.add(stringRequest)
    }

    fun clickBack(v: View?) {
        super.onBackPressed()
    }

    fun clickSend(v: View?) {
        val queue = Volley.newRequestQueue(this)
        val map = HashMap<String, String>()
        map["login"] = login
        val url = CreatorRequest("send", map).result
        val stringRequest = StringRequest(Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Toast.makeText(this@InfoActivity, "Success", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener {
                    Toast.makeText(this@InfoActivity, "Error", Toast.LENGTH_SHORT).show()
                })
        queue.add(stringRequest)
    }

    companion object {
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private class Person {
            lateinit var name: String
            var place: String? = null
            var current: String? = null
            var time: String? = null
            var image: String? = null
        }
    }
}

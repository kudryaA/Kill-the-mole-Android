package kudrya.killthemole.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import kudrya.killthemole.InfoActivity
import kudrya.killthemole.ListActivity
import kudrya.killthemole.R

class ItemPerson(private val context: Context, val login: String, val name: String) {
    @SuppressLint("InflateParams")
    val view: View = LayoutInflater.from(context).inflate(R.layout.item_pane, null)

    init {
        val textView = view.findViewById<Button>(R.id.textView)
        textView.text = name
        textView.setOnClickListener({
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra("login", login)
            context.startActivity(intent)
        })
    }
}
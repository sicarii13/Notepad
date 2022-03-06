package thinktankesolutions.com.notepad

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddNote : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val btnAdd: Button = findViewById(R.id.btnAdd)
        val etTitle: EditText = findViewById(R.id.etTitle)
        val etDesc: EditText = findViewById(R.id.etDescription)

        try {
            val bundle : Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id!=0)
            {
                //update note
                //change actionbar title
                supportActionBar!!.title = "Update Note"
                //change button text
                btnAdd.text = "Update"
                etTitle.setText(bundle.getString("name"))
                etDesc.setText(bundle.getString("des"))
            }
        }catch (ex : Exception){

        }

    }

    fun addFunc(view: View)
    {
        var dbManager = DbManager(this)

        var values = ContentValues()

        val etTitle: EditText = findViewById(R.id.etTitle)
        val etDesc: EditText = findViewById(R.id.etDescription)

        values.put("Title", etTitle.text.toString())
        values.put("Description", etDesc.text.toString())

        if (id == 0)
        {
            val ID = dbManager.insert(values)
            if (ID>0)
            {

                Toast.makeText(this, "Note is added", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                Toast.makeText(this, "Error adding note...", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            var selectionArgs = arrayOf(id.toString())
            val ID = dbManager.update(values, "ID=?", selectionArgs)
            if (ID>0)
            {
                Toast.makeText(this, "Note is updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            else
            {
                Toast.makeText(this, "Error updating note...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
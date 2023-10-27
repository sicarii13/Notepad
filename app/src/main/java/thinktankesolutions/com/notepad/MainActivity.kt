package thinktankesolutions.com.notepad

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.SearchManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    //sample comment
    var listNotes = ArrayList<Note>()

    //shared preference
    var mSharedPref : SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSharedPref = this.getSharedPreferences("My_Data", Context.MODE_PRIVATE)

        //load sorting technique as selected before, default setting will be newest
        val mSorting = mSharedPref!!.getString("Sort", "newest")
        when(mSorting)
        {
            "newest" -> LoadQueryNewest("%")
            "oldest" -> LoadQueryOldest("%")
            "ascending" -> LoadQueryAscending("%")
            "descending" -> LoadQueryDescending("%")
        }
    }

    override fun onResume() {
        super.onResume()
        //load sorting technique as selected before, default setting will be newest
        val mSorting = mSharedPref!!.getString("Sort", "newest")
        when(mSorting)
        {
            "newest" -> LoadQueryNewest("%")
            "oldest" -> LoadQueryOldest("%")
            "ascending" -> LoadQueryAscending("%")
            "descending" -> LoadQueryDescending("%")
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryAscending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        //sort by title
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        //ascending
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToNext())
        }

        //adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        //set adapter
        var lvNotes: ListView = findViewById(R.id.lvNotes)
        lvNotes.adapter = myNotesAdapter

        //get total number of tasks from List
        val total = lvNotes.count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryDescending(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        //sort by title
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        listNotes.clear()
        //descending
        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToPrevious())
        }

        //adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        //set adapter
        var lvNotes: ListView = findViewById(R.id.lvNotes)
        lvNotes.adapter = myNotesAdapter

        //get total number of tasks from List
        val total = lvNotes.count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryNewest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        //sort by ID
        val cursor = dbManager.Query(projections, "ID like ?", selectionArgs, "ID")
        listNotes.clear()

        //Newest first(the record will be entered at the bottom of previous records and has larger ID then previous records)

        if (cursor.moveToLast()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToPrevious())
        }

        //adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        //set adapter
        var lvNotes: ListView = findViewById(R.id.lvNotes)
        lvNotes.adapter = myNotesAdapter

        //get total number of tasks from List
        val total = lvNotes.count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    @SuppressLint("Range")
    private fun LoadQueryOldest(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        //sort by ID
        val cursor = dbManager.Query(projections, "ID like ?", selectionArgs, "ID")
        listNotes.clear()

        //oldest first(the record will be entered at the bottom of previous records and has larger ID then previous records, so lesser the ID is the oldest the record is)

        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                listNotes.add(Note(ID, Title, Description))

            } while (cursor.moveToNext())
        }

        //adapter
        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        //set adapter
        var lvNotes: ListView = findViewById(R.id.lvNotes)
        lvNotes.adapter = myNotesAdapter

        //get total number of tasks from List
        val total = lvNotes.count
        //actionbar
        val mActionBar = supportActionBar
        if (mActionBar != null)
        {
            //set to actionbar as subtitle of actionbar
            mActionBar.subtitle = "You have $total note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView

        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                LoadQueryAscending("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQueryAscending("%"+newText+"%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null)
        {
            when(item.itemId)
            {
                R.id.addNote ->
                {
                    startActivity(Intent(this, AddNote::class.java))
                }
                R.id.action_sort ->
                {
                    //show sorting dialog
                    showSortDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val sortOption = arrayOf("Newest", "Older", "Title(Ascending)", "Title(Descending)")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Sort by")
        mBuilder.setIcon(R.drawable.ic_sort_black_24dp)
        mBuilder.setSingleChoiceItems(sortOption, -1)
        {
                dialogInterface, i ->
            if (i==0)
            {
                //newest first
                Toast.makeText(this,"Newest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "newest")
                editor.apply()
                LoadQueryNewest("%")

            }
            if (i==1)
            {
                //older first
                Toast.makeText(this,"Oldest",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "oldest")
                editor.apply()
                LoadQueryOldest("%")
            }
            if (i==2)
            {
                //title ascending
                Toast.makeText(this,"Title(Ascending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "ascending")
                editor.apply()
                LoadQueryAscending("%")
            }
            if (i==3)
            {
                //title descending
                Toast.makeText(this,"Title(Descending)",Toast.LENGTH_SHORT).show()
                val editor = mSharedPref!!.edit()
                editor.putString("Sort", "Descending")
                editor.apply()
                LoadQueryDescending("%")
            }
            dialogInterface.dismiss()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotesAdapter: ArrayList<Note>) : super() {
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //inflate layout row.xml
            var myView = layoutInflater.inflate(R.layout.row, null)
            val myNote = listNotesAdapter[position]

            val tvTitle: TextView = myView.findViewById(R.id.tvTitle)
            val tvDescription: TextView = myView.findViewById(R.id.tvDescription)
            val ibDelete: ImageButton = myView.findViewById(R.id.ibDelete)
            val ibEdit: ImageButton = myView.findViewById(R.id.ibEdit)
            val ibCopy: ImageButton = myView.findViewById(R.id.ibCopy)
            val ibShare: ImageButton = myView.findViewById(R.id.ibShare)

            tvTitle.text = myNote.noteName
            tvDescription.text = myNote.noteDes
            //delete btn click
            ibDelete.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.noteID.toString())
                dbManager.delete("ID=?", selectionArgs)
                LoadQueryAscending("%")
            }
            //edit//update button click
            ibEdit.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            //copy btn click
            ibCopy.setOnClickListener {
                //get title
                val title = tvTitle.text.toString()
                //get description
                val desc = tvDescription.text.toString()
                //concatenate
                val s = title + "\n" + desc
                val cb = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                cb.text = s // add to clipboard
                Toast.makeText(this@MainActivity, "Copied...", Toast.LENGTH_SHORT).show()
            }
            //share btn click
            ibShare.setOnClickListener {

                //get title
                val title = tvTitle.text.toString()
                //get description
                val desc = tvDescription.text.toString()
                //concatenate
                val s = title + "\n" + desc
                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))

            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: Note) {
        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("ID", myNote.noteID)
        intent.putExtra("name", myNote.noteName)
        intent.putExtra("des", myNote.noteDes)
        startActivity(intent)
    }
}
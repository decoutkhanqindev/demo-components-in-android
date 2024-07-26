package com.example.democomponentsinandroid.contentprovider

import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.example.democomponentsinandroid.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityContactsBinding.inflate(layoutInflater)
    }

    companion object {
        private val URI = Uri.parse("content://com.decoutkhanqindev.student.provider/students")
    }

    private val readContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            readContacts()
        } else {
            Toast.makeText(this, "Denied Permission", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            readContacts()
        } else {
            //  dialog xin quyen
            readContactsPermissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
        }

        // demo students content provider
//        binding.textView.setOnClickListener {
//            contentResolver.insert(
//                /* url = */ StudentsContentProvider.CONTENT_URI,
//                /* values = */ contentValuesOf(StudentsContentProvider.COLUMN_NAME to "Student ${System.currentTimeMillis()}")
//                ).let {
//                Log.d("ContactsActivity", "Inserted student: $it")
//            }
//        }

        // demo my students content provider
        binding.textView.setOnClickListener {
            val cursor = contentResolver.query(URI, arrayOf("id", "name"), null, null, "id ASC")
            cursor?.use {
                while (cursor.moveToNext()) {
                    Log.d("ContactsActivity","Start Query Students\n")
                    while(cursor.moveToNext()) {
                        val id = cursor.getLongOrNull(cursor.getColumnIndexOrThrow("id"))
                        val name = cursor.getStringOrNull(cursor.getColumnIndexOrThrow("name"))
                        Log.d("ContactsActivity","id=$id, name=$name\n")
                    }
                    Log.d("ContactsActivity","End Query Students")
                }
            }
        }

        contentResolver.registerContentObserver(
            /* uri = */ StudentsContentProvider.CONTENT_URI,
            /* notifyForDescendants = */ false,
            /* observer = */ object : android.database.ContentObserver(null) {
                override fun onChange(selfChange: Boolean) {
                    super.onChange(selfChange)
                    queryStudents()
                    Log.d("ContactsActivity", "$selfChange")
                }
            }
        )
    }

    private fun readContacts() {
        // obj cua content provider
        val resolver: ContentResolver = this.contentResolver

        val cursor: Cursor? =
            resolver.query(
                /* uri = */ ContactsContract.Contacts.CONTENT_URI,
                /* projection = */ arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME),
                /* selection = */ "${ContactsContract.Contacts.DISPLAY_NAME} LIKE ?",
                /* selectionArgs = */arrayOf("abc%"),
                /* sortOrder = */"${ContactsContract.Contacts.DISPLAY_NAME} ASC"
            )

        // File("112").bufferedReader().use {  } // sau khi handle file xong se tu dong close

        cursor?.use {
            // use cursor -> auto close
            val contacts = mutableListOf<Contact>()
            val idColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
            val nameColumnIndex = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getStringOrNull(idColumnIndex)
                val name = cursor.getStringOrNull(nameColumnIndex)
                if (id != null && name != null) {
                    contacts.add(Contact(id, name))
                }
            }

            if (contacts.isEmpty()) {
                binding.textView.text = "No contacts found"
            } else {
                binding.textView.text = contacts.joinToString("\n") {it.toString()}
            }
        }
        // cursor is closed
        Log.d("ContactsActivity", "Cursor is closed: ${cursor?.isClosed}")
    }

    private fun queryStudents() {
        val cursor = contentResolver.query(
            /* uri = */ StudentsContentProvider.CONTENT_URI,
            /* projection = */ arrayOf(StudentsContentProvider.COLUMN_ID, StudentsContentProvider.COLUMN_NAME),
            /* selection = */ null,
            /* selectionArgs = */ null,
            /* sortOrder = */ "${StudentsContentProvider.COLUMN_ID} ASC"
        )

        cursor?.use {
            Log.d("ContactsActivity","Start Query Students\n")
            while(cursor.moveToNext()) {
                val id = cursor.getLongOrNull(cursor.getColumnIndexOrThrow(StudentsContentProvider.COLUMN_ID))
                val name = cursor.getStringOrNull(cursor.getColumnIndexOrThrow(StudentsContentProvider.COLUMN_NAME))
                Log.d("ContactsActivity","id=$id, name=$name\n")
            }
            Log.d("ContactsActivity","End Query Students")
        }
    }
}

data class Contact(val id: String, val name: String)
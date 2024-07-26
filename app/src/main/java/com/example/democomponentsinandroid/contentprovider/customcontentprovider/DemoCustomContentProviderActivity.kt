package com.example.democomponentsinandroid.contentprovider.customcontentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getStringOrNull
import com.example.democomponentsinandroid.databinding.ActivityDemoCustomContentProviderBinding

class DemoCustomContentProviderActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDemoCustomContentProviderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Button to insert a new student record
        binding.insertButton.setOnClickListener {
            val contentValues = ContentValues().apply {
                put(StudentsContentProvider.COLUMN_NAME, binding.textName.text.toString())
            }
            contentResolver.insert(
                /* url = */ StudentsContentProvider.CONTENT_URI,
                /* values = */ contentValues
            ).let {
                Toast.makeText(this, "New Record Inserted", Toast.LENGTH_LONG).show()
            }
        }

        // Button to delete a student record by ID
        binding.deleteButton.setOnClickListener {
            val studentId = binding.textID.text.toString().trim()
            val uri = ContentUris.withAppendedId(
                StudentsContentProvider.CONTENT_URI,
                studentId.toLong()
            )
            val rowsDeleted = contentResolver.delete(
                /* url = */ uri,
                /* where = */ "${StudentsContentProvider.COLUMN_ID} = ?",
                /* selectionArgs = */ arrayOf(studentId)
            )
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show()
            }
        }

        // Button to load and display student records
        binding.loadButton.setOnClickListener {
            val cursor = contentResolver.query(
                /* uri = */ StudentsContentProvider.CONTENT_URI,
                /* projection = */
                arrayOf(StudentsContentProvider.COLUMN_ID, StudentsContentProvider.COLUMN_NAME),
                /* selection = */
                null,
                /* selectionArgs = */
                null,
                /* sortOrder = */
                "${StudentsContentProvider.COLUMN_ID} ASC"
            )

            cursor?.use {
                val idColumnIndex = cursor.getColumnIndexOrThrow(StudentsContentProvider.COLUMN_ID)
                val nameColumnIndex = cursor.getColumnIndexOrThrow(StudentsContentProvider.COLUMN_NAME)
                val arrStudents = StringBuilder()
                while (cursor.moveToNext()) {
                    val id = cursor.getStringOrNull(idColumnIndex)
                    val name = cursor.getStringOrNull(nameColumnIndex)
                    arrStudents.append("id=$id, name=$name\n")
                }
                binding.result.text = arrStudents.toString()
            }
        }
    }
}

package com.example.democomponentsinandroid.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class StudentDatabaseHelper(
    context: Context
) : SQLiteOpenHelper(
    /* context = */ context,
    /* name = */ "students.db",
    /* factory = */ null,
    /* version = */ 1
) {
    companion object {
        const val TABLE_NAME = "students"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
    }

    // create table
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """CREATE TABLE $TABLE_NAME (
            |$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            |$COLUMN_NAME TEXT NOT NULL
            )
            """.trimMargin()
        )
    }

    // update version old to new
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}

class StudentsContentProvider: ContentProvider() {
    private lateinit var databaseHelper: StudentDatabaseHelper

    override fun onCreate(): Boolean {
        databaseHelper = StudentDatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val  db = databaseHelper.readableDatabase
        val cursor: Cursor? = db.query(
            /* table = */ StudentDatabaseHelper.TABLE_NAME,
            /* columns = */ projection,
            /* selection = */ selection,
            /* selectionArgs = */ selectionArgs,
            /* groupBy = */ null,
            /* having = */ null,
            /* orderBy = */ sortOrder
        )

        // register to watch a content URI for changes
        cursor?.setNotificationUri(context!!.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.dir/$AUTHORITY.students"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val db = databaseHelper.writableDatabase
        val rowId = db.insert(
            /* table = */ StudentDatabaseHelper.TABLE_NAME,
            /* nullColumnHack = */ null,
            /* values = */ values
        )

        context?.contentResolver?.notifyChange(uri, null)

        return ContentUris.withAppendedId(CONTENT_URI, rowId)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val AUTHORITY = "com.student.contentProvider"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/students")!!
        const val COLUMN_ID = StudentDatabaseHelper.COLUMN_ID
        const val COLUMN_NAME = StudentDatabaseHelper.COLUMN_NAME
    }
}

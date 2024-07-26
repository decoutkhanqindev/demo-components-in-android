package com.example.democomponentsinandroid.contentprovider.customcontentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class StudentsContentProvider : ContentProvider() {

    companion object {
        // Authority of the content provider
        private const val AUTHORITY = "com.decoutkhanqindev.student.provider"

        // Content URI
        private const val URL = "content://$AUTHORITY/students"
        val CONTENT_URI: Uri = Uri.parse(URL)

        // Constants for database
        private const val DB_NAME = "my_students_db"
        private const val TABLE_NAME = "my_students_tb"
        internal const val COLUMN_ID = "id"
        internal const val COLUMN_NAME = "name"
        private const val DB_VERSION = 1
        private const val CREATE_DB_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL
            );
        """

        // URI matching codes
        private const val URI_CODE = 1
        private val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "students", URI_CODE)
            addURI(AUTHORITY, "students/*", URI_CODE)
        }
    }

    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val dbHelper = StudentsDatabaseHelper(context)
        db = dbHelper.writableDatabase
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder().apply {
            tables = TABLE_NAME
            when (uriMatcher.match(uri)) {
                URI_CODE -> projectionMap = null // or set your projection map if needed
                else -> throw IllegalArgumentException("Unknown URI $uri")
            }
        }

        val cursor = qb.query(
            db, projection, selection, selectionArgs, null, null, sortOrder
        )
        cursor?.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            URI_CODE -> "vnd.android.cursor.dir/students"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val rowId = db!!.insert(TABLE_NAME, null, values)
        if (rowId > 0) {
            val newUri = ContentUris.withAppendedId(CONTENT_URI, rowId)
            context!!.contentResolver.notifyChange(newUri, null)
            return newUri
        }
        throw SQLiteException("Failed to add a record into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            URI_CODE -> db!!.delete(TABLE_NAME, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ): Int {
        val count = when (uriMatcher.match(uri)) {
            URI_CODE -> db!!.update(TABLE_NAME, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    private class StudentsDatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context, DB_NAME, null, DB_VERSION
    ) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(CREATE_DB_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
}

package com.example.democomponentsinandroid.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) : SQLiteOpenHelper(
    /* context = */ context,
    /* name = */ "students.db",
    /* factory = */ null,
    /* version = */ 1
) {
    companion object {
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    // create table
    override fun onCreate(db: SQLiteDatabase?) {

    }

    // update version old to new
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

}

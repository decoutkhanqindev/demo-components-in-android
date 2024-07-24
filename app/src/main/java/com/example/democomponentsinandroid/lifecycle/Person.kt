package com.example.democomponentsinandroid.lifecycle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Person(
    val id: String,
    val name: String
) : Parcelable

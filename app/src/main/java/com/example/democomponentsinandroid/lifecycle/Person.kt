package com.example.democomponentsinandroid.lifecycle

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Person(
    val id: String,
    val name: String
) : Parcelable

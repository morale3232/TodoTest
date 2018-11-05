package com.example.morale.todotestandroid

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

class TodoItem (
        var docID: String = "No doc ID",
        var docRef: DocumentReference? = null,
        var title: String = "No title",
        var completed: Boolean = false,
        var owner_id: String = "No owner_id",
        var dateAdded: String = "No date added"
) {

    constructor(snapshot: DocumentSnapshot) : this() {
        val data = snapshot.data
        docID = snapshot.id
        docRef = snapshot.reference
        title = data?.get("title") as String
        completed = (data["completed"] as String).toBoolean()
        owner_id = data["owner_id"] as String
        dateAdded = data["dateAdded"] as String

    }
    fun toAnyObject() : Map<String, Any> = mapOf(
                "title" to title,
                "owner_id" to owner_id,
                "completed" to completed,
                "dateAdded" to dateAdded
                )
}

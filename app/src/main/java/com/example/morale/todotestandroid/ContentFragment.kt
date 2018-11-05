package com.example.morale.todotestandroid

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_fragment.*
import java.text.SimpleDateFormat
import java.util.*


class ContentFragment : Fragment() {

    private var listOfFragmentTodos = ArrayList<TodoItem>()
    private lateinit var adapter: TodoListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var reference: FirebaseFirestore
    private lateinit var swipeContainer: SwipeRefreshLayout

    companion object {
        fun newInstance(): ContentFragment {
            return ContentFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("ARTHUR", "onCreateView: ")
        val view = inflater.inflate(R.layout.content_fragment, container, false)

        reference = FirebaseFirestore.getInstance()

        adapter = TodoListAdapter(listOfFragmentTodos)

        swipeContainer = view.findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            fetchAllTodoItems()
        }

        recyclerView = view?.findViewById(R.id.todo_list_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        return view
    }

    override fun onAttach(context: Context?) {
        Log.d("ARTHUR", "onAttach: ")
        super.onAttach(context)
        fetchAllTodoItems()
    }

    private fun fetchAllTodoItems() {
        Log.d(Constants.TAG, "Fetching list of todo items")
        FirebaseFirestore.getInstance()
                .collection("items").get()
                .addOnCompleteListener {
                    listOfFragmentTodos.clear()
                    it.result?.documentChanges?.forEach {
                        val item = it.document.toObject(TodoItem::class.java)
                        Log.d("ARTHUR", "In completion block: ${item.toAnyObject()}")
                        listOfFragmentTodos.add(item)
                    }
                    adapter.notifyDataSetChanged()
                    swipeContainer.isRefreshing = false
                    Log.d("ARTHUR", "notifyDataSetChanged: ")
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_button?.setOnClickListener {
            Log.d("MORALE", "Add button clicked")
            askForNewItem()
        }
    }

    private fun askForNewItem() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add New Item")
        builder.setMessage("Enter the item's name.")
        val textView = EditText(activity)
        builder.setView(textView)
        builder.setPositiveButton("Done") { _, _ ->
            val newTodoName = textView.text.toString()
            val ownerID = FirebaseAuth.getInstance().currentUser?.uid ?: "No owner ID"
            val calendar = Calendar.getInstance()
            val dateString = SimpleDateFormat("MM-dd-yyyy", Locale.US).format(calendar.time)
            val newItem = TodoItem(title = newTodoName, owner_id = ownerID, dateAdded = dateString)

            reference.collection(Constants.ITEMS).add(newItem.toAnyObject())
                    .addOnSuccessListener {
                        Log.d(Constants.TAG, "Add new item successful: ${newItem.toAnyObject()}")
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        Log.d(Constants.TAG, "Add new item FAILED")
                        Toast.makeText(context, "Add new item failed! Try again.", Toast
                                .LENGTH_SHORT).show()
                    }
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.create().show()
    }
}

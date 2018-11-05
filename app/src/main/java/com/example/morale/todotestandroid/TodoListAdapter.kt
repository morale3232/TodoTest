package com.example.morale.todotestandroid

import android.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.todo_item_recycler_row.view.*

class TodoListAdapter(private var listOfTodos: ArrayList<TodoItem>)
    : RecyclerView.Adapter<TodoListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("ARTHUR", "onCreateViewHolder: ")
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout
                .todo_item_recycler_row, parent, false)
        return MyViewHolder(inflatedView)
    }

    override fun getItemCount() = listOfTodos.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("ARTHUR", "onBindViewHolder: $position")
        val todoItem = listOfTodos[position]
        Log.d("ARTHUR", "In onBindViewHolder: ${todoItem.toAnyObject()}")
        holder.bindTodoItem(todoItem, position)
    }

    class MyViewHolder(private val v: View) : RecyclerView.ViewHolder(v), View.OnClickListener,
            View.OnLongClickListener {

        private lateinit var todoItem: TodoItem
        private var titleTV: TextView = v.findViewById(R.id.todo_title)
        private var radioButton: RadioButton = v.findViewById(R.id.todo_radio_button)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.todo_radio_button?.isChecked = !(v?.todo_radio_button?.isChecked ?: false)
        }

        override fun onLongClick(v: View?): Boolean {
            return requestDeleteItem(v)
        }

        fun bindTodoItem(todo: TodoItem, position: Int) {
            Log.d("ARTHUR", "bindTodoItem: ${todo.title}")
            todoItem = todo
            titleTV.text = todo.title
            radioButton.isChecked = todo.completed
            v.contentDescription = position.toString()
        }

        private fun requestDeleteItem(view: View?) : Boolean {
            val builder = AlertDialog.Builder(v.context)
            builder.setTitle("Delete this item?")
            val description = view?.contentDescription.toString().toInt()
            Log.d(Constants.TAG, description.toString())

            builder.setPositiveButton("OK") { _, _ ->
                // TODO delete item, update list and UI
            }
            builder.setNegativeButton("Cancel") {_, _ -> }
            builder.create().show()
            return true
        }
    }
}

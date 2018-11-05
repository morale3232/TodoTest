package com.example.morale.todotestandroid

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.todo_item_recycler_row.view.*

class FirebaseRecyclerAdapter(private var todos: ArrayList<TodoItem>) : RecyclerView.Adapter<FirebaseRecyclerAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_recycler_row, parent, false)
        return TestViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val item = todos[position]
        holder.bindTodoItem(item)
    }

    override fun getItemCount(): Int {
        return todos.size
    }



    class TestViewHolder(private val v: View) : RecyclerView.ViewHolder(v) {
        private lateinit var item: TodoItem

        fun bindTodoItem(todoItem: TodoItem) {
            item = todoItem
            v.todo_title.text = todoItem.title
            v.todo_radio_button.isChecked = todoItem.completed
        }
    }
}

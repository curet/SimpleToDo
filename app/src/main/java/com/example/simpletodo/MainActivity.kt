package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }

        }

        loadItems()

        // Look up for recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // so that recycleView and adapter know each other
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // button and input fields to allow user enter a task
        findViewById<Button>(R.id.button).setOnClickListener {

            val userInputTask = inputTextField.text.toString()

            listOfTasks.add(userInputTask)
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // reset text field to allow enter next task easily
            inputTextField.setText("")

            saveItems()
        }
    }

    // save input data
    fun readFile() : File{
        return File(filesDir, "data.txt")
    }

    fun loadItems(){
        try{
            listOfTasks = FileUtils.readLines(readFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    fun saveItems(){
        try{
            FileUtils.writeLines(readFile(), listOfTasks)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

}
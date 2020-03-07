package com.asif.paginationdemo

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private var mAdapter: DataAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var studentList: ArrayList<Student?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        studentList = ArrayList()
        handler = Handler()
        loadDummyData()
        recyclerViewPagination.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        recyclerViewPagination.layoutManager = mLayoutManager
        mAdapter = DataAdapter(studentList, recyclerViewPagination)
        recyclerViewPagination.adapter = mAdapter
        //if empty size then we hide recycler view
        if (studentList!!.isEmpty()) {
            recyclerViewPagination.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerViewPagination.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
        //load more screen
        mAdapter!!.setOnLoadMoreListener(onLoadMoreListener = OnLoadMoreListener {
            studentList!!.add(null)
            mAdapter!!.notifyItemInserted(studentList!!.size - 1)
            mAdapter!!.notifyItemRemoved(studentList!!.size)
            handler!!.postDelayed({
                //   remove progress item
                studentList!!.removeAt(studentList!!.size - 1)
                mAdapter!!.notifyItemRemoved(studentList!!.size)
                //add items one by one
                val start = studentList!!.size
                val end = start + 20
                for (i in start + 1..end) {
                    studentList!!.add(
                        Student(
                            "Student $i",
                            "AndroidStudent$i@gmail.com"
                        )
                    )
                    mAdapter!!.notifyItemInserted(studentList!!.size)
                }
                mAdapter!!.setLoaded()
                //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
            }, 2000)

        })


    }

    private fun loadDummyData() {
        for (i in 0..20) {
            studentList!!.add(Student("Student $i", "androidstudent$i@gmail.com"))
        }
    }



}

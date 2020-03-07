package com.asif.paginationdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class DataAdapter(private val studentList: ArrayList<Student?>?, recyclerView: RecyclerView) :
    RecyclerView.Adapter<ViewHolder>() {

    var viewItem = 1
    private var viewProgress = 0
    var visibleThreshold = 5
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    private var loading = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    init {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView
                .layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(
                    recyclerView: RecyclerView,
                    dx: Int, dy: Int
                ) {
                    super.onScrolled(recyclerView, dx, dy)
                    totalItemCount = linearLayoutManager!!.itemCount
                    lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition()
                    if (!loading
                        && totalItemCount <= lastVisibleItem + visibleThreshold
                    ) {
                        onLoadMoreListener?.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (studentList!![position] != null) viewItem else viewProgress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vh: ViewHolder
        if (viewType == viewItem) {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.list_item, parent, false
            )
            vh = StudentViewHolder(v)
        } else {
            val v: View = LayoutInflater.from(parent.context).inflate(
                R.layout.progressbar_load_more, parent, false
            )
            vh = ProgressViewHolder(v)
        }
        return vh

    }

    override fun getItemCount(): Int {
        return studentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is StudentViewHolder) {
            val singleStudent = studentList!![position]
            holder.tvName.text = singleStudent!!.name
            holder.tvEmailId.text = singleStudent.emailId
            holder.student = singleStudent
        } else {
            (holder as ProgressViewHolder).progressBar.isIndeterminate = true
        }
    }

    fun setLoaded() {
        loading = false
    }


    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener?) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    companion object {
        class StudentViewHolder(v: View) : ViewHolder(v) {
            var tvName: TextView = v.findViewById<View>(R.id.itemName) as TextView
            var tvEmailId: TextView = v.findViewById<View>(R.id.itemEmail) as TextView
            var student: Student? = null

        }

        class ProgressViewHolder(v: View) : ViewHolder(v) {
            var progressBar: ProgressBar = v.findViewById<View>(R.id.progressBar1) as ProgressBar

        }
    }
}
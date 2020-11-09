package com.myniprojects.newsi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myniprojects.newsi.R
import com.myniprojects.newsi.utils.getDateFormatted

class SeparatorViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
{
    private val description: TextView = view.findViewById(R.id.txtTitle)

    fun bind(date: String)
    {
        description.text = date.getDateFormatted(view.context)
    }

    companion object
    {
        fun create(parent: ViewGroup): SeparatorViewHolder
        {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.news_separator_item, parent, false)
            return SeparatorViewHolder(view)
        }
    }
}
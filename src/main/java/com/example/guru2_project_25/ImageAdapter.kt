package com.example.guru2_project_25

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.example.guru2_project_25.R

class ImageAdapter(private val context: Context, private val images: Array<Int>) : BaseAdapter() {

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView
        if (convertView == null) {
            imageView = ImageView(context)
            //imageView.layoutParams = GridView.LayoutParams(200, 200) // Set the size of each item
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8) // Set padding between items
        } else {
            imageView = convertView as ImageView
        }

        imageView.setImageResource(images[position])

        return imageView
    }
}

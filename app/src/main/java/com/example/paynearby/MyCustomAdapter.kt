package com.example.paynearby

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class MyCustomAdapter(var topImageList: MutableList<String>?, var ctx: Context) : PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater
    lateinit var context:Context

    override fun getCount(): Int {
        return topImageList!!.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(ctx)
        var view = layoutInflater.inflate(R.layout.item, container, false)
        try {
            val img = view.findViewById<ImageView>(R.id.simpleimg)
            img.setImageURI(Uri.parse(topImageList!!.get(position)))
            container.addView(view)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
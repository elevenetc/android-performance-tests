package com.elevenetc.android.flat.performance

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ITEMS_COUNT = 150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainAdapter()
    }

    class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            //val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return buildTextView(parent)
            //return buildDeep(parent)
        }

        private fun buildTextView(parent: ViewGroup): MainViewHolder {
            return MainViewHolder(TextView(parent.context))
        }

        private fun buildDeep(parent: ViewGroup): MainViewHolder {
            var i = 15
            val context = parent.context
            var rl = RelativeLayout(context)

            while (i > 0) {

                val tv = TextView(context)
                tv.text = i.toString()
                rl.addView(tv)

                val newRl = RelativeLayout(context)
                newRl.addView(rl)

                rl = newRl

                i--
            }

            return MainViewHolder(rl)
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            //val ch0 = (holder.itemView as RelativeLayout).getChildAt(0)
            //val ch1 = (holder.itemView as RelativeLayout).getChildAt(1)

            //Thread.sleep(100)

            if (holder.itemView is TextView) {
                holder.itemView.text = "This is item $position"
            }

            //(holder.itemView as TextView).text = "This is item $position"
            // Add sleep to recreate work on the main thread
//           Thread.sleep(150)
//        if (position % 20 == 0) {
//            try {
//                Thread.sleep(150)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        }
        }

        override fun getItemCount(): Int = ITEMS_COUNT

    }

    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view)

}

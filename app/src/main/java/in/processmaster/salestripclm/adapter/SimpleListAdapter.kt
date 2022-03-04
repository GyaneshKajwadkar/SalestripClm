package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SimpleListAdapter(var mainList: ArrayList<String>, var subList: ArrayList<Int>) : RecyclerView.Adapter<SimpleListAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleListAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.simple_list_view, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var header=view.findViewById<TextView>(R.id.header)
        var subHeader=view.findViewById<TextView>(R.id.subHeader)
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        if(subList.size==0)
        {
            holder.subHeader.visibility=View.GONE
        }
        else{
            holder.subHeader.setText("Qty:-"+subList.get(position))
        }
        holder.header.setText(mainList.get(position).lowercase())
    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}
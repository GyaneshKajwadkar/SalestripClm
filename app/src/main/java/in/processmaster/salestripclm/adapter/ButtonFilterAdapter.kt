package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.CommonModel
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView

class ButtonFilterAdapter(
    val categoryList: java.util.ArrayList<CommonModel.FilterModel>,
    val pobProductSelectAdapter: PobProductAdapter
) : RecyclerView.Adapter<ButtonFilterAdapter.MyViewHolder>(){

    var selectedCategoryList:ArrayList<CommonModel.FilterModel> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ButtonFilterAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_button_adapter, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var togglebutton = view.findViewById<ToggleButton>(R.id.toggleButton)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        var value= categoryList.get(position)
        if(value.categoryName.isEmpty())
        {
            holder.togglebutton.setTextOff("NA")
            holder.togglebutton.setTextOn("NA")
            holder.togglebutton.text="NA"
        }
        else
        {
            holder.togglebutton.setTextOff(value.categoryName)
            holder.togglebutton.setTextOn(value.categoryName)
            holder.togglebutton.text=value.categoryName
        }

        if(value.isSelected==false) holder.togglebutton.isChecked=false

        holder.togglebutton.setOnClickListener {
            if(holder.togglebutton.isChecked)
            {
                for(category in categoryList)
                {
                    if(category.categoryName.equals(value.categoryName)) category.isSelected=true
                        else  category.isSelected=false
                }

                selectedCategoryList.add(value)
           }
            else {
                for(category in categoryList)
                { category.isSelected=false }
                selectedCategoryList.clear()
            }
            pobProductSelectAdapter.filterUsingSelection(selectedCategoryList)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return categoryList?.size!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}
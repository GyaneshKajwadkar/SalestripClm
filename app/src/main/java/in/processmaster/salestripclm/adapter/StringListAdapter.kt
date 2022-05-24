package `in`.processmaster.salestripclm.adapter
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.interfaceCode.StringInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class StringListAdapter(
    val stringList: ArrayList<String>,
    var mCallback : StringInterface
) : RecyclerView.Adapter<StringListAdapter.ViewHolders>() {

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolders {
        return ViewHolders(LayoutInflater.from(parent.context).inflate(R.layout.string_adapter_view, parent, false))
    }


    override fun onBindViewHolder(holder:ViewHolders, position: Int) {
        val modeldata = stringList?.get(position)
        holder.stringText.text=modeldata

        holder.parentStringLayout.setOnClickListener {
            selectedPosition=position;
            notifyDataSetChanged();
            mCallback.onClickString(modeldata)
        }

        if(selectedPosition==position)
        {
            holder.stringText.setTextColor(Color.parseColor("#ffffff"))
            holder.stringText.setBackgroundColor(Color.parseColor("#055E99"))
        }

        else
        {
            holder.stringText.setTextColor(Color.parseColor("#000000"))
            holder.stringText.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return stringList!!.size
    }

    inner class ViewHolders(view: View): RecyclerView.ViewHolder(view){
        var stringText=view.findViewById<TextView>(R.id.stringText)
        var parentStringLayout=view.findViewById<LinearLayout>(R.id.parentStringLayout)
    }
}
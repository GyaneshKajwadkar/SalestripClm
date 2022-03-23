package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.PreCallModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class LastRCPA_Adapter(val lastRCPADetails: List<PreCallModel.Data.LastVisitSummary.LastRCPADetail>?) : RecyclerView.Adapter<LastRCPA_Adapter.ViewHolders>() {


    class ViewHolders(view: View) : RecyclerView.ViewHolder(view) {
        var retailer_tv=view.findViewById<TextView>(R.id.retailer_tv)
        var date_tv=view.findViewById<TextView>(R.id.date_tv)
        var ownBrand_tv=view.findViewById<TextView>(R.id.ownBrand_tv)
        var rxUnit_tv=view.findViewById<TextView>(R.id.rxUnit_tv)
        var comp1_tv=view.findViewById<TextView>(R.id.comp1_tv)
        var comp2_tv=view.findViewById<TextView>(R.id.comp2_tv)
        var comp3_tv=view.findViewById<TextView>(R.id.comp3_tv)
        var comp4_tv=view.findViewById<TextView>(R.id.comp4_tv)
        var cp1rx_tv=view.findViewById<TextView>(R.id.cp1rx_tv)
        var cp2rx_tv=view.findViewById<TextView>(R.id.cp2rx_tv)
        var cp3rx_tv=view.findViewById<TextView>(R.id.cp3rx_tv)
        var cp4rx_tv=view.findViewById<TextView>(R.id.cp4rx_tv)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LastRCPA_Adapter.ViewHolders {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.lastrcpa_layout, parent, false)
        return ViewHolders(itemView)
    }

    override fun onBindViewHolder(holder: LastRCPA_Adapter.ViewHolders, position: Int)
    {
        val model=lastRCPADetails?.get(position)
        holder.retailer_tv.setText("Retailer: "+model?.retailerName)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateFormatterSet = SimpleDateFormat("dd-MM-yyyy")
        val startDate: Date = sdf.parse(model?.lastRCPADate)

        holder.date_tv.setText("Date: "+dateFormatterSet.format(startDate))
        holder.ownBrand_tv.setText("Own Brand: "+model?.brandName)
        holder.rxUnit_tv.setText("Rx Unit: "+model?.brandUnits+"Qty")
        holder.comp1_tv.setText("Comp 1: "+model?.getcP1())
        holder.comp2_tv.setText("Comp 2: "+model?.getcP2())
        holder.comp3_tv.setText("Comp 3: "+model?.getcP3())
        holder.comp4_tv.setText("Comp 4: "+model?.getcP4())
        holder.cp1rx_tv.setText("Cp1 RX Unit: "+model?.cpRx1)
        holder.cp2rx_tv.setText("Cp2 RX Unit: "+model?.cpRx2)
        holder.cp3rx_tv.setText("Cp3 RX Unit: "+model?.cpRx3)
        holder.cp4rx_tv.setText("Cp4 RX Unit: "+model?.cpRx4)
    }

    override fun getItemCount(): Int {
        return lastRCPADetails?.size!!
    }
}
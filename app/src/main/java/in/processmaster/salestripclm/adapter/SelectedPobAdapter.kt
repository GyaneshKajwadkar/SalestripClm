package `in`.processmaster.salestripclm.adapter

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.interfaceCode.productTransfer
import `in`.processmaster.salestripclm.models.SyncModel
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SelectedPobAdapter(
    var sendEDetailingArray: ArrayList<SyncModel.Data.Product>?,
    var sendProductInterface: productTransfer?,
    val submiteDetailingactivity: SubmitE_DetailingActivity?
): RecyclerView.Adapter<SelectedPobAdapter.MyViewHolder>() {

    constructor() : this(null, null,null) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_pob_adapter, parent, false)
        return MyViewHolder(itemView)
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titlePobproduct_tv = view.findViewById<TextView>(R.id.titlePobproduct_tv)
        var uom_tv = view.findViewById<TextView>(R.id.uom_tv)
        var rate_tv = view.findViewById<TextView>(R.id.rate_tv)
        var scheme_tv = view.findViewById<TextView>(R.id.scheme_tv)
        var qty_et = view.findViewById<EditText>(R.id.qty_et)
        var free_tv = view.findViewById<TextView>(R.id.free_tv)
        var total_tv = view.findViewById<TextView>(R.id.total_tv)
        var qty_tv = view.findViewById<TextView>(R.id.qty_tv)
        var close = view.findViewById<ImageView>(R.id.close)
        var editIv = view.findViewById<View>(R.id.editIv)
        var cardViewParentSelected = view.findViewById<View>(R.id.cardViewParentSelected)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {

        val model = sendEDetailingArray?.get(position)

        if(model?.notApi?.isSaved==false)
        {
            holder.cardViewParentSelected.visibility=View.GONE
        }
        else
        {
            holder.cardViewParentSelected.visibility=View.VISIBLE
        }

        holder.titlePobproduct_tv.text=model?.productName
        holder.uom_tv.text="UOM: "+model?.packingTypeName
        holder.rate_tv.text="Rate: "+String.format("%.2f", model?.notApi?.rate)
        holder.free_tv.text="Free: "+model?.notApi?.freeQty
        holder.total_tv.text="Total: "+String.format("%.2f", model?.notApi?.amount)
        holder.qty_tv.text="Total Qty: "+model?.notApi?.qty
        holder.qty_et.setText("Qty: "+ model?.notApi?.qty.toString())

        model?.notApi?.scheme.let {
            holder.scheme_tv.visibility=View.VISIBLE
            holder.scheme_tv.text=model?.notApi?.scheme
        }

        holder.editIv.setOnClickListener({
            callEditAlert(model,position)

        })


        holder.close.setOnClickListener({
            model?.notApi=SyncModel.Data.Product.NotApiData()
            model?.let { it1 -> sendEDetailingArray?.set(position, it1) }

            notifyItemChanged(position)
            submiteDetailingactivity?.calculateTotalProduct()
            Handler(Looper.getMainLooper()).postDelayed({
                submiteDetailingactivity?.pobProductSelectAdapter?.notifyItemChanged(position)
            }, 100)


         //   sendEDetailingArray?.let { sendProductInterface?.onClickButtonProduct(it) }
        })

        holder.qty_et.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (hasFocus) {
                    v.postDelayed({
                        if (!v.hasFocus()) {
                            v.requestFocus()
                        }
                    }, 200)
                }
            }
        })

    }

    fun getSchemeObject(
        model: SyncModel.Data.Product,
        s: String
    ): SyncModel.Data.Product
    {
        if( !model?.notApi?.scheme.toString().isEmpty())
        {
            val getFree=s.toString().toInt()/ model.notApi?.salesQtyMain!! *model.notApi?.freeQtyMain!!
            model?.notApi?.freeQty=getFree.toInt()
            val totalQty=getFree+s.toString().toInt()
            model?.notApi?.qty=s.toInt()
            model?.notApi?.amount=s.toInt()*model.price
        }
        else
        {
            model?.notApi?.amount=s.toString().toInt()*model.price
            model?.notApi?.qty=s.toString().toInt()
            model?.notApi?.totalQty=s.toString().toInt()
        }
        return model
    }

    override fun getItemCount(): Int {
        return sendEDetailingArray?.size!!
    }

    override fun getItemViewType(position: Int): Int = position

    fun callEditAlert(model: SyncModel.Data.Product?, position: Int)
    {
        val activity = submiteDetailingactivity as Activity
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.pob_edit_layout, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val ok_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn = dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton
        val alertqty_et = dialogView.findViewById<View>(R.id.alertqty_et) as EditText

        alertqty_et.setText(model?.notApi?.qty.toString())

        ok_btn.setOnClickListener{

            if(alertqty_et.text.isEmpty() || alertqty_et.text.toString().toInt()==0)
            {
                alertqty_et.setError("Required")
                alertqty_et.requestFocus()
                return@setOnClickListener
            }

           val returnModel= model?.let { it1 -> getSchemeObject(it1,alertqty_et.text.toString()) }
            returnModel?.let { it1 -> sendEDetailingArray?.set(position, it1) }
            submiteDetailingactivity.updateSpecificElement(returnModel,position)
            notifyDataSetChanged()
         //   returnModel?.let { it1 -> sendEDetailingArray?.set(position, it1) }
         //   sendEDetailingArray?.let { mCallback?.onClickButtonPOB(it) }

            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }



}
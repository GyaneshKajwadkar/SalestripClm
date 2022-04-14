package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class VisualAdsModel_Send {

    @SerializedName("startDate")
    @Expose
    var startDate:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("isEnd")
    @Expose
    var isEnd = false
        get() = field
        set(value) { field = value }

    @SerializedName("duration")
    @Expose
    var duration = 0
        get() = field
        set(value) { field = value }

    @SerializedName("endDate")
    @Expose
    var endDate:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("empId")
    @Expose
    var empId:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("doctorId")
    @Expose
    var doctorId:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("brandId")
    @Expose
    var brandId:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("brandName")
    @Expose
    var brandName:String?=null
        get() = field
        set(value) { field = value }

    @SerializedName("feedback")
    @Expose
    var feedback:String?=null
        get() = field
        set(value) { field = value }

    var monitorTime = 0
        get() = field
        set(value) { field = value }

    @SerializedName("rating")
    @Expose
    var rating = 0f
        get() = field
        set(value) { field = value }

 /*   var brandWiseStartTime :String?=null
        get() = field
        set(value) { field = value }

    var brandWiseStopTime :String?=null
        get() = field
        set(value) { field = value }*/

    @SerializedName("fileTransList")
    @Expose
    var fileTransList= ArrayList<childData>()
        get() = field
        set(value) { field = value }

    var id = 0
        get() = field
        set(value) { field = value }


    class childData {
        var fileId: String?=null
            get() = field
            set(value) { field = value }
        var viewTime: String?=null
            get() = field
            set(value) { field = value }
        var isLike = false
            get() = field
            set(value) { field = value }
        var comment: String?=null
            get() = field
            set(value) { field = value }
        var productName: String?=null
            get() = field
            set(value) { field = value }
    }
}
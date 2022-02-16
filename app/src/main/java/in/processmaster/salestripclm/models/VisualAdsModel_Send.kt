package `in`.processmaster.salestripclm.models

import java.util.ArrayList

class VisualAdsModel_Send {
    var startDate:String?=null
        get() = field
        set(value) { field = value }

    var isEnd = false
        get() = field
        set(value) { field = value }

    var endDate:String?=null
        get() = field
        set(value) { field = value }

    var empId:String?=null
        get() = field
        set(value) { field = value }

    var doctorId:String?=null
        get() = field
        set(value) { field = value }

    var brandId:String?=null
        get() = field
        set(value) { field = value }

    var brandName:String?=null
        get() = field
        set(value) { field = value }

    var feedback:String?=null
        get() = field
        set(value) { field = value }

    var monitorTime = 0
        get() = field
        set(value) { field = value }

    var rating = 0f
        get() = field
        set(value) { field = value }

    var brandWiseStartTime :String?=null
        get() = field
        set(value) { field = value }

    var brandWiseStopTime :String?=null
        get() = field
        set(value) { field = value }

    var childDataArray= ArrayList<childData>()
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
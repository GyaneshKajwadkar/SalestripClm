package `in`.processmaster.salestripclm.models

import java.util.ArrayList

class VisualAdsModel_Send {
    var startDate=""
        get() = field
        set(value) { field = value }

    var isEnd = false
        get() = field
        set(value) { field = value }

    var endDate=""
        get() = field
        set(value) { field = value }

    var empId=""
        get() = field
        set(value) { field = value }

    var doctorId=""
        get() = field
        set(value) { field = value }

    var brandId=""
        get() = field
        set(value) { field = value }

    var brandName=""
        get() = field
        set(value) { field = value }

    var feedback=""
        get() = field
        set(value) { field = value }

    var monitorTime = 0
        get() = field
        set(value) { field = value }

    var rating = 0f
        get() = field
        set(value) { field = value }

    var brandWiseStartTime = ""
        get() = field
        set(value) { field = value }

    var brandWiseStopTime = ""
        get() = field
        set(value) { field = value }

    var childDataArray= ArrayList<childData>()
        get() = field
        set(value) { field = value }

    var id = 0
        get() = field
        set(value) { field = value }


    class childData {
        var fileId: String=""
        var viewTime: String=""
        var isLike = false
        var comment: String=""
        var productName: String=""
    }
}
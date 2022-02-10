package `in`.processmaster.salestripclm.models

import java.util.ArrayList

class SaveEdetailingToDB_Model {

    var purposeTovisit=0
        get() = field
        set(value) { field = value }

    var remark=""
        get() = field
        set(value) { field = value }

    var workWithList = ArrayList<IdNameBoll_model>()
        get() = field
        set(value) { field = value }

    var sampleList = ArrayList<IdNameBoll_model>()
        get() = field
        set(value) { field = value }

    var giftList = ArrayList<IdNameBoll_model>()
        get() = field
        set(value) { field = value }

    var visualArray = ArrayList<VisualAdsModel_Send>()
        get() = field
        set(value) { field = value }
}
package `in`.processmaster.salestripclm.interfaceCode

import `in`.processmaster.salestripclm.models.IdNameBoll_model
import `in`.processmaster.salestripclm.models.SyncModel
import java.util.ArrayList

interface IdNameBoll_interface {
    fun onChangeArray(passingArrayList: ArrayList<IdNameBoll_model>, isUpdate:Boolean, selectionType: Int)
}
package `in`.processmaster.salestripclm.interfaceCode

import `in`.processmaster.salestripclm.models.SyncModel
import java.util.ArrayList

interface DisplayVisualInterface {
    fun onClickString(passingInterface: String)
    fun onClickDoctor(passingInterfaceList: ArrayList<SyncModel.Data.Doctor.LinkedBrand>)
}
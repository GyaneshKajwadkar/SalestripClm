package `in`.processmaster.salestripclm.interfaceCode

import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.Send_EDetailingModel
import `in`.processmaster.salestripclm.models.SyncModel
import kotlin.collections.ArrayList

interface SortingDisplayVisual {
    fun onClickButton(downloadList: ArrayList<DownloadFileModel>)

}

interface PobProductTransfer{
    fun onClickButtonPOB(downloadList: ArrayList<Send_EDetailingModel.PobObj.PobDetailList>)
}

interface productTransfer{
    fun onClickButtonProduct(selectedList: ArrayList<SyncModel.Data.Product>)
}
package `in`.processmaster.salestripclm.interfaceCode

import `in`.processmaster.salestripclm.models.DailyDocVisitModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.SyncModel
import kotlin.collections.ArrayList

interface SortingDisplayVisual {
    fun onClickButton(downloadList: ArrayList<DownloadFileModel>)

}

interface PobProductTransfer{
    fun onClickButtonPOB(downloadList: ArrayList<DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList>)
}

interface productTransfer{
    fun onClickButtonProduct(selectedList: ArrayList<SyncModel.Data.Product>, type:Int)
}

interface productTransferIndividual{
    fun onClickButtonProduct(productModel: SyncModel.Data.Product,positon:Int)
}
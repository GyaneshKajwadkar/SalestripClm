package `in`.processmaster.salestripclm.interfaceCode

import `in`.processmaster.salestripclm.models.DownloadFileModel
import kotlin.collections.ArrayList

interface SortingDisplayVisual {
    fun onClickButton(downloadList: ArrayList<DownloadFileModel>)

}
package `in`.processmaster.salestripclm.models

import java.io.Serializable

class DownloadFileModel : Serializable {

    var filePath: String = ""
        get() = field
        set(value) { field = value }

    var fileName: String = ""
        get() = field
        set(value) { field = value }

    var fileUrl: String = ""
        get() = field
        set(value) { field = value }

    var downloadType: String = ""
        get() = field
        set(value) { field = value }

    var brandId = 0
        get() = field
        set(value) { field = value }

    var brandName: String = ""
        get() = field
        set(value) { field = value }

    var eDetailingId = 0
        get() = field
        set(value) { field = value }

    var setContentType: String = ""
        get() = field
        set(value) { field = value }

    var fileId = 0
        get() = field
        set(value) { field = value }

    var doctorId = 0
        get() = field
        set(value) { field = value }

    var zipExtractFilePath: String? = null
        get() = field
        set(value) { field = value }

    var favFilePath: String = ""
        get() = field
        set(value) { field = value }

    var favFile = false
        get() = field
        set(value) { field = value }

    var favFileName: String = ""
        get() = field
        set(value) { field = value }

    var fileDirectoryPath: String = ""
        get() = field
        set(value) { field = value }

    var model: DownloadEdetail_model.Data.EDetailingImages? = null
        get() = field
        set(value) { field = value }



}
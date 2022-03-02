package `in`.processmaster.salestripclm.utils

import `in`.processmaster.salestripclm.models.DevisionModel
import `in`.processmaster.salestripclm.models.DownloadFileModel
import `in`.processmaster.salestripclm.models.VisualAdsModel_Send
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson

class DatabaseKotlin (val context: Context) :
    SQLiteOpenHelper(context, "SalesTrip_CLM_db", null, 1) {

    private val KEY_ID = "id"
    //=========================================hold SyncData===============================================

    //=========================================hold SyncData===============================================
    private val TABLE_SAVE_API = "apiData"
    private val KEY_DATA = "data"

    //=========================================hold eDetailingDataParent===================================
    private val TABLE_EDETAILING = "edetaling"
    private val EDETAILING_DATA = "e_data"
    private val IS_DOWNLOADED = "isDownloaded"
    private val FILEPATh = "filePath"
    private val ISFAV = "is_fav"

    //=========================================hold eDetailingDataChild=====================================
    private val TABLE_EDETAILINGDOWNLOAD = "edetaling_download"
    private val KEY_IDParent = "id_parent"
    private val KEY_IDChild = "id_child"
    private val EDETALING_DOWNLOAD_DATA = "e_detailData"
    private val EDETALING_TYPE = "e_Type"
    private val ISFAVITEM = "is_favItem"

    //=========================================hold eDetailingData==================================
    private val TABLE_SENDVISUALADS = "visual_ads"
    private val DOCTOR_ID = "doctor_id"
    private val BRAND_ID = "brand_id"
    private val SLIDE_START_TIME = "set_timeslide"
    private val SLIDE_STOP_TIME = "end_timeslide"
    private val IS_SUBMIT = "data_submit"
    private val BRAND_NAME = "brand_name"
    private val MONITOR_TIME = "monitorTime"
    private val START_TIME_BRANDS = "starttimebrands"
    private val END_TIME_BRANDS = "endtimebrands"


    //=======================================eDetailingChild=========================================
    private val TABLE_CHILD_VISUAL_ADS = "visual_ads_child"
    private val TIMESET = "setTime"
    private val ISLIKE = "is_like"
    private val COMMENT = "_comment"
    private val SINGLE_SLIDE_TIMER = "singleSlide_timer"
    private val FILE_ID = "file_id"

    //=========================================hold StoreApi===============================================
    private val TABLE_SAVE_SEND_API = "APIDataSave"
    private val API_TYPE = "apiType"
    private val APIKEY_DATA = "data"

    //==========================================Save doctor edeailing date wise=============================
    private val TABLE_SAVE_DOCTOR_EDETAIL = "save_doctor_eDetail"
    private val DETAILING_DATE = "detailingDate"

    // Creating Tables
    override fun onCreate(db: SQLiteDatabase) {
        //Sync database
        val CREATE_SYNC_TABLE = ("CREATE TABLE " + TABLE_SAVE_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATA + " TEXT" + ")")
        db.execSQL(CREATE_SYNC_TABLE)

        //eDetailing database
        val CREATE_EDETAILING_TABLE = ("CREATE TABLE " + TABLE_EDETAILING + "("
                + KEY_ID + " INTEGER," + EDETAILING_DATA + " TEXT," + IS_DOWNLOADED + " INTEGER," + FILEPATh + " TEXT," + ISFAV + " INTEGER" + ")")
        db.execSQL(CREATE_EDETAILING_TABLE)

        //eDetailingDownload database
        val CREATE_EDETAILING_DOWNLOAD_TABLE =
            ("CREATE TABLE " + TABLE_EDETAILINGDOWNLOAD + "("
                    + KEY_IDChild + " INTEGER," + KEY_IDParent + " INTEGER," + EDETALING_DOWNLOAD_DATA + " TEXT," + EDETALING_TYPE + " TEXT," + ISFAVITEM + " INTEGER" + ")")
        db.execSQL(CREATE_EDETAILING_DOWNLOAD_TABLE)

        //visual database
        val CREATE_VISUALADS_TABLE = ("CREATE TABLE " + TABLE_SENDVISUALADS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DOCTOR_ID
                + " INTEGER," + BRAND_ID + " INTEGER,"
                + SLIDE_START_TIME + " TEXT," + SLIDE_STOP_TIME + " TEXT,"
                + IS_SUBMIT + " INTEGER," + BRAND_NAME + " TEXT," + MONITOR_TIME + " INTEGER,"
                + START_TIME_BRANDS + " TEXT," + END_TIME_BRANDS + " TEXT" + ")")
        db.execSQL(CREATE_VISUALADS_TABLE)

        //visual child database
        val CREATE_VISUALADS_CHILD_TABLE =
            ("CREATE TABLE " + TABLE_CHILD_VISUAL_ADS + "("
                    + KEY_ID + " INTEGER,"
                    + TIMESET + " TEXT,"
                    + ISLIKE + " INTEGER," + COMMENT + " TEXT,"
                    + SINGLE_SLIDE_TIMER + " INTEGER," + FILE_ID + " INTEGER" + ")")
        db.execSQL(CREATE_VISUALADS_CHILD_TABLE)

        //api store database
        val CREATE_API_TABLE = ("CREATE TABLE " + TABLE_SAVE_SEND_API + "("
                + KEY_ID + " INTEGER,"
                + API_TYPE + " TEXT,"
                + APIKEY_DATA + " TEXT"
                + ")")
        db.execSQL(CREATE_API_TABLE)

        //store edetailing doctor wise
        val CREATE_DOCTOR_EDETAIL =
            ("CREATE TABLE " + TABLE_SAVE_DOCTOR_EDETAIL + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DOCTOR_ID + " INTEGER,"
                    + DETAILING_DATE + " TEXT"
                    + ")")
        db.execSQL(CREATE_DOCTOR_EDETAIL)
    }

    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_API)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILING)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDVISUALADS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILINGDOWNLOAD)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD_VISUAL_ADS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_SEND_API)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_DOCTOR_EDETAIL)
        onCreate(db)
    }

    //=====================================Sync DataBase method=====================================
    //add data to database as a string.
    fun addAPIData(data: String?, id: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_DATA, data)
        val u = db.update(TABLE_SAVE_API, values, "id=?", arrayOf(id.toString()))
        if (u == 0) {
            values.put(KEY_ID, id)
            db.insert(TABLE_SAVE_API, null, values)
        }
        db.close()
    }

    // Getting sync data Count
    fun getDatasCount(): Int {
        var countCursor = 0
        val countQuery = "SELECT  * FROM " + TABLE_SAVE_API
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)
        countCursor = cursor.count
        cursor.close()
        return countCursor
    }

    // delete all data
    fun deleteAll() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_SAVE_API)
    }

    @SuppressLint("Range")
    fun getApiDetail(id: Int): String {
        var strData = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SAVE_API, arrayOf(
                KEY_DATA
            ), KEY_ID + "=?", arrayOf(id.toString()), null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                strData = cursor.getString(cursor.getColumnIndex(KEY_DATA))
            } while (cursor.moveToNext())
        }
        return strData
    }


    //=======================================EDetailing table============================================

    //=======================================EDetailing table============================================
    //insert or update edetailing table
    fun insertOrUpdateEDetail(idModel: String, data: String?) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_ID, idModel)
        initialValues.put(EDETAILING_DATA, data)
        //first check is data available if yes then update if no then insert
        val u = db.update(
            TABLE_EDETAILING, initialValues, "id=?", arrayOf(
                idModel
            )
        )
        if (u == 0) {
            db.insert(TABLE_EDETAILING, null, initialValues)
        }
        db.close()
    }

    fun updateFavourite(idModel: String, isFavInt: Int) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(ISFAV, isFavInt)
        db.update(
            TABLE_EDETAILING, initialValues, "id=?", arrayOf(
                idModel
            )
        )
        db.close()
    }

    //insert file path using id
    fun insertFilePath(isDownloaded: Int, filePath: String?, idModel: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(IS_DOWNLOADED, isDownloaded)
        initialValues.put(FILEPATh, filePath)
        db.update(
            TABLE_EDETAILING, initialValues, "id=?", arrayOf(
                idModel
            )
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    //get all edetailing list
    fun getAlleDetail(): ArrayList<DevisionModel.Data.EDetailing> {
        val edetailList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList<DevisionModel.Data.EDetailing>()
        val selectQuery = "SELECT  * FROM " + TABLE_EDETAILING
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                //convert save string to model class
                val data = cursor.getString(1)
                val gson = Gson()
                val contact: DevisionModel.Data.EDetailing = gson.fromJson<DevisionModel.Data.EDetailing>(data, DevisionModel.Data.EDetailing::class.java)

                //if downloaded file path exist then add to edetailng model class
                if (cursor.getString(3) != null) {
                    contact.filePath = cursor.getString(3)
                    contact.isSaved = cursor.getString(2).toInt()
                }
                edetailList.add(contact)
            } while (cursor.moveToNext())
        }
        db.close()
        return edetailList
    }


    //get all edetailing list
    fun getSelectedeDetail(isDownloaded: Boolean): ArrayList<DevisionModel.Data.EDetailing> {
        val edetailList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList<DevisionModel.Data.EDetailing>()
        val selectQuery = "SELECT  * FROM " + TABLE_EDETAILING
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                //convert save string to model class
                val data = cursor.getString(1)
                val gson = Gson()
                val edetailModel: DevisionModel.Data.EDetailing =
                    gson.fromJson<DevisionModel.Data.EDetailing>(data, DevisionModel.Data.EDetailing::class.java)

                //if downloaded file path exist then add to edetailng model class
                if (cursor.getString(3) != null) {
                    edetailModel.filePath = cursor.getString(3)
                    edetailModel.isSaved = cursor.getString(2).toInt()
                }
                if (isDownloaded && edetailModel.isSaved == 1) {
                    edetailList.add(edetailModel)
                } else if (!isDownloaded && edetailModel.isSaved != 1) {
                    edetailList.add(edetailModel)
                }
            } while (cursor.moveToNext())
        }
        db.close()
        return edetailList
    }

    fun getDownloadStatus(eretailDetailList: ArrayList<DevisionModel.Data.EDetailing.EretailDetail>): Boolean {
        var returnValue = false
        for (i in eretailDetailList.indices) {
            val returnData: DownloadFileModel = getSingleDownloadedData(eretailDetailList[i].fileId!!)
            if (returnData.fileName != null) {
                returnValue = true
            } else {
                returnValue = false
                break
            }
        }
        return returnValue
    }


    //getFav edetailing
    @SuppressLint("Range")
    fun getAllFavBrands(): ArrayList<DevisionModel.Data.EDetailing>? {
        val id = 1
        val downloadFileList: ArrayList<DevisionModel.Data.EDetailing> = ArrayList<DevisionModel.Data.EDetailing>()
        var data = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EDETAILING,
            arrayOf(
                EDETAILING_DATA,
                IS_DOWNLOADED,
                FILEPATh
            ),
            ISFAV + "=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                data = cursor.getString(cursor.getColumnIndex(EDETAILING_DATA))
                val gson = Gson()
                val contact: DevisionModel.Data.EDetailing = gson.fromJson<DevisionModel.Data.EDetailing>(data, DevisionModel.Data.EDetailing::class.java)
                if (cursor.getString(cursor.getColumnIndex(FILEPATh)) != null) {
                    contact.filePath =
                        cursor.getString(cursor.getColumnIndex(FILEPATh))
                    contact.isSaved =
                        cursor.getInt(cursor.getColumnIndex(IS_DOWNLOADED))
                }
                downloadFileList.add(contact)
            } while (cursor.moveToNext())
        }
        return downloadFileList
    }

    fun deleteEdetailingData(id: String) {
        val db = writableDatabase
        db.delete(TABLE_EDETAILING, KEY_ID + " = ?", arrayOf(id))
        db.close()
    }

    //=======================================EDetailingDownload table============================================

    //=======================================EDetailingDownload table============================================
    //insert or update edetailing table
    fun insertOrUpdateEDetailDownload(
        parentId: Int,
        childId: Int,
        data: String?,
        downloadedtype: String?
    ) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_IDParent, parentId)
        initialValues.put(KEY_IDChild, childId)
        initialValues.put(EDETALING_DOWNLOAD_DATA, data)
        initialValues.put(EDETALING_TYPE, downloadedtype)

        //first check is data available if yes then update if no then insert
        if (CheckEdetailDownload(childId.toString(), "child")) {
            db.update(
                TABLE_EDETAILINGDOWNLOAD,
                initialValues,
                "id_child=?",
                arrayOf(childId.toString())
            )
            db.close()
        } else {
            db.insert(TABLE_EDETAILINGDOWNLOAD, null, initialValues)
            db.close()
        }
    }

    // check data is already exist.
    fun CheckEdetailDownload(fieldValue: String, idType: String): Boolean {
        val sqldb = this.readableDatabase
        var Query = ""
        Query = if (idType == "child") {
            "Select * from " + TABLE_EDETAILINGDOWNLOAD + " where " + KEY_IDChild + " = " + fieldValue
        } else {
            "Select * from " + TABLE_EDETAILINGDOWNLOAD + " where " + KEY_IDParent + " = " + fieldValue
        }
        val cursor = sqldb.rawQuery(Query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    //get single file path using id
    @SuppressLint("Range")
    fun getSingleDownloadedData(id: Int): DownloadFileModel {
        var downloadFileModel = DownloadFileModel()
        var data = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EDETAILINGDOWNLOAD, arrayOf(
                KEY_IDChild,
                EDETALING_DOWNLOAD_DATA, ISFAVITEM
            ), KEY_IDChild + "=?", arrayOf(id.toString()), null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                data =
                    cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA))
                val getFav = cursor.getInt(cursor.getColumnIndex(ISFAVITEM))
                val gson = Gson()
                downloadFileModel =
                    gson.fromJson<DownloadFileModel>(data, DownloadFileModel::class.java)
                if (getFav == 1) {
                    downloadFileModel.favFile = true
                }
            } while (cursor.moveToNext())
        }
        return downloadFileModel
    }

    //get all file path using id
    @SuppressLint("Range")
    fun getAllDownloadedData(id: Int): ArrayList<DownloadFileModel> {
        val downloadFileList: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
        var data = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EDETAILINGDOWNLOAD, arrayOf(
                KEY_IDParent,
                EDETALING_DOWNLOAD_DATA, ISFAVITEM
            ), KEY_IDParent + "=?", arrayOf(id.toString()), null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                var downloadFileModel = DownloadFileModel()
                data =
                    cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA))
                val getFav = cursor.getInt(cursor.getColumnIndex(ISFAVITEM))
                val gson = Gson()
                downloadFileModel =
                    gson.fromJson<DownloadFileModel>(data, DownloadFileModel::class.java)
                if (getFav == 1) {
                    downloadFileModel.favFile = true
                }
                downloadFileModel.favFileName = ""
                downloadFileList.add(downloadFileModel)
            } while (cursor.moveToNext())
        }
        return downloadFileList
    }

    fun deleteEdetailDownloada(id: String) {
        val db = writableDatabase
        if (CheckEdetailDownload(id, "parent")) {
            db.delete(
                TABLE_EDETAILINGDOWNLOAD,  // Where to delete
                KEY_IDParent + " = ?", arrayOf(id)
            )
        }
        db.close()
    }

    @SuppressLint("Range")
    fun getAllDataUsingType(type: String): ArrayList<DownloadFileModel>? {
        val downloadFileList: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
        var data = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EDETAILINGDOWNLOAD, arrayOf(
                KEY_IDParent,
                EDETALING_DOWNLOAD_DATA
            ), EDETALING_TYPE + "=?", arrayOf(type), null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                data =
                    cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA))
                val gson = Gson()
                downloadFileList.add(
                    gson.fromJson<DownloadFileModel>(
                        data,
                        DownloadFileModel::class.java
                    )
                )
            } while (cursor.moveToNext())
        }
        return downloadFileList
    }

    @SuppressLint("Range")
    fun getAllFavList(): ArrayList<DownloadFileModel> {
        val downloadFileList: ArrayList<DownloadFileModel> = ArrayList<DownloadFileModel>()
        var data = ""
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_EDETAILINGDOWNLOAD, arrayOf(
                KEY_IDParent,
                EDETALING_DOWNLOAD_DATA, ISFAVITEM
            ), ISFAVITEM + "=?", arrayOf("1"), null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                data =
                    cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA))
                val gson = Gson()
                var model = DownloadFileModel()
                model = gson.fromJson<DownloadFileModel>(data, DownloadFileModel::class.java)
                model.favFile = true
                downloadFileList.add(model)
            } while (cursor.moveToNext())
        }
        return downloadFileList
    }

    fun updateFavouriteItem(idModel: String, isFavInt: Int) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(ISFAVITEM, isFavInt)
        db.update(
            TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", arrayOf(
                idModel
            )
        )
        db.close()
    }


    fun updateFavouriteItemWithModel(idModel: String, isFavInt: Int, data: String?) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(ISFAVITEM, isFavInt)
        initialValues.put(EDETALING_DOWNLOAD_DATA, data)
        db.update(
            TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", arrayOf(
                idModel
            )
        )
        db.close()
    }

    //=======================================VisualAds table==============================================
    //insert or update visual ads table
    fun insertStartTimeSlide(
        currentTime: String,
        addDoctorId: Int,
        addBrandId: Int,
        brandName: String?,
        monitorTime: Int,
        startTime: String?
    ) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(SLIDE_START_TIME, currentTime)
        initialValues.put(DOCTOR_ID, addDoctorId)
        initialValues.put(BRAND_ID, addBrandId)
        initialValues.put(BRAND_NAME, brandName)
        initialValues.put(START_TIME_BRANDS, startTime)
        if (CheckVisualAds(currentTime, addBrandId)) {
        } else {
            Log.e("insertStartTimeSlide", "insert")
            db.insert(TABLE_SENDVISUALADS, null, initialValues)
        }
        db.close()
    }

    //Add end time
    fun updateendData(endTime: String?, startTime: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(SLIDE_STOP_TIME, endTime)
        initialValues.put(IS_SUBMIT, 1)
        db.update(
            TABLE_SENDVISUALADS, initialValues, "set_timeslide=?", arrayOf(
                startTime
            )
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    //get all submitted visualads list
    fun getAllSubmitVisual(): ArrayList<VisualAdsModel_Send> {
        val edetailList: ArrayList<VisualAdsModel_Send> = ArrayList<VisualAdsModel_Send>()
        val selectQuery = "SELECT  * FROM " + TABLE_SENDVISUALADS
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val startTime = cursor.getString(3)
                val isEnd = cursor.getInt(5)
                if (isEnd == 1) {
                    val visualModel = VisualAdsModel_Send()
                    visualModel.id = id
                    visualModel.doctorId = cursor.getString(1)
                    visualModel.startDate = startTime
                    visualModel.endDate = cursor.getString(4)
                    visualModel.brandId = cursor.getString(2)
                    visualModel.brandName = cursor.getString(6)
                    visualModel.monitorTime = cursor.getInt(7)
                    visualModel.isEnd = true
                    visualModel.childDataArray = getAllChildBy_ID(startTime)
                    edetailList.add(visualModel)
                } else {
                    deleteVisualAds(id.toString())
                    deleteVisualAdsChild(startTime)
                }
            } while (cursor.moveToNext())
        }
        return edetailList
    }

    //delete single entry
    fun deleteVisualAds(deleteId: String): Boolean {
        val db = this.writableDatabase
        return db.delete(
            TABLE_SENDVISUALADS, KEY_ID + " = ?", arrayOf(
                deleteId
            )
        ) > 0
    }

    // delete all data
    fun deleteAllVisualAds() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_SENDVISUALADS)
    }

    fun insertBrandTime(setTime: Int, mainTime: String, brandId: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(MONITOR_TIME, setTime)
        db.update(
            TABLE_SENDVISUALADS,
            initialValues,
            SLIDE_START_TIME + " = ? AND " + BRAND_ID + " = ? ",
            arrayOf(mainTime, brandId)
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    @SuppressLint("Range")
    fun getBrandTime(brandId: String, mainTime: String): Int {
        var getDbTimer = 0
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SENDVISUALADS,
            arrayOf(MONITOR_TIME),
            SLIDE_START_TIME + " = ? AND " + BRAND_ID + " = ? ",
            arrayOf(mainTime, brandId),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                getDbTimer = cursor.getInt(cursor.getColumnIndex(MONITOR_TIME))
            } while (cursor.moveToNext())
        }
        return getDbTimer
    }

    @SuppressLint("Range") // check visual ads is already exist.
    fun CheckVisualAds(startTime: String, brandId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SENDVISUALADS,
            arrayOf(SLIDE_START_TIME),
            BRAND_ID + "=?",
            arrayOf(brandId.toString()),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val dbDateTime = cursor.getString(cursor.getColumnIndex("set_timeslide"))
                if (dbDateTime == startTime) {
                    return true
                }
            } while (cursor.moveToNext())
        }
        return false
    }


    // =======================================Child visual ads table================================================

    // =======================================Child visual ads table================================================
    //insert file id
    fun insertFileID(setfileId: Int, startTime: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(TIMESET, startTime)
        initialValues.put(FILE_ID, setfileId)
        if (ChecktimeandFileIdExist(setfileId.toString(), startTime)) {
        } else {
            db.insert(TABLE_CHILD_VISUAL_ADS, null, initialValues)
            db.close()
        }
    }

    //insert like or dislike
    fun insertlike(setLike: Int, setfileId: Int, startTime: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(ISLIKE, setLike)
        db.update(
            TABLE_CHILD_VISUAL_ADS,
            initialValues,
            "setTime = ? AND file_id= ? ",
            arrayOf(startTime, setfileId.toString())
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    //insert comment
    fun insertComment(setComment: String?, setfileId: Int, startTime: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(COMMENT, setComment)
        db.update(
            TABLE_CHILD_VISUAL_ADS,
            initialValues,
            "setTime = ? AND file_id= ? ",
            arrayOf(startTime, setfileId.toString())
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    //insert time in second
    fun insertTime(setTime: Int, setfileId: Int, startTime: String) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(SINGLE_SLIDE_TIMER, setTime)
        db.update(
            TABLE_CHILD_VISUAL_ADS,
            initialValues,
            "setTime = ? AND file_id= ? ",
            arrayOf(startTime, setfileId.toString())
        ) // number 1 is the _id here, update to variable for your code
        db.close()
    }

    //getLike
    @SuppressLint("Range")
    fun getLike(setfileId: String, startTime: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CHILD_VISUAL_ADS,
            arrayOf(
                TIMESET,
                ISLIKE,
                COMMENT,
                SINGLE_SLIDE_TIMER,
                FILE_ID
            ),
            TIMESET + "=?",
            arrayOf(
                startTime
            ),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val data = cursor.getInt(cursor.getColumnIndex("is_like"))
                val dbFileID = cursor.getInt(cursor.getColumnIndex("file_id"))
                if (data == 1) {
                    if (dbFileID == setfileId.toInt()) {
                        return true
                    }
                }
            } while (cursor.moveToNext())
        }
        return false
    }

    //get comment
    @SuppressLint("Range")
    fun getComment(setfileId: String, startTime: String): String {
        var getDbComment = " "
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CHILD_VISUAL_ADS,
            arrayOf(
                TIMESET,
                ISLIKE,
                COMMENT,
                SINGLE_SLIDE_TIMER,
                FILE_ID
            ),
            TIMESET + "=?",
            arrayOf(
                startTime
            ),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val dbFileID = cursor.getInt(cursor.getColumnIndex("file_id"))
                if (dbFileID == setfileId.toInt()) {
                    getDbComment = cursor.getString(cursor.getColumnIndex("_comment"))
                }
            } while (cursor.moveToNext())
        }
        return getDbComment
    }

    //get time in second
    @SuppressLint("Range")
    fun getTime(setfileId: String, startTime: String): Int {
        var getDbTimer = 0
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CHILD_VISUAL_ADS,
            arrayOf(
                TIMESET,
                ISLIKE,
                COMMENT,
                SINGLE_SLIDE_TIMER,
                FILE_ID
            ),
            TIMESET + "=?",
            arrayOf(
                startTime
            ),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val dbFileID = cursor.getInt(cursor.getColumnIndex("file_id"))
                if (dbFileID == setfileId.toInt()) {
                    getDbTimer = cursor.getInt(cursor.getColumnIndex("singleSlide_timer"))
                }
            } while (cursor.moveToNext())
        }
        return getDbTimer
    }

    // get all same id childs
    @SuppressLint("Range")
    fun getAllChildBy_ID(startTime: String): ArrayList<VisualAdsModel_Send.childData> {
        val childListVisualAds: ArrayList<VisualAdsModel_Send.childData> = ArrayList<VisualAdsModel_Send.childData>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CHILD_VISUAL_ADS,
            arrayOf(
                TIMESET,
                ISLIKE,
                COMMENT,
                SINGLE_SLIDE_TIMER,
                FILE_ID
            ),
            TIMESET + "=?",
            arrayOf(
                startTime
            ),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val model = VisualAdsModel_Send.childData()
                val isLike = cursor.getInt(cursor.getColumnIndex("is_like"))
                val comment = cursor.getString(cursor.getColumnIndex("_comment"))
                val singleSlideTimer = cursor.getString(cursor.getColumnIndex("singleSlide_timer"))
                val fileId = cursor.getInt(cursor.getColumnIndex("file_id"))
                if (isLike == 1) {
                    model.isLike = true
                }
                model.comment = comment
                model.viewTime = singleSlideTimer
                model.fileId = fileId.toString() + ""
                childListVisualAds.add(model)
            } while (cursor.moveToNext())
        }
        return childListVisualAds
    }

    //delete visual Ads child
    fun deleteVisualAdsChild(startDate: String): Boolean {
        val db = this.writableDatabase
        return db.delete(
            TABLE_CHILD_VISUAL_ADS, TIMESET + " = ?", arrayOf(
                startDate
            )
        ) > 0
    }

    // delete all child data
    fun deleteAllChildVisual() {
        val db = this.writableDatabase
        db.execSQL("delete from " + TABLE_CHILD_VISUAL_ADS)
    }

    //check is visualads exist using date and fileID
    @SuppressLint("Range")
    fun ChecktimeandFileIdExist(fileIdCheck: String, dateTime: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CHILD_VISUAL_ADS,
            arrayOf(
                TIMESET,
                ISLIKE,
                COMMENT,
                SINGLE_SLIDE_TIMER,
                FILE_ID
            ),
            TIMESET + "=?",
            arrayOf(
                dateTime
            ),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val dbFileID = cursor.getInt(cursor.getColumnIndex("file_id"))
                if (dbFileID == fileIdCheck.toInt()) {
                    return true
                }
            } while (cursor.moveToNext())
        }
        return false
    }

    // =======================================api storage table================================================

    // =======================================api storage table================================================
    fun insertOrUpdateSaveAPI(id: Int, data: String?, apiType: String?) {
        val db = writableDatabase
        val initialValues = ContentValues()
        initialValues.put(KEY_ID, id)
        initialValues.put(API_TYPE, apiType)
        initialValues.put(APIKEY_DATA, data)
        val u = db.update(
            TABLE_SAVE_SEND_API,
            initialValues,
            "id=?",
            arrayOf(id.toString())
        )
        if (u == 0) {
            db.insert(TABLE_SAVE_SEND_API, null, initialValues)
        }
    }

    @SuppressLint("Range")
    fun getAllSaveSend(type: String): ArrayList<String>? {
        val saveStringList = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SAVE_SEND_API,
            arrayOf(APIKEY_DATA),
            API_TYPE + "=?",
            arrayOf(type),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val data = cursor.getString(cursor.getColumnIndex(APIKEY_DATA))
                saveStringList.add(data)
            } while (cursor.moveToNext())
        }
        return saveStringList
    }

    fun deleteSaveSend(id: Int): Boolean {
        val db = this.writableDatabase
        return db.delete(
            TABLE_SAVE_SEND_API,
            KEY_ID + " = ?",
            arrayOf(id.toString())
        ) > 0
    }

//=============================================Doctor e detailing table===================================

    //=============================================Doctor e detailing table===================================
    fun insertdoctorData(doctorID: Int, date: String?) {
        val db = this.writableDatabase
        val initialValues = ContentValues()
        initialValues.put(DOCTOR_ID, doctorID)
        initialValues.put(DETAILING_DATE, date)
        db.insert(TABLE_SAVE_DOCTOR_EDETAIL, null, initialValues)
        db.close()
    }

    fun isEDetailingAvailable(doctorId: Int, detailingDate: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SAVE_DOCTOR_EDETAIL,
            arrayOf(DOCTOR_ID, DETAILING_DATE),
            DOCTOR_ID + "=? AND " + DETAILING_DATE + "=?",
            arrayOf(doctorId.toString(), detailingDate),
            null,
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                return true
            } while (cursor.moveToNext())
        }
        return false
    }

}
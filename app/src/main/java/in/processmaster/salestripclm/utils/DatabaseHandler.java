package in.processmaster.salestripclm.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.processmaster.salestripclm.models.DevisionModel;
import in.processmaster.salestripclm.models.DownloadFileModel;
import in.processmaster.salestripclm.models.VisualAdsModel_Send;

import com.google.gson.Gson;

import java.util.ArrayList;

//Java T point
public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SalesTrip_CLM_db";
    private static final String KEY_ID = "id";

    //=========================================hold SyncData===============================================

    private static final String TABLE_SAVE_API = "apiData";
    private static final String KEY_DATA = "data";

    //=========================================hold eDetailingDataParent===================================
    private static final String TABLE_EDETAILING = "edetaling";
    private static final String EDETAILING_DATA = "e_data";
    private static final String IS_DOWNLOADED = "isDownloaded";
    private static final String FILEPATh = "filePath";
    private static final String ISFAV = "is_fav";

    //=========================================hold eDetailingDataChild=====================================
    private static final String TABLE_EDETAILINGDOWNLOAD = "edetaling_download";
    private static final String KEY_IDParent = "id_parent";
    private static final String KEY_IDChild = "id_child";
    private static final String EDETALING_DOWNLOAD_DATA = "e_detailData";
    private static final String EDETALING_TYPE = "e_Type";
    private static final String ISFAVITEM = "is_favItem";

    //=========================================hold eDetailingData==================================
    private static final String TABLE_SENDVISUALADS = "visual_ads";
    private static final String DOCTOR_ID = "doctor_id";
    private static final String BRAND_ID = "brand_id";
    private static final String SLIDE_START_TIME = "set_timeslide";
    private static final String SLIDE_STOP_TIME = "end_timeslide";
    private static final String IS_SUBMIT = "data_submit";
    private static final String BRAND_NAME = "brand_name";
    private static final String MONITOR_TIME = "monitorTime";
    private static final String START_TIME_BRANDS = "starttimebrands";
    private static final String END_TIME_BRANDS = "endtimebrands";


    //=======================================eDetailingChild=========================================
    private static final String TABLE_CHILD_VISUAL_ADS = "visual_ads_child";
    private static final String TIMESET = "setTime";
    private static final String ISLIKE = "is_like";
    private static final String COMMENT = "_comment";
    private static final String SINGLE_SLIDE_TIMER = "singleSlide_timer";
    private static final String FILE_ID = "file_id";

    //=========================================hold StoreApi===============================================
    private static final String TABLE_SAVE_SEND_API = "APIDataSave";
    private static final String API_TYPE = "apiType";
    private static final String APIKEY_DATA = "data";

    //==========================================Save doctor edeailing date wise=============================
    private static final String TABLE_SAVE_DOCTOR_EDETAIL = "save_doctor_eDetail";
    private static final String DETAILING_DATE = "detailingDate";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Sync database
        String CREATE_SYNC_TABLE = "CREATE TABLE " + TABLE_SAVE_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_SYNC_TABLE);

        //eDetailing database
        String CREATE_EDETAILING_TABLE = "CREATE TABLE " + TABLE_EDETAILING + "("
                + KEY_ID + " INTEGER," + EDETAILING_DATA + " TEXT," + IS_DOWNLOADED + " INTEGER," +FILEPATh + " TEXT," +ISFAV + " INTEGER" +")";
        db.execSQL(CREATE_EDETAILING_TABLE);

        //eDetailingDownload database
        String CREATE_EDETAILING_DOWNLOAD_TABLE = "CREATE TABLE " + TABLE_EDETAILINGDOWNLOAD + "("
                + KEY_IDChild + " INTEGER," + KEY_IDParent + " INTEGER," + EDETALING_DOWNLOAD_DATA + " TEXT," + EDETALING_TYPE + " TEXT,"  +ISFAVITEM + " INTEGER"+")";
        db.execSQL(CREATE_EDETAILING_DOWNLOAD_TABLE);

        //visual database
        String CREATE_VISUALADS_TABLE = "CREATE TABLE " + TABLE_SENDVISUALADS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ DOCTOR_ID
                + " INTEGER," +  BRAND_ID + " INTEGER,"
                +  SLIDE_START_TIME + " TEXT," +  SLIDE_STOP_TIME + " TEXT,"
                +  IS_SUBMIT + " INTEGER," +BRAND_NAME + " TEXT," +MONITOR_TIME + " INTEGER,"
                +START_TIME_BRANDS + " TEXT,"+ END_TIME_BRANDS +" TEXT" +")";
        db.execSQL(CREATE_VISUALADS_TABLE);

        //visual child database
        String CREATE_VISUALADS_CHILD_TABLE = "CREATE TABLE " + TABLE_CHILD_VISUAL_ADS + "("
                + KEY_ID + " INTEGER,"
                +  TIMESET + " TEXT,"
                +  ISLIKE + " INTEGER," +  COMMENT + " TEXT,"
                +  SINGLE_SLIDE_TIMER + " INTEGER," +  FILE_ID + " INTEGER" +")";
        db.execSQL(CREATE_VISUALADS_CHILD_TABLE);

        //api store database
        String CREATE_API_TABLE = "CREATE TABLE " + TABLE_SAVE_SEND_API + "("
                + KEY_ID + " INTEGER,"
                +  API_TYPE + " TEXT,"
                +  APIKEY_DATA + " TEXT"
                + ")";
        db.execSQL(CREATE_API_TABLE);

        //store edetailing doctor wise
        String CREATE_DOCTOR_EDETAIL = "CREATE TABLE " + TABLE_SAVE_DOCTOR_EDETAIL + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +  DOCTOR_ID + " INTEGER,"
                +  DETAILING_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_DOCTOR_EDETAIL);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDVISUALADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILINGDOWNLOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD_VISUAL_ADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_SEND_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_DOCTOR_EDETAIL);
        onCreate(db);
    }

    //=====================================Sync DataBase method=====================================
    //add data to database as a string.
    public void addAPIData(String data,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATA, data);
        int u = db.update(TABLE_SAVE_API, values, "id=?", new String[]{String.valueOf(id)});
        if (u == 0) {
            values.put(KEY_ID, id);
            db.insert(TABLE_SAVE_API, null, values);
        }
        db.close();

    }

    // Getting sync data Count
    public int getDatasCount() {
        int countCursor=0;
        String countQuery = "SELECT  * FROM " + TABLE_SAVE_API;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        countCursor=cursor.getCount();
        cursor.close();
        return countCursor;
    }

    // delete all data
    public void deleteAll()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SAVE_API);
    }

//    // code to get all data in a list view
//    public String getAllDataSync() {
//        String strData = "";
//        String selectQuery = "SELECT  * FROM " + TABLE_SAVE_API;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                strData=cursor.getString(1);
//            } while (cursor.moveToNext());
//        }
//        return strData;
//    }

    @SuppressLint("Range")
    public String getApiDetail(int id)
    {
        String strData = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVE_API, new String[] {
                        KEY_DATA }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // strData=cursor.getString(1);
                strData=cursor.getString(cursor.getColumnIndex(KEY_DATA));
            } while (cursor.moveToNext());
        }
        return strData;
    }


    //=======================================EDetailing table============================================

    //insert or update edetailing table
    public void insertOrUpdateEDetail(String idModel,String data) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, idModel);
        initialValues.put(EDETAILING_DATA, data);

        //first check is data available if yes then update if no then insert
        int u = db.update(TABLE_EDETAILING, initialValues, "id=?", new String[]{String.valueOf(idModel)});

        if (u == 0) {
            db.insert(TABLE_EDETAILING, null, initialValues);
        }

//        if(CheckIsDataAlreadyInDBorNot(String.valueOf(idModel)))
//        {
//            db.update(TABLE_EDETAILING, initialValues, "id=?", new String[] {String.valueOf(idModel)});
//
//        }
//        else
//        {
//            db.insert(TABLE_EDETAILING, null, initialValues);
//
//        }
        db.close();
    }

    public void updateFavourite(String idModel,int isFavInt)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAV, isFavInt);

        db.update(TABLE_EDETAILING, initialValues, "id=?", new String[] {String.valueOf(idModel)});
        db.close();
    }

    // check data is already exist.
    public boolean CheckIsDataAlreadyInDBorNot(String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_EDETAILING + " where " + KEY_ID + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //insert file path using id
    public void insertFilePath(int isDownloaded,String filePath,String idModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(IS_DOWNLOADED, isDownloaded);
        initialValues.put(FILEPATh, filePath);

        db.update(TABLE_EDETAILING, initialValues, "id=?", new String[] {String.valueOf(idModel)});  // number 1 is the _id here, update to variable for your code
        db.close();
    }

    //get all edetailing list
    public ArrayList<DevisionModel.Data.EDetailing> getAlleDetail() {
        ArrayList<DevisionModel.Data.EDetailing> edetailList = new ArrayList<DevisionModel.Data.EDetailing>();

        String selectQuery = "SELECT  * FROM " + TABLE_EDETAILING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //convert save string to model class
                String data=cursor.getString(1);
                Gson gson = new Gson();
                DevisionModel.Data.EDetailing contact  = gson.fromJson(data, DevisionModel.Data.EDetailing.class);

                //if downloaded file path exist then add to edetailng model class
                if(cursor.getString(3)!=null)
                {
                    contact.setFilePath(cursor.getString(3));
                    contact.setIsSaved(Integer.parseInt(cursor.getString(2)));
                }

                edetailList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return edetailList;
    }


    //get all edetailing list
    public ArrayList<DevisionModel.Data.EDetailing> getSelectedeDetail(boolean isDownloaded) {
        ArrayList<DevisionModel.Data.EDetailing> edetailList = new ArrayList<DevisionModel.Data.EDetailing>();

        String selectQuery = "SELECT  * FROM " + TABLE_EDETAILING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //convert save string to model class
                String data=cursor.getString(1);
                Gson gson = new Gson();
                DevisionModel.Data.EDetailing edetailModel  = gson.fromJson(data, DevisionModel.Data.EDetailing.class);

                //if downloaded file path exist then add to edetailng model class
                if(cursor.getString(3)!=null)
                {
                    edetailModel.setFilePath(cursor.getString(3));
                    edetailModel.setIsSaved(Integer.parseInt(cursor.getString(2)));
                }

                if(isDownloaded && edetailModel.getIsSaved()==1)
                {
                    edetailList.add(edetailModel);
                }
                else if(!isDownloaded && edetailModel.getIsSaved()!=1)
                {
                    edetailList.add(edetailModel);
                }

            } while (cursor.moveToNext());
        }
        db.close();
        return edetailList;
    }

    public boolean getDownloadStatus(ArrayList<DevisionModel.Data.EDetailing.EretailDetail> eretailDetailList)
    {
        boolean returnValue=false;
        for(int i=0;i<eretailDetailList.size();i++)
        {
            DownloadFileModel returnData= getSingleDownloadedData(eretailDetailList.get(i).getFileId());
            if(returnData.getFileName()!=null)
            {
                returnValue=true;
            }
            else
            {
                returnValue=false;
                break;
            }
        }

        return returnValue;
    }


    //getFav edetailing
    @SuppressLint("Range")
    public  ArrayList<DevisionModel.Data.EDetailing> getAllFavBrands()
    {
        int id=1;
        ArrayList<DevisionModel.Data.EDetailing> downloadFileList=new ArrayList<>();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILING, new String[] { EDETAILING_DATA,IS_DOWNLOADED,FILEPATh}, ISFAV + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data=cursor.getString(cursor.getColumnIndex("e_data"));
                Gson gson = new Gson();
                DevisionModel.Data.EDetailing contact  = gson.fromJson(data, DevisionModel.Data.EDetailing.class);

                if(cursor.getString(cursor.getColumnIndex("filePath"))!=null)
                {
                    contact.setFilePath(cursor.getString(cursor.getColumnIndex("filePath")));
                    contact.setIsSaved(cursor.getInt(cursor.getColumnIndex("isDownloaded")));
                }
                downloadFileList.add(contact);

            }
            while (cursor.moveToNext());
        }
        return downloadFileList;
    }

    //get single file path using id
    @SuppressLint("Range")
    public  String getDownloadedSingleData(String id)
    {
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILING, new String[] { KEY_ID,
                        FILEPATh }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data=cursor.getString(cursor.getColumnIndex("filePath"));
            } while (cursor.moveToNext());
        }
        return data;
    }

    public void deleteEdetailingData(String id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EDETAILING, KEY_ID+" = ?", new String[]{id});
        db.close();

    }

    //=======================================EDetailingDownload table============================================

    //insert or update edetailing table
    public void insertOrUpdateEDetailDownload(int parentId,int childId,String data,String downloadedtype) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IDParent, parentId);
        initialValues.put(KEY_IDChild, childId);
        initialValues.put(EDETALING_DOWNLOAD_DATA, data);
        initialValues.put(EDETALING_TYPE, downloadedtype);

        //first check is data available if yes then update if no then insert
        if(CheckEdetailDownload(String.valueOf(childId),"child"))
        {
            db.update(TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", new String[] {String.valueOf(childId)});
            db.close();
        }
        else
        {
            db.insert(TABLE_EDETAILINGDOWNLOAD, null, initialValues);
            db.close();
        }

    }

    // check data is already exist.
    public boolean CheckEdetailDownload(String fieldValue,String idType)
    {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query ="";
        if(idType.equals("child"))
        {
            Query = "Select * from " + TABLE_EDETAILINGDOWNLOAD + " where " + KEY_IDChild + " = " + fieldValue;
        }
        else
        {
            Query = "Select * from " + TABLE_EDETAILINGDOWNLOAD + " where " + KEY_IDParent + " = " + fieldValue;
        }

        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //get single file path using id
    @SuppressLint("Range")
    public  DownloadFileModel getSingleDownloadedData(int id)
    {
        DownloadFileModel downloadFileModel=new DownloadFileModel();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILINGDOWNLOAD, new String[] { KEY_IDChild,
                        EDETALING_DOWNLOAD_DATA,ISFAVITEM }, KEY_IDChild + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {
                data=cursor.getString(cursor.getColumnIndex("e_detailData"));
                int getFav=cursor.getInt(cursor.getColumnIndex("is_favItem"));
                Gson gson = new Gson();
                downloadFileModel  = gson.fromJson(data, DownloadFileModel.class);
                if(getFav==1)
                {
                    downloadFileModel.setFavFile(true);
                }


            }
            while (cursor.moveToNext());
        }
        return downloadFileModel;
    }

    //get all file path using id
    @SuppressLint("Range")
    public  ArrayList<DownloadFileModel> getAllDownloadedData(int id)
    {
        ArrayList<DownloadFileModel> downloadFileList=new ArrayList<>();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILINGDOWNLOAD, new String[] { KEY_IDParent,
                        EDETALING_DOWNLOAD_DATA,ISFAVITEM }, KEY_IDParent + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DownloadFileModel downloadFileModel=new DownloadFileModel();

                data=cursor.getString(cursor.getColumnIndex("e_detailData"));
                int getFav=cursor.getInt(cursor.getColumnIndex("is_favItem"));

                Gson gson = new Gson();
                downloadFileModel  = gson.fromJson(data, DownloadFileModel.class);
                if(getFav==1)
                {
                    downloadFileModel.setFavFile(true);

                }
                downloadFileModel.setFavFileName("");

                downloadFileList.add(downloadFileModel);
            }
            while (cursor.moveToNext());
        }
        return downloadFileList;
    }

    //get all file path using id
    @SuppressLint("Range")
    public  ArrayList<DownloadFileModel> getAllFavListChild(int id)
    {
        ArrayList<DownloadFileModel> downloadFileList=new ArrayList<>();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILINGDOWNLOAD, new String[] { KEY_IDParent,
                        EDETALING_DOWNLOAD_DATA,ISFAVITEM }, KEY_IDParent + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DownloadFileModel downloadFileModel=new DownloadFileModel();

                data=cursor.getString(cursor.getColumnIndex("e_detailData"));
                int getFav=cursor.getInt(cursor.getColumnIndex("is_favItem"));

                if(getFav==1)
                {
                    Gson gson = new Gson();
                    downloadFileModel  = gson.fromJson(data, DownloadFileModel.class);
                    downloadFileModel.setFavFile(true);

                    downloadFileList.add(downloadFileModel);
                }

            }
            while (cursor.moveToNext());
        }
        return downloadFileList;
    }

    public void deleteEdetailDownloada(String id)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(CheckEdetailDownload(id,"parent"))
        {
            db.delete(
                    TABLE_EDETAILINGDOWNLOAD,  // Where to delete
                    KEY_IDParent+" = ?",
                    new String[]{id}
            );
        }
        db.close();

    }

    @SuppressLint("Range")
    public ArrayList<DownloadFileModel> getAllDataUsingType(String type)
    {
        ArrayList<DownloadFileModel> downloadFileList=new ArrayList<>();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILINGDOWNLOAD, new String[] { KEY_IDParent,
                        EDETALING_DOWNLOAD_DATA }, EDETALING_TYPE + "=?",
                new String[] { type }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data=cursor.getString(cursor.getColumnIndex("e_detailData"));
                Gson gson = new Gson();
                downloadFileList.add(gson.fromJson(data, DownloadFileModel.class));

            }
            while (cursor.moveToNext());
        }
        return downloadFileList;
    }

    @SuppressLint("Range")
    public ArrayList<DownloadFileModel> getAllFavList()
    {
        ArrayList<DownloadFileModel> downloadFileList=new ArrayList<>();
        String data="";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EDETAILINGDOWNLOAD, new String[] { KEY_IDParent,
                        EDETALING_DOWNLOAD_DATA,ISFAVITEM }, ISFAVITEM + "=?",
                new String[] { "1" }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                data=cursor.getString(cursor.getColumnIndex("e_detailData"));
                Gson gson = new Gson();
                DownloadFileModel model=new DownloadFileModel();
                model=gson.fromJson(data, DownloadFileModel.class);
                model.setFavFile(true);
                downloadFileList.add(model);

            }
            while (cursor.moveToNext());
        }
        return downloadFileList;
    }

    public void updateFavouriteItem(String idModel,int isFavInt)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAVITEM, isFavInt);

        db.update(TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", new String[] {String.valueOf(idModel)});
        db.close();
    }


    public void updateFavouriteItemWithModel(String idModel,int isFavInt, String data)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAVITEM, isFavInt);
        initialValues.put(EDETALING_DOWNLOAD_DATA, data);

        db.update(TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", new String[] {String.valueOf(idModel)});
        db.close();
    }

    //=======================================VisualAds table==============================================
    //insert or update visual ads table
    public void insertStartTimeSlide(String currentTime,int addDoctorId,int addBrandId, String brandName,int monitorTime,String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SLIDE_START_TIME, currentTime);
        initialValues.put(DOCTOR_ID, addDoctorId);
        initialValues.put(BRAND_ID, addBrandId);
        initialValues.put(BRAND_NAME, brandName);
        initialValues.put(START_TIME_BRANDS, startTime);

        if(CheckVisualAds(currentTime,addBrandId))
        {
//           String brandingStartTime=getBrandStartTime(String.valueOf(addBrandId),currentTime);
//           Log.e("hfisdhfuisgf",brandingStartTime);
//           ContentValues initialValues2 = new ContentValues();
//           initialValues2.put(START_TIME_BRANDS, brandingStartTime+startTime);
//
//           db.update(TABLE_SENDVISUALADS, initialValues2, "set_timeslide=? AND brand_id= ? AND doctor_id= ?", new String[] {String.valueOf(startTime), String.valueOf(addBrandId), String.valueOf(addDoctorId)});  // number 1 is the _id here, update to variable for your code
//
//           Log.e("insertStartTimeSlide","update");
        }
        else
        {
            Log.e("insertStartTimeSlide","insert");
            db.insert(TABLE_SENDVISUALADS, null, initialValues);
        }
        db.close();
    }

    public void updateBrandEndTime(String endTime, String startTime, int doctorID, int brandID)
    {
        //  String brandingStartTime=getBrandStartTime(String.valueOf(brandID),startTime);
//
        //  SQLiteDatabase db = this.getWritableDatabase();
        //  ContentValues initialValues = new ContentValues();
        //  initialValues.put(START_TIME_BRANDS, brandingStartTime+"-"+endTime+", ");
        //  db.update(TABLE_SENDVISUALADS, initialValues, "set_timeslide=? AND brand_id= ? AND doctor_id= ?", new String[] {String.valueOf(startTime), String.valueOf(brandID), String.valueOf(doctorID)});  // number 1 is the _id here, update to variable for your code
        //  db.close();
    }

    //Add end time
    public void updateendData(String endTime, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SLIDE_STOP_TIME, endTime);
        initialValues.put(IS_SUBMIT, 1);

        db.update(TABLE_SENDVISUALADS, initialValues, "set_timeslide=?", new String[] {String.valueOf(startTime)});  // number 1 is the _id here, update to variable for your code
        db.close();
    }

    //get all submitted visualads list
    public ArrayList<VisualAdsModel_Send> getAllSubmitVisual() {
        ArrayList<VisualAdsModel_Send> edetailList = new ArrayList<VisualAdsModel_Send>();
        String selectQuery = "SELECT  * FROM " + TABLE_SENDVISUALADS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id=cursor.getInt(0);
                String startTime= cursor.getString(3);
                int isEnd=cursor.getInt(5);

                if(isEnd==1)
                {
                    VisualAdsModel_Send visualModel  = new VisualAdsModel_Send();
                    visualModel.setId(id);
                    visualModel.setDoctorId(cursor.getString(1));
                    visualModel.setStartDate(startTime);
                    visualModel.setEndDate(cursor.getString(4));
                    visualModel.setBrandId(cursor.getString(2));
                    visualModel.setBrandName(cursor.getString(6));
                    visualModel.setMonitorTime(cursor.getInt(7));
                    //  visualModel.setBrandWiseStartTime(cursor.getString(8));
                    //  visualModel.setBrandWiseStopTime(cursor.getString(9));

                    visualModel.setEnd(true);
                    visualModel.setChildDataArray(getAllChildBy_ID(startTime));
                    edetailList.add(visualModel);

                }
                else{
                    deleteVisualAds(String.valueOf(id));
                    deleteVisualAdsChild(startTime);
                }

            } while (cursor.moveToNext());
        }
        return edetailList;
    }

    //delete single entry
    public boolean  deleteVisualAds(String deleteId) {

        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_SENDVISUALADS, KEY_ID + " = ?",
                new String[] { String.valueOf(deleteId) }) > 0;
    }

    // delete all data
    public void deleteAllVisualAds()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SENDVISUALADS);
    }

    public void insertBrandTime(int setTime, String mainTime,String brandId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(MONITOR_TIME, setTime);
        db.update(TABLE_SENDVISUALADS, initialValues, SLIDE_START_TIME+" = ? AND "+BRAND_ID+" = ? ", new String[] {mainTime, brandId});  // number 1 is the _id here, update to variable for your code
        db.close();
    }

    @SuppressLint("Range")
    public int getBrandTime(String brandId, String mainTime)
    {
        int getDbTimer= 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SENDVISUALADS, new String[] {MONITOR_TIME}, SLIDE_START_TIME+" = ? AND "+BRAND_ID+" = ? ",
                new String[] {mainTime,brandId}, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do { getDbTimer=cursor.getInt(cursor.getColumnIndex(MONITOR_TIME)); }
            while (cursor.moveToNext());
        }
        return getDbTimer;
    }

    @SuppressLint("Range")
    public String getBrandStartTime(String brandId, String mainTime)
    {
        String getSingleStartTime="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SENDVISUALADS, new String[] {START_TIME_BRANDS}, SLIDE_START_TIME+" = ? AND "+BRAND_ID+" = ? ",
                new String[] {mainTime,brandId}, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do { getSingleStartTime=cursor.getString(cursor.getColumnIndex(START_TIME_BRANDS));
            }
            while (cursor.moveToNext());
        }
        return getSingleStartTime;
    }


    @SuppressLint("Range")
    // check visual ads is already exist.
    public boolean CheckVisualAds(String startTime, int brandId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SENDVISUALADS, new String[] {SLIDE_START_TIME}, BRAND_ID + "=?",
                new String[] { String.valueOf(brandId) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do {
                String dbDateTime=cursor.getString(cursor.getColumnIndex("set_timeslide"));
                if(dbDateTime.equals(startTime))
                {
                    return true;
                }
            }
            while (cursor.moveToNext());
        }
        return false;
    }


    // =======================================Child visual ads table================================================

    //insert file id
    public void  insertFileID(int setfileId,String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TIMESET, startTime);
        initialValues.put(FILE_ID, setfileId);

        if(ChecktimeandFileIdExist(String.valueOf(setfileId),startTime))
        { }
        else
        {
            db.insert(TABLE_CHILD_VISUAL_ADS, null, initialValues);
            db.close();
        }

    }

    //insert like or dislike
    public void  insertlike(int setLike,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISLIKE, setLike);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        db.close();
    }

    //insert comment
    public void  insertComment(String setComment,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COMMENT, setComment);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        db.close();
    }

    //insert time in second
    public void insertTime(int setTime,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SINGLE_SLIDE_TIMER, setTime);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        db.close();

    }

    //getLike
    @SuppressLint("Range")
    public boolean getLike(String setfileId, String startTime)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID}, TIMESET + "=?",
                new String[] { String.valueOf(startTime) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do {
                int data=cursor.getInt(cursor.getColumnIndex("is_like"));
                int dbFileID =cursor.getInt(cursor.getColumnIndex("file_id"));
                if(data==1)
                {
                    if(dbFileID==Integer.parseInt(setfileId))
                    {
                        return true;
                    }
                }
            }
            while (cursor.moveToNext());
        }
        return false;
    }

    //get comment
    @SuppressLint("Range")
    public String getComment(String setfileId, String startTime)
    {
        String getDbComment=" ";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID}, TIMESET + "=?",
                new String[] { String.valueOf(startTime) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do {
                int dbFileID =cursor.getInt(cursor.getColumnIndex("file_id"));

                if(dbFileID==Integer.parseInt(setfileId))
                {
                    getDbComment=cursor.getString(cursor.getColumnIndex("_comment"));
                }
            }
            while (cursor.moveToNext());
        }
        return getDbComment;
    }

    //get time in second
    @SuppressLint("Range")
    public int getTime(String setfileId, String startTime)
    {
        int getDbTimer= 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID}, TIMESET + "=?",
                new String[] { String.valueOf(startTime) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do {
                int dbFileID =cursor.getInt(cursor.getColumnIndex("file_id"));

                if(dbFileID==Integer.parseInt(setfileId))
                {
                    getDbTimer=cursor.getInt(cursor.getColumnIndex("singleSlide_timer"));
                }
            }
            while (cursor.moveToNext());
        }
        return getDbTimer;
    }

    // get all same id childs
    @SuppressLint("Range")
    public ArrayList<VisualAdsModel_Send.childData> getAllChildBy_ID(String startTime)
    {
        ArrayList<VisualAdsModel_Send.childData> childListVisualAds=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID }, TIMESET + "=?",
                new String[] { String.valueOf(startTime) }, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do
            {

                VisualAdsModel_Send.childData model = new VisualAdsModel_Send.childData();

                int isLike=  cursor.getInt(cursor.getColumnIndex("is_like"));
                String comment=  cursor.getString(cursor.getColumnIndex("_comment"));
                String singleSlideTimer=  cursor.getString(cursor.getColumnIndex("singleSlide_timer"));
                int fileId=   cursor.getInt(cursor.getColumnIndex("file_id"));

                if(isLike==1)
                {
                    model.setLike(true);
                }
                model.setComment(comment);
                model.setViewTime(singleSlideTimer);
                model.setFileId(fileId+"");

                childListVisualAds.add(model);

            }
            while (cursor.moveToNext());
        }
        return childListVisualAds;
    }

    //delete visual Ads child
    public boolean  deleteVisualAdsChild(String startDate) {

        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_CHILD_VISUAL_ADS, TIMESET + " = ?",
                new String[] { String.valueOf(startDate) }) > 0;
    }

    // delete all child data
    public void deleteAllChildVisual()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_CHILD_VISUAL_ADS);
    }

    //check is visualads exist using date and fileID
    @SuppressLint("Range")
    public boolean ChecktimeandFileIdExist(String fileIdCheck,String dateTime) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID}, TIMESET + "=?",
                new String[] { String.valueOf(dateTime) }, null, null, null, null);

        if (cursor.moveToFirst())
        {
            do {
                int dbFileID =cursor.getInt(cursor.getColumnIndex("file_id"));

                if(dbFileID==Integer.parseInt(fileIdCheck))
                {
                    return true;
                }
            }
            while (cursor.moveToNext());
        }
        return false;
    }

    // =======================================api storage table================================================

    public void insertOrUpdateSaveAPI(int id,String data,String apiType) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(API_TYPE,apiType);
        initialValues.put(APIKEY_DATA, data);

        int u = db.update(TABLE_SAVE_SEND_API, initialValues, "id=?", new String[]{String.valueOf(id)});

        if (u == 0) {
            db.insert(TABLE_SAVE_SEND_API, null, initialValues);
        }

    }

    @SuppressLint("Range")
    public ArrayList<String>getAllSaveSend(String type)
    {
        ArrayList<String> saveStringList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVE_SEND_API, new String[] { APIKEY_DATA}, API_TYPE + "=?",
                new String[] {type }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String data=cursor.getString(cursor.getColumnIndex(APIKEY_DATA));
                saveStringList.add(data);
            }
            while (cursor.moveToNext());
        }
        return saveStringList;
    }

    public boolean  deleteSaveSend(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_SAVE_SEND_API, KEY_ID + " = ?",
                new String[] { String.valueOf(id) }) > 0;
    }

//=============================================Doctor e detailing table===================================

    public void insertdoctorData(int doctorID,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(DOCTOR_ID, doctorID);
        initialValues.put(DETAILING_DATE, date);
        db.insert(TABLE_SAVE_DOCTOR_EDETAIL, null, initialValues);
        db.close();
    }

    public boolean isEDetailingAvailable(int doctorId, String detailingDate)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVE_DOCTOR_EDETAIL, new String[] {DOCTOR_ID,DETAILING_DATE}, DOCTOR_ID + "=? AND "+DETAILING_DATE + "=?",
                new String[] { String.valueOf(doctorId), detailingDate}, null, null, null, null);
        if (cursor.moveToFirst())
        {
            do { return true; }
            while (cursor.moveToNext());
        }
        return false;
    }

//    public String getApiDetail(int id)
//    {
//        String strData = "";
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.query(TABLE_SAVE_SEND_API, new String[] { KEY_ID,
//                        APIKEY_DATA }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor.moveToFirst()) {
//            do {
//                strData=cursor.getString(1);
//            } while (cursor.moveToNext());
//        }
//        return strData;
//    }

}

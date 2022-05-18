package in.processmaster.salestripclm.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import in.processmaster.salestripclm.models.DailyDocVisitModel;
import in.processmaster.salestripclm.models.DevisionModel;
import in.processmaster.salestripclm.models.DownloadFileModel;
import in.processmaster.salestripclm.models.SyncModel;
import in.processmaster.salestripclm.models.VisualAdsModel_Send;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

//Java T point
public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SalesTrip_CLM_db";
    private static final String KEY_ID = "id";

    //=========================================hold sync api data==============================================
    private static final String TABLE_SYNC_API = "syncDataTable";
    private static final String SETTINGDCR = "settingDcrData";
    private static final String WORKTYPELIST = "workTypeList";
    private static final String PRODUCTLIST = "productList";
    private static final String BRANDLIST = "brandList";
    private static final String WORKINGWITHLIST = "workingWithList";
    private static final String FIELDSTAFFTEAMLIST = "fieldStaffTeamList";
    private static final String RETAILERFIELDCONFIGDICT = "retailerFieldConfigDict";
    private static final String CONFIGURATIONSETTING = "configurationSetting";
    private static final String SCHEMELIST = "schemeList";
    private static final String RTPDETAILDATA = "rtpDetailData";
    private static final String DOCTORLIST = "doctorList";
            //===================================Create sync api route list=====================================
            private static final String TABLE_SYNCROUTE_API = "syncRouteTable";
            //===================================Create sync api Retailer list=====================================
            private static final String TABLE_SYNCRETAILER_API = "syncRetailerTable";
            //===================================Create sync api Docotor list=====================================
            private static final String TABLE_SYNCPRODUCT_API = "syncDocotrTable";


    //=========================================hold api Data using static id===============================================

    //id=1 for sync api, id=2 for getScheduleMeetingApi, id=3 for getQuantityApi id=4 for getDoctorGraphApi
    // id=5 for getDocCall api // id=6 for profile api //id=7 for save temp edetailing feeback form


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

    //==========================================Save custome presentation edeailing date wise=============================
    private static final String TABLE_CREATE_EDETAIL_PRESENTATION = "create_eDetail";
    private static final String PRESENTATION_NAME = "presentaion_name";
    private static final String EDETAILING_ITEM = "edetailing_item";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // ==============================sync api table=====================================
        //sync api database
        String CREATE_SYNC_TABLE = "CREATE TABLE " + TABLE_SYNC_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + SETTINGDCR + " TEXT,"
                + WORKTYPELIST + " TEXT," + PRODUCTLIST + " TEXT,"
                + BRANDLIST + " TEXT,"  + WORKINGWITHLIST + " TEXT,"  + FIELDSTAFFTEAMLIST + " TEXT,"
                + RETAILERFIELDCONFIGDICT + " TEXT," + DOCTORLIST + " TEXT," + CONFIGURATIONSETTING + " TEXT," + SCHEMELIST + " TEXT,"
                + RTPDETAILDATA + " TEXT" +")";
        db.execSQL(CREATE_SYNC_TABLE);
            // sync sub data tables
        String CREATE_ROUTE_TABLE = "CREATE TABLE " + TABLE_SYNCROUTE_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_ROUTE_TABLE);
        String CREATE_RETAILER_TABLE = "CREATE TABLE " + TABLE_SYNCRETAILER_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_RETAILER_TABLE);
        String CREATE_DOCTOR_TABLE = "CREATE TABLE " + TABLE_SYNCPRODUCT_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_DOCTOR_TABLE);


        //Common api database
        String CREATE_COMMONAPI_TABLE = "CREATE TABLE " + TABLE_SAVE_API + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATA + " TEXT" + ")";
        db.execSQL(CREATE_COMMONAPI_TABLE);

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
                +  BRAND_ID + " INTEGER,"
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

        //store edetailing custome presentaion wise
        String CREATE_CUSTOME_EDETAIL = "CREATE TABLE " + TABLE_CREATE_EDETAIL_PRESENTATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                +  PRESENTATION_NAME + " TEXT,"
                +  EDETAILING_ITEM + " TEXT"
                + ")";
        db.execSQL(CREATE_CUSTOME_EDETAIL);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNC_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCROUTE_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCRETAILER_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCPRODUCT_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SENDVISUALADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDETAILINGDOWNLOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILD_VISUAL_ADS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_SEND_API);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_DOCTOR_EDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_EDETAIL_PRESENTATION);
        onCreate(db);
    }

    //====================================== add sync api data methods=============================

    public void addSYNCAPIData(String setting, String workingType,String productlist, String brandList,
            String workingWithList,String fieldStaffList, String retailerFieldList, String configSetting, String schemeList,
            String rtpDetailList,int id,String  doctorList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SETTINGDCR, setting);
        values.put(WORKTYPELIST, workingType);
        values.put(PRODUCTLIST, productlist);
        values.put(BRANDLIST, brandList);
        values.put(WORKINGWITHLIST, workingWithList);
        values.put(FIELDSTAFFTEAMLIST, fieldStaffList);
        values.put(RETAILERFIELDCONFIGDICT, retailerFieldList);
        values.put(CONFIGURATIONSETTING, configSetting);
        values.put(SCHEMELIST, schemeList);
        values.put(RTPDETAILDATA, rtpDetailList);
        values.put(DOCTORLIST, doctorList);

        int u = db.update(TABLE_SYNC_API, values, "id=?", new String[]{String.valueOf(id)});
        if (u == 0) {
            values.put(KEY_ID, id);
            db.insert(TABLE_SYNC_API, null, values);
        }
        //db.close();

    }

    @SuppressLint("Range")
    public SyncModel.Data getSYNCApiData(int id)
    {
        String strData = "";
        SyncModel.Data syncModel=new SyncModel.Data();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SYNC_API, new String[] {
                        SETTINGDCR, WORKTYPELIST,PRODUCTLIST,WORKINGWITHLIST,CONFIGURATIONSETTING,SCHEMELIST,FIELDSTAFFTEAMLIST,DOCTORLIST}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                Gson gson= new Gson();
                syncModel.setSettingDCR(gson.fromJson(cursor.getString(cursor.getColumnIndex(SETTINGDCR)), SyncModel.Data.SettingDCR.class));
                syncModel.setConfigurationSetting(cursor.getString(cursor.getColumnIndex(CONFIGURATIONSETTING)));
                Type typeWorkType = new TypeToken<ArrayList<SyncModel.Data.WorkType>>() {}.getType();
                Type typeproduct = new TypeToken<ArrayList<SyncModel.Data.Product>>() {}.getType();
                Type typeWorkingWith = new TypeToken<ArrayList<SyncModel.Data.WorkingWith>>() {}.getType();
                Type typeschemeList = new TypeToken<ArrayList<SyncModel.Data.Scheme>>() {}.getType();
                Type typeschemeFieldStaff = new TypeToken<ArrayList<SyncModel.Data.FieldStaffTeam>>() {}.getType();
                Type typeDocotrList = new TypeToken<ArrayList<SyncModel.Data.Doctor>>() {}.getType();

                syncModel.setWorkTypeList(gson.fromJson(cursor.getString(cursor.getColumnIndex(WORKTYPELIST)), typeWorkType));
              //  syncModel.setProductList(gson.fromJson(cursor.getString(cursor.getColumnIndex(PRODUCTLIST)), typeproduct));
                syncModel.setWorkingWithList(gson.fromJson(cursor.getString(cursor.getColumnIndex(WORKINGWITHLIST)), typeWorkingWith));
                syncModel.setSchemeList(gson.fromJson(cursor.getString(cursor.getColumnIndex(SCHEMELIST)), typeschemeList));
                syncModel.setFieldStaffTeamList(gson.fromJson(cursor.getString(cursor.getColumnIndex(FIELDSTAFFTEAMLIST)), typeschemeFieldStaff));
                syncModel.setDoctorList(gson.fromJson(cursor.getString(cursor.getColumnIndex(DOCTORLIST)), typeDocotrList));
                syncModel.setRetailerList(getAllRetailers());
                syncModel.setRouteList(getAllRoutes());
                syncModel.setProductList(getAllProduct());


            } while (cursor.moveToNext());
        }
        return syncModel;
    }

    public void addRoutes(ArrayList< SyncModel.Data.Route> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for(int i=0 ; i<list.size();i++)
            {
                SyncModel.Data.Route route = list.get(i);
                values.put(KEY_DATA, new Gson().toJson(route));
                db.insert(TABLE_SYNCROUTE_API, null, values);
                if(i==list.size()-1)
                {
                    Log.e("greaterThanaddRoutes","greater");
                    db.setTransactionSuccessful();
                }
            }

        } finally {
            db.endTransaction();
        }
    }

    public void addRetailer(ArrayList< SyncModel.Data.Retailer> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for(int i=0 ; i<list.size();i++)
            {
                SyncModel.Data.Retailer retail = list.get(i);
                values.put(KEY_DATA, new Gson().toJson(retail));
                db.insert(TABLE_SYNCRETAILER_API, null, values);
                if(i==list.size()-1)
                {
                    Log.e("greaterThanaddRetailer","greater");
                    db.setTransactionSuccessful();
                }
            }

        } finally {
            db.endTransaction();
        }
    }

    public void addProduct(ArrayList< SyncModel.Data.Product> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for(int i=0 ; i<list.size();i++)
            {
                SyncModel.Data.Product product = list.get(i);
                values.put(KEY_DATA, new Gson().toJson(product));
                db.insert(TABLE_SYNCPRODUCT_API, null, values);
                if(i==list.size()-1)
                {
                    Log.e("greaterThanaddProd","greater");
                    db.setTransactionSuccessful();
                }
            }
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public ArrayList<SyncModel.Data.Route> getAllRoutes() {
        ArrayList<SyncModel.Data.Route> routeList = new ArrayList<SyncModel.Data.Route>();

        String selectQuery = "SELECT  * FROM " + TABLE_SYNCROUTE_API;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
               String data=cursor.getString(cursor.getColumnIndex(KEY_DATA));
                Gson gson = new Gson();
                SyncModel.Data.Route contact  = gson.fromJson(data, SyncModel.Data.Route.class);
                routeList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return routeList;
    }


    @SuppressLint("Range")
    public ArrayList<SyncModel.Data.Retailer> getAllRetailers() {
        ArrayList<SyncModel.Data.Retailer> retailerList = new ArrayList<SyncModel.Data.Retailer>();

        String selectQuery = "SELECT  * FROM " + TABLE_SYNCRETAILER_API;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String data=cursor.getString(cursor.getColumnIndex(KEY_DATA));
                Gson gson = new Gson();
                SyncModel.Data.Retailer contact  = gson.fromJson(data, SyncModel.Data.Retailer.class);
                retailerList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return retailerList;
    }

    @SuppressLint("Range")
    public ArrayList<SyncModel.Data.Product> getAllProduct() {
        ArrayList<SyncModel.Data.Product> productList = new ArrayList<SyncModel.Data.Product>();

        String selectQuery = "SELECT  * FROM " + TABLE_SYNCPRODUCT_API;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String data=cursor.getString(cursor.getColumnIndex(KEY_DATA));
                Gson gson = new Gson();
                SyncModel.Data.Product contact  = gson.fromJson(data, SyncModel.Data.Product.class);
                productList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return productList;
    }


    // delete all data
    public void deleteAll_SYNCAPI()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SYNC_API);
        db.execSQL("delete from "+ TABLE_SYNCROUTE_API);
        db.execSQL("delete from "+ TABLE_SYNCRETAILER_API);
        db.execSQL("delete from "+ TABLE_SYNCPRODUCT_API);
    }

    public int getDatasCount() {
        int countCursor=0;
        String countQuery = "SELECT  * FROM " + TABLE_SYNC_API;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        countCursor=cursor.getCount();
        cursor.close();
        return countCursor;
    }


    //=====================================Common api DataBase method=====================================
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
        //db.close();

    }

    // delete all data
    public void deleteAll()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SAVE_API);
    }

    // Getting sync data Count



    public boolean  deleteApiData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_SAVE_API, KEY_ID + " = ?",
                new String[] { String.valueOf(id) }) > 0;
    }

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
                strData=cursor.getString(cursor.getColumnIndex(KEY_DATA));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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
        //db.close();
    }

    public void updateFavourite(String idModel,int isFavInt)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAV, isFavInt);
        db.update(TABLE_EDETAILING, initialValues, "id=?", new String[] {String.valueOf(idModel)});
        //db.close();
    }

    //insert file path using id
    public void insertFilePath(int isDownloaded,String filePath,String idModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(IS_DOWNLOADED, isDownloaded);
        initialValues.put(FILEPATh, filePath);
        db.update(TABLE_EDETAILING, initialValues, "id=?", new String[] {String.valueOf(idModel)});  // number 1 is the _id here, update to variable for your code
        //db.close();
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
        cursor.close();
        //db.close();
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
        cursor.close();
        //db.close();
        return edetailList;
    }

    public boolean getDownloadStatus(ArrayList<DevisionModel.Data.EDetailing.EretailDetail> eretailDetailList)
    {
        boolean returnValue=false;
        for(int i=0;i<eretailDetailList.size();i++)
        {
            DownloadFileModel returnData= getSingleDownloadedData(eretailDetailList.get(i).getFileId());
            if(returnData.getFileName()!=null && returnData.getFileName()!="" && !returnData.getFileName().isEmpty())
            { returnValue=true; }
            else
            { returnValue=false; break; }
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
                data=cursor.getString(cursor.getColumnIndex(EDETAILING_DATA));
                Gson gson = new Gson();
                DevisionModel.Data.EDetailing contact  = gson.fromJson(data, DevisionModel.Data.EDetailing.class);

                if(cursor.getString(cursor.getColumnIndex(FILEPATh))!=null)
                {
                    contact.setFilePath(cursor.getString(cursor.getColumnIndex(FILEPATh)));
                    contact.setIsSaved(cursor.getInt(cursor.getColumnIndex(IS_DOWNLOADED)));
                }
                downloadFileList.add(contact);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return downloadFileList;
    }

    public void deleteEdetailingData(String id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EDETAILING, KEY_ID+" = ?", new String[]{id});
        //db.close();
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
            //db.close();
        }
        else
        {
            db.insert(TABLE_EDETAILINGDOWNLOAD, null, initialValues);
            //db.close();
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
            //sqlb.close();
            return false;
        }
        cursor.close();
       // sqldb.close();
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
                data=cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA));
                int getFav=cursor.getInt(cursor.getColumnIndex(ISFAVITEM));
                Gson gson = new Gson();
                downloadFileModel  = gson.fromJson(data, DownloadFileModel.class);
                if(getFav==1)
                { downloadFileModel.setFavFile(true); }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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

                data=cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA));
                int getFav=cursor.getInt(cursor.getColumnIndex(ISFAVITEM));

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
        cursor.close();
        //db.close();
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
                    new String[]{id});
        }
        //db.close();
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
                data=cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA));
                Gson gson = new Gson();
                downloadFileList.add(gson.fromJson(data, DownloadFileModel.class));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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
                data=cursor.getString(cursor.getColumnIndex(EDETALING_DOWNLOAD_DATA));
                Gson gson = new Gson();
                DownloadFileModel model=new DownloadFileModel();
                model=gson.fromJson(data, DownloadFileModel.class);
                model.setFavFile(true);
                downloadFileList.add(model);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return downloadFileList;
    }

    public void updateFavouriteItem(String idModel,int isFavInt)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAVITEM, isFavInt);

        db.update(TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", new String[] {String.valueOf(idModel)});
        //db.close();
    }


    public void updateFavouriteItemWithModel(String idModel,int isFavInt, String data)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISFAVITEM, isFavInt);
        initialValues.put(EDETALING_DOWNLOAD_DATA, data);

        db.update(TABLE_EDETAILINGDOWNLOAD, initialValues, "id_child=?", new String[] {String.valueOf(idModel)});
        //db.close();
    }

    //=======================================VisualAds table==============================================
    //insert or update visual ads table
    public void insertStartTimeSlide(String startTime,int addDoctorId,int addBrandId, String brandName,int monitorTime,String currentTime)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SLIDE_START_TIME, startTime);
        initialValues.put(DOCTOR_ID, addDoctorId);
        initialValues.put(BRAND_ID, addBrandId);
        initialValues.put(BRAND_NAME, brandName);
        initialValues.put(START_TIME_BRANDS, currentTime);

        if(CheckVisualAds(startTime,addBrandId))
        {

        }
        else
        {
            Log.e("insertStartTimeSlide","insert");
            db.insert(TABLE_SENDVISUALADS, null, initialValues);
        }
        //db.close();
    }

    //Add end time
    public void updateendData(String endTime, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SLIDE_STOP_TIME, endTime);
        initialValues.put(IS_SUBMIT, 1);

        db.update(TABLE_SENDVISUALADS, initialValues, "set_timeslide=?", new String[] {String.valueOf(startTime)});  // number 1 is the _id here, update to variable for your code
        //db.close();
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
                    visualModel.setEnd(true);
                    visualModel.setFileTransList(getAllChildBy_ID(startTime,Integer.parseInt(visualModel.getBrandId())));
                    int durationCount=0;
                    for(int i=0 ; i < visualModel.getFileTransList().size() ;i++)
                    {
                        VisualAdsModel_Send.childData childData = visualModel.getFileTransList().get(i);
                        if(childData.getViewTime()!=null && !childData.getViewTime().isEmpty())
                        {
                            durationCount= durationCount+Integer.parseInt(childData.getViewTime());
                        }
                    }
                    visualModel.setDuration(durationCount);
                    edetailList.add(visualModel);
                }
                else{
                    deleteVisualAds(String.valueOf(id));
                    deleteVisualAdsChild(startTime);
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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
        //db.close();
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
        cursor.close();
        //db.close();
        return getDbTimer;
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
        cursor.close();
        //db.close();
        return false;
    }


    // =======================================Child visual ads table================================================

    //insert file id
    public void  insertFileID(int setfileId,String startTime, int brandId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(TIMESET, startTime);
        initialValues.put(FILE_ID, setfileId);
        initialValues.put(BRAND_ID , brandId);

        if(ChecktimeandFileIdExist(String.valueOf(setfileId),startTime))
        { }
        else
        {
            db.insert(TABLE_CHILD_VISUAL_ADS, null, initialValues);
            //db.close();
        }

    }

    //insert like or dislike
    public void  insertlike(int setLike,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(ISLIKE, setLike);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        //db.close();
    }

    //insert comment
    public void  insertComment(String setComment,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(COMMENT, setComment);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        //db.close();
    }

    //insert time in second
    public void insertTime(int setTime,int setfileId, String startTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(SINGLE_SLIDE_TIMER, setTime);

        db.update(TABLE_CHILD_VISUAL_ADS, initialValues, "setTime = ? AND file_id= ? ", new String[] {startTime, String.valueOf(setfileId)});  // number 1 is the _id here, update to variable for your code
        //db.close();

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
        cursor.close();
        //db.close();
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
        cursor.close();
        //db.close();
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
        cursor.close();
        //db.close();
        return getDbTimer;
    }

    // get all same id childs
    @SuppressLint("Range")
    public ArrayList<VisualAdsModel_Send.childData> getAllChildBy_ID(String startTime, int brandId)
    {
        ArrayList<VisualAdsModel_Send.childData> childListVisualAds=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CHILD_VISUAL_ADS, new String[] {TIMESET,ISLIKE,COMMENT,SINGLE_SLIDE_TIMER, FILE_ID }, TIMESET + "=?"+ " AND "+ BRAND_ID + "=?",
                new String[] { String.valueOf(startTime), String.valueOf(brandId)}, null, null, null, null);
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
        cursor.close();
        //db.close();
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
                { return true; }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
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
    public ArrayList<DailyDocVisitModel.Data.DcrDoctor>getAllSaveSend(String type)
    {
        ArrayList<DailyDocVisitModel.Data.DcrDoctor> edetailList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVE_SEND_API, new String[] { APIKEY_DATA}, API_TYPE + "=?",
                new String[] {type}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String data=cursor.getString(cursor.getColumnIndex(APIKEY_DATA));
                DailyDocVisitModel.Data.DcrDoctor getSaveModel  = new Gson().fromJson(data, DailyDocVisitModel.Data.DcrDoctor.class);
                getSaveModel.setOffline(true);
                edetailList.add(getSaveModel);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return edetailList;
    }

    @SuppressLint("Range")
    public ArrayList<DailyDocVisitModel.Data.DcrDoctor>getAllSaveSendRetailer(String type)
    {
        ArrayList<DailyDocVisitModel.Data.DcrDoctor> edetailList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SAVE_SEND_API, new String[] { APIKEY_DATA}, API_TYPE + "=?",
                new String[] {type}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String data=cursor.getString(cursor.getColumnIndex(APIKEY_DATA));
                DailyDocVisitModel.Data.DcrDoctor getSaveModel  = new Gson().fromJson(data, DailyDocVisitModel.Data.DcrDoctor.class);
                edetailList.add(getSaveModel);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return edetailList;
    }


    public boolean  deleteSaveSend(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_SAVE_SEND_API, KEY_ID + " = ?",
                new String[] { String.valueOf(id) }) > 0;
    }

    public void deleteSendEdetailing()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SAVE_SEND_API);
    }

//=============================================Doctor e detailing table===================================

    public void insertdoctorData(int doctorID,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(DOCTOR_ID, doctorID);
        initialValues.put(DETAILING_DATE, date);
        db.insert(TABLE_SAVE_DOCTOR_EDETAIL, null, initialValues);
        //db.close();
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


    // delete all data
    public void deleteAllDoctorEdetailing()
    {  SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_SAVE_DOCTOR_EDETAIL);
    }

    //===================================Create presentaion =======================================

    public void insertCreatePresentaionData(String presentaionName,String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(PRESENTATION_NAME, presentaionName);
        initialValues.put(EDETAILING_ITEM, data);
        db.insert(TABLE_CREATE_EDETAIL_PRESENTATION, null, initialValues);
    }

    public void updatePresentaionData(String presentaionName,String data)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(EDETAILING_ITEM, data);
        db.update(TABLE_CREATE_EDETAIL_PRESENTATION, initialValues, PRESENTATION_NAME+"=?", new String[]{presentaionName});
    }

    public void deletePresentaionData(String presentaionName)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CREATE_EDETAIL_PRESENTATION, PRESENTATION_NAME+"=?", new String[]{presentaionName});
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllSavedPresentationName() {
        ArrayList<String> presentationNameList = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_CREATE_EDETAIL_PRESENTATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                presentationNameList.add(cursor.getString(cursor.getColumnIndex(PRESENTATION_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return presentationNameList;
    }


    @SuppressLint("Range")
    public  ArrayList<DownloadFileModel> getAllPresentationItem(String name)
    {
        ArrayList<DownloadFileModel> itemList=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CREATE_EDETAIL_PRESENTATION, new String[] {
                        EDETAILING_ITEM }, PRESENTATION_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Type typeType = new TypeToken<ArrayList<DownloadFileModel>>() {}.getType();
                itemList.addAll(new Gson().fromJson(cursor.getString(cursor.getColumnIndex(EDETAILING_ITEM)), typeType));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }
}

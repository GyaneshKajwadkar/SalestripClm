package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.models.SyncModel

class CommonListGetClass {

    fun getWorkTypeForSpinner(): ArrayList<SyncModel.Data.WorkType> {
        var visitPurposeList= ArrayList<SyncModel.Data.WorkType>()
        var visitobj=SyncModel.Data.WorkType()
        visitobj.workType="Select"
        visitPurposeList?.add(visitobj)
         visitPurposeList?.addAll(SplashActivity.staticSyncData?.data?.workTypeList!!)
        return visitPurposeList;
    }

    fun workinWithListForSpinner(): ArrayList<SyncModel.Data.WorkingWith> {
        var workinWithList= ArrayList<SyncModel.Data.WorkingWith>()
        var workingobj=SyncModel.Data.WorkingWith()
        workingobj.fullName="Select"
        workinWithList?.add(workingobj)
        workinWithList?.addAll(SplashActivity.staticSyncData?.data?.workingWithList!!)
        return workinWithList;
    }

    fun getNonRouteListForSpinner(): ArrayList<SyncModel.Data.Route> {
        val routWithNegativeList =  SplashActivity.staticSyncData?.data?.routeList!!?.filter { s -> s.routeId <0} as java.util.ArrayList<SyncModel.Data.Route>
        var routeList= ArrayList<SyncModel.Data.Route>()
        var routeobj=SyncModel.Data.Route()
        routeobj.routeName="Select"
        routeList?.add(routeobj)
        routeList?.addAll(routWithNegativeList)
        return routeList;
    }

    fun getRouteListForSpinner(): ArrayList<SyncModel.Data.Route> {
        val routWithNegativeList =  SplashActivity.staticSyncData?.data?.routeList!!?.filter { s -> s.routeId >=0} as java.util.ArrayList<SyncModel.Data.Route>
        var routeList= ArrayList<SyncModel.Data.Route>()
        var routeobj=SyncModel.Data.Route()
        routeobj.routeName="Select"
        routeList?.add(routeobj)
        routeList?.addAll(routWithNegativeList)
        return routeList;
    }

}
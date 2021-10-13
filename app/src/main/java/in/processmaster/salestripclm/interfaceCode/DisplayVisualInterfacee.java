package in.processmaster.salestripclm.interfaceCode;

import in.processmaster.salestripclm.models.SyncModel;

import java.util.ArrayList;

public interface DisplayVisualInterfacee {
    void onClickString(String passingInterface);
    void onClickDoctor(ArrayList<SyncModel.Data.Doctor.LinkedBrand> passingInterfaceList);
}

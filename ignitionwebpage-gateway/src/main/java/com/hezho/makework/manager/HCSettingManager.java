package com.hezho.makework.manager;

import com.hezho.makework.records.HCSettingsRecord;
import com.inductiveautomation.ignition.gateway.localdb.persistence.IRecordListener;
import com.inductiveautomation.ignition.gateway.web.models.KeyValue;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class HCSettingManager implements IRecordListener<HCSettingsRecord> {
    @NonNull private final RecordManager recordManager;

    @Override
    public void recordUpdated(HCSettingsRecord hcSettingsRecord) {
        log.info("recordUpdated()");
//        recordManager.update(
//                recordManager.getOnlyRecord(HCSettingsRecord.META)
//        );
        //createRecords(hcSettingsRecord);
        updateRecords(hcSettingsRecord);


    }

    @Override
    public void recordAdded(HCSettingsRecord hcSettingsRecord) {
        log.info("recordAdded()");
    }

    @Override
    public void recordDeleted(KeyValue keyValue) {
        log.info("recordDeleted()");
    }

    // 不知道为什么，这个会持续更新 Id 是 0 的那个数据
    public void createRecords(@NonNull HCSettingsRecord hcSettingsRecord){
        log.debug("HELLO THERE, JUN 27 !");
        recordManager.createNewAndSave(
                HCSettingsRecord.META,
                record->
                        record.setBroadcastSSID(hcSettingsRecord.getBroadcastSSID())
                            .setAllowInterop(hcSettingsRecord.getAllowInterop())
                            .setHCDeviceCount(hcSettingsRecord.getHCDeviceCount())
                            .setHCHubName(hcSettingsRecord.getHCHubName())
                            .setHCIPAddress(hcSettingsRecord.getHCIPAddress())
                            .setHCPowerOutput(hcSettingsRecord.getHCPowerOutput())

        );
    }


    public void updateRecords(@NonNull HCSettingsRecord hcSettingsRecord){
        log.debug("HELLO THERE, JUN 27 !");
        recordManager.updateWithoutNotify(hcSettingsRecord);
    }

}

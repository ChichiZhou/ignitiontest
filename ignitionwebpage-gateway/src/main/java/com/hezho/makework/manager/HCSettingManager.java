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
    }

    @Override
    public void recordAdded(HCSettingsRecord hcSettingsRecord) {
        log.info("recordAdded()");
    }

    @Override
    public void recordDeleted(KeyValue keyValue) {
        log.info("recordDeleted()");
    }
}

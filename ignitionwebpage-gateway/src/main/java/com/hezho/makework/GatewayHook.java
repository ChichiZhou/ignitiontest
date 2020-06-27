package com.hezho.makework;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.hezho.makework.manager.HCSettingManager;
import com.hezho.makework.manager.RecordManager;
import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.util.LogUtil;
import com.inductiveautomation.ignition.common.util.LoggerEx;
import com.hezho.makework.records.HCSettingsRecord;
import com.hezho.makework.web.HCSettingsPage;
import com.inductiveautomation.ignition.gateway.localdb.persistence.IRecordListener;
import com.inductiveautomation.ignition.gateway.localdb.persistence.PersistenceInterface;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.components.ConfigPanel;
import com.inductiveautomation.ignition.gateway.web.models.ConfigCategory;
import com.inductiveautomation.ignition.gateway.web.models.DefaultConfigTab;
import com.inductiveautomation.ignition.gateway.web.models.IConfigTab;
import com.inductiveautomation.ignition.gateway.web.models.KeyValue;


/**
 * Filename: GatewayHook.java
 * Author: Perry Arellano-Jones
 * Created on: 5/21/15
 * Project: home-connect-example
 *
 * The "gateway hook" is the entry point for a module on the gateway.
 *
 * This example uses the new status and config pages for 7.9 and later.
 *
 */
public class GatewayHook extends AbstractGatewayModuleHook {
    private GatewayContext context;

    private final LoggerEx log = LogUtil.getLogger(getClass().getSimpleName());

    /**
     * This sets up the status panel which we'll add to the statusPanels list. The controller will be
     * HomeConnectStatusRoutes.java, and the model and view will be in our javascript folder. The status panel is optional
     * Only add if your module will provide meaningful info.
     */


    /**
     * This sets up the config panel
     */
    public static final ConfigCategory CONFIG_CATEGORY =
            new ConfigCategory("HomeConnect", "HomeConnect.nav.header", 700);

    @Override
    public List<ConfigCategory> getConfigCategories() {
        return Collections.singletonList(CONFIG_CATEGORY);
    }

    /**
     * An IConfigTab contains all the info necessary to create a link to your config page on the gateway nav menu.
     * In order to make sure the breadcrumb and navigation works properly, the 'name' field should line up
     * with the right-hand value returned from {@link ConfigPanel#getMenuLocation}. In this case name("homeconnect")
     * lines up with HCSettingsPage#getMenuLocation().getRight()
     */
    public static final IConfigTab HCE_CONFIG_ENTRY = DefaultConfigTab.builder()
            .category(CONFIG_CATEGORY)
            .name("homeconnect")
            .i18n("HomeConnect.nav.settings.title")
            .page(HCSettingsPage.class)
            .terms("home connect settings")
            .build();

    @Override
    public List<? extends IConfigTab> getConfigPanels() {
        return Collections.singletonList(
                HCE_CONFIG_ENTRY
        );
    }

    /**
     * We'll add an overview contributor. This is optional -- only add if your module will provide meaningful info.
     */



    @Override
    public void setup(GatewayContext gatewayContext) {
        this.context = gatewayContext;

        log.debug("Beginning setup of HomeConnect Module");

        // Register GatewayHook.properties by registering the GatewayHook.class with BundleUtils
        BundleUtil.get().addBundle("HomeConnect", getClass(), "HomeConnect");

        //Verify tables for persistent records if necessary
        verifySchema(context);



        // create records if needed
        maybeCreateHCSettings(context);

        // get the settings record and do something with it...
        //HCSettingsRecord theOneRecord = context.getLocalPersistenceInterface().find(HCSettingsRecord.META, 0L);

        PersistenceInterface persistenceInterface = context.getPersistenceInterface(); // use getPersistenceInterface here
        HCSettingManager hcSettingManager = new HCSettingManager(new RecordManager(persistenceInterface));

//        log.info("Hub name: " + theOneRecord.getHCHubName());
//        log.info("IP address: " + theOneRecord.getHCIPAddress());

        // listen for updates to the settings record...

//        HCSettingsRecord.META.addRecordListener(new IRecordListener<HCSettingsRecord>() {
//            @Override
//            public void recordUpdated(HCSettingsRecord hcSettingsRecord) {
//                log.info("recordUpdated()");
//            }
//
//            @Override
//            public void recordAdded(HCSettingsRecord hcSettingsRecord) {
//                log.info("recordAdded()");
//            }
//
//            @Override
//            public void recordDeleted(KeyValue keyValue) {
//                log.info("recordDeleted()");
//            }
//        });
        // 把这个部分逻辑另外放在一个别的 class 中
//        HCSettingManager hcSettingManager = new HCSettingManager();

        // 这个 addRecordListener 必须在这
        HCSettingsRecord.META.addRecordListener(hcSettingManager);

        log.debug("Setup Complete.");
    }

    private void verifySchema(GatewayContext context) {
        try {
            context.getSchemaUpdater().updatePersistentRecords(HCSettingsRecord.META);
        } catch (SQLException e) {
            log.error("Error verifying persistent record schemas for HomeConnect records.", e);
        }
    }

    public void maybeCreateHCSettings(GatewayContext context) {
        log.trace("Attempting to create HomeConnect Settings Record");
        try {
            HCSettingsRecord settingsRecord = context.getLocalPersistenceInterface().createNew(HCSettingsRecord.META);
            settingsRecord.setId(0L);
            settingsRecord.setHCIPAddress("192.168.1.99");
            settingsRecord.setHCHubName("HomeConnect Hub");
            settingsRecord.setHCPowerOutput(23);
            settingsRecord.setHCDeviceCount(15);
            settingsRecord.setBroadcastSSID(false);

            context.getSchemaUpdater().ensureRecordExists(settingsRecord);
        } catch (Exception e) {
            log.error("Failed to establish HCSettings Record exists", e);
        }

        log.trace("HomeConnect Settings Record Established");
    }


    @Override
    public void startup(LicenseState licenseState) {

    }

    @Override
    public void shutdown() {
        /* remove our bundle */
        BundleUtil.get().removeBundle("HomeConnect");
    }

    /**
     * The following methods are used by the status panel. Only add these if you are providing a status panel.
     */
}


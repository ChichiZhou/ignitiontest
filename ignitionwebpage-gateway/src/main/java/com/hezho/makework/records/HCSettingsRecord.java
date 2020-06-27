package com.hezho.makework.records;

import com.inductiveautomation.ignition.gateway.localdb.persistence.*;
import simpleorm.dataset.SFieldFlags;

/**
 * Filename: HCSettingsRecord
 * Author: Perry Arellano-Jones
 * Created on: 6/8/15
 * Project: home-connect-example
 */
public class HCSettingsRecord extends PersistentRecord {

    public static final RecordMeta<HCSettingsRecord> META = new RecordMeta<HCSettingsRecord>(
            HCSettingsRecord.class, "HCSettingsRecord");

    public static final IdentityField Id = new IdentityField(META);  // 这个应该是 primary key

    //Hub Settings -- for our imaginary IoT controller device
    public static final StringField HCHubName = new StringField(META, "HomeConnectHubName", SFieldFlags.SMANDATORY);
    public static final BooleanField BroadcastSSID = new BooleanField(META, "BroadcastSSID").setDefault(false);
    public static final IntField HCDeviceCount = new IntField(META, "DeviceCount", SFieldFlags.SMANDATORY);
    public static final StringField HCIPAddress = new StringField(META, "IPAddress", SFieldFlags.SMANDATORY);
    public static final BooleanField AllowInterop = new BooleanField(META, "AllowInterop").setDefault(true);
    public static final IntField HCPowerOutput = new IntField(META, "PowerOutput", SFieldFlags.SMANDATORY).setDefault(23);
    public static final StringField IamNew = new StringField(META, "IamNew").setDefault("HELLO THERE, I AM NEW!");
    // create categories for our record entries, getting titles from the HCSettingsRecord.properties, and
    // ordering through integer ranking
    static final Category HubConfiguration = new Category("HCSettingsRecord.Category.Configuration", 1000).include(HCHubName, HCIPAddress, IamNew);
    static final Category Security = new Category("HCSettingsRecord.Category.Security", 1001).include(BroadcastSSID, HCDeviceCount, AllowInterop);
    static final Category Power = new Category("HCSettingsRecord.Category.Power", 1001).include(HCPowerOutput);



    // accessors for our record entries
    public HCSettingsRecord setAllowInterop(Boolean allow) {
        setBoolean(AllowInterop, allow);
        return this;
    }
    public boolean getAllowInterop() {
        return getBoolean(AllowInterop);
    }

    public void setId(Long id) {
        setLong(Id, id);
    }

    public Long getId() {
        return getLong(Id);
    }

    public HCSettingsRecord setBroadcastSSID(Boolean broadcast){
        setBoolean(BroadcastSSID, broadcast);
        return this;
    }

    public boolean getBroadcastSSID() {
        return getBoolean(BroadcastSSID);
    }

    public HCSettingsRecord setHCDeviceCount(Integer count) {
        setInt(HCDeviceCount, count);
        return this;
    }
    public Integer getHCDeviceCount() {
        return getInt(HCDeviceCount);
    }

    public HCSettingsRecord setHCHubName(String name) {
        setString(HCHubName, name);
        return this;
    }

    public String getHCHubName() {
        return getString(HCHubName);
    }

    public HCSettingsRecord setHCIPAddress(String ip){
        setString(HCIPAddress, ip);
        return this;
    }

    public String getHCIPAddress() {
        return getString(HCIPAddress);
    }

    public Integer getHCPowerOutput() {
        return getInt(HCPowerOutput);
    }

    public HCSettingsRecord setHCPowerOutput(Integer power){
        setInt(HCPowerOutput, power);
        return this;
    }

    @Override
    public RecordMeta<?> getMeta() {
        return META;
    }
}

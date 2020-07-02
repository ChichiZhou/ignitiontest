package com.hezho.makework.web;

import com.hezho.makework.GatewayHook;
import com.hezho.makework.records.HCSettingsRecord;
import com.inductiveautomation.ignition.gateway.model.IgnitionWebApp;
import com.inductiveautomation.ignition.gateway.web.components.RecordEditForm;
import com.inductiveautomation.ignition.gateway.web.models.LenientResourceModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Application;

/**
 * Filename: HCSettingsPage
 * Author: Perry Arellano-Jones
 * Created on: 5/21/15
 * Project: home-connect-example
 *
 * HCSettings extends  {@link RecordEditForm} to provide a page where we can edit records in our PersistentRecord.
 */


/**
 * A Wicket panel that provides web-form editing capability for persistent record objects.
 * Can edit more than one record at once, flattening them and combining their field categories to make them look like a single object.
 * In this way extension object parent records and their type-specific properties records can be edited as a whole.
 */

public class HCSettingsPage extends RecordEditForm {
    public static final Pair<String, String> MENU_LOCATION =
        Pair.of(GatewayHook.CONFIG_CATEGORY.getName(), "homeconnect");

    public HCSettingsPage(final IConfigPage configPage) {
        super(configPage, null, new LenientResourceModel("HomeConnect.nav.settings.panelTitle"),
            ((IgnitionWebApp) Application.get()).getContext().getPersistenceInterface().find(HCSettingsRecord.META, 0L)
        );
    }

    @Override
    public Pair<String, String> getMenuLocation() {
        return MENU_LOCATION;
    }

}

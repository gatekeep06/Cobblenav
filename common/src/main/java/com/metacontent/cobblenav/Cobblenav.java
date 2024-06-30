package com.metacontent.cobblenav;

import com.metacontent.cobblenav.item.CobblenavItems;
import com.metacontent.cobblenav.permission.PermissionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblenav {
	public static final String ID = "cobblenav";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final String CONFIG_PATH = "/" + ID + "/cobblenav-config.json";
    public static final String CLIENT_CONFIG_PATH = "config/" + ID + "/client-cobblenav-config.json";
	public static final PermissionHandler PERMISSIONS = new PermissionHandler();

    public static void init() {
		CobblenavItems.registerCobblenavItems();
	}
}

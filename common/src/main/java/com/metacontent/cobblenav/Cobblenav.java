package com.metacontent.cobblenav;

import com.metacontent.cobblenav.item.CobblenavItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cobblenav {
	public static final String ID = "cobblenav";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final String CONFIG_PATH = "/" + ID + "/cobblenav-config.json";

	public static void init() {
		CobblenavItems.registerCobblenavItems();
	}
}

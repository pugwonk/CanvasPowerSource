package com.betaminus.phonepower;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import java.util.Arrays;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

import com.betaminus.phonepower.R;
import com.pennas.pebblecanvas.plugin.PebbleCanvasPlugin;

public class PowerSourcePlugin extends PebbleCanvasPlugin {
	public static final String LOG_TAG = "CANV_PLUGGEDIN";
	
	public static final int PLUGGEDIN_ID = 1; // Needs to be unique only within this plugin package
	
	private static final String[] MASKS = { "%T", "%P" };
	private static final int MASK_TIME = 0;
	private static final int MASK_POWERSTATE = 1;
	
	private static class PowerStateChanged {
		Date time;
		String state;
	}
	private static PowerStateChanged current_state = new PowerStateChanged();
	
	// send plugin metadata to Canvas when requested
	@Override
	protected ArrayList<PluginDefinition> get_plugin_definitions(Context context) {
		Log.i(LOG_TAG, "get_plugin_definitions");
		
		// create a list of plugins provided by this app
		ArrayList<PluginDefinition> plugins = new ArrayList<PluginDefinition>();
		
		// now playing (text)
		TextPluginDefinition tplug = new TextPluginDefinition();
		tplug.id = PLUGGEDIN_ID;
		tplug.name = context.getString(R.string.plugin_name);
		tplug.format_mask_descriptions = new ArrayList<String>(Arrays.asList(context.getResources().getStringArray(R.array.format_mask_descs)));
		// populate example content for each field (optional) to be display in the format mask editor
		ArrayList<String> examples = new ArrayList<String>();
		examples.add("10:35");
		examples.add("Plugged in");
		tplug.format_mask_examples = examples;
		tplug.format_masks = new ArrayList<String>(Arrays.asList(MASKS));
		tplug.default_format_string = "%T: %P";
		plugins.add(tplug);
		
		return plugins;
	}
	
	// send current text values to canvas when requested
	@Override
	protected String get_format_mask_value(int def_id, String format_mask, Context context, String param) {
		Log.i(LOG_TAG, "get_format_mask_value def_id = " + def_id + " format_mask = '" + format_mask + "'");
		if (def_id == PLUGGEDIN_ID) {
			// which field to return current value for?
			if (format_mask.equals(MASKS[MASK_TIME])) {
				return new SimpleDateFormat("H:mm:ss").format(Calendar.getInstance().getTime());
			} else if (format_mask.equals(MASKS[MASK_POWERSTATE])) {
				return current_state.state;
			}
		}
		Log.i(LOG_TAG, "no matching mask found");
		return null;
	}
	
	// send bitmap value to canvas when requested
	@Override
	protected Bitmap get_bitmap_value(int def_id, Context context, String param) {
		return null;
	}

	
	public static void stateChanged(String action, Context context) {
        
        if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
            // Do something when power connected
        	current_state.state = "Plugged in";
        }
        else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
            // Do something when power disconnected
        	current_state.state = "On battery";
        }
        
		notify_canvas_updates_available(PLUGGEDIN_ID, context);
	}

}

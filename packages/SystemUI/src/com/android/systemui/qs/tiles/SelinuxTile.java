/*
 * Copyright (C) 2015 CyanideL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.qs.tiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.android.systemui.utils.AbstractAsyncSuCMDProcessor;
import com.android.systemui.utils.CMDProcessor;
import android.graphics.Bitmap;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import cyanogenmod.hardware.CMHardwareManager;
import android.provider.MediaStore;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.preference.PreferenceScreen;
import android.preference.SeekBarPreference;
import android.database.ContentObserver;
import android.content.res.Configuration;
import com.android.systemui.utils.Helpers;
import com.android.systemui.cm.SystemSettingSwitchPreference;
import android.os.SELinux;
import android.util.Slog;
import android.net.Uri;
import com.android.systemui.R;
import com.android.systemui.qs.QSTile;
import android.os.UserHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SelinuxTile extends QSTile<QSTile.BooleanState> {
private boolean mListening;
private static final String SELINUX = "selinux";
public static final String SELINUX_STATUS = "selinux_status";
private Context mContext;
private SwitchPreference mSelinux;
private final Configuration mCurConfig = new Configuration();
private SelinuxObserver mObserver;


    public SelinuxTile(Host host) {
        super(host);
        mObserver = new SelinuxObserver(mHandler);
    }

    @Override
    protected BooleanState newTileState() {
        return new BooleanState();
    }

    
    public void handleClick() {
	Preference preference=mSelinux;
	 Object newValue=1;
	BooleanState state;
            if (preference == mSelinux) {
            if (newValue.toString().equals("true")) {
                CMDProcessor.runSuCommand("setenforce 1");
            } else if (newValue.toString().equals("false")) {
                CMDProcessor.runSuCommand("setenforce 0");
                
            }
         }
   }	

    @Override
    protected void handleSecondaryClick() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.settings",
            "com.android.settings.Settings$MiscSettings");
        mHost.startSettingsActivity(intent);
    }
    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.visible = true;
	state.value = isSELinuxEnforced();
	if (state.value){
            state.icon = ResourceIcon.get(R.drawable.ic_qs_selinux_enforcing);
	    state.label = mContext.getString(R.string.selinux_enforcing_title);	
        } else {
            state.icon = ResourceIcon.get(R.drawable.ic_qs_selinux_permissive);
	    state.label = mContext.getString(R.string.selinux_permissive_title);	
        		}
		}		
    @Override
    public void setListening(boolean listening) {
        if (listening)
        if (mListening == listening) return;
        mListening = listening;
    }

    	
    private class SelinuxObserver extends ContentObserver {
        public SelinuxObserver(Handler handler) {
            super(handler);
        }
    } 	
          public boolean isSELinuxEnforced()
	{  
		if(CMDProcessor.runShellCommand("getenforce").getStdout().contains("Enforcing"))
		{		
		return true;
		}
		else
		return false;
	}	
					
	
}


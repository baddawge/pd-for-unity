package com.bronsonzgeb.android.unity;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;

public class Wrapper {
	private Activity activity;
	
	public Wrapper( Activity currentActivity ) {
		activity = currentActivity;
	}
	
	public int openFile( String filename ) {
		Resources res = activity.getResources();
		File patchFile = null;
		int handle = 0;
		try {
			String baseFilename = filename.split("\\.(?=[^\\.]+$)")[0];
			int resourceId = res.getIdentifier( baseFilename, "raw", activity.getPackageName());
			InputStream in = res.openRawResource(resourceId);
			patchFile = IoUtils.extractResource(in, filename, activity.getCacheDir());
			handle = PdBase.openPatch(patchFile);
		} catch (IOException e) {
			Log.e("Unity", e.toString());
		} finally {
			if (patchFile != null) patchFile.delete();
		}
		
		return handle;
	}
	
	public void closeFile( int handle ) {
		PdBase.closePatch( handle );
	}
	
	public void sendFloat( float f, String receiver ) {
		PdBase.sendFloat( receiver, f );
	}
	
	public void sendBang( String receiver ) {
		PdBase.sendBang( receiver );
	}
	
	public void sendSymbol( String symbol, String receiver ) {
		PdBase.sendSymbol( receiver, symbol );
	}
	
	public void subscribe( String symbol, String unityObject, String unityMethod ) {
		
	}
	
	public void unsubscribe( String symbol ) {
		
	}
	
	public void startAudio() {
		try {
			int srate = AudioParameters.suggestSampleRate();
//			int nic = AudioParameters.suggestInputChannels();
			int noc = AudioParameters.suggestOutputChannels();
//			float millis = 50.0f;
//			int tpb = (int) (0.001f * millis * srate / PdBase.blockSize()) + 1;
			
			PdAudio.initAudio(srate, 0, noc, 1, true);
			PdAudio.startAudio(activity);
		} catch (IOException e) {
			Log.e("Unity", e.toString());
		}
	}
	
	public void pauseAudio() {
		PdBase.pauseAudio();
	}
	
	public void stopAudio() {
		PdAudio.stopAudio();
	}
	
	public void initPd() {
		AudioParameters.init(activity);
		Log.d("Unity", "Init Pd");
	}
}

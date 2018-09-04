/* Copyright 2012 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the �Sample code usage restrictions� document for further information.
 *
 */

package com.ecity.android.map.layer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.ecity.android.map.core.tile.TileFutureTask;
import com.ecity.android.map.layer.ECityCacheableTiledServiceLayer;
import com.ecity.android.map.layer.ECityCacheableTiledServiceLayerInfo;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.arcgis.android.samples.helloworld.R;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The HelloWorld app is the most basic Map app for the ArcGIS Runtime SDK for Android. It shows how to create a {@link #mMapView} object
 * and populate it with a tileLayer and show the layer on the map.
 *
 * @author EsriAndroidTeam
 * @version 2.0
 */

public class HelloWorld extends Activity {

	protected final ConcurrentHashMap<String, TileFutureTask> tasks    = new ConcurrentHashMap<String, TileFutureTask>();
	private         MapView                                   mMapView = null;
	private String                          cachePath;
	private ECityCacheableTiledServiceLayer ecityCacheableTileLayer;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		String sdPath = Environment.getExternalStorageDirectory().getPath();
		cachePath = sdPath + "//ECity//map//";
		// Retrieve the map and initial extent from XML layout
		mMapView = (MapView) findViewById(R.id.map);
		/* create a @ArcGISTiledMapServiceLayer */
		ecityCacheableTileLayer = new ECityCacheableTiledServiceLayer(cachePath,
																	  "http://221.6.214.119:8010/ServiceEngine/ecity/rest/services/appmap1/MapServer",
																	  "test", true);
		// Add tiled layer to MapView
		mMapView.addLayer(ecityCacheableTileLayer);
		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object arg0, STATUS arg1) {
				// TODO Auto-generated method stub
				if (arg1 == STATUS.LAYER_LOADED) {
					try {
						new ECityCacheableTiledServiceLayerInfo(cachePath,
																"http://221.6.214.119:8010/ServiceEngine/ecity/rest/services/appmap/MapServer",
																ecityCacheableTileLayer.getName());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		});

	}


	@Override
	protected void onPause() {
		super.onPause();
		mMapView.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.unpause();
	}

}
/**
 * Copyright(c) 2013 DRAWNZER.ORG PROJECTS -> ANURAG
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
 *                             
 *                             anurag.dev1512@gmail.com
 *
 */


package org.anurag.file.quest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class MediaElementAdapter extends ArrayAdapter<File>{

	public static ArrayList<File> MULTI_FILES;
	public static boolean MULTI_SELECT;
	public static boolean[] thumbselection;
	static Context context;
	private ArrayList<File> list;
	private static PackageManager manager;
	public static long C;
	static LayoutInflater inflater;
	Image img;
	public MediaElementAdapter(Context con, int textViewResourceId, ArrayList<File> objects) {
		super(con , textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		list=objects;
		C = 0;
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		MULTI_FILES = new ArrayList<File>();
		MULTI_SELECT = false;
		thumbselection = new boolean[list.size()];
		context = con;
		manager = con.getPackageManager();
	}
	
	
	/**
	 * 
	 * @author Anurag
	 *
	 */
	static class Holder{
		ImageView icon;
		TextView fName;
		TextView fType;
		TextView fSize;
		CheckBox box;
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		File f = list.get(pos);
		Holder h = new Holder();
		if(convertView == null){
			h = new Holder();
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row_list_1, arg2 , false);
			h.icon = (ImageView)convertView.findViewById(R.id.fileIcon);
			h.fName = (TextView)convertView.findViewById(R.id.fileName);
			h.fType = (TextView)convertView.findViewById(R.id.fileType);
			h.fSize = (TextView)convertView.findViewById(R.id.fileSize);
			h.box = (CheckBox)convertView.findViewById(R.id.checkbox);
			convertView.setTag(h);
		}
		
		else
			h = (Holder)convertView.getTag();
		MULTI_FILES.add(null);
		if(MULTI_SELECT){
			h.box.setVisibility(View.VISIBLE);
			h.box.setId(pos);
			h.box.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox ch = (CheckBox) v;
					int id = ch.getId();
					if(thumbselection[id]){
						ch.setChecked(false);
						thumbselection[id] = false;
						MULTI_FILES.remove(id);
						MULTI_FILES.add(id, null);
						C--;
					}else{
						C++;
						ch.setChecked(true);
						thumbselection[id] = true;
						MULTI_FILES.remove(id);
						MULTI_FILES.add(id, list.get(id));
					}
				}
			});
			h.box.setChecked(thumbselection[pos]);
		}else
			h.box.setVisibility(View.GONE);
		
		
		
		h.fName.setText(f.getName());
		
		String Ext = getExt(f);
		
		if(Ext.equalsIgnoreCase(".apk")){
			h.fType.setText(context.getString(R.string.application));
			h.fSize.setText(size(f));
			new ApkImage(h.icon).execute(f.getPath());
		}
		else if(Ext.equalsIgnoreCase(".png")||
				Ext.equalsIgnoreCase(".jpg")||
				Ext.equalsIgnoreCase(".jpeg")){
			h.fSize.setText(size(f));
			h.fType.setText(context.getString(R.string.image));
			new Image(h.icon).execute(f.getPath());
		}else{
			if(f.isFile()){
				h.fSize.setText(size(f));
				if(Ext.equalsIgnoreCase(".mp3")||
						Ext.equalsIgnoreCase(".ogg")||
						Ext.equalsIgnoreCase(".amr")||
						Ext.equalsIgnoreCase(".acc")||
						Ext.equalsIgnoreCase(".m4a")||
						Ext.equalsIgnoreCase(".wav")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_music));
					h.fType.setText(context.getString(R.string.music));
				}else if(Ext.equalsIgnoreCase(".zip")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_zip_it));
					h.fType.setText("Zip");
				}else if(Ext.equalsIgnoreCase(".mp4")||
						Ext.equalsIgnoreCase(".3gp")||
						Ext.equalsIgnoreCase(".flv")||
						Ext.equalsIgnoreCase(".avi")||
						Ext.equalsIgnoreCase(".mkv")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_video));
					h.fType.setText(context.getString(R.string.vids));
				}else if(Ext.equalsIgnoreCase(".rar")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_rar));
					h.fType.setText(context.getString(R.string.compr));
				}else if(Ext.equalsIgnoreCase(".htm")||
						Ext.equalsIgnoreCase(".html")||
						Ext.equalsIgnoreCase(".mhtml")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_web_pages));
					h.fType.setText(context.getString(R.string.web));
				}else if(Ext.equalsIgnoreCase(".pdf")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_adobe));
					h.fType.setText(context.getString(R.string.pdf));
				}else if(Ext.equalsIgnoreCase(".doc")||
						Ext.equalsIgnoreCase(".docx")||
						Ext.equalsIgnoreCase(".ppt")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_ppt));
					h.fType.setText(context.getString(R.string.document));
				}else if(Ext.equalsIgnoreCase(".txt")||Ext.equalsIgnoreCase(".log")||Ext.equalsIgnoreCase(".ini")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_text));
					h.fType.setText(context.getString(R.string.text));
				}else if(Ext.equalsIgnoreCase(".tar")||Ext.equalsIgnoreCase(".TAR")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_tar));
					h.fType.setText(context.getString(R.string.compr));
				}else if(Ext.equalsIgnoreCase(".7z")||Ext.equalsIgnoreCase(".7Z")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_7zip));
					h.fType.setText(context.getString(R.string.compr));
				}else if(Ext.equalsIgnoreCase(".gz")||Ext.equalsIgnoreCase(".GZ")){
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_gzip));
					h.fType.setText(context.getString(R.string.compr));
				}else {
					h.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_unknown));
					h.fType.setText(context.getString(R.string.unknown));
				}				
			}
		}		
		return convertView;
	}
	
	/**
	 * THIS FUNCTION THE EXTENSION OF THE GIVEN FILE
	 * @param f
	 * @return THE EXTENSION OF A FILE
	 */
	public static String getExt(File f){
		String name = f.getName();
		try{
			return name.substring(name.lastIndexOf("."), name.length());
		}catch(IndexOutOfBoundsException e){
			return "";
		}
	}
	
	/**
	 * THIS FUNCTION RETURN THE SIZE IF THE GIVEN FIZE IN PARAMETER
	 * @param f
	 * @return
	 */
	public static String size(File f){
		long size = f.length();
		if(size>Constants.GB)
			return String.format(context.getString(R.string.sizegb), (double)size/(Constants.GB));
		
		else if(size > Constants.MB)
			return String.format(context.getString(R.string.sizemb), (double)size/(Constants.MB));
		
		else if(size>1024)
			return String.format(context.getString(R.string.sizekb), (double)size/(1024));
		
		else
			return String.format(context.getString(R.string.sizebytes), (double)size);
	}
	
	/**
	 * THIS FUNCTION RETURNS THE ICON OF THE APP IF GIVEN FILE IS AN APP 
	 * @param f
	 * @return
	 */
	private static Drawable getApkIcon(String f){
		Drawable draw;
		PackageInfo info;
		try{
			info = manager.getPackageArchiveInfo(f, 0);
			info.applicationInfo.publicSourceDir = f;
			draw = info.applicationInfo.loadIcon(manager);
			return draw;
		}catch(Exception e){
			return context.getResources().getDrawable(R.drawable.ic_launcher_apk);
		}
	}
	
	public static class ApkImage extends AsyncTask<String , Void, Void>{

		Drawable draw;
		ImageView icon;
		public ApkImage(ImageView iV ){
			icon = iV;
			
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_apk));
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(String... path) {
			// TODO Auto-generated method stub
			draw = getApkIcon(path[0]);
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			icon.setImageDrawable(draw);
			super.onPostExecute(result);
		}
		
	}

	private static class Image extends AsyncTask<String, Void, Void>{

		Bitmap draw;
		ImageView view; 
		public Image(ImageView v){
			view = v;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(draw !=null)
				view.setImageBitmap(draw);
			else if(draw == null)
				view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_images ));
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			view.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_images));
		}

		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			draw = getPreview(arg0[0]);
			return null;
		}
		
	}
	
	private static  Bitmap getPreview(String url) {
        File image = new File(url);
        int size =72;
        
        InputStream photoStream = null;
		Bitmap mBitmap = null;
		try {
			photoStream = new FileInputStream(image);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			opts.inSampleSize = 1;

			mBitmap = BitmapFactory.decodeStream(photoStream, null, opts);
			if (opts.outWidth > opts.outHeight && opts.outWidth > size) {
				opts.inSampleSize = opts.outWidth / size;
			} else if (opts.outWidth < opts.outHeight && opts.outHeight > size) {
				opts.inSampleSize = opts.outHeight / size;
			}
			if (opts.inSampleSize < 1) {
				opts.inSampleSize = 1;
			}
			opts.inJustDecodeBounds = false;
			photoStream.close();
			photoStream = new FileInputStream(image);
			mBitmap = BitmapFactory.decodeStream(photoStream, null, opts);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (photoStream != null) {
				try {
					photoStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mBitmap;
      
    }
		
}

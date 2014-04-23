package com.example.filereconciler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class FileBrowseActivity extends ListActivity {

	private FileNode			currentDir;
	private FileNode			parentDirectory;
	private FileArrayAdapter	adapter;
	private View				selectedView;
	private FileNode			selectedRow;
	private FileNode			root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browse);

		
		/*
		 * if (this.isExternalStorageWritable()){ }
		 */

		File cur = Environment.getExternalStorageDirectory();
		FileNode n = new FileNode(cur);
		root = n;

		this.changeCurrentDirectory(n);

		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView < ? > arg0,
							View v, int row, long arg3) {
						
						selectRow(v, row);
						return true;
					}
				});

		Button button = (Button) findViewById(R.id.button11);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (selectedRow == null) {
					Toast.makeText(FileBrowseActivity.this,
							"Please select a file or folder",
							Toast.LENGTH_SHORT).show();
				}
				else {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("filename", selectedRow.getPath());
					setResult(RESULT_OK, returnIntent);
					finish();
				}


			}

		});

	}

	public void changeCurrentDirectory(FileNode fn) {//f is already checked for being a folder 

		if (fn.equals(root.getParent())) { return; }

		FileNode parent = fn.getParent();
		parent.setName("..");
		fn.setName(".");

		List < FileNode > fils = new LinkedList < FileNode >();
		fils.add(parent);
		fils.add(fn);
		for (FileNode f : fn.getContents()) {
			fils.add(f);
		}

		adapter = new FileArrayAdapter(FileBrowseActivity.this,
				R.layout.filenode_layout, fils);
		this.setListAdapter(adapter);
		currentDir = fn;
		this.selectedRow = null;


	}

	public void selectRow(View v, int pos) {
		if (selectedView != null) {
			selectedView.setBackgroundColor(Color.TRANSPARENT);
		}
		
		
		
		for (int i =0;i<this.getListView().getChildCount();i++){
			((View)this.getListView().getChildAt(i)).setBackgroundColor(Color.TRANSPARENT);
		}
		v.setBackgroundColor(Color.CYAN);
		
		selectedView = v;
		selectedRow = adapter.getItem(pos);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_browse, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) { return true; }
		return super.onOptionsItemSelected(item);
	}


	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) { return true; }
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) { return true; }
		return false;
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		FileNode fn = adapter.getItem(position);
		if (fn.isDir()) {
			this.changeCurrentDirectory(fn);
		}
		else {
			this.selectRow(v, position);
		}
	}


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_file_browse,
					container, false);
			return rootView;
		}
	}

}

package hu.akoss.git.mp3remotecontrol.client.ui.listing.adapter;

import hu.akoss.git.mp3control.ui.R;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.Folder;
import hu.akoss.git.mp3remotecontrol.client.ui.listing.wrapper.FolderWrapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FolderAdapter extends ArrayAdapter<Folder> {

	private Context _context;
	
	public FolderAdapter(Context context, int textViewResourceId, Folder [] folders_) {
		super(context, textViewResourceId, folders_);
		
		_context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row=convertView;
		FolderWrapper wrapper=null;
		
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater)_context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			
			row = inflater.inflate(R.layout.tpl_folder, null);
			wrapper = new FolderWrapper(row);
			row.setTag(wrapper);
		}
		else {
			wrapper = (FolderWrapper)row.getTag();
		}
		
		wrapper.populateFrom(getItem(position));
		
		return(row);
	}
}

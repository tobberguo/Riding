package com.tobber.riding.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.riding.R;
import com.tobber.riding.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainLeftFragment extends Fragment implements OnClickListener{
	
	private LayoutInflater mInflater;
	private SimpleAdapter simpleAdapter;
	private ListView list;
	private ImageView user_image;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_left_fragment, container,false);
		
		initView(view);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mInflater = LayoutInflater.from(getActivity());
	}

	private void initView(View view) {
		
		user_image = (ImageView) view.findViewById(R.id.picPerson);
		
		
		list = (ListView) view.findViewById(R.id.listAct);
		
		simpleAdapter = new SimpleAdapter(view.getContext(), initDate(), R.layout.left_frame_list_item,
				new String[]{"item_id","item_info"},
				new int[]{R.id.item_id,R.id.item_info});
		
		list.setAdapter(simpleAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String itemId = ((TextView)view.findViewById(R.id.item_id)).getText().toString().trim();
				if(itemId.equals("1")){
					Intent intent = new Intent();
	                intent.setClass(getActivity(), MainActivity.class);
	                getActivity().startActivity(intent);
				}
			}
		});
        
	}

	@Override 
	public void onClick(View v) {

	}
	
	private ArrayList<Map<String,String>> initDate(){
		ArrayList<Map<String,String>> data = new ArrayList<Map<String,String>>();
		
		Map<String, String> map1 =  new HashMap<String, String>();
		map1.put("item_id", 1+"");
		map1.put("item_info", "骑行");
		data.add(map1);
		
		Map<String, String> map2 =  new HashMap<String, String>();
		map2.put("item_id", 2+"");
		map2.put("item_info", "附近骑友");
		data.add(map2);
		
		Map<String, String> map3 =  new HashMap<String, String>();
		map3.put("item_id", 3+"");
		map3.put("item_info", "我");
		data.add(map3);
		
		Map<String, String> map4 =  new HashMap<String, String>();
		map4.put("item_id", 4+"");
		map4.put("item_info", "离线地图");
		data.add(map4);
		
		Map<String, String> map5 =  new HashMap<String, String>();
		map5.put("item_id", 4+"");
		map5.put("item_info", "关于骑行控");
		data.add(map5);
		
		return data;
	}

}

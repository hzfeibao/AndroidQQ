package com.example.android_qqfix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LoginActivity extends Activity{
	TextView textFetchPassWord = null, textRegister = null;
	Button loginButton = null;
	ImageButton listIndicatorButton = null, deleteButtonOfEdit=null;
	ImageView currentUserImage = null;
	ListView loginList = null;
	EditText qqEdit = null, passwordEdit = null;
	private static boolean isVisible = false; //listview
	private static boolean isIndicatorUp = false; //indicator
	
	public static int currentSelectedPosition = -1;
	
	String[] from = {"userPhoto","userQQ","deletButton"};
	int[] to = {R.id.login_userPhoto,R.id.login_userQQ,R.id.login_deleteButton};
	List<Map> list = null;
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		textFetchPassWord = (TextView)findViewById(R.id.fetchPassword);
		textRegister = (TextView)findViewById(R.id.registQQ);
		loginButton = (Button)findViewById(R.id.qqLoginButton);
		listIndicatorButton = (ImageButton)findViewById(R.id.qqListIndicator);
		deleteButtonOfEdit = (ImageButton)findViewById(R.id.delete_button_edit);
		currentUserImage = (ImageView)findViewById(R.id.myImage);
		loginList = (ListView)findViewById(R.id.loginQQList);
		qqEdit = (EditText)findViewById(R.id.qqNum);
		passwordEdit = (EditText)findViewById(R.id.qqPassword);
		deleteButtonOfEdit = (ImageButton)findViewById(R.id.delete_button_edit);
		
		qqEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(qqEdit.getText().toString().equals("") == false){
					deleteButtonOfEdit.setVisibility(View.VISIBLE);
				}
			}
			
		});
		
		deleteButtonOfEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				currentUserImage.setImageResource(R.drawable.qqmain);
				qqEdit.setText("");
				passwordEdit.setText("");
				currentSelectedPosition = -1;
				deleteButtonOfEdit.setVisibility(View.GONE);
			}
			
		});
		
		LoginUserInfo user1 = new LoginUserInfo(R.drawable.contact_0, "1234567", R.drawable.deletebutton);
		LoginUserInfo user2 = new LoginUserInfo(R.drawable.contact_1, "3456789", R.drawable.deletebutton);
		addUser(user1);
		addUser(user2);
		
		if(currentSelectedPosition == -1){
			currentUserImage.setImageResource(R.drawable.qqmain);
			qqEdit.setText("");
		}else{
			currentUserImage.setImageResource((Integer)list.get(currentSelectedPosition).get(from[0]));
			qqEdit.setText((String)list.get(currentSelectedPosition).get(from[1]));
			
		}
		MyLoginListAdapter adapter = new MyLoginListAdapter(this, list, R.layout.login_listitem, from, to);
		loginList.setAdapter(adapter);
		loginList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				currentUserImage.setImageResource((Integer)list.get(position).get(from[0]));
				qqEdit.setText((String)list.get(position).get(from[1]));
				currentSelectedPosition = position;
				
				loginList.setVisibility(View.GONE);
				listIndicatorButton.setBackgroundResource(R.drawable.indicator_down);
				
			}
			
		});
	}
	
	private void addUser(LoginUserInfo user){
		Map map = new HashMap();
		map.put(from[0], user.userPhoto);
		map.put(from[1], user.userQQ);
		map.put(from[2], user.deleteButtonRes);
		list.add(map);
	}
	
	public class MyLoginListAdapter extends BaseAdapter{
		protected Context context;
		protected List<Map> list;
		protected int itemLayout;
		protected String[] from;
		protected int[] to;
		
		/**
		 * @param context
		 * @param list
		 * @param itemLayout
		 * @param from
		 * @param to
		 */
		public MyLoginListAdapter(Context context, List<Map> list,
				int itemLayout, String[] from, int[] to) {
			super();
			this.context = context;
			this.list = list;
			this.itemLayout = itemLayout;
			this.from = from;
			this.to = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		class ViewHolder{
            public ImageView userPhoto;
            public TextView userQQ;
            public ImageButton deleteButton;
        }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			/* currentPosition=position;    
            不能使用currentPosition，因为每绘制完一个Item就会更新currentPosition
            这样得到的currentPosition将始终是最后一个Item的position        
            */
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(itemLayout, null);
				holder.userPhoto = (ImageView)convertView.findViewById(to[0]);
				holder.userQQ = (TextView)convertView.findViewById(to[1]);
				holder.deleteButton = (ImageButton)convertView.findViewById(to[2]);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.userPhoto.setBackgroundResource((Integer)list.get(position).get(from[0]));
			holder.userQQ.setText((String)list.get(position).get(from[1]));
			holder.deleteButton.setBackgroundResource((Integer)list.get(position).get(from[2]));
			holder.deleteButton.setOnClickListener(new ListOnClickListener(position));
			return convertView;
		}
		
		class ListOnClickListener implements OnClickListener{
			private int position;
			
			public ListOnClickListener(int position){
				super();
				this.position = position;
			}
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				list.remove(position);
				if(position == currentSelectedPosition){
					currentUserImage.setImageResource(R.drawable.qqmain);
					qqEdit.setText("");
					currentSelectedPosition = -1;
				}else if(position < currentSelectedPosition){
					currentSelectedPosition--;
				}
				listIndicatorButton.setBackgroundResource(R.drawable.indicator_down);
				loginList.setVisibility(View.GONE);
				MyLoginListAdapter.this.notifyDataSetChanged();
			}
			
		}
	}
}

package com.example.android_qqfix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ChatActivity extends Activity{
	int[] faceId={R.drawable.f_static_000,R.drawable.f_static_001,R.drawable.f_static_002,R.drawable.f_static_003
            ,R.drawable.f_static_004,R.drawable.f_static_005,R.drawable.f_static_006,R.drawable.f_static_009,R.drawable.f_static_010,R.drawable.f_static_011
            ,R.drawable.f_static_012,R.drawable.f_static_013,R.drawable.f_static_014,R.drawable.f_static_015,R.drawable.f_static_017,R.drawable.f_static_018};
    String[] faceName={"\\呲牙","\\淘气","\\流汗","\\偷笑","\\再见","\\敲打","\\擦汗","\\流泪","\\掉泪","\\小声","\\炫酷","\\发狂"
             ,"\\委屈","\\便便","\\菜刀","\\微笑","\\色色","\\害羞"};
    
    HashMap<String, Integer> faceMap = null;
    ArrayList<HashMap<String, Object>> chatList = null;
    String[] from={"image","text"};
    int[] to={R.id.chatlist_image_me,R.id.chatlist_text_me,R.id.chatlist_image_other,R.id.chatlist_text_other};
    int[] layout={R.layout.chat_listitem_me,R.layout.chat_listitem_other};
    String userQQ=null;
    
    public final static int OTHER=1;
    public final static int ME=0;
    
    ArrayList<ImageView> pointList=null;
    ArrayList<ArrayList<HashMap<String,Object>>> listGrid=null;
    protected ListView chatListView=null;
    protected Button chatSendButton=null;
    protected EditText editText=null;
    protected ViewFlipper viewFlipper=null;
    protected ImageButton chatBottomLook=null;
    protected RelativeLayout faceLayout=null;
    protected LinearLayout pagePoint=null,fillGapLinear=null;
   
    private boolean expanded=false;
    
    protected MyChatAdapter adapter = null;
    
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.chat_main);
    	
    	faceMap=new HashMap<String,Integer>();    
        chatList=new ArrayList<HashMap<String,Object>>();
        listGrid=new ArrayList<ArrayList<HashMap<String,Object>>>();
        pointList=new ArrayList<ImageView>();
        
        addTextToList("不管你是谁", ME);
        addTextToList("群发的我不回\n  ^_^", OTHER);
        addTextToList("哈哈哈哈", ME);
        addTextToList("新年快乐！", OTHER);
        
        chatSendButton=(Button)findViewById(R.id.chat_bottom_sendbutton);
        editText=(EditText)findViewById(R.id.chat_bottom_edittext);
        chatListView=(ListView)findViewById(R.id.chat_list);
        viewFlipper=(ViewFlipper)findViewById(R.id.faceFlipper);
        chatBottomLook=(ImageButton)findViewById(R.id.chat_bottom_look);
        faceLayout=(RelativeLayout)findViewById(R.id.faceLayout);
        pagePoint=(LinearLayout)findViewById(R.id.pagePoint);
        fillGapLinear=(LinearLayout)findViewById(R.id.fill_the_gap);
        
        chatBottomLook.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(expanded){
					setFaceLayoutExpandState(false);
					expanded = false;
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}else{
					setFaceLayoutExpandState(true);
					expanded = true;
					setPointEffect(0);
				}
			}
        });
        
        editText.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(expanded){
					setFaceLayoutExpandState(false);
					expanded = false;
				}
				return false;
			}
        });
        adapter=new MyChatAdapter(this,chatList,layout,from,to);
        chatSendButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String myWord = null;
				myWord = (editText.getText() + "").toString();
				if(myWord.length() == 0)
					return;
				editText.setText("");
				addTextToList(myWord, ME);
				adapter.notifyDataSetChanged();
				chatListView.setSelection(chatList.size() - 1);
			}
        });
        
        chatListView.setAdapter(adapter);
        
        chatListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setFaceLayoutExpandState(false);
				((InputMethodManager)ChatActivity.this.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				expanded = false;
			}
        });
        
        for(int i = 0; i < faceId.length; i++){
        	faceMap.put(faceName[i], faceId[i]);
        }
        
        addFaceData();
        addGridView();
    }
    
    private void addFaceData(){
    	ArrayList<HashMap<String, Object>> list = null;
    	for(int i = 0; i < faceId.length; i++){
    		if(i%14 == 0){
    			list = new ArrayList<HashMap<String, Object>>();
    			listGrid.add(list);
    		}
    		HashMap<String, Object> map = new HashMap<String, Object>();
    		map.put("image", faceId[i]);
    		map.put("faceName", faceName[i]);
    		listGrid.get(i/14).add(map);
    	}
    }
    
    private void addGridView(){
    	for(int i = 0; i < listGrid.size(); i++){
    		View view = LayoutInflater.from(this).inflate(R.layout.layout_view_item, null);
    		GridView gv = (GridView)view.findViewById(R.id.myGridView);
    		gv.setNumColumns(5);
    		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
    		MyGridAdapter adapter = new MyGridAdapter(this, listGrid.get(i), R.layout.chat_grid_item, new String[]{"image"}, new int[]{R.id.gridImage});
    		gv.setAdapter(adapter);
    		gv.setOnTouchListener(new MyTouchListener(viewFlipper));
    		viewFlipper.addView(view);
    		
    		View pointView = LayoutInflater.from(this).inflate(R.layout.point_image_layout, null);
    		ImageView image = (ImageView)pointView.findViewById(R.id.pointImageView);
    		image.setBackgroundResource(R.drawable.qian_point);
    		pagePoint.addView(pointView);
    		
    		pointList.add(image);
    	}
    }
    
    private void setSoftInputState(){
    	((InputMethodManager)ChatActivity.this.getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    private void setFaceLayoutExpandState(boolean isexpand){
    	if(isexpand == false){
    		viewFlipper.setDisplayedChild(0);
    		ViewGroup.LayoutParams params = faceLayout.getLayoutParams();
    		params.height = 1;
    		faceLayout.setLayoutParams(params);
    		chatBottomLook.setBackgroundResource(R.drawable.chat_bottom_look);
    	}else{
    		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ChatActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    		ViewGroup.LayoutParams params = faceLayout.getLayoutParams();
    		params.height = 150;
    		faceLayout.setLayoutParams(params);
    		chatBottomLook.setBackgroundResource(R.drawable.chat_bottom_keyboard);
    	}
    }
    
    private void setPointEffect(int darkPointNum){
    	for(int i = 0; i < pointList.size(); i++){
    		pointList.get(i).setBackgroundResource(R.drawable.qian_point);
    	}
    	pointList.get(darkPointNum).setBackgroundResource(R.drawable.shen_point);
    }
    
    protected void addTextToList(String text, int who){
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("person",who );
        map.put("image", who==ME?R.drawable.contact_0:R.drawable.contact_1);
        map.put("text", text);
        chatList.add(map);
    }
    
    class MyGridAdapter extends BaseAdapter{
    	Context context=null;
        ArrayList<HashMap<String,Object>> list=null;
        int layout;
        String[] from;
        int[] to;
		/**
		 * @param context
		 * @param list
		 * @param layout
		 * @param from
		 * @param to
		 */
		public MyGridAdapter(Context context,
				ArrayList<HashMap<String, Object>> list, int layout,
				String[] from, int[] to) {
			super();
			this.context = context;
			this.list = list;
			this.layout = layout;
			this.from = from;
			this.to = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		class ViewHolder{
            ImageView image=null;
        }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(layout, null);
				holder = new ViewHolder();
				holder.image = (ImageView)convertView.findViewById(to[0]);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.image.setImageResource((Integer)list.get(position).get(from[0]));
			class MyGridImageClickListener implements OnClickListener{
				int position;
				
				/**
				 * @param position
				 */
				public MyGridImageClickListener(int position) {
					super();
					this.position = position;
				}

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					editText.append((String)list.get(position).get("faceName"));
				}
			}
			holder.image.setOnClickListener(new MyGridImageClickListener(position));
			return convertView;
		}
    }
    
    private boolean moveable = true;
    private float startX = 0;
    
    class MyTouchListener implements OnTouchListener{
    	ViewFlipper viewFlipper = null;
    	
		/**
		 * @param viewFlipper
		 */
		public MyTouchListener(ViewFlipper viewFlipper) {
			super();
			this.viewFlipper = viewFlipper;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				moveable = true;
				break;
			case MotionEvent.ACTION_MOVE:
				if(moveable){
					if(event.getX() - startX > 60){
						moveable = false;
						int childIndex = viewFlipper.getDisplayedChild();
						if(childIndex > 0){
							viewFlipper.setInAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.left_in));
							viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.right_out));
							viewFlipper.showPrevious();
							setPointEffect(childIndex -1);
						}
					}else if(event.getX() - startX < -60){
						moveable = false;
						int childIndex = viewFlipper.getDisplayedChild();
						if(childIndex > 0){
							viewFlipper.setInAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.right_in));
							viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(ChatActivity.this, R.anim.right_out));
							viewFlipper.showNext();
							setPointEffect(childIndex +1);
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				moveable = true;
				break;
			default:break;
			}
			return false;
		}
    	
    }
    
    
    private void setFaceText(TextView textView,String text){
        SpannableString spanStr=parseString(text);
        textView.setText(spanStr);
    }
    
    private void setFace(SpannableStringBuilder spb, String faceName){
    	Integer faceId = faceMap.get(faceName);
    	if(faceId != null){
    		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), faceId);
    		bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
    		ImageSpan imageSpan = new ImageSpan(this, bitmap);
    		SpannableString spanStr = new SpannableString(faceName);
    		spanStr.setSpan(imageSpan, 0, faceName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    		
    		spb.append(spanStr);
    	}else{
    		spb.append(faceName);
    	}
    }
    
    private SpannableString parseString(String inputStr){
        SpannableStringBuilder spb=new SpannableStringBuilder();
        Pattern mPattern= Pattern.compile("\\\\..");
        Matcher mMatcher=mPattern.matcher(inputStr);
        String tempStr=inputStr;
        
        while(mMatcher.find()){
            int start=mMatcher.start();
            int end=mMatcher.end();
            spb.append(tempStr.substring(0,start));
            String faceName=mMatcher.group();
            setFace(spb, faceName);
            tempStr=tempStr.substring(end, tempStr.length());
            /**
             * 更新查找的字符串
             */
            mMatcher.reset(tempStr);
        }
        spb.append(tempStr);
        return new SpannableString(spb);
    }
    
    private class MyChatAdapter extends BaseAdapter{
    	Context context=null;
        ArrayList<HashMap<String,Object>> chatList=null;
        int[] layout;
        String[] from;
        int[] to;
        
		/**
		 * @param context
		 * @param chatList
		 * @param layout
		 * @param from
		 * @param to
		 */
		public MyChatAdapter(Context context,
				ArrayList<HashMap<String, Object>> chatList, int[] layout,
				String[] from, int[] to) {
			super();
			this.context = context;
			this.chatList = chatList;
			this.layout = layout;
			this.from = from;
			this.to = to;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chatList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		class ViewHolder{
            public ImageView imageView=null;
            public TextView textView=null;
        }
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			int who=(Integer)chatList.get(position).get("person");
			convertView = LayoutInflater.from(context).inflate(layout[who==ME?0:1], null);
			holder = new ViewHolder();
			holder.imageView = (ImageView)convertView.findViewById(to[who*2+0]);
			holder.textView = (TextView)convertView.findViewById(to[who*2+1]);
			holder.imageView.setBackgroundResource((Integer)chatList.get(position).get(from[0]));
			setFaceText(holder.textView, chatList.get(position).get(from[1]).toString());
			return convertView;
		}
    	
    }
    
}

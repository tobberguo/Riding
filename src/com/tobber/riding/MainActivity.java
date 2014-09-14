package com.tobber.riding;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.riding.R;
import com.tobber.riding.fragment.MainLeftFragment;
import com.tobber.riding.slidingfragment.BaseSlidingFragmentActivity;
import com.tobber.riding.slidingfragment.SlidingMenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseSlidingFragmentActivity implements OnClickListener {
	private BMapManager mapManager;    
    private MapView mapView;    
    private MapController mapController; 
    private ImageButton titleBtLeft;
    private ImageButton bottomCommBtn; //骑行按钮
    private ImageButton bottomTitleBtLeft; //定位按钮
    protected SlidingMenu mSlidingMenu;
    private Toast mToast;
    private PopupWindow popupWindow;
    private PopupWindow buttomPopupWindow;
    
    /** 
     * 定位SDK的核心类 
     */  
    private LocationClient mLocClient; 
    /** 
     * 我的位置图层 
     */  
    private LocationOverlay myLocationOverlay = null;  
    /** 
     * 用户位置信息  
     */  
    private LocationData mLocData;  
    /** 
     * 弹出窗口图层 
     */  
    private PopupOverlay mPopupOverlay  = null;  
    private boolean isRequest = false;//是否手动触发请求定位  
    private boolean isFirstLoc = true;//是否首次定位  
    /** 
     * 弹出窗口图层的View 
     */  
    private View mPopupView;  
    private BDLocation location; 
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mapManager = new BMapManager(getApplication());
		mapManager.init("CVTDsshE0wyS56wGGvRHsFO3", null);
		initSlidingMenu();
		setContentView(R.layout.main);
		
		//super.initMapActivity(mapManager);    
        mapView = (MapView) findViewById(R.id.map_View); 
        titleBtLeft = (ImageButton) findViewById(R.id.titleBtLeft);
        bottomCommBtn = (ImageButton) findViewById(R.id.bottomCommBtn);
        bottomTitleBtLeft = (ImageButton) findViewById(R.id.bottomTitleBtLeft);
        
        titleBtLeft.setOnClickListener(this);
        bottomTitleBtLeft.setOnClickListener(this);
        bottomCommBtn.setOnClickListener(this);
        
        
        //取得地图控制器对象，用于控制MapView    
        mapController = mapView.getController(); 
        //设置地图是否响应点击事件  
        mapController.enableClick(true); 
        //设置地图默认的缩放级别    
        mapController.setZoom(14);
        //设置启用内置的缩放控件    
        mapView.setBuiltInZoomControls(false);  
        // 设置地图模式为交通地图    
        mapView.setTraffic(true);    
          
        mPopupView = LayoutInflater.from(this).inflate(R.layout.map_layout, null);  
        //实例化弹出窗口图层  
        mPopupOverlay = new PopupOverlay(mapView ,new PopupClickListener() {  
        	//点击弹出窗口图层回调的方法 
            @Override  
            public void onClickedPopup(int arg0) {  
                //隐藏弹出窗口图层  
                mPopupOverlay.hidePop();  
            }  
        });
        
        //实例化定位服务，LocationClient类必须在主线程中声明  
        mLocClient = new LocationClient(getApplicationContext());  
        mLocClient.registerLocationListener(new BDLocationListenerImpl());//注册定位监听接口  
        /** 
         * LocationClientOption 该类用来设置定位SDK的定位方式。 
         */  
        LocationClientOption option = new LocationClientOption();  
        option.setOpenGps(true); //打开GPRS  
        //option.setAddrType("all");//返回的定位结果包含地址信息  
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02  
        //option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先  
        option.setScanSpan(5000); //设置发起定位请求的间隔时间为5000ms  
        //option.disableCache(false);//禁止启用缓存定位  
        mLocClient.setLocOption(option);  //设置定位参数  
        mLocClient.start();  // 调用此方法开始定位  
        //定位图层初始化  
        myLocationOverlay = new LocationOverlay(mapView);  
        //实例化定位数据，并设置在我的位置图层  
        mLocData = new LocationData();  
        myLocationOverlay.setData(mLocData);  
        //添加定位图层  
        mapView.getOverlays().add(myLocationOverlay);  
        //修改定位数据后刷新图层生效  
        mapView.refresh();  
        /*
         * //骑行控监听事件
        bottomCommBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				startActivity(new Intent(MainActivity.this,CoustomDilogActivity.class));
			}
		});*/
        
	}  
	
	//左菜单初始化
	private void initSlidingMenu() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
		setBehindContentView(R.layout.main_left_layout);// 设置左菜单
		FragmentTransaction mFragementTransaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment mFrag = new MainLeftFragment(); //弹出菜单内容
		mFragementTransaction.replace(R.id.main_right_fragment, mFrag);
		mFragementTransaction.commit();
		// customize the SlidingMenu
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑
		mSlidingMenu.setShadowWidth(mScreenWidth / 1);// 设置阴影宽度
		mSlidingMenu.setBehindOffset(250);// 设置菜单宽度
		mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);// 设置左菜单阴影图片
		mSlidingMenu.setSecondaryShadowDrawable(R.drawable.slidingmenu_shadow);// 设置右菜单阴影图片
		mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
		mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
	}
		
	 @Override  
    protected void onResume() {  
        //MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()  
        mapView.onResume();  
        super.onResume();  
    }  
  
    @Override  
    protected void onPause() {  
        //MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()  
        mapView.onPause();  
        super.onPause();  
    }  
  
    @Override  
    protected void onDestroy() {  
        //MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()  
        mapView.destroy();  
        //退出应用调用BMapManager的destroy()方法  
        if(mapManager != null){  
            mapManager.destroy();  
            mapManager = null;  
        }  
        //退出时销毁定位  
        if (mLocClient != null){  
            mLocClient.stop();  
        }  
          
        super.onDestroy();  
    }  
    
	/** 
     * 定位接口，需要实现两个方法 
     * @author xiaanming 
     * 
     */  
    public class BDLocationListenerImpl implements BDLocationListener {  
  
        /** 
         * 接收异步返回的定位结果，参数是BDLocation类型参数 
         */  
        @Override  
        public void onReceiveLocation(BDLocation location) {  
            if (location == null) {  
                return;  
            }  
              
            StringBuffer sb = new StringBuffer(256);  
              sb.append("time : ");  
              sb.append(location.getTime());  
              sb.append("\nerror code : ");  
              sb.append(location.getLocType());  
              sb.append("\nlatitude : ");  
              sb.append(location.getLatitude());  
              sb.append("\nlontitude : ");  
              sb.append(location.getLongitude());  
              sb.append("\nradius : ");  
              sb.append(location.getRadius());  
              if (location.getLocType() == BDLocation.TypeGpsLocation){  
                   sb.append("\nspeed : ");  
                   sb.append(location.getSpeed());  
                   sb.append("\nsatellite : ");  
                   sb.append(location.getSatelliteNumber());  
                   } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){  
                   sb.append("\naddr : ");  
                   sb.append(location.getAddrStr());  
                }   
           
              Log.e("log", sb.toString());  
              
              
            MainActivity.this.location = location;  
              
            mLocData.latitude = location.getLatitude();  
            mLocData.longitude = location.getLongitude();  
            //如果不显示定位精度圈，将accuracy赋值为0即可  
            mLocData.accuracy = 0;  
            mLocData.direction = 0;  
            //将定位数据设置到定位图层里  
            myLocationOverlay.setData(mLocData);  
            //更新图层数据执行刷新后生效  
            mapView.refresh();  
              
              
            if(isFirstLoc || isRequest){  
                //将给定的位置点以动画形式移动至地图中心  
                mapController.animateTo(new GeoPoint(  
                        (int) (location.getLatitude() * 1e6), (int) (location  
                                .getLongitude() * 1e6)));  
                showPopupOverlay(location);  
                isRequest = false;  
            }  
            isFirstLoc = false;  
              
        }  
    }
    
	//继承MyLocationOverlay重写dispatchTap方法  
    private class LocationOverlay extends MyLocationOverlay{  
  
        public LocationOverlay(MapView arg0) {  
            super(arg0);  
        }  
  
          
        /** 
         * 在“我的位置”坐标上处理点击事件。 
         */  
        @Override  
        protected boolean dispatchTap() {  
            //点击我的位置显示PopupOverlay  
            showPopupOverlay(location);  
            return super.dispatchTap();  
        }  
          
    }
    
    /** 
     * 显示弹出窗口图层PopupOverlay 
     * @param location 
     */  
    private void showPopupOverlay(BDLocation location){  
         TextView popText = ((TextView)mPopupView.findViewById(R.id.location_tips));  
         popText.setText("[我的位置]\n" + location.getAddrStr());  
         mPopupOverlay.showPopup(getBitmapFromView(popText),  
                    new GeoPoint((int)(location.getLatitude()*1e6), (int)(location.getLongitude()*1e6)),  
                    15);  
           
    }  
    
    /**  
     * 显示Toast消息  
     * @param msg  
     */    
    private void showToast(String msg){    
        if(mToast == null){    
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);    
        }else{    
            mToast.setText(msg);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }    
        mToast.show();    
    }  
    
    /** 
     * 手动请求定位的方法 
     */  
    public void requestLocation() {  
        isRequest = true;  
          
        if(mLocClient != null && mLocClient.isStarted()){  
            showToast("正在定位......");  
            mLocClient.requestLocation();  
        }else{  
            Log.d("log", "locClient is null or not started");  
        }  
    }  
    
    /** 
     * 将View转换成Bitmap的方法 
     * @param view 
     * @return 
     */  
    public static Bitmap getBitmapFromView(View v) {  
        /*view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        view.buildDrawingCache();  
        Bitmap bitmap = view.getDrawingCache();  */
    	
    	v.clearFocus();    
        v.setPressed(false);    
    
        boolean willNotCache = v.willNotCacheDrawing();    
        v.setWillNotCacheDrawing(false);    
    
        // Reset the drawing cache background color to fully transparent    
        // for the duration of this operation    
        int color = v.getDrawingCacheBackgroundColor();    
        v.setDrawingCacheBackgroundColor(0);    
    
        if (color != 0) {    
            v.destroyDrawingCache();    
        }    
        v.buildDrawingCache();    
        Bitmap cacheBitmap = v.getDrawingCache();    
        if (cacheBitmap == null) {    
            Log.e("Folder", "failed getViewBitmap(" + v + ")", new RuntimeException());    
            return null;    
        }    
    
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);    
    
        // Restore the view    
        v.destroyDrawingCache();    
        v.setWillNotCacheDrawing(willNotCache);    
        v.setDrawingCacheBackgroundColor(color);    
        return bitmap;  
    }  
    
    /** 
     * 常用事件监听，用来处理通常的网络错误，授权验证错误等 
     * @author xiaanming 
     * 
     */  
    public class MKGeneralListenerImpl implements MKGeneralListener{  
  
        /** 
         * 一些网络状态的错误处理回调函数 
         */  
        @Override  
        public void onGetNetworkState(int iError) {  
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {  
                showToast("您的网络出错啦！");  
            }  
        }  
  
        /** 
         * 授权错误的时候调用的回调函数 
         */  
        @Override  
        public void onGetPermissionState(int iError) {  
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {  
                showToast("API KEY错误, 请检查！");  
            }  
        }  
    }
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.titleBtLeft:
				mSlidingMenu.showMenu(true);
			break;
			case R.id.bottomTitleBtLeft:
				 requestLocation();
			break;
			case R.id.bottomCommBtn:
				if(buttomPopupWindow!=null){
					buttomPopupWindow.dismiss();
				}
				bottomShowWindow(v);
			break;
		}
	} 
    
    
    /**
     * 编辑/删除
     * @param parent
     */
    private void bottomShowWindow(final View parent) {
    	View view1;
    	//ListView share_list = null;
        if (buttomPopupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            view1 = layoutInflater.inflate(R.layout.buttonview, null);
            //TextView edit = (TextView) view1.findViewById(R.id.editView);
            //TextView del = (TextView) view1.findViewById(R.id.delView);
 
           /* edit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});
            
            del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});*/
            
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view1,LayoutParams.FILL_PARENT,550);
        }
 
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
 
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
       // int xPos = windowManager.getDefaultDisplay().getWidth() / 2;
             //   - popupWindow.getWidth() / 2;
       // int xPos = windowManager.getDefaultDisplay().getHeight()/4;
       // popupWindow.showAtLocation(parent, Gravity.RIGHT, 10,10);
       // popupWindow.showAsDropDown(parent,1200,0);
       // popupWindow.showAtLocation(parent,0,0,0);//在屏幕顶部|居右，带偏移
        popupWindow.showAtLocation(parent,Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    
}

package com.xl.fntmaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.xl.fntmaker.FontUtils;
import com.xl.game.math.Str;
import com.xl.game.tool.DisplayUtil;
import com.xl.game.tool.SharedPreferencesUtil;
import com.xl.view.ColorDraw;
import com.xl.game.view.FileSelectView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

	public static final int
	DLG_HELP=100,
	DLG_ABOUT=101;
	
	
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId()){
			case R.id.btn_look:
				look();
				break;
			case R.id.btn_save:
				save();
				break;
			case R.id.text_4000:
				text_fontText.append( getTextFromAssets(this,"4000.txt","GBK"));
				break;
			case R.id.text_gb2312:
				text_fontText.append(getTextFromAssets(this,"gb2312.txt","UTF-8"));
				break;
			case R.id.text_zhChar:
				text_fontText.append("“”！～？。，、…（）【】《》＊／＼＿＋－︿＜＃＄＆％＂＇〖〗『』※☆★—→←з」∠╳●○♂◆△▽");
				break;
			
		}
		
	}
	
	
	ColorDraw colorView;
	//字体路径 文字大小 位移 颜色 文件名
	FileSelectView select_fontPath;
	TextView text_fontSize, text_pointY, text_color, text_fileName;
	TextView text_fontWidth, text_fontHeight;
    TextView text_fontText;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		//Var3dFreeFont.createTextureAtlasFont(getTextFromAssets(this,"4000.txt","gbk"),24,0xfff0f0f0);
		//Var3dFreeFont.createTextureAtlasFont("风的影子 饕餮",24,0xff505050);
        setOnClickListenerAllButtons(this);
		colorView = (ColorDraw) findViewById(R.id.fontColor);
		colorView.setColor(0xffffffff);
		select_fontPath = (FileSelectView) findViewById(R.id.edit_fontFile);
		text_fontSize = (TextView) findViewById(R.id.edit_fontSize);
		text_pointY= (TextView) findViewById(R.id.edit_fontY);
		//text_color = (TextView) findViewById(R.id.fontColor);
		text_fileName = (TextView) findViewById(R.id.edit_fontFileName);
		text_fontWidth = (TextView) findViewById(R.id.edit_width);
		text_fontHeight = (TextView) findViewById(R.id.edit_height);
		text_fontText = (TextView) findViewById(R.id.fontText);
		
		//select_fontPath.setThemeBlack(false);
		SharedPreferencesUtil pre = new SharedPreferencesUtil(this);
		select_fontPath.setPath( pre.getString("fontPath",select_fontPath.getPath()));
		text_fontSize.setText(pre.getString("fontSize",text_fontSize.getText().toString()));
		
		String text = pre.getString("text",text_fontText.getText().toString());
		if(text.length()!=0)
			text_fontText.setText(text);
		text_fontWidth.setText(pre.getString("fontWidth",text_fontWidth.getText().toString()));
		text_fontHeight.setText(pre.getString("fontHeight",text_fontHeight.getText().toString()));
		colorView.setColor(pre.getInt("fontColor", colorView.getColor()));
		text_pointY.setText(pre.getString("pointY",text_pointY.getText().toString()));
		text_fileName.setText(pre.getString("fileName",text_fileName.getText().toString()));
		Intent intent = getIntent();
		if(intent.getData()!=null){
			String path = intent.getData().getPath();
			doFntFile(path);
		}
		requestPermission();
    }

	private void requestPermission() {
		PermissionsUtil.requestPermission(getApplication(), new PermissionListener() {
			@Override
			public void permissionGranted(@NonNull String[] permissions) {
				//toast("");

			}

			@Override
			public void permissionDenied(@NonNull String[] permissions) {
				//Toast.makeText(MainActivity.this, "用户拒绝了访问摄像头", Toast.LENGTH_LONG).show();
			}
		},  android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		// TODO: Implement this method
		super.onNewIntent(intent);
		if(intent.getData()!=null){
			String path = intent.getData().getPath();
			doFntFile(path);
		}
	}
	
	
	public void doFntFile(String path){
		ReadFont readFont = new ReadFont(path);
		if(readFont.isReadOk()){
		text_fontText.setText(readFont.getText());
		text_fontSize.setText(""+readFont.getTextSize());
		colorView.setColor(readFont.getTextColor());
		text_fontWidth.setText(""+readFont.getWidth());
		text_fontHeight.setText(""+readFont.getHeight());
		text_fileName.setText(readFont.getName());
		text_pointY.setText(""+readFont.getPointY());
		
		
		toast("读取字库成功");
		}
		else{
			toast("字库读取失败");
		}
	}
	
	public void toast(String text){
		Toast t = Toast.makeText(this,text,0);
		t.setText(text);
		t.show();
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		
		SharedPreferencesUtil pre = new SharedPreferencesUtil(this);
		pre.setString("fontPath",select_fontPath.getPath());
		pre.setString("fontSize",text_fontSize.getText().toString());
		pre.setString("text",text_fontText.getText().toString());
		pre.setString("fontWidth",text_fontWidth.getText().toString());
		pre.setString("fontHeight",text_fontHeight.getText().toString());
		pre.setInt("fontColor", colorView.getColor());
		pre.setString("pointY", text_pointY.getText().toString());
		pre.setString("fileName",text_fileName.getText().toString());
		
		
		super.onStop();
	}
	
	
	
	
	//为根布局下所有按钮设置监听
	public void setOnClickListenerAllButtons(View.OnClickListener listener){
		//获取根布局
		ViewGroup group = (ViewGroup)((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
		setOnClickListenerAllButtons(group,listener);
	}

	//为所有按钮设置监听
	private void setOnClickListenerAllButtons(View view,View.OnClickListener listener) {

		List<View> allchildren = new ArrayList<View>();

		if(view instanceof Button)
		{
			if(view.getId()!= -1)
				view.setOnClickListener(listener);
		}
		else if(view instanceof ImageButton)
		{
			if(view.getId()!=-1)
				view.setOnClickListener(listener);
		}

		else if (view instanceof ViewGroup) {

			ViewGroup vp = (ViewGroup) view;

			for (int i = 0; i < vp.getChildCount(); i++) {

				View viewchild = vp.getChildAt(i);

				setOnClickListenerAllButtons(viewchild,listener);

			}

		}



	}
	
	
	public static String getTextFromAssets(Context context, String assetspath,String coding)
	{
		String r0_String;
		String r1_String = "";
		AssetManager assets = context.getResources().getAssets();
		try {
			InputStream input = assets.open(assetspath);
			byte[] buffer = new byte[input.available()];
			input.read(buffer);
			r0_String = new String(buffer, coding);
			input.close();
			return r0_String;
		} catch (IOException r0_IOException) {
			r0_String = r1_String;
		}


		return r0_String;

	}


	private void look()
	{
		int fontSize = Str.atoi(text_fontSize.getText().toString());
		//if(fontSize>0)fontSize = -fontSize;
		int width = Str.atoi(text_fontWidth.getText().toString());
		int height = Str.atoi(text_fontHeight.getText().toString());
		String filename = text_fileName.getText().toString();
		int color = colorView.getColor();
		int ypoint = Str.atoi(text_pointY.getText().toString());
		if(ypoint>0)ypoint = -ypoint;
		String fontText = text_fontText.getText().toString();
		
	    FontUtils fontUtils = new FontUtils();
		if(select_fontPath.getPath().length()!=0 && (select_fontPath.getPath().endsWith(".ttf") || select_fontPath.getPath().endsWith(".TTF")))
			fontUtils.setTyleFace( Typeface.createFromFile(select_fontPath.getPath()));
		
		fontUtils.setText(fontText);
		text_fontText.setText(fontUtils.getText());
		fontUtils.setWidth(width);
		fontUtils.setHeight(height);
		fontUtils.setTextColor(color);
		fontUtils.setName(filename);
		fontUtils.setTextSize(fontSize);
		fontUtils.setYPoint(ypoint);
		PhotoView photoView = new PhotoView(this);
		photoView.setMinimumHeight(DisplayUtil.dip2px(this,320));
		photoView.setMinimumWidth(DisplayUtil.dip2px(this,320));
		photoView.setImageDrawable(new BitmapDrawable(fontUtils.getBitmapRect()));
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					
				}
				
			
		}
		);
		builder.setView(photoView);
		
		builder.create().show();
		
		
		
	}
	
	
	private void save()
	{
		int fontSize = Str.atoi(text_fontSize.getText().toString());
		//if(fontSize>0)fontSize=-fontSize;
		int width = Str.atoi(text_fontWidth.getText().toString());
		int height = Str.atoi(text_fontHeight.getText().toString());
		String filename = text_fileName.getText().toString();
		int color = colorView.getColor();
		int ypoint = Str.atoi(text_pointY.getText().toString());
		if(ypoint>0)ypoint = -ypoint;
		String fontText = text_fontText.getText().toString();

	    FontUtils fontUtils = new FontUtils();
		if(select_fontPath.getPath().length()!=0 && (select_fontPath.getPath().endsWith(".ttf") || select_fontPath.getPath().endsWith(".TTF")))
			fontUtils.setTyleFace( Typeface.createFromFile(select_fontPath.getPath()));
		fontUtils.setText(fontText);
		fontUtils.setWidth(width);
		fontUtils.setHeight(height);
		fontUtils.setTextColor(color);
		fontUtils.setName(filename);
		fontUtils.setTextSize(fontSize);
		fontUtils.setYPoint(ypoint);
		fontUtils.saveFont();
		fontUtils.saveAtlas();
		fontUtils.saveBitmap();
Toast.makeText(this,"生成成功",Toast.LENGTH_SHORT).show();
		


	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		// TODO: Implement this method
		if(id == DLG_ABOUT)
		{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.app_about);
			builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}


				}
			);
		return builder.create();
		}
		
		if(id == DLG_HELP)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.app_help);
			builder.setPositiveButton("返回", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method

					}


				}
			);
			return builder.create();
		}
		
		return super.onCreateDialog(id);
	}
	
	//菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
		int i=0;
		//组别 id 顺序 文本
		menu.add(0,0,1,"帮助");
		menu.add(0,1,1,"关于");
        menu.add(0,2,1,"检查更新");

		return true; //super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case 0://
showDialog(DLG_HELP);
				break;
			case 1://
				showDialog(DLG_ABOUT);
				break;
			case 2://
			N2J_wap("http://www.yzjlb.net/app/libgdx/fntmaker/");
				break;
		}



		return super.onOptionsItemSelected(item);
	}
	
	//调用浏览器打开
	void N2J_wap(String http)
	{
		/*
		 Uri uri=Uri.parse(http);
		 Intent intent=new Intent( Intent.ACTION_VIEW ,uri );
		 run_activity.startActivity(intent);
		 */
		Intent intent= new Intent();

		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(http);
		intent.setData(content_url);
		try
		{
			this.startActivity(intent);
		}
		catch(Exception e)
		{
			Toast.makeText(this,"请下载网页浏览器",0).show();
		}
	}
	
	
	
}

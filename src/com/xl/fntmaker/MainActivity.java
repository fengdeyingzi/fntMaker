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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.xl.fntmaker.FontUtils;
import com.xl.game.math.Str;
import com.xl.game.tool.DisplayUtil;
import com.xl.view.ColorDraw;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener
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
		}
		
	}
	
	
	ColorDraw colorView;
	//字体路径 文字大小 位移 颜色 文件名
	TextView text_fontPath, text_fontSize, text_pointY, text_color, text_fileName;
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
		text_fontPath = (TextView) findViewById(R.id.edit_fontFile);
		text_fontSize = (TextView) findViewById(R.id.edit_fontSize);
		text_pointY= (TextView) findViewById(R.id.edit_fontY);
		//text_color = (TextView) findViewById(R.id.fontColor);
		text_fileName = (TextView) findViewById(R.id.edit_fontFileName);
		text_fontWidth = (TextView) findViewById(R.id.edit_width);
		text_fontHeight = (TextView) findViewById(R.id.edit_height);
		text_fontText = (TextView) findViewById(R.id.fontText);
		
		
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
		if(text_fontPath.getText().length()!=0)
			fontUtils.setTyleFace( Typeface.createFromFile(text_fontPath.getText().toString()));
		
		fontUtils.setText(fontText);
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
		if(text_fontPath.getText().length()!=0)
			fontUtils.setTyleFace( Typeface.createFromFile(text_fontPath.getText().toString()));
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
Toast.makeText(this,"生成成功",1).show();
		


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

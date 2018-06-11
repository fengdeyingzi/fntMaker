package com.xl.fntmaker;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.os.Environment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FontUtils
{
	String fontName;
	int ypoint;
	String fontText;
	Paint paint;
	int fontColor;
	int fontSize;
	int bitmap_width;
	int bitmap_height;
	int yj; //纵向间隔
	String save_dir = "AppProjects";
	
	public FontUtils()
	{
		this.fontName = "font";
		this.fontColor= 0xffffffff;
		this.ypoint = 4;
		this.fontSize = 40;
		this.fontText="abcdefghijklmnopqrstuvwxyz";
		this.paint = new Paint();
		this.bitmap_width = 1024;
	    this.bitmap_height = 1024;
		this.yj = 0;
		this.save_dir = "AppProjects";
		File file = new File(getSDPath(),save_dir);
		file.mkdir();
	}
	
	public static String getSDPath()
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if(sdCardExist)
		{
			sdDir=Environment.getExternalStorageDirectory();//获取sd卡目录
		}
		else 
		{
			return null;
		}
		return sdDir.getPath();
	}
	
	
	//设置文字
	public void setText(String text){
		this.fontText = dereplication_old(text);
	}
	
	
	
	//获取不重复的字符串
	private String dereplication(String str)
	{
		HashSet<Character> data = new HashSet<Character>();
		StringBuffer buf=new StringBuffer();
		for(char s:str.toCharArray())
		{
			data.add(s);
		}
		//String result = "";
		for(char s : data)
			buf.append(s);
		return buf.toString();
	}
	
	//获取不重复的字符串(效率低，已换成新方法)
	private String dereplication_old(String str)
	{
		List<String> data = new ArrayList<String>();
		StringBuffer buf=new StringBuffer();
		for(int i = 0; i<str.length(); i++)
		{
			String s = str.substring(i, i+1);
			if(!data.contains(s))
				data.add(s);
		}
		//String result = "";
		for(String s : data)
			buf.append(s);
		return buf.toString();
	}
	
	
	//设置生成的名字
	public void setName(String name)
	{
		this.fontName = name;
	}
	//设置文字纵向偏移
	public void setYPoint(int y)
	{
		this.ypoint = y;
	}
	
	//设置字体
	public void setTyleFace(Typeface typeface)
{
	this.paint.setTypeface(typeface);
}	
	//设置文字大小
	public void setTextSize(int textSize)
	{
		this.paint.setTextSize(textSize);
		this.fontSize = textSize;
	}
	//设置文字颜色
	public void setTextColor(int color)
	{
		this.paint.setColor(color);
	}
	//设置图片宽度
	public void setWidth(int width){
		this.bitmap_width = width;
	}
	//设置图片高度
	public void setHeight(int height){
		this.bitmap_height = height;
	}
	
	//
	public void saveBitmap()
	{
		Bitmap bitmap = getBitmap();
		File file = new File(getSDPath()+File.separator+ this.save_dir+File.separator+this.fontName+".png");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//生成图片
	public Bitmap getBitmap()
	{
		Bitmap newb = Bitmap.createBitmap(this.bitmap_width, this.bitmap_height, Bitmap.Config.ARGB_8888);// 创建位图
		Canvas canvas = new Canvas(newb);// 创建画布
		
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
			| Paint.FILTER_BITMAP_FLAG));
		int yi=0;
		int xi=0;
		for (int i = 0; i < fontText.length(); i++) {
			//int yi = i / maxx + 1;
			//int xi = i % maxx;

			int font_w = fontSize;
			if(fontText.charAt(i)<256)
			{
				float f_w = paint.measureText(fontText.substring(i,i+1));
				if(f_w%1>0)font_w=(int)(f_w+1);
				else font_w = (int)f_w;
			}
			else
				font_w = this.fontSize;
			
			canvas.drawText(fontText, i, i + 1, xi, yi+fontSize+ypoint, paint);
			
			/*
			if(isdebug)
			{
				canvas.drawLine(xi,yi,xi,yi+size,paint);
			}
            */
			xi+= font_w;
			if(xi+font_w > this.bitmap_width)
			{
				xi=0; yi+=fontSize+yj;
			}
		}
		return newb;
	}
	
	//生成预览图
	public Bitmap getBitmapRect(){
		Bitmap newb = Bitmap.createBitmap(this.bitmap_width, this.bitmap_height, Bitmap.Config.ARGB_8888);// 创建位图
		Canvas canvas = new Canvas(newb);// 创建画布
		boolean isdebug	= true;	
		
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
		int yi=0;
		int xi=0;
		for (int i = 0; i < fontText.length(); i++) {
			//int yi = i / maxx + 1;
			//int xi = i % maxx;

			int font_w = fontSize;
			if(fontText.charAt(i)<256)
			{
				float f_w = paint.measureText(fontText.substring(i,i+1));
				if(f_w%1>0)font_w=(int)(f_w+1);
				else font_w = (int)f_w;
			}
			else
				font_w = this.fontSize;
			canvas.drawText(fontText, i, i + 1, xi, yi+fontSize+ypoint, paint);

			
			 if(isdebug)
			 {
			 canvas.drawLine(xi,yi,xi,yi+fontSize,paint);
			 canvas.drawLine(xi,yi,xi+font_w,yi,paint);
			 }
			 xi+= font_w;
			if(xi+font_w > this.bitmap_width)
			{
				xi=0; yi+=fontSize+yj;
			}
		}
		return newb;
	}
	
	//生成字体
	public void saveFont(){
		String bitmapname = this.fontName+".fnt";
		int scaleW=this.bitmap_width,scaleH=this.bitmap_height;
		int yi=0;
		int xi=0;
		String str_fnt = "info face=\"黑体\" size="+fontSize+" bold=0 italic=0 charset=\"\" unicode=0 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1\n"
			+"common lineHeight="+(fontSize+yj)+" base="+fontSize+" scaleW="+scaleW+" scaleH="+scaleH+" pages=1 packed=0\n"
			+"page id=0 file=\""+bitmapname+"\"\n"
			+"chars count="+this.fontText.length()+"\n";
		StringBuffer buf_fnt = new StringBuffer();
		buf_fnt.append(str_fnt);
			
		for (int i = 0; i < fontText.length(); i++) {
			//int yi = i / maxx + 1;
			//int xi = i % maxx;

			int font_w = fontSize;
			if(fontText.charAt(i)<256)
			{
				float f_w = paint.measureText(fontText.substring(i,i+1));
				if(f_w%1>0)font_w=(int)(f_w+1);
				else font_w = (int) f_w;
			}
			else
				font_w = this.fontSize;
			
			buf_fnt.append("char id="+((int)fontText.charAt(i))+"   x="+(xi)+"     y="+yi+"     width="+(int)font_w+"     height="+fontSize+"     xoffset="+0+"     yoffset="+0+"    xadvance="+(int)font_w+"     page=0  chnl=0 \n");


			

			xi+= font_w;
			if(xi+font_w > this.bitmap_width)
			{
				xi=0; yi+=fontSize+yj;
			}
		}
		
		try {
			FileOutputStream outStream = new FileOutputStream(
				getSDPath()+File.separator+ save_dir+File.separator+ bitmapname, false);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,
															   "utf-8");
			writer.write(buf_fnt.toString());
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//生成字体Atlas
	public void saveAtlas(){
		String bitmapname = this.fontName+".atlas";
		int scaleW=this.bitmap_width,scaleH=this.bitmap_height;
		int yi=0;
		int xi=0;
		String str_fnt = "info face=\"黑体\" size="+fontSize+" bold=0 italic=0 charset=\"\" unicode=0 stretchH=100 smooth=1 aa=1 padding=0,0,0,0 spacing=1,1\n"
			+"common lineHeight="+(fontSize+yj)+" base="+fontSize+" scaleW="+scaleW+" scaleH="+scaleH+" pages=1 packed=0\n"
			+"page id=0 file=\""+bitmapname+"\"\n"
			+"chars count="+this.fontText.length()+"\n";
		StringBuffer buf_fnt = new StringBuffer();
		buf_fnt.append(str_fnt);

		for (int i = 0; i < fontText.length(); i++) {
			//int yi = i / maxx + 1;
			//int xi = i % maxx;

			int font_w = fontSize;
			if(fontText.charAt(i)<256)
			{
				float f_w = paint.measureText(fontText.substring(i,i+1));
				if(f_w%1>0)font_w=(int)(f_w+1);
				else font_w = (int) f_w;
			}
			else
				font_w = this.fontSize;
			
			buf_fnt.append(fontText.substring(i, i + 1) + "\r\n" + "  rotate: false\r\n" + "  xy:" + xi + "," + (yi)  + "\r\n" + "  size:" + (int)font_w + "," + fontSize + "\r\n" + "  orig:" + fontSize + "," + fontSize + "\r\n" + "  offset:0,0\r\n  index:-1\r\n");




			xi+= font_w;
			if(xi+font_w > this.bitmap_width)
			{
				xi=0; yi+=fontSize+yj;
			}
		}
		
		try {
			FileOutputStream outStream = new FileOutputStream(
				getSDPath()+File.separator+ save_dir+File.separator+ bitmapname, false);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,
															   "utf-8");
			writer.write(buf_fnt.toString());
			writer.flush();
			writer.close();// 记得关闭
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

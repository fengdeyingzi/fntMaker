package com.xl.fntmaker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.xl.game.math.Str;
import com.xl.game.tool.Log;
/*
fnt字体读取

*/
public class ReadFont
{
	//int color;
	//字体路径 文字大小 位移 颜色 文件名
	//String select_fontPath;
	int fontSize, pointY, textColor;
	String filePath;
	String fileName;
	int width,height;
	boolean isReadOk; //是否读取成功
    String text;
	
	public ReadFont(String path){
		if(path==null){
			return;
		}
		File file = new File(path);
		String temp;
		String text;
		if(file.isFile()){
			temp = file.getName().toLowerCase();
			if(temp.endsWith(".fnt")){
				text = readText(path);
				this.filePath = file.getPath();
				this.fileName = file.getName().substring(0,file.getName().length() - 4);
				if(text!=null){
				doText(text);
				isReadOk = true;
				}
			}
		}
		
		
		
	}
	
	private void doText(String text){
		int type=0;
		char c=0;
		int start=0,end=0;
		String head = null;
		String key = null;
		String word = null;
		this.textColor = 0xfff0f0f0;
		StringBuffer buf=new StringBuffer();
		for(int i=0;i<text.length();i++){
			c = text.charAt(i);
			//Log.e("",""+c+" type="+type);
			switch(type){
				case 0:
					if(c>='a' && c<='z'){
						type = 1;
						start =i;
					}
					if(c=='\n'){
						type=0;
					}
				break;
				case 1:
					if(c==' '){
						type=2;
						head = text.substring(start,i);
						
					}
					if(c=='\n'){
						type=0;
					}
				break;
			    case 2:
					if(c>='a'&& c<='z'){
						type = 3;
						start = i;
					}
					if(c=='\n'){
						type=0;
					}
				break;
				case 3:
					if(c=='='){
						type=4;
						key = text.substring(start,i);
						start = i+1;
					}
					if(c=='\n'){
						type=0;
					}
				break;
				case 4:
					if( c=='\r' || c=='\n'){
						type = 0;
						word = text.substring(start, i);
					}
					if(c==' '){
						type = 2;
						word = text.substring(start,i);
						Log.e("","head="+head+",key="+key+",word="+word);
					}
					if(type!=4){
						if(head.equals("info")){
							if(key.equals("size")){
								fontSize = Str.atoi(word);
								pointY = fontSize/10;
							}
							
						}
						if(head.equals("common")){
							if(key.equals("scaleW")){
								this.width = Str.atoi(word);
							}
							if(key.equals("scaleH")){
								this.height = Str.atoi(word);
							}
						}
						if(head.equals("char")){
							if(key.equals("id")){
								buf.append((char)Str.atoi(word));
							}
						}
					}
           }
		}
		this.text = buf.toString();
	}
	
	private String readText(String path){

	    String text=null;
		String encoding = "UTF-8";  
		try
		{
			FileInputStream read =  
				new FileInputStream(path);


			byte[] buf = new byte[read.available()];
			read.read(buf);
			read.close();
			text= new String(buf,encoding);

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return text;
	}
	
	
	public String getText(){
		return this.text;
	}
	
	public int getTextColor(){
		return this.textColor;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
}
   
    public String getName(){
		return this.fileName;
	}

	public int getTextSize(){
		return this.fontSize;
	}

	public int getPointY(){
		return -(this.fontSize/5);
	}
	
	public boolean isReadOk(){
		return this.isReadOk;
	}
	
}

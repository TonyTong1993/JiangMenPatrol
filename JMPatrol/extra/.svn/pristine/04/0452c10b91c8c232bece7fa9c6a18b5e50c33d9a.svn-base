package com.ecity.mobile.android.crossanalysis;

import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class XmlAnalysis {
	/**the result arraylist.*/
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> XmlAnalysis_result = null ;//泛型
	/**the outline arraylist.*/
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> outLine ;
	/**the outpoint arraylist.*/
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> outPoint;
	/**the pipe frame arraylist.*/
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> pipeFrame ;
	/**the pipe sections arraylist.*/
	private ArrayList<Lininfo>   pipeCrosssection;
	/**the line attrabute information of pipes.*/
	private ArrayList<String>    LinInfo ;
	/**the net attrabute information of pipes.*/
	private ArrayList<String>    AnnInfo ;
	private Lininfo              lininfo  ;
	
	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> getXmlAnalysis_result() {
		return XmlAnalysis_result;
	}
	
	
	/**
	 * Init the structure of the arraylist.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XmlAnalysis(){
		XmlAnalysis_result = new ArrayList (4);
		outLine = new ArrayList (8);
		outPoint = new ArrayList (3);
		pipeFrame = new ArrayList (58);
		pipeCrosssection = new ArrayList (20);
	}
	/**
	 * Read a xml document.
	 * @param str the name of xml document.
	 * @return the data stream.
	 */
	public InputStream readFile(String str){
		String foldername = null;
		try{
		 foldername = Environment.getExternalStorageDirectory().getPath();
		 foldername = foldername+"/Ecity";}//找到SD卡Ecity文件
		catch(Exception e){
			e.printStackTrace();
			}
		String targetPath = foldername + str; // 找到Xml文件的名字
		File targetFile = new File(targetPath);
		InputStream inputstream =null;
		try{
			inputstream	= new FileInputStream(targetFile);// 新建输入流

		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return inputstream;
	}
	
	/**
	 * Read the data stream, get a result arraylist.
	 * @param inputstream the data stream of result.
	 * @return the result arraylist.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public  ArrayList<ArrayList> fromXmlInputStream(InputStream inputstream) throws Exception{	
			XmlPullParser parser = Xml.newPullParser();
			//设置输入流编码格式
			parser.setInput(inputstream,"UTF-8");
			//事件类型：文件开头，结尾...	
			int eventType = parser.getEventType();		
			while (eventType != XmlPullParser.END_DOCUMENT&&eventType != XmlPullParser.TEXT) {
				switch (eventType) {
				// 文档开始事件,可以进行数据初始化处理
				case XmlPullParser.START_DOCUMENT:
					new XmlAnalysis();
					break;
					// 开始元素事件	
				case XmlPullParser.START_TAG:
					XmlSection_OutLine(eventType,parser);				
					XmlSection_OutPoint(eventType,parser);	
					XmlSection_PipeFrame(eventType,parser);	
					XmlSection_Pipe(eventType,parser);	
					break;											
				case XmlPullParser.END_TAG:// 结束元素事件
					break;
					}				
				eventType = parser.next();	
				}
			XmlAnalysis_result.add(0,outLine) ;	//把数据装入结果数组
			XmlAnalysis_result.add(1,outPoint) ;	
			XmlAnalysis_result.add(2,pipeFrame) ;	
			XmlAnalysis_result.add(3,pipeCrosssection) ;	
			inputstream.close();		
			return XmlAnalysis_result;		
		}

	private void XmlSection_Pipe(int eventType, XmlPullParser parser) throws Exception {
		 if(parser.getName().equalsIgnoreCase("管线断面")){			
				while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("管线断面"))){
					// 管线断面的gid
						while(!parser.getName().equalsIgnoreCase("gid"))
							parser.nextTag();	
							eventType = parser.getEventType();
							lininfo = new Lininfo();
							lininfo.setGid(parser.nextText());	
							parser.nextTag();	
							eventType = parser.getEventType();
							//一根管子结束
							while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("LinInfo"))){
										if(parser.getName().equalsIgnoreCase("color")){										
											lininfo.setColor(parser.nextText());
											parser.nextTag();
											eventType = parser.getEventType();
										}									
								else if(parser.getName().equalsIgnoreCase("centerX")){
									lininfo.setCenterX(parser.nextText());										
									parser.nextTag();
									eventType = parser.getEventType();
								}
								else if(parser.getName().equalsIgnoreCase("centerY")){
									lininfo.setCenterY(parser.nextText());									
									parser.nextTag();
									eventType = parser.getEventType();
								}
								else if(parser.getName().equalsIgnoreCase("rad")){
									lininfo.setRad(parser.nextText());	
									parser.nextTag();
									eventType = parser.getEventType();
								}		
								else{// LinAtt
								while(!parser.getName().equalsIgnoreCase("ID"))
									parser.nextTag();	
								eventType = parser.getEventType();
								//结尾标志
								while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("LinAtt"))){
									//if(parser.nextText()!= null){//调用一次nextText()后，指针会跳到END_TAG										
									lininfo.setLinAtt(parser.getName(),parser.nextText());
									 parser.nextTag();		
									 eventType = parser.getEventType();
									}	
								//AnnAtt
								while (!parser.getName().equalsIgnoreCase("ID"))
									parser.nextTag();			
									eventType = parser.getEventType();
									//结尾标志								
									while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("NetAtt"))){
										lininfo.setNetAtt(parser.getName(),parser.nextText());
										 parser.nextTag();
										 eventType = parser.getEventType();
										 }	
									//下移到LinInfo 
									parser.nextTag();
									eventType = parser.getEventType();
								}
							}							
							pipeCrosssection.add(lininfo);		
							//下移到LinInfo 
							parser.nextTag();
							eventType = parser.getEventType();
					}		
				}		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void XmlSection_PipeFrame(int eventType, XmlPullParser parser) throws Exception {
		if(parser.getName().equalsIgnoreCase("管架")){
		while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("管架"))){//没有到“管架”的尾结点
			while(!parser.getName().equalsIgnoreCase("gid"))// 管架的gid
				parser.nextTag();		
			eventType = parser.getEventType();
			LinInfo = new ArrayList(9);
			while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("LinInfo"))){
				LinInfo.add(parser.nextText());
				parser.nextTag();//添加完就下移
				eventType = parser.getEventType();
			}
			pipeFrame.add(LinInfo);
			parser.nextTag();
			eventType = parser.getEventType();
			}
		parser.nextTag();
		eventType = parser.getEventType();
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void XmlSection_OutPoint(int eventType, XmlPullParser parser) throws Exception {	
		while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("外框点"))){//没有到“外框点”的尾结点
		while(!parser.getName().equalsIgnoreCase("gid"))// 外框点的gid
			parser.nextTag();		
		eventType = parser.getEventType();
		AnnInfo = new ArrayList(9);
		while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("AnnInfo"))){
			AnnInfo.add(parser.nextText());
			parser.nextTag();//添加完就下移
			eventType = parser.getEventType();
		}
		outPoint.add(AnnInfo);
		parser.nextTag();
		eventType = parser.getEventType();
		}
		parser.nextTag();
		eventType = parser.getEventType();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void XmlSection_OutLine(int eventType,XmlPullParser parser) throws Exception {	
		//没有到“外框线”的尾结点
		while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("外框线"))){
		while(!parser.getName().equalsIgnoreCase("gid"))// 外框线的gid
			parser.nextTag();		
		eventType = parser.getEventType();
		LinInfo = new ArrayList(5);
		while(!(eventType==XmlPullParser.END_TAG&&parser.getName().equalsIgnoreCase("LinInfo"))){
			LinInfo.add(parser.nextText());
			parser.nextTag();//添加完就下移
			eventType = parser.getEventType();
		}
		outLine.add(LinInfo);
		parser.nextTag();
		eventType = parser.getEventType();
		}				
	}				
}


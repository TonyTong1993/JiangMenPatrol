package com.ecity.mobile.android.crossanalysis;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CrossAnalysisAlertDialog extends Dialog{
	private Context context;
	private String title;
	private EditText querygid;
	private String gidstr;
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList>XmlAnalysis_result;
	private CrossAnalysisResultView csaview;
	/**
	 * 
	 * @param context
	 * @param csaview the view of section analysis.
	 * @param title the titile of dialog.
	 */
	public CrossAnalysisAlertDialog(Context context, CrossAnalysisResultView csaview,String title){
		super(context);
		this.context = context;
		this.csaview = csaview;
		this.title = title;
		}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//no title
		setContentView(R.layout.querydialog);
		this.setTitle(title);
		Button okbtn = (Button)findViewById(R.id.btn_queryok);
		Button cancelbtn = (Button)findViewById(R.id.btn_querycancel);
		okbtn.setOnClickListener(clickOkListener);
		cancelbtn.setOnClickListener(clickCancelListener);
		querygid = (EditText)findViewById(R.id.editText_querygid);		
		/**Set the edit box can't enter more than 5.*/
		querygid.addTextChangedListener(new TextWatcher(){
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			int num = 5;

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start,
					int before, int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				selectionStart = querygid.getSelectionStart();
				selectionEnd = querygid.getSelectionEnd();
				if(temp.length()>num){
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					querygid.setSelection(tempSelection);//设置光标在最后
				}
			}				
		});		
	}
	
	private View.OnClickListener clickOkListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {	
			gidstr = querygid.getText().toString();//获取输入的gid
			EditQueryPopop(gidstr);
			}
		};
	
	private View.OnClickListener clickCancelListener = new View.OnClickListener() {
	
		@Override
			public void onClick(View v) {			
				CrossAnalysisAlertDialog.this.dismiss();
			}
		};
		
	/**
	 * Edit box query gid, if found, pop-up query results, if not found, the pop-up error dialog.
	 * @param gidstr the input gid. 
	 */
	@SuppressWarnings("unchecked")
	public void EditQueryPopop(String gidstr){
		int i;		
		if(gidstr.equalsIgnoreCase("")||Integer.parseInt(gidstr)>20||Integer.parseInt(gidstr)<1){
			setToast("输入错误，请重新输入！");
		}
		else{
			XmlAnalysis_result = XmlAnalysisData.xmlAnalysis_result;
			ArrayList<Lininfo>PipeCrosssection = (XmlAnalysis_result.get(3)) ;
			Lininfo CrosssectionLininfo;
			for( i = 0;i<PipeCrosssection.size();i++)	{
				CrosssectionLininfo = PipeCrosssection.get(i);
				if(CrosssectionLininfo.getGid().equalsIgnoreCase(gidstr)){
					csaview.HighLight(CrosssectionLininfo);
					PopupShow(CrosssectionLininfo);
					CrossAnalysisAlertDialog.this.dismiss();
					break;
				}
			}
			if(i==PipeCrosssection.size())//遍寻不得
				setToast("输入格式错误，请重新输入！");
		}
	}

	public void PopupShow(Lininfo CrosssectionLininfo){
		QueryPopup querypopup = new QueryPopup(context, csaview, CrosssectionLininfo);
		LayoutInflater inflater = ((CrossAnalysisResultActivity) context).getLayoutInflater();
		View view = inflater.inflate(R.layout.activity_crossanalysisresult, null);//获取父控件
		querypopup.showPopup(view);
	}
	
	private void setToast(String toastTextString) {
		Toast.makeText(context, toastTextString, Toast.LENGTH_SHORT).show();
	}
}

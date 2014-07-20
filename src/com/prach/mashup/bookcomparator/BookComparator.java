package com.prach.mashup.bookcomparator;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BookComparator extends Activity { 
	private int[] ProcessNumber = null;
    //private String[] ProcessName = null;
    private int current_pnum;
    private TextView tv;
    private String barcode;// = "9780071599887";
    private String AmazonTitle;
    private String AmazonPrice;
    private String[] KinoPrice;
    private String KinoTitle;
    
    public void debug(String msg){
		Log.d("com.prach.mashup.WAExtractor",msg);
	} 
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        tv = (TextView)findViewById(R.id.tview);
        ProcessNumber = getResources().getIntArray(R.array.process_number);
        //ProcessName = getResources().getStringArray(R.array.process_name);
        current_pnum = -1;	
    }
    
	/*private void showInterruptDialog(String process, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Mashup Execution Interrupted");
		builder.setMessage("Relaunch current process ("+process+") or Quit this program ("+R.string.app_name+")");
		builder.setPositiveButton("Relaunch",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// User clicked Yes so do some stuff 
					}
				});
		builder.setNegativeButton("Quit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						finish();
					}
				});
		builder.show();
	}*/
	
	 private void showDialog(String title, String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    @Override
    public void onResume(){
    	debug("onResume()");
    	super.onResume();
    	
    	Intent intent = null;
    	String URL = null;
    	String[] scripts = null;
    	
    	switch (current_pnum) {
    	case -1:
    		intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, ProcessNumber[++current_pnum]);	
    		break;
    	case 0:
    		intent = new Intent("com.prach.mashup.WAExtractor"); 
    		URL = "http://www.amazon.co.jp/?ie=UTF8&force-full-site=1";
    		scripts = new String[2];
    		scripts[0] = "var tagArray1 = document.getElementsByTagName('table');"+
	    		"var parentElement;"+
	    		"for(var i=0;i<tagArray1.length;i++){"+
	    		"    if(i>=3&&i<11&&tagArray1[i].id=='subDropdownTable'){"+
	    		"        parentElement = tagArray1[i];"+
	    		"        break;"+
	    		"    }"+
	    		"}"+
	    		"var BookSearch;"+
	    		"var tagArray2 = parentElement.getElementsByTagName('input');"+
	    		"var childElement;"+
	    		"for(var i=0;i<tagArray2.length;i++){"+
	    		"    if(i==0&&tagArray2[i].id=='twotabsearchtextbox'&&tagArray2[i].name=='field-keywords'&&tagArray2[i].className=='searchSelect'){"+
	    		"        childElement = tagArray2[i];"+
	    		"        childElement.value = '"+barcode+"';"+
	    		"        childElement.form.submit();"+
	    		"    }"+
	    		"}";
    		scripts[1] = "var tagArray1 = document.getElementsByTagName('div');"+
	    		"var parentElement;"+
	    		"for(var i=0;i<tagArray1.length;i++){"+
	    		"    if(i>=34&&i<44&&(tagArray1[i].className=='title'||tagArray1[i].className=='productTitle')){"+
	    		"        parentElement = tagArray1[i];"+
	    		"        break;"+
	    		"    }"+
	    		"}"+
	    		"var AmazonTitle;"+
	    		"var tagArray2 = parentElement.getElementsByTagName('a');"+
	    		"var childElement;"+
	    		"for(var i=0;i<tagArray2.length;i++){"+
	    		"    if(i==0){"+
	    		"        childElement = tagArray2[i];"+
	    		"        AmazonTitle = childElement.textContent;"+
	    		"    }"+
	    		"}"+
	    		"var tagArray1 = document.getElementsByTagName('div');"+
	    		"var parentElement;"+
	    		"for(var i=0;i<tagArray1.length;i++){"+
	    		"    if(i>=35&&i<45&&(tagArray1[i].className=='newPrice bold'||tagArray1[i].className=='bbPrice')){"+
	    		"        window.prach.info(i+':'+tagArray1[i].className);"+
	    		"        parentElement = tagArray1[i];"+
	    		"        break;"+
	    		"    }"+
	    		"}" +
	    		"window.prach.addOutput(AmazonTitle,'AmazonTitle');"+
	    		"var AmazonPrice;"+
	    		"var tagArray2 = parentElement.getElementsByTagName('span');"+
	    		"var childElement;"+
	    		"for(var i=0;i<tagArray2.length;i++){"+
	    		"    if(i==0){"+
	    		"        childElement = tagArray2[i];"+
	    		"        AmazonPrice = childElement.textContent;"+
	    		"    }"+
	    		"}"+
	    		"window.prach.addOutput(AmazonPrice,'AmazonPrice');" +
				"window.prach.setfinishstate('true');";
    		intent.putExtra("MODE", "EXTRACTION");
    		intent.putExtra("URL", URL);
			intent.putExtra("SCRIPTS", scripts);
	        startActivityForResult(intent, ProcessNumber[++current_pnum]);
			break;
    		
		case 1: //start : barcode reader
			intent = new Intent("com.prach.mashup.WAExtractor"); 
			URL = "http://bookweb.kinokuniya.co.jp/indexb.html";
			scripts = new String[2];
			scripts[0] = "var tagArray1 = document.getElementsByTagName('div');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=8&&i<16&&tagArray1[i].className=='search-box'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var BarcodeSearch;"+
			"var tagArray2 = parentElement.getElementsByTagName('input');"+
			"var childElement;"+
			"for(var i=0;i<tagArray2.length;i++){"+
			"    if(i==0&&tagArray2[i].name=='KEYWORD'&&tagArray2[i].className=='skey'){"+
			"        childElement = tagArray2[i];"+
			"        childElement.value = '"+barcode+"';"+
			//"        childElement.value = '9780071599887';"+
			"        childElement.form.submit();"+
			"    }"+
			"}";
			//----//----//----//----//----//----//----//----//----
			scripts[1] = "var tagArray1 = document.getElementsByTagName('span');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(tagArray1[i].className=='font16'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var KinoTitle;"+
			"var tagArray2 = parentElement.getElementsByTagName('strong');"+
			"var childElement;"+
			"for(var i=0;i<tagArray2.length;i++){"+
			"    if(i==0){"+
			"        childElement = tagArray2[i];"+
			"        KinoTitle = childElement.textContent;"+
			"    }"+
			"}"+
			"window.prach.addOutput(KinoTitle,'KinoTitle');"+
			"var tagArray1 = document.getElementsByTagName('div');"+
			"var parentElement;"+
			"for(var i=0;i<tagArray1.length;i++){"+
			"    if(i>=10&&i<18&&tagArray1[i].id=='container'){"+
			"        parentElement = tagArray1[i];"+
			"        break;"+
			"    }"+
			"}"+
			"var KinoPrice;"+
			"KinoPrice = new Array();"+
			"var tagArray2 = parentElement.getElementsByTagName('span');"+
			"for(var i=0,k=0;i<tagArray2.length;i++){"+
			"    if(tagArray2[i].className=='pro-price2'){"+
			"        KinoPrice[k++] = tagArray2[i].textContent;"+
			"    }"+
			"}"+
			"window.prach.addOutputArray(KinoPrice,'KinoPrice');"+
			"window.prach.setfinishstate('true');";
			intent.putExtra("URL", URL);
			intent.putExtra("SCRIPTS", scripts);
	        startActivityForResult(intent, ProcessNumber[++current_pnum]);
			break;
		case 2: //start : barcode reader
			current_pnum++;
			StringBuffer temp = new StringBuffer();
			temp.append("Barcode:"+barcode+"\n\n");
			temp.append("AmazonTitle:"+AmazonTitle+"\n");
			temp.append("AmazonPrice:"+AmazonPrice+"\n\n");
			temp.append("KinoTitle:"+KinoTitle+"\n");
			//String temp2 = KinoPrice.substring(0, KinoPrice.length()-1);
			//String[] kinoprices = temp2.split(":");
			
			temp.append("KinoPrice:"+KinoPrice[0]+","+KinoPrice[1]+"\n\n");
			
			int[] prices = new int[3];
			prices[0] = Integer.parseInt(AmazonPrice.substring(2, AmazonPrice.length()).replaceAll(",", ""));
			prices[1] = Integer.parseInt(KinoPrice[0].substring(1, KinoPrice[0].length()).replaceAll(",", ""));
			prices[2] = Integer.parseInt(KinoPrice[1].substring(1, KinoPrice[1].length()).replaceAll(",", ""));
			
			int index = 0;
			int value = prices[0];
			for (int i = 0; i < prices.length; i++) {
				if(prices[i]<value){
					value = prices[i];
					index = i;
				}
			}
			
			if(index>0)
				temp.append("Kinokuniya is CHEAPER ("+prices[index]+" yen)");
			else
				temp.append("Amazon is CHEAPER ("+prices[index]+" yen)");
			
			tv.setText(temp);
			break;
		default:
			break;
		} 
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		debug("onActivityResult()");
		if (requestCode == ProcessNumber[0]) {
			if (resultCode == RESULT_OK) {
				String output = intent.getStringExtra("SCAN_RESULT");
				barcode = output;
			} else if (resultCode == RESULT_CANCELED) {
				showDialog("Barcode canceled", "some msg");
			}
			killProcess("com.google.zxing.client.android");
		} else if (requestCode == ProcessNumber[1]) {
			if (resultCode == RESULT_OK) {
				// String lat = intent.getStringExtra("LAT");
				// String lng = intent.getStringExtra("LNG");
				String[] outputs = intent.getStringArrayExtra("OUTPUTS");
				// String provider = intent.getStringExtra("PROVIDER");
				//showDialog("WAE Data", outputs[0]+"\n"+outputs[1]);
				AmazonTitle = outputs[0];
				AmazonPrice = outputs[1];
			} else if (resultCode == RESULT_CANCELED) {
				showDialog("WAE canceled", "some msg");
			}
		} else if (requestCode == ProcessNumber[2]) {
			if (resultCode == RESULT_OK) {
				// String lat = intent.getStringExtra("LAT");
				// String lng = intent.getStringExtra("LNG");
				String[] outputs = intent.getStringArrayExtra("OUTPUTS");
				for (int i = 0; i < outputs.length; i++) {
					debug("outputs:["+i+"]="+outputs[i]);
				}
				// String provider = intent.getStringExtra("PROVIDER");
				//showDialog("WAE Data", outputs[0]+"\n"+outputs[1]);
				KinoTitle = outputs[0];
				KinoPrice = new String[] {outputs[1],outputs[2]};
			} else if (resultCode == RESULT_CANCELED) {
				showDialog("WAE canceled", "some msg");
			}
		}
	}
	
	public void killProcess(String pname){
		ActivityManager actManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> processList = actManager.getRunningAppProcesses();
		for (int i = 0; i < processList.size(); i++) {
			debug("processName:"+processList.get(i).processName);
			if(processList.get(i).processName.contains(pname)){
				android.os.Process.killProcess(processList.get(i).pid);
			}
		}
	}
}
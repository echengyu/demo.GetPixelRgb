package demo.GetPixelRgb;

import android.os.Bundle;
import android.util.Log;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GetPixelRgb extends Activity {
	
	private static final int CAMERA_REQUEST = 1888;

	private ImageView imgSource1, imgSource2;
	private TextView colorTextView[];
	private int colorTextViewId [] = {R.id.colorRedTextView, R.id.colorGreenTextView2, R.id.colorBlueTextView};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		String rgbString[] = {"R: ", "G: ", "B: "};
		colorTextView = new TextView[colorTextViewId.length];
		for(int i=0; i<colorTextViewId.length; i++){
			colorTextView[i] = (TextView)findViewById(colorTextViewId[i]);
			colorTextView[i].setText(rgbString[i]);
		}

		imgSource1 = (ImageView)findViewById(R.id.source1);
		imgSource2 = (ImageView)findViewById(R.id.source2);

		imgSource1.setOnTouchListener(imgSourceOnTouchListener);
		imgSource1.setImageResource(R.drawable.peoples);
	}
	
	// 啟動相機按鈕事件
	public void cameraOnClick(View view) {
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	}	
	
	// 接收相機拍攝的影像
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imgSource1.setImageBitmap(photo);
        }  
    }
	
	// 點擊觸螢處發事件
	private OnTouchListener imgSourceOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			
		    switch (event.getAction()) {
		    case MotionEvent.ACTION_DOWN:
		    	// 當接觸螢幕未放開時		    	
		        break;		   
		    case MotionEvent.ACTION_UP:
		    	// 當接觸螢幕已放開時
		    	view.performClick();
		        break;
		    default:
		    	// 
		    	float eventX = event.getX();
				float eventY = event.getY();
				float[] eventXY = new float[] {eventX, eventY};

				Matrix invertMatrix = new Matrix();
				((ImageView)view).getImageMatrix().invert(invertMatrix);

				invertMatrix.mapPoints(eventXY);
				int x = Integer.valueOf((int)eventXY[0]);
				int y = Integer.valueOf((int)eventXY[1]);
				
				Log.d("debug", 
						"Touched position: \n"
					    + String.valueOf(eventX) + " / "
					    + String.valueOf(eventY));

				Log.d("debug", 
					    "Touched position: \n"
					    + String.valueOf(x) + " / "
					    + String.valueOf(y));

				Drawable imgDrawable = ((ImageView)view).getDrawable();
				Bitmap bitmap = ((BitmapDrawable)imgDrawable).getBitmap();

				Log.d("debug", 
					    "Drawable size: \n"
					    + String.valueOf(bitmap.getWidth()) + " / "
					    + String.valueOf(bitmap.getHeight()));

				//Limit x, y range within bitmap
				if(x < 0) {
					x = 0;
				} else if(x > bitmap.getWidth()-1) {
					x = bitmap.getWidth()-1;
				}

				if(y < 0) {
					y = 0;
				} else if(y > bitmap.getHeight()-1) {
					y = bitmap.getHeight()-1;
				}

				int touchedRGB = bitmap.getPixel(x, y);
				
				int red 	= (touchedRGB>>16) & 0x0ff;
				int green	= (touchedRGB>>8)  & 0x0ff;
				int blue	= (touchedRGB)     & 0x0ff;
				
				Log.d("debug",
						"R: " + red + ", " +
						"G: " + green + ", " +
						"B: " + blue);
				
				colorTextView[0].setText("R: " + red);
				colorTextView[1].setText("G: " + green);
				colorTextView[2].setText("B: " + blue);
				
				imgSource2.setBackgroundColor(touchedRGB);
//				colorRGB.setText("Touched color: \n" + "#" + Integer.toHexString(touchedRGB));
//				colorRGB.setText("r" + red + " g" + green + " b" + blue);
//				colorRGB.setTextColor(touchedRGB);
				
		        break;
		    }
		    return true;
		}
	};

}
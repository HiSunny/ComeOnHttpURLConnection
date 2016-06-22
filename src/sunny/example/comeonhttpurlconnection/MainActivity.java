package sunny.example.comeonhttpurlconnection;
//用HTTP协议访问网络java.net.HttpURLConnection
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.view.View.OnClickListener;
import android.view.View;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends ActionBarActivity implements OnClickListener{

	public static final int SHOW_RESPONSE = 0;
	private Button requestBtn;
	private TextView responseTextView;
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SHOW_RESPONSE:
				String response = (String)msg.obj;
				responseTextView.setText(response);
				break;
			default:
				break;
			
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		requestBtn = (Button)findViewById(R.id.requestBtn);
		responseTextView = (TextView)findViewById(R.id.responseText);
		requestBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.requestBtn){
			sendRequestWithHttpURLConnection();
		}
	}

	private void sendRequestWithHttpURLConnection(){
		//Android4.0以后的所有网络操作都不能在主线程
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try{
					URL url = new URL("http://www.baidu.com");
			//public URLConnection openConnection () 
			//Returns a new connection to the resource referred to by this URL.
					//向url发出HTTP请求的HttpURLConnection实例
			//Obtain a new HttpURLConnection by calling URL.openConnection() and casting the result to HttpURLConnection
				    connection =  (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					//发生连接（在其之前设置好参数）
					InputStream in = connection.getInputStream();//同url.openStream()；可以不用connection
					//java.io.BufferedReader.BufferedReader(Reader in)
					//Constructs a new BufferedReader, providing in with a buffer of 8192 characters.
					//public class BufferedReader extends Reader
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder responseStringBuilder = new StringBuilder();
					String lineStr ;
					while((lineStr = reader.readLine())!= null){
						responseStringBuilder.append(lineStr);
					}
					Message message = new Message();
					message.what = SHOW_RESPONSE;
					message.obj = responseStringBuilder.toString();
					handler.sendMessage(message);
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(connection != null){
					//Releases this connection so that its resources may be either reused or closed.
						connection.disconnect();
					}
				}
			}
			
		}).start();
	}
	
}

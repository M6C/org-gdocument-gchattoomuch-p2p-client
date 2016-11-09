package org.gdocument.gchattoomuch.p2p.client.activity;

import org.gdocument.gchattoomuch.lib.log.Logger;
import org.gdocument.gchattoomuch.lib.parser.SmsParser.MSG_TYPE;
import org.gdocument.gchattoomuch.p2p.client.R;
import org.gdocument.gchattoomuch.p2p.client.business.MessageTypeBusiness;
import org.gdocument.gchattoomuch.p2p.client.service.OutgoingSMSService;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cameleon.common.android.factory.FactoryDialog;
import com.cameleon.common.android.inotifier.INotifierMessage;

public class MainActivity extends Activity {

	private final static String TAG = MainActivity.class.getName();
	private TextView tvMessage;
	private Handler handler = new Handler();
	private ProgressBar pbProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tvMessage = (TextView) findViewById(R.id.tv_message);
		pbProgress = (ProgressBar) findViewById(R.id.pb_progress);

		Context context = getApplicationContext();
		context.startService(new Intent(context.getApplicationContext(), OutgoingSMSService.class));
	}

	@Override
	protected void onResume() {
		super.onResume();
		OnClickListener onClickOkListener = new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				tvMessage.setText("");

				new AsyncTask<Object, Void, Void>() {

					@Override
					protected Void doInBackground(Object... params) {
						new MessageTypeBusiness(MainActivity.this, new Notifier()).processMessage(handler, MSG_TYPE.SEND_DB, "068346958", null);
						return null;
					}
				}.execute();
			}
		};
    	OnClickListener onClickCancelListener = new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if ("".equals(tvMessage.getText().toString())) {
					finish();
				}
			}
		};
    	FactoryDialog.getInstance().buildOkCancelDialog(this, onClickOkListener, onClickCancelListener, R.string.app_name, R.string.text_start_export).show();
	}

	@SuppressWarnings("unused")
	private void logMe(String msg) {
		Logger.logMe(TAG, msg);
	}

	@SuppressWarnings("unused")
	private static void logMe(Exception ex) {
		Logger.logMe(TAG, ex);
    }

	private class Notifier implements INotifierMessage {

		@Override
		public void notifyError(Exception ex) {
			notifyMessage(ex.getMessage());
		}

		@Override
		public void notifyMessage(final String msg) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					tvMessage.append(msg + "\r\n");
				}
			});
		}
		
	}
}
package org.gdocument.gchattoomuch.p2p.client;

import org.gdocument.gchattoomuch.p2p.client.service.OutgoingSMSService;

import android.app.Application;
import android.content.Intent;

public class P2PApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		startService(new Intent(getApplicationContext(), OutgoingSMSService.class));
	}
}

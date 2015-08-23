package zwendelaar.client.ui;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.NotificationMole;
import com.google.gwt.user.client.ui.RootPanel;

public class NotificationManager extends NotificationMole {
	
	private static NotificationManager nm;
	
	private NotificationManager() {
		super();
	    this.setAnimationDuration(550);
	    this.setWidth("100%");
	    this.setHeight("50px");
	    RootPanel.get("notification").add(this);
	}
	
	public static NotificationManager get() {
		if(nm == null) nm = new NotificationManager();
		return nm;
	}
	
	public void createNotification(String text) {
		this.setMessage(text);
		run(2500);
	}
	
	public void createNotification(String text, int time) {
		this.setMessage(text);
		run(time);
	}
	
	public void run(int showTime) {
		this.show();
	    Timer t = new Timer() {
			@Override
			public void run() {
				nm.hide();				
			}
		};
		t.schedule(showTime);
	}
	
	
}

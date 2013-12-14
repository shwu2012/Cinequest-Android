package edu.sjsu.cinequest;

import java.util.TimeZone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import edu.sjsu.cinequest.android.AndroidPlatform;
import edu.sjsu.cinequest.comm.Callback;
import edu.sjsu.cinequest.comm.Platform;
import edu.sjsu.cinequest.comm.QueryManager;

public class SplashScreenActivity extends Activity {
	private LoadData loadData = null;
	private View mLoginStatusView;
	private static String calendarName="Cinequest Calendar";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_splash_screen);
		mLoginStatusView = findViewById(R.id.login_status);						
		Platform.setInstance(new AndroidPlatform(getApplicationContext()));		
		loadData = new LoadData();
		loadData.execute((Void) null);
		showProgress(true);
		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mLoginStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});
			
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);			
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class LoadData extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {						
			//Check whether Cinequest calendar exists in Device or Not. If not exists, create Cinequest Calendar in device.
			
			/*String[] proj = new String[]{"_id", "calendar_displayName"};
			String calSelection = "(calendar_displayName= ?) "; 				
			String[] calSelectionArgs = new String[] {calendarName}; 
			Uri event = Uri.parse("content://com.android.calendar/calendars");        

			Cursor l_managedCursor = getContentResolver().query(event, proj, calSelection, calSelectionArgs, null );
			if (l_managedCursor.getCount()<=0) {                                                    

				ContentValues l_event = new ContentValues();
				l_event.put("account_name", "Cinequest");
				l_event.put("account_type", "LOCAL");                  
				l_event.put("name", calendarName);
				//l_event.put("displayName", "Cinequest Calendar");
				//l_event.put("color", 0xffff0000);
				//l_event.put("access_level",  700);
				//l_event.put("timezone", TimeZone.getDefault().getID());
				l_event.put("calendar_displayName", calendarName);
				l_event.put("calendar_color", 0xffff0000);
				l_event.put("calendar_access_level",  700);
				l_event.put("calendar_timezone", TimeZone.getDefault().getID());
				l_event.put("ownerAccount", "owner");
				l_event.put("visible", 1);
				Uri.Builder builder = event.buildUpon();
				builder.appendQueryParameter("account_name", "Cinequest");
				builder.appendQueryParameter("account_type", "LOCAL");                
				builder.appendQueryParameter( "caller_is_syncadapter", "true");
				Uri uri = getContentResolver().insert(builder.build(), l_event); 
				l_managedCursor.close();
				l_managedCursor=null;
			}
*/
					
			//Create QueryManager Object and assign it to HomeActivity class queryManager variable. 
			HomeActivity.setQueryManager(new QueryManager());				
				//Load News Feed, Festival & Venue Feed
				HomeActivity.getQueryManager().LoadFestival(new Callback(){
					@Override
					public void invoke(Object result) {								
						showProgress(false);
						Intent i = new Intent(SplashScreenActivity.this,MainTab.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						finish();						
					}
					@Override public void starting() {}			
					@Override public void failure(Throwable t) {
						Platform.getInstance().log(t);				
					}        	
				});			
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {						
			if (success) {
				
			} 
		}

		@Override
		protected void onCancelled() {
			loadData = null;
			showProgress(false);
		}
	}
}
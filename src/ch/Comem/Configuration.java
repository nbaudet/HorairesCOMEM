package ch.Comem;

import ch.Comem.HorairesCOMEM.R;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Configuration extends Activity {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	/**
	 * Ajoute les boutons dans l'actionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.config_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * G�re les clics dans l'actionBar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_about:
	            //openSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Fonction appel�e lorsque l'activit� est cr�e pour la premi�re fois.
	 * C'est ici qu'il faut g�n�rer les petits objets que l'on r�g�n�re r�guli�rement
	 * comme les listes de profs, classe ou modules.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// D�finition de la vue � utiliser
		setContentView(R.layout.configuration);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null) {
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			Toast.makeText(getApplicationContext(), "Widget ID : " + appWidgetId, Toast.LENGTH_LONG).show();
		}
		/*else {
			Toast.makeText(getApplicationContext(), "Pas d'id !", Toast.LENGTH_LONG).show();
		}*/
		
		// Met le r�sultat � annul� si l'utilisateur quitte l'activit� sans valider les modifs de la config
		setResult(RESULT_CANCELED, null);
		
		// TODO configuration de l'interface utilisateur
		
		
		Button ok_btn = (Button) findViewById(R.id.ok_button);
		ok_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				completedConfiguration();
			}
		});
		
		Button cancel_btn = (Button) findViewById(R.id.cancel_button);
		cancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
			
	}
	
	private void completedConfiguration() {
		// Sauve la config pour appWidgetId
		// Pr�vient le gestionnaire de widgets que la config est termin�e
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, result);
		finish();
	}

}

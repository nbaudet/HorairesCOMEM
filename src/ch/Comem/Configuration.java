package ch.Comem;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import ch.Comem.HorairesCOMEM.R;

import com.Wsdl2Code.WebServices.Service.IWsdl2CodeEvents;
import com.Wsdl2Code.WebServices.Service.Service;

public class Configuration extends Activity implements IWsdl2CodeEvents {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	/**
	 * Fonction appel�e lorsque l'activit� est cr��e pour la premi�re fois.
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
			Toast.makeText(getApplicationContext(), "Widget ID : " + appWidgetId, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Pas d'id en extra !", Toast.LENGTH_LONG).show();
		}
		
		// Met le r�sultat � annul� si l'utilisateur quitte l'activit� sans valider les modifs de la config
		setResult(RESULT_CANCELED, null);
		
		// TODO configuration de l'interface utilisateur
		setClasses();
		
		// Gestion du clic sur les bouton
		Button ok_btn = (Button) findViewById(R.id.ok_button);
		ok_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				completedConfiguration(appWidgetId);
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
	
	/**
	 * Remplit le s�lecteur de classes en faisant appel au webservice
	 */
	private void setClasses() {
		//callWebService();
	}
	
	
	/**
	 * Actions � ex�cuter quand la config est termin�e
	 * @param appWidgetId
	 */
	private void completedConfiguration(int appWidgetId) {
		// Sauve la config pour appWidgetId
		// Pr�vient le gestionnaire de widgets que la config est termin�e et lui passe l'id du widget � modifier
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, result);
		finish();
	}
	
	/*********************************************************************
	 * Gestion des t�ches asynchrones pour le webservice
	 ********************************************************************/
	/**
	 * Lance une t�che asynchrone pour r�cup�rer les infos du webservice.
	 */
	public void callWebService(){
		
		Service s = new Service(this);
		
		try {
			s.GetScheduleInfoAsync();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void Wsdl2CodeStartedRequest() {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeStartedRequest");
	}
	
	/**
	 * Fonction ex�cut�e lorsque l'activit� re�oit le retour du webservice.
	 */
	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished");
		Log.e(HoraireWidgetProvider.TAG, methodName);
		
	}
	
	/**
	 * Fonction ex�cut�e lorsque le webservice a rencontr� une erreur.
	 * @param ex
	 */
	@Override
	public void Wsdl2CodeFinishedWithException(Exception ex) {
		Log.e(HoraireWidgetProvider.TAG , "Wsdl2CodeFinishedWithException");
		Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " n'a pas r�ussi � contacter le webservice.", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void Wsdl2CodeEndedRequest() {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeEndedRequest");
	}
	
	/*********************************************************************
	 * Gestion de l'ActionBar
	 ********************************************************************/
	/**
	 * Ajoute les boutons dans l'ActionBar
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
	    // Action en fonction du clic
	    switch (item.getItemId()) {
	        case R.id.action_about:
	        	// On cr�e directement le dialogue "A propos", mais on pourrait utiliser un fragment d�rivant de DialogFragment
	        	TextView tv = new TextView(this);
	        	tv.setText(R.string.dialog_about);
	        	Linkify.addLinks(tv, Linkify.WEB_URLS); // Permet d'avoir un lien cliquable vers le site
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        	builder.setView(tv)
	        	       .setTitle(R.string.title_about)
	        	       .setIcon(R.drawable.ic_action_about)
	        	       .setPositiveButton(R.string.close_button, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {}
				       });
	        	AlertDialog dialog = builder.create();
	        	dialog.show();
	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}

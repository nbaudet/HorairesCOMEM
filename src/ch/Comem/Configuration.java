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
	 * Fonction appelée lorsque l'activité est créée pour la première fois.
	 * C'est ici qu'il faut générer les petits objets que l'on régénère régulièrement
	 * comme les listes de profs, classe ou modules.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Définition de la vue à utiliser
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
		
		// Met le résultat à annulé si l'utilisateur quitte l'activité sans valider les modifs de la config
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
	 * Remplit le sélecteur de classes en faisant appel au webservice
	 */
	private void setClasses() {
		//callWebService();
	}
	
	
	/**
	 * Actions à exécuter quand la config est terminée
	 * @param appWidgetId
	 */
	private void completedConfiguration(int appWidgetId) {
		// Sauve la config pour appWidgetId
		// Prévient le gestionnaire de widgets que la config est terminée et lui passe l'id du widget à modifier
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, result);
		finish();
	}
	
	/*********************************************************************
	 * Gestion des tâches asynchrones pour le webservice
	 ********************************************************************/
	/**
	 * Lance une tâche asynchrone pour récupérer les infos du webservice.
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
	 * Fonction exécutée lorsque l'activité reçoit le retour du webservice.
	 */
	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished");
		Log.e(HoraireWidgetProvider.TAG, methodName);
		
	}
	
	/**
	 * Fonction exécutée lorsque le webservice a rencontré une erreur.
	 * @param ex
	 */
	@Override
	public void Wsdl2CodeFinishedWithException(Exception ex) {
		Log.e(HoraireWidgetProvider.TAG , "Wsdl2CodeFinishedWithException");
		Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " n'a pas réussi à contacter le webservice.", Toast.LENGTH_SHORT).show();
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
	 * Gère les clics dans l'actionBar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Action en fonction du clic
	    switch (item.getItemId()) {
	        case R.id.action_about:
	        	// On crée directement le dialogue "A propos", mais on pourrait utiliser un fragment dérivant de DialogFragment
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

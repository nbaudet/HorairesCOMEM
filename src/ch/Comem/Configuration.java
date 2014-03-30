package ch.Comem;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ch.Comem.HorairesCOMEM.R;

import com.Wsdl2Code.WebServices.Service.IWsdl2CodeEvents;
import com.Wsdl2Code.WebServices.Service.RequestEntity;
import com.Wsdl2Code.WebServices.Service.Service;

public class Configuration extends Activity implements IWsdl2CodeEvents {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	private Service service;
	private RequestEntity req;
	private ScheduleFactory sf = null;
	
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
		
		// Met le r�sultat � annul� si l'utilisateur quitte l'activit� sans valider les modifs de la config
		setResult(RESULT_CANCELED, null);
		
		// Attaque du webservice pour remplir les champs
		if(this.sf == null){
			Toast.makeText(getApplicationContext(), R.string.wait, Toast.LENGTH_SHORT).show();
			new AsyncTask<Void, Void, Void>() {
		    	@Override
		    	protected Void doInBackground(Void... params) {
		    		callWebService();
					return null;
		    	}
			}.execute();
		}
		else{
			Toast.makeText(getApplicationContext(), "SF d�j� sett�.", Toast.LENGTH_SHORT).show();
		}
		
		// R�cup�ration de l'id du widget
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null) {
			this.appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			//Toast.makeText(getApplicationContext(), "Widget ID : " + appWidgetId, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Pas d'id en extra !", Toast.LENGTH_LONG).show();
		}
		
		// TODO configuration de l'interface utilisateur
		//setClasses();
		
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
	 * Ces deux fonctions permettent de r�cup�rer les valeurs s�lectionn�es pr�c�demment
	 * quand on quitte ou qu'on tourne l'�cran, et les r�-affiche au lieu de tout r�initialiser.
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// TODO Enregistrer les valeurs
		//P. ex. : savedInstanceState.putString("MyString", "Welcome back to Android");
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// TODO R�cup�rer les valeurs
		//P. ex. : String myString = savedInstanceState.getString("MyString");
	}
	
	/*********************************************************************
	 * Accesseurs aux objets du webservice
	 ********************************************************************/
	public void setScheduleInfo(String methodName, Object Data) {
		
		this.sf = new ScheduleFactory(Data);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner_class);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getClasses());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    spinner = (Spinner) findViewById(R.id.spinner_teacher);
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getTeachers());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    spinner = (Spinner) findViewById(R.id.spinner_course);
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getCourses());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    // Couleurs
	    spinner = (Spinner) findViewById(R.id.spinner_color);
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_color, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    // Nombre de jours
	    spinner = (Spinner) findViewById(R.id.spinner_number_of_days);
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_number_of_days, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    // TODO R�cup�rer les pr�f�rences de ce widget et les afficher dans les spinners
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
		
		//TODO Ajouter les pr�f�rences au widget et chercher les horaires voulus
		
		
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
		
		this.service = new Service(this);
		
		try {
			this.service.GetScheduleInfoAsync();
			
		} catch (Exception e) {
			displayError();
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void Wsdl2CodeStartedRequest() {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeStartedRequest");
		//Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " est en train d'attaquer le webservice.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Fonction ex�cut�e lorsque l'activit� re�oit le retour du webservice.
	 */
	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished");
		Log.e(HoraireWidgetProvider.TAG, methodName);
		setScheduleInfo(methodName, Data);
		//Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " a re�u les infos du webservice.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Fonction ex�cut�e lorsque le webservice a rencontr� une erreur.
	 * @param ex
	 */
	@Override
	public void Wsdl2CodeFinishedWithException(Exception ex) {
		Log.e(HoraireWidgetProvider.TAG , "Wsdl2CodeFinishedWithException");
		//Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void Wsdl2CodeEndedRequest() {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeEndedRequest");
	}
	
	/**
	 * Fonction s'appelant lorsque l'attaque du webservice a �chou�
	 */
	public void displayError(){
		Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
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

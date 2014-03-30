package ch.Comem;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	
	// Stockage des préférences
	SharedPreferences prefs;
	public static final String PREF_CLASS   = "PREF_CLASS";
	public static final String PREF_TEACHER = "PREF_TEACHER";
	public static final String PREF_COURSE  = "PREF_COURSE";
	public static final String PREF_COLOR   = "PREF_COLOR";
	public static final String PREF_TIME    = "PREF_TIME";
	
	private Spinner spinnerClasses;
	private Spinner spinnerCourses;
	private Spinner spinnerTeachers;
	private Spinner spinnerColors;
	private Spinner spinnerNumDays;
	
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
		
		// Met le résultat à annulé si l'utilisateur quitte l'activité sans valider les modifs de la config
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
			Toast.makeText(getApplicationContext(), "SF déjà setté.", Toast.LENGTH_SHORT).show();
		}
		
		// Récupération de l'id du widget
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null) {
			this.appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			//Toast.makeText(getApplicationContext(), "Widget ID : " + appWidgetId, Toast.LENGTH_SHORT).show();
		}
		else {
			Toast.makeText(getApplicationContext(), "Pas d'id en extra !", Toast.LENGTH_LONG).show();
		}
		
		// Désérialisation des vues et récupération des préférences (si elles existent)
		spinnerClasses  = (Spinner) findViewById(R.id.spinner_class);
		spinnerCourses  = (Spinner) findViewById(R.id.spinner_course);
		spinnerTeachers = (Spinner) findViewById(R.id.spinner_teacher);
		spinnerColors   = (Spinner) findViewById(R.id.spinner_color);
		spinnerNumDays  = (Spinner) findViewById(R.id.spinner_number_of_days);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		
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
	 * Récupération des préférences, et affichage de celles-ci dans les spinners.
	 */
	@SuppressWarnings("unchecked")
	private void updateUIFromPreferences(){
		ArrayAdapter<String> aa;
		int pos;
		Context context = getApplicationContext();
		
		//String selectedClass = prefs.getString(PREF_CLASS, "Classe");
		String selectedClass = getPref(context, PREF_CLASS);
		/*String selectedCourse = prefs.getString(PREF_COURSE, "Cours");
		String selectedTeacher = prefs.getString(PREF_TEACHER, "Intervenants");
		String selectedColor = prefs.getString(PREF_COLOR, "Bleu");
		String selectedNumDays = prefs.getString(PREF_TIME, "1 semaine");*/
		String selectedCourse = getPref(context, PREF_COURSE);
		String selectedTeacher = getPref(context, PREF_TEACHER);
		String selectedColor = getPref(context, PREF_COLOR);
		String selectedNumDays = getPref(context, PREF_TIME);
		
		// On set les préférences dans les spinners
		aa = (ArrayAdapter<String>) this.spinnerClasses.getAdapter();
		pos = aa.getPosition(selectedClass);
		spinnerClasses.setSelection(pos);
		
		aa = (ArrayAdapter<String>) this.spinnerCourses.getAdapter();
		pos = aa.getPosition(selectedCourse);
		spinnerCourses.setSelection(pos);
		
		aa = (ArrayAdapter<String>) this.spinnerTeachers.getAdapter();
		pos = aa.getPosition(selectedTeacher);
		spinnerTeachers.setSelection(pos);
		
		aa = (ArrayAdapter<String>) this.spinnerColors.getAdapter();
		pos = aa.getPosition(selectedColor);
		spinnerColors.setSelection(pos);
		
		aa = (ArrayAdapter<String>) this.spinnerNumDays.getAdapter();
		pos = aa.getPosition(selectedNumDays);
		spinnerNumDays.setSelection(pos);
	}
	
	/**
	 * Ces deux fonctions permettent de récupérer les valeurs sélectionnées précédemment
	 * quand on quitte ou qu'on tourne l'écran, et les ré-affiche au lieu de tout réinitialiser.
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
		// TODO Récupérer les valeurs
		//P. ex. : String myString = savedInstanceState.getString("MyString");
	}
	
	/*********************************************************************
	 * Accesseurs aux objets du webservice
	 ********************************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setScheduleInfo(String methodName, Object Data) {
		
		this.sf = new ScheduleFactory(Data);
		
		// Classes
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getClasses());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerClasses.setAdapter(adapter);
	    
	    // Intervenants
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getTeachers());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerTeachers.setAdapter(adapter);
	    
	    // Cours
		adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sf.getCourses());
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerCourses.setAdapter(adapter);
	    
	    // Couleurs
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_color, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerColors.setAdapter(adapter);
	    
	    // Nombre de jours
		adapter = ArrayAdapter.createFromResource(this, R.array.spinner_number_of_days, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    this.spinnerNumDays.setAdapter(adapter);
	}
	
	/**
	 * Permet de récupérer les préférences de chaque widget.
	 * @param context Le context de l'appli
	 * @param pref La préférence à récupérer pour ce widget
	 * @return La préférence paramétrée pour ce widget
	 */
	public String getPref(Context context, Object pref) {
	     SharedPreferences sharedPreferences = context.getSharedPreferences(
	            pref + String.valueOf(appWidgetId),
	            Context.MODE_PRIVATE);
	    return sharedPreferences.getString(
	            pref + String.valueOf(appWidgetId), null);
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
		Context context = getApplicationContext();
		
		String selectedClass   = this.spinnerClasses.getSelectedItem().toString();
		String selectedCourse  = this.spinnerCourses.getSelectedItem().toString();
		String selectedTeacher = this.spinnerTeachers.getSelectedItem().toString();
		String selectedColor   = this.spinnerColors.getSelectedItem().toString();
		String selectedNumDays = this.spinnerNumDays.getSelectedItem().toString();
		
		Editor edit = context.getSharedPreferences(PREF_CLASS + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();
		edit.putString(PREF_CLASS + String.valueOf(appWidgetId), selectedClass);
		edit.commit();
		
		edit = context.getSharedPreferences(PREF_COURSE + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();
		edit.putString(PREF_COURSE + String.valueOf(appWidgetId), selectedCourse);
		edit.commit();
		
		edit = context.getSharedPreferences(PREF_TEACHER + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();
		edit.putString(PREF_TEACHER + String.valueOf(appWidgetId), selectedTeacher);
		edit.commit();
		
		edit = context.getSharedPreferences(PREF_COLOR + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();
		edit.putString(PREF_COLOR + String.valueOf(appWidgetId), selectedColor);
		edit.commit();
		
		edit = context.getSharedPreferences(PREF_TIME + String.valueOf(appWidgetId), Context.MODE_PRIVATE).edit();
		edit.putString(PREF_TIME + String.valueOf(appWidgetId), selectedNumDays);
		edit.commit();
		
		//TODO spécifier le widget à updater avec ces préférences
		
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
	 * Fonction exécutée lorsque l'activité reçoit le retour du webservice.
	 */
	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		Log.e(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished");
		Log.e(HoraireWidgetProvider.TAG, methodName);
		setScheduleInfo(methodName, Data);
		updateUIFromPreferences();
		//Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " a reçu les infos du webservice.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Fonction exécutée lorsque le webservice a rencontré une erreur.
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
	 * Fonction s'appelant lorsque l'attaque du webservice a échoué
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

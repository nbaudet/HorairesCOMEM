package ch.Comem;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import ch.Comem.HorairesCOMEM.R;

import com.Wsdl2Code.WebServices.Service.IWsdl2CodeEvents;
import com.Wsdl2Code.WebServices.Service.RequestEntity;
import com.Wsdl2Code.WebServices.Service.Service;
import com.Wsdl2Code.WebServices.Service.VectorScheduleEntity;

/**
 * La classe qui fournit le widget.
 */
public class HoraireWidgetProvider extends AppWidgetProvider implements IWsdl2CodeEvents {
    
	public static String TAG = "HoraireWidgetProvider";
	public static String EXTRA_APPWIDGET_ID;
	public Service service;
	public ScheduleInfoFactory sf;
	
	private Context context; 
	
	// Stockage des préférences
	public static final String PREF_CLASS   = "PREF_CLASS";
	public static final String PREF_TEACHER = "PREF_TEACHER";
	public static final String PREF_COURSE  = "PREF_COURSE";
	public static final String PREF_COLOR   = "PREF_COLOR";
	public static final String PREF_TIME    = "PREF_TIME";
	
	public static final String DEFAULT_CLASS = "Classes";
	public static final String DEFAULT_COURSE = "Cours";
	public static final String DEFAULT_TEACHER = "Intervenants";
	public static final String DEFAULT_COLOR = "Bleu";
	public static final String DEFAULT_TIME = "1 semaine";
	
    /**
     * S'exécute lorsque le widget est activé (cf. cycle de vie des applications android),
     * comme par exemple quand le service va chercher les nouveaux horaires. On doit donc
     * écouter pour savoir quand mettre à jour les nouveaux horaires.
     */
    @Override
    public void onEnabled(Context context) {
        // Register for external updates to the data to trigger an update of the widget.  When using
        // content providers, the data is often updated via a background service, or in response to
        // user interaction in the main app.  To ensure that the widget always reflects the current
        // state of the data, we must listen for changes and update ourselves accordingly.
        /*final ContentResolver r = context.getContentResolver();
		if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, HoraireWidgetProvider.class);
            sDataObserver = new HoraireDataProviderObserver(mgr, cn, sWorkerQueue);
            r.registerContentObserver(HoraireDataProvider.CONTENT_URI, true, sDataObserver);
        }*/
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();
        
        this.context = ctx;
        
        // Réception du clic sur le bouton OK de l'activité de configuration
        if(action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
        	Toast.makeText(ctx, "HoWi:onReceive", Toast.LENGTH_SHORT).show();
        }
        
    	super.onReceive(ctx, intent);
    }

    /**
     * Ajoute les horaires reçus à l'interface
     * @param context
     * @param courses
     * @param largeLayout
     * @return
     */
    private RemoteViews buildLayout(Context context, int appWdigetId, ScheduleFactory courses, boolean largeLayout) {
    	Toast.makeText(context, "HoWi:buildLayout", Toast.LENGTH_SHORT).show();
    	Log.i(HoraireWidgetProvider.TAG, courses.toString());
    	
    	RemoteViews rv = new RemoteViews(context.getPackageName(), R.id.courses_listview);
    	ArrayList<String> horaires = courses.getHoraires();
    	
    	for(String se : horaires) {
    		rv.setTextViewText(appWdigetId, se);
    	}
    	
    	return rv;
    }
    
    /**
     * Construit l'interface à jour du widget dans une vue distante et la renvoie.
     * @param context Le contexte de l'application
     * @param appWidgetId L'id du widget
     * @param largeLayout Si on est sur un écran large ou pas (pas implémenté)  
     * @return les RemoteViews qu'on pourra placer dans notre widget
     */
    private RemoteViews buildLayout(Context context, int appWidgetId, boolean largeLayout) {
    	
    	Toast.makeText(context, "HoWi:buildLayout", Toast.LENGTH_SHORT).show();
    	
        /*RemoteViews rv;
        if (largeLayout) {
            // Specify the service to provide data for the collection widget.  Note that we need to
            // embed the appWidgetId via the data otherwise it will be ignored.
            final Intent intent = new Intent(context, HoraireWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout); // Déclare le layout à utiliser pour la RemoteView
            rv.setRemoteAdapter(appWidgetId, R.id.horaire_list, intent);

            // Set the empty view to be displayed if the collection is empty.  It must be a sibling
            // view of the collection view.
            rv.setEmptyView(R.id.horaire_list, R.id.empty_view);

            // Bind a click listener template for the contents of the weather list.  Note that we
            // need to update the intent's data if we set an extra, since the extras will be
            // ignored otherwise.
            final Intent onClickIntent = new Intent(context, HoraireWidgetProvider.class);
            onClickIntent.setAction(HoraireWidgetProvider.CLICK_ACTION);
            onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0,
                    onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.horaire_list, onClickPendingIntent);

            // Bind the click intent for the refresh button on the widget
            final Intent refreshIntent = new Intent(context, HoraireWidgetProvider.class);
            refreshIntent.setAction(HoraireWidgetProvider.REFRESH_ACTION);
            final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0,
                    refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.refresh, refreshPendingIntent);

            // Restore the minimal header
            rv.setTextViewText(R.id.city_name, context.getString(R.string.city_name));
        } else {
            rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout_small);

            // Update the header to reflect the weather for "today"
            Cursor c = context.getContentResolver().query(HoraireDataProvider.CONTENT_URI, null,
                    null, null, null);
            if (c.moveToPosition(0)) {
                int tempColIndex = c.getColumnIndex(HoraireDataProvider.Columns.TEMPERATURE);
                int temp = c.getInt(tempColIndex);
                String formatStr = context.getResources().getString(R.string.header_format_string);
                String header = String.format(formatStr, temp,
                        context.getString(R.string.city_name));
                rv.setTextViewText(R.id.city_name, header);
            }
            c.close();
        }
        return rv;*/
        return new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    }

    /**
     * Permet l'affichage de l'interface, et est appelée lors des rafraîchissements commandés
     * par le gestionnaire de bureau.
     */
    @SuppressWarnings("deprecation")
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
    	//Toast.makeText(context, "HoWi:onUpdate", Toast.LENGTH_SHORT).show();
    	
    	// Récupération de la vue distante
    	RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    	
    	// Configuration de l'interface de chaque widget
    	for (int i = 0; i < appWidgetIds.length; ++i) {
    		final int appWidgetId = appWidgetIds[i];
    		//Toast.makeText(context, Integer.toString(appWidgetId), Toast.LENGTH_SHORT).show();
    		
    		// Mise à jour widget courant en fonction des paramètres
    		updateWidgetName(context, rv, R.id.horaire_name, appWidgetId);
    		
    		Intent intent = new Intent(context, Configuration.class);
        	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        	// <3 http://stackoverflow.com/questions/4011178/multiple-instances-of-widget-only-updating-last-widget
        	PendingIntent pi = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);;
        	rv.setOnClickPendingIntent(R.id.config_button, pi);
        	
        	// Attaque du webservice pour remplir les champs
    		if(this.sf == null) {
				new AsyncTask<Void, Void, Void>() {
					@Override
			    	protected Void doInBackground(Void... params) {
			    		callWebService(HoraireWidgetProvider.this.context, appWidgetId);
						return null;
			    	}
				}.execute();
    		}
        	
	        appWidgetManager.updateAppWidget(appWidgetId, rv);
    	}
    }

    /**
     * Affiche le nom de l'horaire en fonction des paramètres
     * @param rv
     * @param horaireName
     * @param horaire_name
     */
    private void updateWidgetName(Context context, RemoteViews rv, int horaireName,
			int appWidgetId) {
    	
    	String horaire_name = getPref(context, HoraireWidgetProvider.PREF_CLASS, appWidgetId);
    	
    	if(horaire_name == null)
    		horaire_name = "Horaires COMEM";
    	
    	else if(horaire_name.equals("Classes"))
			horaire_name = "Horaires COMEM";

		rv.setTextViewText(R.id.horaire_name, horaire_name);
	}

	//  TODO Compléter cette méthode ?
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, Bundle newOptions) {

    	//Toast.makeText(context, "HoWi:onAppWidgetOptionsChanged", Toast.LENGTH_SHORT).show();
    	
        //RemoteViews layout;
        /*if (minHeight < 100) {
            mIsLargeLayout = false;
        } else {
            mIsLargeLayout = true;
        }*/
        //layout = buildLayout(context, appWidgetId, true);
        //appWidgetManager.updateAppWidget(appWidgetId, layout);
    }
    
    /*********************************************************************
	 * Gestion des tâches asynchrones pour le webservice
	 ********************************************************************/
	/**
	 * Lance une tâche asynchrone pour récupérer les horaires du webservice.
	 */
	public void callWebService(Context context, int appWidgetId){
		
		int pos;
		
		String selectedClass   = getPref(context, PREF_CLASS,   appWidgetId);
		String selectedCourse  = getPref(context, PREF_COURSE,  appWidgetId);
		String selectedTeacher = getPref(context, PREF_TEACHER, appWidgetId);
		String selectedColor   = getPref(context, PREF_COLOR,   appWidgetId);
		String selectedNumDays = getPref(context, PREF_TIME,    appWidgetId);
		
		if(selectedClass == null)
			selectedClass = HoraireWidgetProvider.DEFAULT_CLASS;
		if(selectedCourse == null)
			selectedCourse = HoraireWidgetProvider.DEFAULT_COURSE;
		if(selectedTeacher == null)
			selectedTeacher = HoraireWidgetProvider.DEFAULT_TEACHER;
		
		Log.i(HoraireWidgetProvider.TAG, "Infos dans callWebService : " + selectedClass + selectedCourse + selectedTeacher + selectedColor + selectedNumDays);
		
		this.service = new Service(this);
		RequestEntity req = new RequestEntity();
		//req.startingDate = "";
		//req.endingDate = "";
		
		// Définition de la requête en fonction des préférences
		if(		!selectedClass.equals(HoraireWidgetProvider.DEFAULT_CLASS) &&
				!selectedCourse.equals(HoraireWidgetProvider.DEFAULT_COURSE) &&
				!selectedTeacher.equals(HoraireWidgetProvider.DEFAULT_TEACHER)) {
			
			Log.i(HoraireWidgetProvider.TAG, "RECHERCHE POUR TOUS");
			req.classIdField = selectedClass;
			req.teacherId    = selectedTeacher;
			req.courseId     = selectedCourse;
			req.startingDate = "";
			req.endingDate   = "";
		}
		
		else if(!selectedClass.equals(DEFAULT_CLASS) && !selectedCourse.equals(DEFAULT_COURSE)) {
			Log.i(HoraireWidgetProvider.TAG, "RECHERCHE POUR UNE CLASSE ET UN COURS");
			req.classIdField = selectedClass;
			req.courseId     = selectedCourse;
		}
		
		else if(!selectedClass.equals(DEFAULT_CLASS) && !selectedTeacher.equals(DEFAULT_TEACHER)) {
			Log.i(HoraireWidgetProvider.TAG, "RECHERCHE POUR UNE CLASSE ET UN INTERVENANT");
			req.classIdField = selectedClass;
			req.teacherId    = selectedTeacher;
		}
		
		else if(!selectedCourse.equals(DEFAULT_COURSE) && !selectedTeacher.equals(DEFAULT_TEACHER)) {
			Log.i(HoraireWidgetProvider.TAG, "RECHERCHE POUR UN COURS ET UN INTERVENANT");
			req.courseId  = selectedCourse;
			req.teacherId = selectedTeacher;
		}
		
		else {
			// Si on n'a choisi qu'une classe
			if(!selectedClass.equals(DEFAULT_CLASS)) {
				req.classIdField = selectedClass;
				Log.i(HoraireWidgetProvider.TAG, "On fait une recherche pour une classe");
			}
			// Si on n'a choisi qu'un intervenant
			if(!selectedTeacher.equals(DEFAULT_TEACHER)) {
				req.teacherId = selectedTeacher;
				Log.i(HoraireWidgetProvider.TAG, "On fait une recherche pour un intervenant");
			}
			// Si on n'a choisi qu'un cours
			if(!selectedCourse.equals(DEFAULT_COURSE)) {
				req.courseId = selectedCourse;
				Log.i(HoraireWidgetProvider.TAG, "On fait une recherche pour un cours");
			}
		}
		
		try {
			// Et on appelle le webservice qui va bien.
			this.service.GetScheduleAsync(req);
			
		} catch (Exception e) {
			displayError();
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void Wsdl2CodeStartedRequest() {
		Log.i(HoraireWidgetProvider.TAG, "Wsdl2CodeStartedRequest dans le PROVIDER");
		//Toast.makeText(getApplicationContext(), HoraireWidgetProvider.TAG + " est en train d'attaquer le webservice.", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Fonction exécutée lorsque l'activité reçoit les horaires du webservice.
	 */
	@Override
	public void Wsdl2CodeFinished(String methodName, Object Data) {
		Log.i(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished dans le PROVIDER avec l'appel : " + methodName);
		VectorScheduleEntity se = (VectorScheduleEntity) Data;
		Log.i(HoraireWidgetProvider.TAG, se.toString());
		
		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		int id = AppWidgetManager.INVALID_APPWIDGET_ID; // TODO Gestion de multiples Widget !
		
		/*if(Data != null && Data.getClass().equals(VectorScheduleEntity.class)) {
			ScheduleFactory sf = new ScheduleFactory(Data);
		
			//ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, R.id.courses_listview, sf.getHoraires());
		    //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    
			RemoteViews rv = buildLayout(context, id, sf, true);
			awm.updateAppWidget(id, rv);
		}*/
	}
	
	/**
	 * Fonction exécutée lorsque le webservice a rencontré une erreur.
	 * @param ex L'exception qui a été levée
	 */
	@Override
	public void Wsdl2CodeFinishedWithException(Exception ex) {
		Log.e(HoraireWidgetProvider.TAG , "Wsdl2CodeFinishedWithException dans le PROVIDER");
	}
	/**
	 * Fonction exécutée lorsque le webservice ne retourne pas de cours.
	 */
	@Override
	public void Wsdl2CodeEndedRequest() {
		Log.i(HoraireWidgetProvider.TAG, "Wsdl2CodeEndedRequest dans le PROVIDER");
		// TODO Ajouter une vue "pas d'horaire" au widget
	}
	
	/**
	 * Fonction s'appelant lorsque l'attaque du webservice a échoué
	 */
	public void displayError(){
		Toast.makeText(this.context, R.string.error, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Permet de récupérer les préférences de chaque widget.
	 * @param context Le context de l'appli
	 * @param pref La préférence à récupérer pour ce widget
	 * @param appWidgetId l'id du widget dont on veut récupérer les préférences
	 * @return La préférence paramétrée pour ce widget
	 */
	public String getPref(Context context, Object pref, int appWidgetId) {
	     SharedPreferences sharedPreferences = context.getSharedPreferences(
	            pref + String.valueOf(appWidgetId),
	            Context.MODE_PRIVATE);
	    return sharedPreferences.getString(
	            pref + String.valueOf(appWidgetId), null);
	}
}
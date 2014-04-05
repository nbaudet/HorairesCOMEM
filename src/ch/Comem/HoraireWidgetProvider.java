package ch.Comem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
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
public class HoraireWidgetProvider extends AppWidgetProvider {
    
	public static String TAG = "HoraireWidgetProvider";
	public static String EXTRA_APPWIDGET_ID;
	
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
        
        if(action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
        	Log.d(HoraireWidgetProvider.TAG, "HoWi:onReceive");
        }
        
    	super.onReceive(ctx, intent);
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
	@Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
    	//Toast.makeText(context, "HoWi:onUpdate", Toast.LENGTH_SHORT).show();
    	
    	// Récupération de la vue distante
    	RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    	
    	// Configuration de l'interface de chaque widget
    	for (int i = 0; i < appWidgetIds.length; ++i) {
    		
    		final int appWidgetId = appWidgetIds[i];
    		//Toast.makeText(context, Integer.toString(appWidgetId), Toast.LENGTH_SHORT).show();
    		
    		updateWidgetName(context, rv, R.id.horaire_name, appWidgetId);
    		
    		// Listener sur le bouton de configuration
    		Intent intent = new Intent(context, Configuration.class);
        	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        	// thanks to http://stackoverflow.com/questions/4011178/multiple-instances-of-widget-only-updating-last-widget
        	PendingIntent pi = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);;
        	rv.setOnClickPendingIntent(R.id.config_button, pi);

	        appWidgetManager.updateAppWidget(appWidgetId, rv);
    		
    		/*
    		 * We are being asked to update widget 'i'. To do that, we have to call
    		 * the Web Service (asynchronously), process the response and update the UI.
    		 * We should be able to do:
    		 * 
    		 * Service service = new Service()
    		 */
    		final Service service = new Service(new IWsdl2CodeEvents(){

				@Override
				public void Wsdl2CodeStartedRequest() {
				}

				@Override
				public void Wsdl2CodeFinished(String methodName, Object Data) {
					
					Log.d(HoraireWidgetProvider.TAG, "Wsdl2CodeFinished dans le PROVIDER avec l'appel : " + methodName);
					HoWiHoraire horaire = new HoWiHoraire((VectorScheduleEntity) Data);
					Log.d(HoraireWidgetProvider.TAG, horaire.toString());
					
					// TODO gérer l'affichage
					AppWidgetManager awm = AppWidgetManager.getInstance(context);
					
					if(horaire != null) {
						/*ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1);
					    adapter.addAll(horaire.toString());*/
					    
					    
						//RemoteViews rv = buildLayout(context, appWidgetId, sf, true);
					    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
					    rv.setTextViewText(R.id.courses_list, horaire.toString());
						awm.updateAppWidget(appWidgetId, rv);
					}
				}

				@Override
				public void Wsdl2CodeFinishedWithException(Exception ex) {
					Log.d(HoraireWidgetProvider.TAG , "Wsdl2CodeFinishedWithException dans le PROVIDER");
				}

				@Override
				public void Wsdl2CodeEndedRequest() {
					Log.d(HoraireWidgetProvider.TAG, "Wsdl2CodeEndedRequest dans le PROVIDER");
				}
    		});
    		
			callWebService(service, HoraireWidgetProvider.this.context, appWidgetId);

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
    	
    	// TODO Mieux gérer la personnalisation du titre en fonction des préférences  
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

    	Log.d(HoraireWidgetProvider.TAG, "Dans onAppwidgetOptionsChanged");
    	
        //RemoteViews layout;
        /*if (minHeight < 100) {
            mIsLargeLayout = false;
        } else {
            mIsLargeLayout = true;
        }*/
        //layout = buildLayout(context, appWidgetId, true);
        //appWidgetManager.updateAppWidget(appWidgetId, layout);
    }
    
    @Override
    public void onDeleted(Context contect, int[] appWidgetIds) {
    	Log.d(TAG, "onDeleted");
    	// TODO Se renseigner si utile de faire cette partie
        // On efface les préférences associées ce widget
        /*final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            ExampleAppWidgetConfigure.deleteTitlePref(context, appWidgetIds[i]);
        }*/
    }
    
    /*********************************************************************
	 * Gestion des tâches asynchrones pour le webservice
	 ********************************************************************/
    /**
     * Lance une tâche asynchrone pour récupérer les horaires du webservice.
     * Remarque : les dates du webservice sont de format <mois>-<jour>-<année>
     * @param service Le webservice
     * @param context Le contexte courant
     * @param appWidgetId L'id du widget pour lequel on fait la requête
     */
	@SuppressLint("SimpleDateFormat")
	public void callWebService(Service service, Context context, int appWidgetId){
		
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
		if(selectedNumDays == null)
			selectedNumDays = HoraireWidgetProvider.DEFAULT_TIME;
		
		Log.d(HoraireWidgetProvider.TAG, "Infos dans callWebService : " + selectedClass + selectedCourse + selectedTeacher + selectedColor + selectedNumDays);
		
		// Définit les paramètres "fixes" de la requête
		RequestEntity req = new RequestEntity();
		int nbJours;
		if(selectedNumDays.equals("1 semaine")){
			nbJours = 7;
		}
		else if(selectedNumDays.equals("1 mois")) {
			nbJours = 30;
		}
		else {
			nbJours = 60;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Date du jour
        req.startingDate = dateFormat.format(c.getTime());
        c.add(Calendar.DATE, nbJours); // Date + préférence de l'utilisateur
        req.endingDate = dateFormat.format(c.getTime());
		
		// Définition de la requête en fonction des préférences
		if(		!selectedClass.equals(HoraireWidgetProvider.DEFAULT_CLASS) &&
				!selectedCourse.equals(HoraireWidgetProvider.DEFAULT_COURSE) &&
				!selectedTeacher.equals(HoraireWidgetProvider.DEFAULT_TEACHER)) {
			
			Log.d(HoraireWidgetProvider.TAG, "RECHERCHE POUR TOUS");
			req.classIdField = selectedClass;
			req.teacherId    = selectedTeacher;
			req.courseId     = selectedCourse;
			req.startingDate = "";
			req.endingDate   = "";
		}
		
		else if(!selectedClass.equals(DEFAULT_CLASS) && !selectedCourse.equals(DEFAULT_COURSE)) {
			Log.d(HoraireWidgetProvider.TAG, "RECHERCHE POUR UNE CLASSE ET UN COURS");
			req.classIdField = selectedClass;
			req.courseId     = selectedCourse;
		}
		
		else if(!selectedClass.equals(DEFAULT_CLASS) && !selectedTeacher.equals(DEFAULT_TEACHER)) {
			Log.d(HoraireWidgetProvider.TAG, "RECHERCHE POUR UNE CLASSE ET UN INTERVENANT");
			req.classIdField = selectedClass;
			req.teacherId    = selectedTeacher;
		}
		
		else if(!selectedCourse.equals(DEFAULT_COURSE) && !selectedTeacher.equals(DEFAULT_TEACHER)) {
			Log.d(HoraireWidgetProvider.TAG, "RECHERCHE POUR UN COURS ET UN INTERVENANT");
			req.courseId  = selectedCourse;
			req.teacherId = selectedTeacher;
		}
		
		else {
			// Si on n'a choisi qu'une classe
			if(!selectedClass.equals(DEFAULT_CLASS)) {
				req.classIdField = selectedClass;
				Log.d(HoraireWidgetProvider.TAG, "On fait une recherche pour une classe");
			}
			// Si on n'a choisi qu'un intervenant
			if(!selectedTeacher.equals(DEFAULT_TEACHER)) {
				req.teacherId = selectedTeacher;
				Log.d(HoraireWidgetProvider.TAG, "On fait une recherche pour un intervenant");
			}
			// Si on n'a choisi qu'un cours
			if(!selectedCourse.equals(DEFAULT_COURSE)) {
				req.courseId = selectedCourse;
				Log.d(HoraireWidgetProvider.TAG, "On fait une recherche pour un cours");
			}
		}
		
		try {
			// Et on appelle le webservice qui va bien.
			service.GetScheduleAsync(req);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
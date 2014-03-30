/**
 * 
 */
package ch.Comem;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;
import ch.Comem.HorairesCOMEM.R;

/**
 * Notre observateur de données notifie tous les widgets quand il détecte un changement.
 */
/*class HoraireDataProviderObserver extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;

    HoraireDataProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);
        mAppWidgetManager = mgr;
        mComponentName = cn;
    }

    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which will requery the
        // cursor for the new data.
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
        		mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.courses_list);
    }
}*/

/**
 * La classe qui fournit le widget.
 */
public class HoraireWidgetProvider extends AppWidgetProvider {
    
	public static String TAG = "HoraireWidgetProvider";
	public static String EXTRA_APPWIDGET_ID;

    //public HoraireWidgetProvider() {}
    
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
        
        //Toast.makeText(ctx, "HoWi:onReceive, TAG : " + action, Toast.LENGTH_SHORT).show();
        //Log.d(TAG, "onReceive() " + action);
        
        // Réception du click sur le bouton de configuration
        
        
        /*if (ACTION_CONFIG_CLICKED.equals(action)){
	        // Ouverture de l'activity de configuration
        	Toast.makeText(ctx, "Horaire Widget : Config cliqué !", Toast.LENGTH_SHORT).show();
	    }
        
        else if (FORCE_WIDGET_UPDATE.equals(action)){
        	Toast.makeText(ctx, "HoraireCOMEM FORCE WIDGET UPDATE", Toast.LENGTH_SHORT).show();
        	// TODO: mettre à jour le widget
        	// On peut le lancer comme suit : sendBroadcast(new Intent(HoraireWidgetProvider .FORCE_WIDGET_UPDATE));
        }
        else{
        	//Toast.makeText(ctx, "onReceive else", Toast.LENGTH_SHORT).show();
        }*/
        
        /*if (action.equals(REFRESH_ACTION)) {
            // BroadcastReceivers have a limited amount of time to do work, so for this sample, we
            // are triggering an update of the data on another thread.  In practice, this update
            // can be triggered from a background service, or perhaps as a result of user actions
            // inside the main application.
            final Context context = ctx;
            sWorkerQueue.removeMessages(0);
            sWorkerQueue.post(new Runnable() {
                @Override
                public void run() {
                    final ContentResolver r = context.getContentResolver();
                    final Cursor c = r.query(HoraireDataProvider.CONTENT_URI, null, null, null, 
                            null);
                    final int count = c.getCount();

                    // We disable the data changed observer temporarily since each of the updates
                    // will trigger an onChange() in our data observer.
                    r.unregisterContentObserver(sDataObserver);
                    for (int i = 0; i < count; ++i) {
                        final Uri uri = ContentUris.withAppendedId(HoraireDataProvider.CONTENT_URI, i);
                        final ContentValues values = new ContentValues();
                        values.put(HoraireDataProvider.Columns.TEMPERATURE,
                                new Random().nextInt(sMaxDegrees));
                        r.update(uri, values, null, null);
                    }
                    r.registerContentObserver(HoraireDataProvider.CONTENT_URI, true, sDataObserver);

                    final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                    final ComponentName cn = new ComponentName(context, HoraireWidgetProvider.class);
                    mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.horaire_list);
                }
            });

            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        } else if (action.equals(CLICK_ACTION)) {
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }*/

        /*final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Toast.makeText(ctx, Integer.toString(appWidgetId), Toast.LENGTH_SHORT).show();*/
        
    	super.onReceive(ctx, intent);
    }

    /**
     * Construit l'interface à jour du widget dans une vue distante et la renvoie.
     * @param context
     * @param appWidgetId
     * @param largeLayout
     * @return
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
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
    	//Toast.makeText(context, "HoWi:onUpdate", Toast.LENGTH_SHORT).show();
    	
    	// Récupération de la vue distante
    	RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    	
    	
    	
    	// Configuration de l'interface de chaque widget
    	for (int i = 0; i < appWidgetIds.length; ++i) {
    		
    		Intent intent = new Intent(context, Configuration.class);

    		int appWidgetId = appWidgetIds[i];
    		//Toast.makeText(context, Integer.toString(appWidgetId), Toast.LENGTH_SHORT).show();
        	intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    		
        	rv.setTextViewText(R.id.horaire_name, "Widget n° " + Integer.toString(appWidgetId));
        	
        	// <3 http://stackoverflow.com/questions/4011178/multiple-instances-of-widget-only-updating-last-widget
        	PendingIntent pi = PendingIntent.getActivity(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);;
        	rv.setOnClickPendingIntent(R.id.config_button, pi);
        	
        	// Mise à jour du widget courant
	        appWidgetManager.updateAppWidget(appWidgetId, rv);
    	}
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, Bundle newOptions) {

    	Toast.makeText(context, "HoWi:onAppWidgetOptionsChanged", Toast.LENGTH_SHORT).show();
    	
        RemoteViews layout;
        /*if (minHeight < 100) {
            mIsLargeLayout = false;
        } else {
            mIsLargeLayout = true;
        }*/
        //layout = buildLayout(context, appWidgetId, true);
        //appWidgetManager.updateAppWidget(appWidgetId, layout);
    }
}
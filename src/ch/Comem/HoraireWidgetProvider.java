/**
 * 
 */
package ch.Comem;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import ch.Comem.horairescomem.R;

/**
 * Notre observateur de données notifie tous les widgets quand il détecte un changement.
 */
class HoraireDataProviderObserver extends ContentObserver {
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
}

/**
 * The weather widget's AppWidgetProvider.
 */
public class HoraireWidgetProvider extends AppWidgetProvider {
    public static String TAG = "HoraireWidgetProvider";
	public static String ACTION_CONFIG_CLICKED = "ch.comem.CONFIG_CLICKED"; // Permet la gestion des Button et des Intent dans un widget

    public HoraireWidgetProvider() {
    	
    }
    
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
        
        Toast.makeText(ctx, "Horaire Widget dans onReceive", Toast.LENGTH_SHORT).show();
        
        // Réception du click sur le bouton de configuration
        Log.d(TAG, "onReceive() " + intent.getAction());
        
        if (ACTION_CONFIG_CLICKED.equals(action)){
	        // Ouverture de l'activity de configuration
        	Toast.makeText(ctx, "Horaire Widget : Config cliqué !", Toast.LENGTH_SHORT).show();
	    }
        
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
            // Show a toast
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            Toast.makeText(ctx, "prout", Toast.LENGTH_SHORT).show();
        }*/

        super.onReceive(ctx, intent);
    }

    /**
     * Construit l'interface du widget à partir d'une RemoteView.
     * @param context
     * @param appWidgetId
     * @param largeLayout
     * @return
     */
    private RemoteViews buildLayout(Context context, int appWidgetId, boolean largeLayout) {
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
     * Permet l'affichage de l'interface, et est appelée lors des rafraîchissements commandés par le gestionnaire de bureau.  
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        
    	super.onUpdate(context, appWidgetManager, appWidgetIds);
    	
    	// Intent lors du clic sur le bouton de configuration
    	Intent intent = new Intent(ACTION_CONFIG_CLICKED);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    	RemoteViews layout_test = buildLayout(context, appWidgetIds[0], true);
    	layout_test.setOnClickPendingIntent(R.id.config_button, pendingIntent);
    	
    	Log.v(TAG, "Dans onUpdate");
    	Toast.makeText(context, "Horaire Widget dans onUpdate", Toast.LENGTH_SHORT).show();
    	
    	// Update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            RemoteViews layout = buildLayout(context, appWidgetIds[i], true);
            appWidgetManager.updateAppWidget(appWidgetIds[i], layout);
            
        	/*Intent intent = new Intent(context, Mainpage.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);*/
        }
        
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId, Bundle newOptions) {

        RemoteViews layout;
        /*if (minHeight < 100) {
            mIsLargeLayout = false;
        } else {
            mIsLargeLayout = true;
        }*/
        layout = buildLayout(context, appWidgetId, true);
        appWidgetManager.updateAppWidget(appWidgetId, layout);
    }
}
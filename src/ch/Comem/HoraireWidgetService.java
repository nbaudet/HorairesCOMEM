/**
 * 
 */
package ch.Comem;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * Ce service de vues distantes est utilisé comme une enveloppe et permet d'instancier et de
 * gérer une RemoteViewsFactory qui, à son tour, sert à fournir chacune des vues affichées
 * dans le widget collection.
 * @author nicolas
 */
public class HoraireWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotUsedHoraireRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/**
 * La Factory qui va créer et remplir les vues dans les widget collection.
 */
class NotUsedHoraireRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Intent intent;
    private Cursor cursor;
    private int appWidgetId;

    /**
     * Implémentation facultative d'un constructeur, mais utile pour récupérer
     * les références au contexte du widget appelant.
     * @param context
     * @param intent
     */
    public NotUsedHoraireRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    /**
     * Configure les connexions/curseurs vers les sources de données.
     * Si cet appel dure plus de 20 secondes, un ANR interviendra !
     */
    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }
    
    /**
     * Appelée lorsque la collection de données sous-jacente affichée est modifiée.
     * (On peut utiliser la méthode notifyAppWidgetViewDataChanged de
     * l'AppWidgetManager pour déclencher ce gestionnaire.)
     */
    public void onDataSetChanged() {
        // Refresh the cursor
        if (cursor != null) {
            cursor.close();
        }
        cursor = context.getContentResolver().query(HoraireDataProvider.CONTENT_URI, null, null,
                null, null);
    }
    
    /**
     * Renvoie le nombre d'éléments de la collection affichée.
     */
    public int getCount() {
        return cursor.getCount();
    }

    /**
     * Renvoie true si les identifiants uniques fournis par chaque élément
     * sont stables - càd s'ils ne changent pas à l'exécution.
     */
    public boolean hasStableIds() {
        return false;
    }
    
    /**
     * Renvoie l'identifiant unique associé à l'élément d'indice indiqué.
     */
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * Nombre de définitions de vues différentes.
     */
    public int getViewTypeCount() {
        return 1;
    }
    
    /**
     * Fournit éventuellement une vue de chargement à afficher.
     * Renvoyer null permet d'utiliser la vue par défaut.
     */
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * Crée et remplit la vue à afficher à l'index indiqué.
     */
    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider
        /*String day = "Unknown Day";
        int temp = 0;
        if (mCursor.moveToPosition(position)) {
            final int dayColIndex = mCursor.getColumnIndex(HoraireDataProvider.Columns.DAY);
            final int tempColIndex = mCursor.getColumnIndex(
                    HoraireDataProvider.Columns.TEMPERATURE);
            day = mCursor.getString(dayColIndex);
            temp = mCursor.getInt(tempColIndex);
        }

        // Return a proper item with the proper day and temperature
        final String formatStr = mContext.getResources().getString(R.string.item_format_string);
        final int itemId = R.layout.widget_item;
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), itemId);
        rv.setTextViewText(R.id.widget_item, String.format(formatStr, temp, day));

        // Set the click intent so that we can handle it and show a toast message
        final Intent fillInIntent = new Intent();
        final Bundle extras = new Bundle();
        extras.putString(HoraireWidgetProvider.EXTRA_DAY_ID, day);
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return rv;*/
    	return new RemoteViews(context.getPackageName(), 1);
    }
    
    /**
     * Ferme les connexions, les curseurs ou tout autre état persistant
     * créé dans onCreate.
     */
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }
}

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
 * Ce service de vues distantes est utilis� comme une enveloppe et permet d'instancier et de
 * g�rer une RemoteViewsFactory qui, � son tour, sert � fournir chacune des vues affich�es
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
 * La Factory qui va cr�er et remplir les vues dans les widget collection.
 */
class NotUsedHoraireRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Intent intent;
    private Cursor cursor;
    private int appWidgetId;

    /**
     * Impl�mentation facultative d'un constructeur, mais utile pour r�cup�rer
     * les r�f�rences au contexte du widget appelant.
     * @param context
     * @param intent
     */
    public NotUsedHoraireRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    /**
     * Configure les connexions/curseurs vers les sources de donn�es.
     * Si cet appel dure plus de 20 secondes, un ANR interviendra !
     */
    public void onCreate() {
        // Since we reload the cursor in onDataSetChanged() which gets called immediately after
        // onCreate(), we do nothing here.
    }
    
    /**
     * Appel�e lorsque la collection de donn�es sous-jacente affich�e est modifi�e.
     * (On peut utiliser la m�thode notifyAppWidgetViewDataChanged de
     * l'AppWidgetManager pour d�clencher ce gestionnaire.)
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
     * Renvoie le nombre d'�l�ments de la collection affich�e.
     */
    public int getCount() {
        return cursor.getCount();
    }

    /**
     * Renvoie true si les identifiants uniques fournis par chaque �l�ment
     * sont stables - c�d s'ils ne changent pas � l'ex�cution.
     */
    public boolean hasStableIds() {
        return false;
    }
    
    /**
     * Renvoie l'identifiant unique associ� � l'�l�ment d'indice indiqu�.
     */
    public long getItemId(int position) {
        return position;
    }
    
    /**
     * Nombre de d�finitions de vues diff�rentes.
     */
    public int getViewTypeCount() {
        return 1;
    }
    
    /**
     * Fournit �ventuellement une vue de chargement � afficher.
     * Renvoyer null permet d'utiliser la vue par d�faut.
     */
    public RemoteViews getLoadingView() {
        return null;
    }

    /**
     * Cr�e et remplit la vue � afficher � l'index indiqu�.
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
     * Ferme les connexions, les curseurs ou tout autre �tat persistant
     * cr�� dans onCreate.
     */
    public void onDestroy() {
        if (cursor != null) {
            cursor.close();
        }
    }
}

/**
 * 
 */
package ch.comem;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

/**
 * A dummy class that we are going to use internally to store weather data.  Generally, this data
 * will be stored in an external and persistent location (ie. File, Database, SharedPreferences) so
 * that the data can persist if the process is ever killed.  For simplicity, in this sample the
 * data will only be stored in memory.
 * author : Nicolas Baudet
 */
class HoraireDataPoint {
    String nom;
    String salle;

    HoraireDataPoint(String nom, String salle) {
        this.nom = nom;
        this.salle = salle;
    }
}
	
/**
 * The AppWidgetProvider for our sample weather widget.
 */
public class HoraireDataProvider extends ContentProvider {
    public static final Uri CONTENT_URI =
        Uri.parse("content://com.example.android.weatherlistwidget.provider");
    public static class Columns {
        public static final String ID = "_id";
        public static final String DAY = "day";
        public static final String TEMPERATURE = "temperature";
    }

    /**
     * Generally, this data will be stored in an external and persistent location (ie. File,
     * Database, SharedPreferences) so that the data can persist if the process is ever killed.
     * For simplicity, in this sample the data will only be stored in memory.
     */
    private static final ArrayList<HoraireDataPoint> sData = new ArrayList<HoraireDataPoint>();

    @Override
    public boolean onCreate() {
        // We are going to initialize the data provider with some default values
        sData.add(new HoraireDataPoint("Biologie", "T158"));
        sData.add(new HoraireDataPoint("Chimie", "T158"));
        sData.add(new HoraireDataPoint("Programmation", "T158"));
        sData.add(new HoraireDataPoint("Gymnastique", "T158"));
        sData.add(new HoraireDataPoint("Physique", "T158"));

        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        assert(uri.getPathSegments().isEmpty());

        // In this sample, we only query without any parameters, so we can just return a cursor to
        // all the weather data.
        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.ID, Columns.DAY, Columns.TEMPERATURE });
        for (int i = 0; i < sData.size(); ++i) {
            final HoraireDataPoint data = sData.get(i);
            c.addRow(new Object[]{ Integer.valueOf(i), data.nom, data.salle });
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
    	//return "Pouet !"; ////////////////////////////////////////////// TODO : trouver à quoi ça sert !
        return "vnd.android.cursor.dir/vnd.horairelistwidget.nom";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // This example code does not support inserting
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // This example code does not support deleting
        return 0;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        assert(uri.getPathSegments().size() == 1);

        // In this sample, we only update the content provider individually for each row with new
        // temperature values.
        final int index = Integer.parseInt(uri.getPathSegments().get(0));
        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.ID, Columns.DAY, Columns.TEMPERATURE });
        assert(0 <= index && index < sData.size());
        final HoraireDataPoint data = sData.get(index);
        data.salle = values.getAsString(Columns.TEMPERATURE);

        // Notify any listeners that the data backing the content provider has changed, and return
        // the number of rows affected.
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }
}

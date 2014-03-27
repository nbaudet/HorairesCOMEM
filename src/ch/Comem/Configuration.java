package ch.Comem;

import ch.Comem.HorairesCOMEM.R;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Configuration extends Activity {

	private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	
	/**
	 * Fonction appel�e lorsque l'activit� est cr�e pour la premi�re fois.
	 * C'est ici qu'il faut g�n�rer les petits objets que l'on r�g�n�re r�guli�rement
	 * comme les listes de profs, classe ou modules.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Toast.makeText(getApplicationContext(), "HoWi:Configuration.onCreate", Toast.LENGTH_LONG).show();
		
		super.onCreate(savedInstanceState);
		// D�finition de la vue � utiliser
		setContentView(R.layout.configuration);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if(extras != null) {
			appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		else {
			Toast.makeText(getApplicationContext(), "Pas d'id !", Toast.LENGTH_LONG).show();
		}
		
		// Met le r�sultat � annul� si l'utilisateur quitte l'activit� sans valider les modifs de la config
		setResult(RESULT_CANCELED, null);
		
		// TODO configuration de l'interface utilisateur
	}
	
	private void completedConfiguration() {
		// Sauve la config pour appWidgetId
		// Pr�vient le gestionnaire de widgets que la config est termin�e
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		setResult(RESULT_OK, result);
		finish();
	}

}

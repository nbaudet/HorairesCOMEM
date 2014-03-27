package ch.Comem;

import ch.Comem.horairescomem.R;
import android.app.Activity;
import android.os.Bundle;

public class Configuration extends Activity {

	/**
	 * Fonction appelée lorsque l'activité est crée pour la première fois.
	 * C'est ici qu'il faut générer les petits objets que l'on régénère régulièrement
	 * comme les listes de profs, classe ou modules.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Définition de la vue à utiliser
		setContentView(R.layout.configuration);
	}

}

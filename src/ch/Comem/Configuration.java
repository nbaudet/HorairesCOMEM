package ch.Comem;

import ch.Comem.horairescomem.R;
import android.app.Activity;
import android.os.Bundle;

public class Configuration extends Activity {

	/**
	 * Fonction appel�e lorsque l'activit� est cr�e pour la premi�re fois.
	 * C'est ici qu'il faut g�n�rer les petits objets que l'on r�g�n�re r�guli�rement
	 * comme les listes de profs, classe ou modules.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// D�finition de la vue � utiliser
		setContentView(R.layout.configuration);
	}

}

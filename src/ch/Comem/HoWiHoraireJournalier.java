package ch.Comem;

import java.util.ArrayList;

import com.Wsdl2Code.WebServices.Service.ScheduleEntity;

/**
 * Cette classe permet de gérer l'horaire pour un jour. Elle organise les horaires  
 * @author nicolas
 *
 */
public class HoWiHoraireJournalier {
	
	private String day;
	private String date;
	private ArrayList<String> am, pm;
	
	/**
	 * Construit l'horaire d'une journée en arrangeant les horaires le matin ou l'après-midi.
	 * @param horaire
	 */
	public HoWiHoraireJournalier(String day, String date, ScheduleEntity horaire) {
		if(day != null && date != null && horaire != null){
			this.day  = day;
			this.date = date.substring(0, 10);
			this.am   = new ArrayList<String>(); 
			this.pm   = new ArrayList<String>();
			this.addHoraire(horaire);
		}
		else {
			this.day  = "Horaire Vide";
			this.date = "";
			this.am = this.pm = new ArrayList<String>();
		}
	}
	
	/**
	 * Permet de retourner un jour, avec sa date et ses cours.
	 * @return L'horaire d'un jour, comprenant date et deux arrayLists de cours (matin et après-midi)
	 */
	protected HoWiHoraireJournalier getHoWiHoraireJournalier() {
		return this;
	}
	
	/**
	 * Permet d'ajouter un cours au bon endroit dans un horaire journalier.
	 * @param se Le cours à ajouter
	 */
	protected void addHoraire(ScheduleEntity se) {
		if(se != null) {
			// Set des valeurs par défaut au cas où les informations sont inconnues
			if(se.room == null)
				se.room = "N/A";
			if(se.teacherId == null)
				se.teacherId = "N/A";
			
			if(se.period.equals("am")) {
				this.am.add(se.toString());
			}
			else {
				this.pm.add(se.toString());
			}
		}
	}
	
	/**
	 * Permet d'afficher un horaire.
	 * @return La string formattée à afficher pour cet horaire
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.day + " - " + this.date + "\n");
		sb.append("-----------------------------\n");
		sb.append(this.display(this.am) + "\n");
		sb.append(this.display(this.pm) + "\n");
		return sb.toString();
	}
	
	/**
	 * Permet de "surcharger" le toString d'un array pour ne pas voir "am" et "pm" s'afficher.
	 * @param array Le tableau d'horaires à afficher
	 * @return la string des horaires pour cette journée
	 */
	private String display(ArrayList<String> array) {
		StringBuilder sb = new StringBuilder();
		for(String course : array) {
			sb.append(course + "\n");
		}
		return sb.toString();
	}
}

package ch.Comem;

import java.util.ArrayList;

import com.Wsdl2Code.WebServices.Service.Classe;
import com.Wsdl2Code.WebServices.Service.Cours;
import com.Wsdl2Code.WebServices.Service.Intervenant;
import com.Wsdl2Code.WebServices.Service.ScheduleEntity;
import com.Wsdl2Code.WebServices.Service.ScheduleInfo;
import com.Wsdl2Code.WebServices.Service.VectorClasse;
import com.Wsdl2Code.WebServices.Service.VectorCours;
import com.Wsdl2Code.WebServices.Service.VectorIntervenant;

public class ScheduleFactory {

	private ArrayList<String> classes, cours, intervenants, modules;
	/*private ArrayList<Cours> cours;
	private ArrayList<Intervenant> intervenants;
	private ArrayList<Module> modules;*/
	private ArrayList<ScheduleEntity> horaires;
	
	public ScheduleFactory(Object data) {
		this.classes = new ArrayList<String>();
		this.cours = new ArrayList<String>();
		this.intervenants = new ArrayList<String>();
		ScheduleInfo si = (ScheduleInfo) data;
		
		// Set les classes
		VectorClasse vc = (VectorClasse) si.getProperty(0);
		this.classes.add("Classes");
		for(int i = 0; i < vc.getPropertyCount(); i++){
			Classe classe = (Classe) vc.getProperty(i);
			this.classes.add(classe.toString());
		}
		
		// Set les cours
		VectorCours vco = (VectorCours) si.getProperty(1);
		this.cours.add("Cours");
		for(int i = 0; i < vco.getPropertyCount(); i++){
			Cours cours = (Cours) vco.getProperty(i);
			this.cours.add(cours.toString());
		}
		
		// Set les intervenants
		VectorIntervenant vi = (VectorIntervenant) si.getProperty(2);
		this.intervenants.add("Intervenants");
		for(int i = 0; i < vi.getPropertyCount(); i++){
			Intervenant inter = (Intervenant) vi.getProperty(i);
			this.intervenants.add(inter.toString());
		}
	}
	
	protected ArrayList<String> getClasses(){
		return this.classes;
	}
	
	protected ArrayList<String> getTeachers(){
		return this.intervenants;
	}
	
	protected ArrayList<String> getCourses(){
		return this.cours;
	}

}

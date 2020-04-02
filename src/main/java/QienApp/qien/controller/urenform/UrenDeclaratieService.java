package QienApp.qien.controller.urenform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import QienApp.qien.controller.MedewerkerRepository;
import QienApp.qien.controller.MedewerkerService;
import QienApp.qien.domein.Medewerker;
import QienApp.qien.domein.urenform.GewerkteDag;
import QienApp.qien.domein.urenform.Urendeclaratie;


@Service
public class UrenDeclaratieService {

	@Autowired
	private UrenDeclaratieRepository urenDeclaratieRepository;
	@Autowired
	private MedewerkerRepository medewerkerRepository;
	@Autowired
	private MedewerkerService medewerkerService;
	@Autowired
	private GewerkteDagService gewerkteDagService;
	@Autowired
	private GewerkteDagRepository gewerkteDagRepository;
	
	/** 1 *
	 * UPDATEURENDECLARATIE
	 * @param urendDeclaratieDetails	nieuw urendeclaratie object
	 * @return ud						de aangepaste urendeclaratie		
	 */
	public Urendeclaratie postOrUpdateUrendeclaratie(Urendeclaratie urendeclaratie ) 
	{
		return urenDeclaratieRepository.save(urendeclaratie);
	}
	
	/** 2 *
	 * MAAK EEN LEEG URENDECLARATIEFORMULIER, gevuld met dagen
	 * @PARAM maandNaam		de naam van de maand waarvoor het formulier wordt aangemaakt
	 * @PARAM maandNr		het nummer van de maand, benodigd voor aantal dagen dat er in komt
	 * @RETURN dezemaand	het lege urenformulier voor deze maand
	 */
	public Urendeclaratie maakUrendeclaratieForm(String maandNaam, int maandNr) 
	{
		// maak nieuw urendeclaratie object
		Urendeclaratie dezemaand = new Urendeclaratie();
		// geef de maand een naam
		dezemaand.setMaandNaam(maandNaam);
		// populate met dagen
		switch(maandNr) {
		case 2:
			for (int x = 0; x < 29; x++) {
				GewerkteDag dag = new GewerkteDag();
				dag.setDagnr(x+1);
				dezemaand.addDagToList(dag);
				gewerkteDagRepository.save(dag);	//TODO: is dit beter dan zoals op regel 66, 73? Ivo, Felix???
			} break;
		case 4: case 6: case 9: case 11:		
			for (int x = 0; x < 30; x++) {
				GewerkteDag dag = new GewerkteDag();
				dag.setDagnr(x+1);
				dezemaand.addDagToList(dag);
				gewerkteDagService.addDagToRepository(dag);
			} break;
		default:
			for (int x = 0; x < 31; x++) {
				GewerkteDag dag = new GewerkteDag();
				dag.setDagnr(x+1);
				dezemaand.addDagToList(dag);
				gewerkteDagService.addDagToRepository(dag);
			} break;
		}
		urenDeclaratieRepository.save(dezemaand);
		return dezemaand;
	}
	
	/** TEST * MAAK & KOPPEL EEN LEEG URENDECLARATIEOBJECT VOOR/AAN ALLE MEDEWERKERS IN DE DATABASE
	 * @param u		leeg urendeclaratieobject
	 * @return		mededeling dat het gelukt is
	 */
	public String maakEnKoppelAanAllen(String maandNaam, int maandNr) 
	{
		for (Medewerker persoon: medewerkerRepository.findAll()) {
			
			Urendeclaratie u = maakUrendeclaratieForm(maandNaam, maandNr);
			
			u.setMedewerker(persoon);
			persoon.addUrendeclaratie(u);
			urenDeclaratieRepository.save(u);
			medewerkerRepository.save(persoon);
			
		}
		return "Alle medewerkers kunnen nu de declaratie van "+ maandNaam + " gaan invullen";
	}
	
//	/** 3 * MAAK & KOPPEL EEN LEEG URENDECLARATIEOBJECT VOOR/AAN ALLE MEDEWERKERS IN DE DATABASE
//	 * @param u		leeg urendeclaratieobject
//	 * @return		mededeling dat het gelukt is
//	 */
//	public String maakEnKoppelAanAllen(String maandNaam, int maandNr) 
//	{
//		for (Medewerker persoon: medewerkerRepository.findAll()) {
//			
//			Urendeclaratie u = maakUrendeclaratieForm(maandNaam, maandNr);
//			persoon.addUrendeclaratie(u);
//			medewerkerRepository.save(persoon);
//			//u.setMedewerker(persoon);  ==>>> TODO relatie is nog niet bidrectioneel
//			//urenDeclaratieRepository.save(u);
//		}
//		return "Alle medewerkers kunnen nu de declaratie van "+ maandNaam + " gaan invullen";
//	}

	/** 1 *
	 * GET ONE URENDECLARATIE
	 * @PARAM id	ID van een specifieke urendeclaratie
	 * @RETURN		de gevraagde urendeclaratie
	 */
	public Urendeclaratie getUrendeclaraties(Long id) {
		return urenDeclaratieRepository.findById(id).get();
	}

	/** 2 * VIND ALLE URENDECLARATIEFORMULIEREN
	 * 
	 * @return
	 */
	public Iterable<Urendeclaratie> getAllUrendeclaraties() {
		return urenDeclaratieRepository.findAll();
	}



	/** 3.
	 * KOPPEL FORM AAN MEDEWERKER & SAVE
	 * @param formId
	 * @param medewerkerId		
	 * @return urendeclaratieformulier met eigenaar
	 */
	public Urendeclaratie koppelFormAanMedewerker(Long formId, Long medewerkerId) 
	{
		Urendeclaratie tempUd = getUrendeclaraties(formId);
		Medewerker tempMw = medewerkerService.getMedewerkerById(medewerkerId);
		System.out.println("DEBUG" + tempUd);
		System.out.println("DEBUG" + tempMw);
		//add FORM to MW
		tempMw.addUrendeclaratie(tempUd);
		//tempUd.setMedewerker(tempMw);
		
		medewerkerRepository.save(tempMw);
		return urenDeclaratieRepository.save(tempUd);
	}


	
}

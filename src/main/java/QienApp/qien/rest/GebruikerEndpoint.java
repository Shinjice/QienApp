package QienApp.qien.rest;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import QienApp.qien.controller.GebruikerService;
import QienApp.qien.domein.Gebruiker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/gebruikers")
@Api(tags = "GebruikerEndPoint", description = "REST APIs gerelateerd aan Gebruiker-entiteit.")
public class GebruikerEndpoint {
	@Autowired
	GebruikerService gebruikerService;
	

	
	
	@GetMapping("/achternaam/{achternaam}") 
	public List<Gebruiker> zoekAchternaam(@PathVariable(value="achternaam") String achternaam) {
		return gebruikerService.findByAchternaam(achternaam);
	}
	
	@GetMapping("/voornaam/{voornaam}") 
	public Optional<Gebruiker> zoekVoornaam(@PathVariable(value="voornaam") String voornaam) {
		return gebruikerService.findByVoornaam(voornaam);
	}

	@ApiOperation(value = "Verkrijg alle gebruikers.", notes = "Verkrijg alle gebruikers uit de database.", response = Gebruiker.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Alle gebruikers succesvol verkregen."),
					@ApiResponse(code = 404, message = "Could not retrieve an applicant with the specified ID"),
					@ApiResponse(code = 400, message = "Invalid ID value") })
	@GetMapping("/")
	public Iterable<Gebruiker> verkrijgGebruikers() {
		return gebruikerService.getAllGebruikers();
	}
	@GetMapping("/{id}")
	public Gebruiker verkrijgGebruiker(@PathVariable(value = "id") String gebruikerId) {
		return gebruikerService.getGebruikerById(Long.parseLong(gebruikerId));
	}
	@PostMapping("/")
	public Gebruiker toevoegenGebruiker(@RequestBody Gebruiker gebruiker) {
		return gebruikerService.addGebruiker(gebruiker);
	}
	@DeleteMapping("/{id}")
	public void verwijderGebruiker(@PathVariable(value = "id") String gebruikerId) {
		gebruikerService.deleteGebruiker(Long.parseLong(gebruikerId));
	}
	@PutMapping("/{id}")
	public Gebruiker vernieuwGebruiker(@PathVariable(value = "id") String gebruikerId, @RequestBody Gebruiker gebruikerDetails) {
		return gebruikerService.updateGebruiker(Long.parseLong(gebruikerId), gebruikerDetails);
	}
}
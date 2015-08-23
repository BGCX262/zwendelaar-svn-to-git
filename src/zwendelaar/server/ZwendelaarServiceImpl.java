package zwendelaar.server;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.ZwendelaarService;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ZwendelaarServiceImpl extends RemoteServiceServlet implements ZwendelaarService
{
	
	public ZwendelaarServiceImpl()
	{
	}

	@Override
	public Gebruiker loginUser(String email, String password) {
		return Database.login(email, password);
	}

	@Override
	public boolean registerUser(String voornaam, String achternaam, String straat, String huisnummer, String postcode, String plaats, String email, String password) {
		System.out.println(voornaam+achternaam);
		return Database.register(voornaam, achternaam, straat, huisnummer, postcode, plaats, email, password);
	}

	@Override
	public ArrayList<Veiling> getAlleVeilingen() {
		
		return Database.getAllVeilingen();
	}
	
	@Override
	public ArrayList<Veiling> getVeilingenCategorie(String Categorie){
		return Database.getVeilingenCategorie(Categorie);
	}
	

	@Override
	public Veiling getVeiling(int ID) {
		return Database.getVeiling(ID);
	}
	
	@Override
	public ArrayList<Veiling> getAllVeilingenByUser(String email) {
		return Database.getAllVeilingenByUser(email);
	}
	
	@Override
	public void blockUser(String email){
		Database.blockUser(email);
	}
	
	@Override
	public void unblockUser(String email){
		Database.unblockUser(email);
	}
	
	@Override 
	public void blockProd(int id){
		Database.blockProd(id);
	}
	
	@Override
	public void unblockProd(int id){
		Database.unblockProd(id);
	}

	@Override
	public int addBod(Bod b, Gebruiker g, int ID) {
		return Database.addBod(b, g, ID);
	}

	@Override
	public ArrayList<Product> getAllProducts() {
		return Database.getAllProducts();
	}

	@Override
	public Product addProduct(String nm, String omsc, String mk, String cat) {
		return Database.addProduct(nm, omsc, mk, cat);
	}

	@Override
	public boolean addVeiling(String email, int pID, int credits, String bDatum, String eDatum) {
		return Database.addVeiling(email, pID, credits, bDatum, eDatum);
	}

	@Override
	public ArrayList<String> getAlleCategories() {
		return Database.getAllCategorie();
	}
	
	@Override
	public ArrayList<Gebruiker> getAllUsers(){
		return Database.getAllUsers();
	}

	@Override
	public boolean updateGebruiker(Gebruiker g) {
		return Database.updateGebruiker(g);
	}

	@Override
	public ArrayList<Veiling> getAllVeilingenByBod(String email) {
		return Database.getAllVeilingenByBod(email);
	}

	@Override
	public ArrayList<Bod> getBiedingen(Date start, Date eind) {
		return Database.getBiedingen(start, eind);
	}

	@Override
	public ArrayList<Product> getAlleProducts() {
		return Database.getAlleProducts();
	}
	
	@Override
	public ArrayList<Veiling> zoekProduct(String naam){
		return Database.zoekProduct(naam);
	}
	
	@Override
	public ArrayList<Veiling> zoekProductCat(String naam, String category){
		return Database.zoekProductCat(naam, category);
	}

	@Override
	public ArrayList<Veiling> getTopVeilingen() {
		return Database.getTopVeilingen();
	}

	@Override
	public Gebruiker zoekGebruiker(String email) {
		// TODO Auto-generated method stub
		return Database.zoekGebruiker(email);
	}
}

package zwendelaar.client;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("zwendelaar")
public interface ZwendelaarService extends RemoteService {
	public Gebruiker loginUser(String email, String password);
	public boolean registerUser(String voornaam, String achternaam, String straat, String huisnummer, String postcode, String plaats, String email, String password);
	public ArrayList<Veiling> getAlleVeilingen();
	public ArrayList<Product> getAllProducts();
	public ArrayList<String> getAlleCategories();
	public ArrayList<Gebruiker> getAllUsers();
	public ArrayList<Veiling> getVeilingenCategorie(String Categorie);
	public ArrayList<Veiling> getTopVeilingen();
	public Product addProduct(String nm, String omsc, String mk, String cat);
	public Veiling getVeiling(int ID);
	public int addBod(Bod b, Gebruiker g, int ID);
	public boolean addVeiling(String email, int pID, int credits, String bDatum, String eDatum);
	public boolean updateGebruiker(Gebruiker g);
	public void blockUser(String email);
	public void unblockUser(String email);
	public void blockProd(int id);
	public void unblockProd(int id);
	public ArrayList<Veiling> getAllVeilingenByUser(String email);
	public ArrayList<Veiling> getAllVeilingenByBod(String email);
	public ArrayList<Bod> getBiedingen(Date start, Date eind);
	public ArrayList<Product> getAlleProducts();
	public ArrayList<Veiling> zoekProduct(String naam);
	public ArrayList<Veiling> zoekProductCat(String naam, String category);
	public Gebruiker zoekGebruiker(String email);
}

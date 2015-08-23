package zwendelaar.client;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ZwendelaarServiceAsync {
	public void loginUser(String email, String password, AsyncCallback<Gebruiker> callback);
	public void registerUser(String voornaam, String achternaam, String straat, String huisnummer, String postcode, String plaats, String email, String password, AsyncCallback<Boolean> callback);
	public void getAlleVeilingen(AsyncCallback<ArrayList<Veiling>> callback);
	public void getAllUsers(AsyncCallback<ArrayList<Gebruiker>> callback);
	public void getAllProducts(AsyncCallback<ArrayList<Product>> callback);
	public void getAlleCategories(AsyncCallback<ArrayList<String>> callback);
	public void getVeilingenCategorie(String Categorie, AsyncCallback<ArrayList<Veiling>> callback);
	public void getVeiling(int ID, AsyncCallback<Veiling> callback);
	public void addBod(Bod b, Gebruiker g, int ID, AsyncCallback<Integer> callback);
	public void addProduct(String nm, String omsc, String merk, String cat, AsyncCallback<Product> callback);
	public void addVeiling(String email, int pID, int credits, String bDatum, String eDatum, AsyncCallback<Boolean> callback);
	public void updateGebruiker(Gebruiker g, AsyncCallback<Boolean> callback);
	public void getAllVeilingenByUser(String email, AsyncCallback<ArrayList<Veiling>> callback);
	public void getAllVeilingenByBod(String email, AsyncCallback<ArrayList<Veiling>> callback);
	public void blockUser(String email, AsyncCallback<Void> callback);
	public void blockProd(int id, AsyncCallback<Void> callback);
	public void getBiedingen(Date start, Date eind, AsyncCallback<ArrayList<Bod>> callback);
	public void unblockUser(String email, AsyncCallback<Void> callback);
	public void unblockProd(int id, AsyncCallback<Void> callback);
	public void getAlleProducts(AsyncCallback<ArrayList<Product>> callback);
	public void zoekProduct(String naam, AsyncCallback<ArrayList<Veiling>> callback);
	public void zoekProductCat(String naam, String category, AsyncCallback<ArrayList<Veiling>> callback);
	public void getTopVeilingen(AsyncCallback<ArrayList<Veiling>> callback);
	public void zoekGebruiker(String email, AsyncCallback<Gebruiker> callback);
}

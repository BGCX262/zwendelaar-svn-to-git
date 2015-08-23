package zwendelaar.server;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import oracle.jdbc.OracleTypes;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;

public class Database {

	private static String driver = "oracle.jdbc.OracleDriver";
	private static String url = "jdbc:oracle:thin:@//ondora01.hu.nl:8521/cursus01.hu.nl";
	private static String username = "THO5_2011_3A_TEAM3";
	private static String password = "THO5_2011_3A_TEAM3";

	public Database(){

	}

	private static Connection getConnection()
	{
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static boolean register(String voornaam, String achternaam, String straat, String huisnummer, String postcode, String plaats , String email, String password)
	{
		Connection connection = getConnection();
		PreparedStatement pst;
		if (connection != null)
		{
			try {		
				if (!userExists(email, connection))
				{
					postcode = postcode.replaceAll("\\s+", "");
					pst = connection.prepareStatement("INSERT INTO Gebruiker VALUES(0, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0)");
					pst.setString(1, voornaam);
					pst.setString(2, achternaam);
					pst.setString(3, straat);
					pst.setString(4, huisnummer);
					pst.setString(5, postcode);
					pst.setString(6, plaats);
					pst.setString(7, email);
					pst.setString(8, password);
					pst.executeUpdate();
					pst.close();
					connection.close();
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;

	}	

	public static Gebruiker login(String email, String Password)
	{
		Gebruiker gebruiker = null;
		Connection connection = getConnection();
		if (connection != null)
		{
			PreparedStatement pst;
			try {
				pst = connection.prepareStatement("SELECT * FROM Gebruiker WHERE email = ?");
				pst.setString(1, email);
				ResultSet rs = pst.executeQuery();
				rs.next();
				if (rs.getString("password").equalsIgnoreCase(Password))
				{
					gebruiker = new Gebruiker(rs.getString("voornaam"), rs.getString("achternaam"), rs.getString("woonplaats"), rs.getString("postcode"), rs.getString("straat"), rs.getString("huisnummer"), rs.getInt("credits"), rs.getString("email"));
					if(rs.getInt("beheerder") == 1){
						gebruiker.setAdmin(true);
					}else{
						gebruiker.setAdmin(false);
					}
					if(rs.getInt("geblokkeerd") == 1){
						gebruiker.setGeblokkeerd(true);
					}else{
						gebruiker.setGeblokkeerd(false);
					}
				}
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return gebruiker;		
	}

	public static boolean userExists(String email, Connection connection)
	{
		PreparedStatement pst;
		boolean result = false;
		try {
			pst = connection.prepareStatement("SELECT COUNT(*) FROM Gebruikers WHERE email = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			rs.next();
			int count = rs.getInt(1);
			if (count == 1)
			{
				result = true;
			}
			pst.close();
			connection.close();
		} catch (SQLException e) {
			return false;
		}
		return result;
	}

	public static ArrayList<Gebruiker> getAllUsers(){
		ArrayList<Gebruiker> gebruikers = new ArrayList<Gebruiker>();
		Gebruiker gebruiker = null;
		Connection connection = getConnection();
		if(connection != null){
			PreparedStatement pst;
			try{
				pst = connection.prepareStatement("SELECT * FROM Gebruiker");
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					gebruiker = new Gebruiker(rs.getString("voornaam"), rs.getString("achternaam"), rs.getString("woonplaats"), rs.getString("postcode"), rs.getString("straat"), rs.getString("huisnummer"), rs.getInt("credits"), rs.getString("email"));
					gebruiker.setAdmin(rs.getInt("beheerder") == 1 ? true : false);
					gebruiker.setGeblokkeerd(rs.getInt("geblokkeerd") == 1 ? true : false);
					gebruikers.add(gebruiker);
				}
				pst.close();
				connection.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return gebruikers;
	}

	public static ArrayList<String> getAllCategorie(){
		ArrayList<String> categories = new ArrayList<String>();
		Connection connection = getConnection();
		if (connection != null)
		{
			PreparedStatement pst;
			try {
				pst = connection.prepareStatement("SELECT * FROM Categorie");
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					categories.add(rs.getString("categorie"));
				}
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}
		return categories;	
	}

	public static boolean addVeiling(String email, int pID, int credits, String sDatum, String bDatum){

		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				String[] start = sDatum.split("-");
				Calendar cstart = Calendar.getInstance();
				cstart.set(Calendar.DATE, Integer.parseInt(start[0]));
				cstart.set(Calendar.MONTH, Integer.parseInt(start[1]));
				cstart.set(Calendar.YEAR, Integer.parseInt(start[2]));

				String[] eind = sDatum.split("-");
				Calendar ceind = Calendar.getInstance();
				ceind.set(Calendar.DATE, Integer.parseInt(eind[0]));
				ceind.set(Calendar.MONTH, Integer.parseInt(eind[1]));
				ceind.set(Calendar.YEAR, Integer.parseInt(eind[2]));

				PreparedStatement pst = connection.prepareStatement("INSERT INTO VEILING(startdatum, sluitdatum, productid, credits, email) VALUES(?, ?, ?, ?, ?)");

				pst.setDate(1, new Date(cstart.getTime().getTime()));
				pst.setDate(2, new Date(ceind.getTime().getTime()));
				pst.setInt(3, pID);
				pst.setInt(4, credits);
				pst.setString(5, email);
				pst.executeUpdate();

				connection.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static Veiling getVeiling(int ID)
	{
		Veiling v = null;
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				PreparedStatement pst = connection.prepareStatement("SELECT * FROM Veiling WHERE ID = ?");
				pst.setString(1, String.valueOf(ID));
				ResultSet rsVeiling = pst.executeQuery();
				if (rsVeiling.next()) {
					PreparedStatement pst2 = connection.prepareStatement("SELECT id, naam, status, omschrijving, merk, categorie FROM product WHERE ID=?");
					pst2.setInt(1, rsVeiling.getInt("productid"));
					ResultSet rsProduct = pst2.executeQuery();
					if (rsProduct.next()) {
						v = new Veiling(rsVeiling.getString("email"), ID, rsVeiling.getInt("credits"), rsVeiling.getDate("sluitdatum").toString(), rsVeiling.getDate("startdatum").toString(), new Product(rsProduct.getInt(1), rsProduct.getString(2), rsProduct.getString(3), rsProduct.getString(4), rsProduct.getString(5), rsProduct.getString(6)));
						PreparedStatement pst3 = connection.prepareStatement("SELECT * FROM bod WHERE veilingID= ? ORDER BY Credits DESC");
						pst3.setInt(1, ID);
						ResultSet rsBiedingen = pst3.executeQuery();
						while (rsBiedingen.next())
						{
							v.addBod(new Bod(rsBiedingen.getInt("veilingid"), rsBiedingen.getInt("credits"), rsBiedingen.getDate("datum").toString(), rsBiedingen.getString("email")));
						}
						pst3.close();
					}
					pst2.close();
				}
				pst.close();
				
				
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return v;
	}

	public static ArrayList<Veiling> getAllVeilingenByUser(String email)
	{
		ArrayList<Veiling> al = new ArrayList<Veiling>();
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM veiling WHERE EMAIL='"+email+"' AND sluitdatum >= sysdate");
				while(rs.next())
				{
					PreparedStatement pst = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND ID=?");
					pst.setInt(1, rs.getInt("productid"));
					ResultSet rs2 = pst.executeQuery();
					rs2.next();
					Veiling v = new Veiling(rs.getString("email"), rs.getInt("id"), rs.getInt("credits"), rs.getDate("startdatum").toString(), rs.getDate("sluitdatum").toString(), new Product(rs2.getInt("id"), rs2.getString("naam"), rs2.getString("status"), rs2.getString("omschrijving"), rs2.getString("merk"), rs2.getString("categorie")));
					PreparedStatement pst3 = connection.prepareStatement("SELECT * FROM bod WHERE veilingID= ? ORDER BY Credits DESC");
					pst3.setInt(1, rs.getInt("id"));
					ResultSet rsBiedingen = pst3.executeQuery();
					while (rsBiedingen.next())
					{
						v.addBod(new Bod(rsBiedingen.getInt("veilingid"), rsBiedingen.getInt("credits"), rsBiedingen.getDate("datum").toString(), rsBiedingen.getString("email")));
					}
					al.add(v);
					pst.close();
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return al;
	}

	public static ArrayList<Veiling> getAllVeilingenByBod(String email)
	{
		ArrayList<Veiling> al = new ArrayList<Veiling>();
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				ResultSet uniqueBod = connection.createStatement().executeQuery("SELECT email, veilingid, MAX(credits) as credits FROM bod WHERE EMAIL='"+email+"' GROUP BY email,veilingid");
				while(uniqueBod.next())
				{

					PreparedStatement AllVeilingen = connection.prepareStatement("SELECT * FROM veiling WHERE id= ? AND sluitdatum >= sysdate");
					AllVeilingen.setString(1, uniqueBod.getString("veilingid"));
					ResultSet getAllVeilingen = AllVeilingen.executeQuery();
					getAllVeilingen.next();

					PreparedStatement AllProducts = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND ID=?");

					AllProducts.setInt(1, getAllVeilingen.getInt("productid"));
					ResultSet getAllProducts = AllProducts.executeQuery();

					getAllProducts.next();

					Veiling v = new Veiling(getAllVeilingen.getString("email"), getAllVeilingen.getInt("id"), getAllVeilingen.getInt("credits"), getAllVeilingen.getDate("startdatum").toString(), getAllVeilingen.getDate("sluitdatum").toString(), new Product(getAllProducts.getInt("id"), getAllProducts.getString("naam"), getAllProducts.getString("status"), getAllProducts.getString("omschrijving"), getAllProducts.getString("merk"), getAllProducts.getString("categorie")));

					ResultSet bodHigh = connection.createStatement().executeQuery("SELECT * FROM bod WHERE veilingid='"+uniqueBod.getInt("veilingid")+"' ORDER BY Credits DESC");
					while(bodHigh.next()){
						v.addBod(new Bod(bodHigh.getInt("veilingid"), bodHigh.getInt("credits"), bodHigh.getDate("datum").toString(), bodHigh.getString("email")));
					}
					al.add(v);
					AllVeilingen.close();
					AllProducts.close();
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return al;
	}

	public static ArrayList<Veiling> getAllVeilingen()
	{
		ArrayList<Veiling> al = new ArrayList<Veiling>();
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Veiling WHERE sluitdatum >= sysdate");
				while(rs.next())
				{
					PreparedStatement pst = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND ID=?");
					pst.setInt(1, rs.getInt("productid"));
					ResultSet rs2 = pst.executeQuery();
					rs2.next();
					Veiling v = new Veiling(rs.getString("email"), rs.getInt("id"), rs.getInt("credits"), rs.getDate("startdatum").toString(), rs.getDate("sluitdatum").toString(), new Product(rs2.getInt("id"), rs2.getString("naam"), rs2.getString("status"), rs2.getString("omschrijving"), rs2.getString("merk"), rs2.getString("categorie")));
					PreparedStatement pst3 = connection.prepareStatement("SELECT * FROM bod WHERE veilingID= ? ORDER BY Credits DESC");
					pst3.setInt(1, rs.getInt("id"));
					ResultSet rsBiedingen = pst3.executeQuery();
					while (rsBiedingen.next())
					{
						v.addBod(new Bod(rsBiedingen.getInt("veilingid"), rsBiedingen.getInt("credits"), rsBiedingen.getDate("datum").toString(), rsBiedingen.getString("email")));
					}
					al.add(v);
					pst.close();
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return al;
	}
	
	//Return de veilingen met meeste biedingen
	public static ArrayList<Veiling> getTopVeilingen()
	{
		ArrayList<Veiling> al = new ArrayList<Veiling>();
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				ResultSet getTopVeilingen = connection.createStatement().executeQuery("SELECT veilingid, COUNT(veilingid) AS aantalBoden from bod GROUP BY veilingid ORDER BY aantalboden DESC");
				while(getTopVeilingen.next())
				{
					PreparedStatement getVeilingen = connection.prepareStatement("SELECT * FROM veiling WHERE id=?");
					getVeilingen.setInt(1, getTopVeilingen.getInt("veilingid"));
					
					ResultSet resultGetVeilingen = getVeilingen.executeQuery();
					resultGetVeilingen.next();
					
					PreparedStatement getProducts = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND ID=?");
					getProducts.setInt(1, resultGetVeilingen.getInt("productid"));
					ResultSet resultProducts = getProducts.executeQuery();
					resultProducts.next();
					
					Veiling v = new Veiling(resultGetVeilingen.getString("email"), resultGetVeilingen.getInt("id"), resultGetVeilingen.getInt("credits"), resultGetVeilingen.getDate("startdatum").toString(), resultGetVeilingen.getDate("sluitdatum").toString(), new Product(resultProducts.getInt("id"), resultProducts.getString("naam"), resultProducts.getString("status"), resultProducts.getString("omschrijving"), resultProducts.getString("merk"), resultProducts.getString("categorie")));
					al.add(v);
					getVeilingen.close();
					getProducts.close();
				}
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return al;
	}

	public static int userhasCredits(String email, int Credits, Connection con)
	{
		int result = -1;
		if (con != null)
		{
			try {
				PreparedStatement pst = con.prepareStatement("SELECT credits FROM gebruiker WHERE email=?");
				pst.setString(1, email);
				ResultSet rs = pst.executeQuery();
				rs.next();
				if (Credits <= rs.getInt(1)) result = rs.getInt(1);
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static int addBod(Bod b, Gebruiker g, int ID)
	{
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				int Credits = userhasCredits(b.getEmail(), b.getCredits(), connection);
				if(Credits > 0)
				{
					g.substractCredits(b.credits);
					PreparedStatement pst = connection.prepareStatement("INSERT INTO BOD VALUES(?, ?, SYSDATE, ?)");
					PreparedStatement pst2 = connection.prepareStatement("UPDATE GEBRUIKER SET CREDITS = ? WHERE email = ?");
					pst.setInt(1, b.getVeilingid());
					pst.setString(2, b.getEmail());
					pst.setInt(3, b.getCredits());
					pst2.setInt(1, Credits - b.credits);
					pst2.setString(2, b.getEmail());
					pst.executeUpdate();
					pst2.executeUpdate();
					pst.close();
					pst2.close();
					connection.close();
					return 1;
				}
				else {
					connection.close();
					return 0;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	// Exclusief geblokkeerde producten
	public static ArrayList<Product> getAllProducts()
	{
		ArrayList<Product> products = new ArrayList<Product>();
		Connection connection = getConnection();
		Product product = null;
		if (connection != null)
		{
			try {
				PreparedStatement pst = connection.prepareStatement("SELECT * FROM Product WHERE geblokkeerd=0");
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					product =(new Product(rs.getInt("id"), rs.getString("naam"), rs.getString("status"), rs.getString("omschrijving"), rs.getString("merk"), rs.getString("categorie")));
					products.add(product);
				}
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return products;
	}

	// Inclusief geblokkeerde producten
	public static ArrayList<Product> getAlleProducts()
	{
		ArrayList<Product> products = new ArrayList<Product>();
		Connection connection = getConnection();
		Product product = null;
		if (connection != null)
		{
			PreparedStatement pst;
			try {
				pst = connection.prepareStatement("SELECT * FROM Product ORDER BY id");
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
					product =(new Product(rs.getInt("id"), rs.getString("naam"), rs.getString("status"), rs.getString("omschrijving"), rs.getString("merk"), rs.getString("categorie")));
					product.setGeblokkeerd(rs.getInt("geblokkeerd") == 1 ? true : false);
					products.add(product);
				}
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return products;
	}



	public static Product addProduct(String nm, String omschr, String merk, String cat)
	{
		Connection conn = getConnection();
		Product product = null;
		if (conn != null)
		{
			try {
				CallableStatement cstmt = conn.prepareCall("{? = call createProduct(?, ?, ?, ?)}");
				cstmt.registerOutParameter(1, OracleTypes.NUMBER);
				cstmt.setString(2, nm);
				cstmt.setString(3, omschr);
				cstmt.setString(4, merk);
				cstmt.setString(5, cat);
				cstmt.executeUpdate();
				int ID = cstmt.getInt(1);
				cstmt.close();
				product = new Product(ID, nm, "", omschr, merk, cat);

				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return product;
	}

	public static ArrayList<Veiling> getVeilingenCategorie(String Categorie){
		Connection connection = getConnection();
		ArrayList<Veiling> veilingen = new ArrayList<Veiling>();
		PreparedStatement pst;
		if (connection != null)
		{
			try {
				ResultSet rs = connection.createStatement().executeQuery("SELECT PRODUCTID, STARTDATUM, SLUITDATUM, CREDITS, EMAIL FROM Veiling");
				while(rs.next())
				{
					pst = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND categorie=? AND id=? ORDER BY Naam DESC");
					pst.setString(1, Categorie);
					pst.setInt(2, rs.getInt("productID"));
					ResultSet rs2 = pst.executeQuery();
					if(rs2.next()){
						veilingen.add(new Veiling(rs.getString("EMAIL"),
								rs.getInt(1),
								rs.getInt(4),
								rs.getDate(2).toString(),
								rs.getDate(3).toString(),
								new Product(rs2.getInt(1),
										rs2.getString(2),
										rs2.getString(3),
										rs2.getString(4),
										rs2.getString(5),
										rs2.getString(6))));
					}
					pst.close();
				}	
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return veilingen;
	}



	public static boolean updateGebruiker(Gebruiker g)
	{
		boolean Result = false;
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				PreparedStatement pst = connection.prepareStatement("UPDATE Gebruiker SET Voornaam = ?, Achternaam = ?, Straat = ?, Huisnummer = ?, Postcode = ?, Woonplaats = ?, Credits = ? WHERE email = ?");
				pst.setString(1, g.getVoornaam());
				pst.setString(2, g.getAchternaam());
				pst.setString(3, g.getStraatNaam());
				pst.setString(4, g.getHuisNummer());
				pst.setString(5, g.getPostCode());
				pst.setString(6, g.getWoonPlaats());
				pst.setInt(7, g.getCredits());
				pst.setString(8, g.getEmailAdres());
				pst.executeUpdate();
				pst.close();
				connection.close();
				Result = true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Result;
	}

	public static void blockUser(String email){
		Connection connection = getConnection();
		PreparedStatement pst;
		String updateString = "UPDATE Gebruiker SET Geblokkeerd=1 WHERE Email=?";
		if (connection != null)
		{
			try {
				pst = connection.prepareStatement(updateString);
				pst.setString(1, email);
				pst.execute();
				pst.close();
				connection.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}

	public static void unblockUser(String email){
		Connection connection = getConnection();
		PreparedStatement pst;
		String updateString = "UPDATE Gebruiker SET Geblokkeerd=0 WHERE Email=?";
		if (connection != null)
		{
			try {
				pst = connection.prepareStatement(updateString);
				pst.setString(1, email);
				pst.execute();
				pst.close();
				connection.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}

	public static void blockProd(int id) {
		Connection connection = getConnection();
		PreparedStatement pst;
		String updateString = "UPDATE Product SET Geblokkeerd=1 WHERE id=?";
		if (connection != null)
		{
			try {
				pst = connection.prepareStatement(updateString);
				pst.setInt(1, id);
				pst.execute();
				pst.close();
				connection.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}

	public static void unblockProd(int id) {
		Connection connection = getConnection();
		PreparedStatement pst;
		String updateString = "UPDATE Product SET Geblokkeerd=0 WHERE id=?";
		if (connection != null)
		{
			try {
				pst = connection.prepareStatement(updateString);
				pst.setInt(1, id);
				pst.execute();
				pst.close();
				connection.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
	}

	public static ArrayList<Bod> getBiedingen(java.util.Date start, java.util.Date eind) {
		ArrayList<Bod> result = new ArrayList<Bod>();
		Connection connection = getConnection();
		if (connection != null)
		{
			try {
				PreparedStatement pst = connection.prepareStatement("SELECT * FROM bod WHERE datum >= ? AND datum <= ? ORDER BY Credits DESC");
				pst.setDate(1, new Date(start.getTime()));
				pst.setDate(2, new Date(eind.getTime()));
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					result.add(new Bod(rs.getInt("veilingid"), rs.getInt("credits"), rs.getString("datum"), rs.getString("email")));
				}
				pst.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}
		}
		return result;
	}

	public static ArrayList<Veiling> zoekProduct(String naam){
		Connection connection = getConnection();
		ArrayList<Veiling> veilingen = new ArrayList<Veiling>();
		PreparedStatement pst;
		if (connection != null)
		{
			try {
				ResultSet rs = connection.createStatement().executeQuery("SELECT PRODUCTID, STARTDATUM, SLUITDATUM, CREDITS, EMAIL FROM Veiling");
				while(rs.next())
				{
					pst = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND naam like ? AND id=? ORDER BY Naam DESC");
					pst.setString(1, naam);
					pst.setInt(2, rs.getInt("productID"));
					ResultSet rs2 = pst.executeQuery();
					if(rs2.next()){
						veilingen.add(new Veiling(rs.getString("EMAIL"),
								rs.getInt(1),
								rs.getInt(4),
								rs.getDate(2).toString(),
								rs.getDate(3).toString(),
								new Product(rs2.getInt(1),
										rs2.getString(2),
										rs2.getString(3),
										rs2.getString(4),
										rs2.getString(5),
										rs2.getString(6))));
					}
					pst.close();
				}	
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return veilingen;
	}

	public static ArrayList<Veiling> zoekProductCat(String naam, String category) {
		Connection connection = getConnection();
		ArrayList<Veiling> veilingen = new ArrayList<Veiling>();
		PreparedStatement pst;
		if (connection != null)
		{
			try {
				ResultSet rs = connection.createStatement().executeQuery("SELECT PRODUCTID, STARTDATUM, SLUITDATUM, CREDITS, EMAIL FROM Veiling");
				while(rs.next())
				{
					pst = connection.prepareStatement("SELECT * FROM product WHERE geblokkeerd=0 AND naam like ? AND Categorie=? AND id=? ORDER BY Naam DESC");
					pst.setString(1, naam);
					pst.setString(2, category);
					pst.setInt(3, rs.getInt("productID"));
					ResultSet rs2 = pst.executeQuery();
					if(rs2.next()){
						veilingen.add(new Veiling(rs.getString("EMAIL"),
								rs.getInt(1),
								rs.getInt(4),
								rs.getDate(2).toString(),
								rs.getDate(3).toString(),
								new Product(rs2.getInt(1),
										rs2.getString(2),
										rs2.getString(3),
										rs2.getString(4),
										rs2.getString(5),
										rs2.getString(6))));
					}
					pst.close();
				}	
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		return veilingen;
	}

	public static Gebruiker zoekGebruiker(String email){
		{
			Gebruiker gebruiker = null;
			Connection connection = getConnection();
			if (connection != null)
			{
				PreparedStatement pst;
				try {
					pst = connection.prepareStatement("SELECT * FROM Gebruiker WHERE email = ?");
					System.out.println("" + email);
					pst.setString(1, email);
					ResultSet rs = pst.executeQuery();
					rs.next();
					gebruiker = new Gebruiker(rs.getString("voornaam"), rs.getString("achternaam"), rs.getString("woonplaats"), rs.getString("postcode"), rs.getString("straat"), rs.getString("huisnummer"), rs.getInt("credits"), rs.getString("email"));
					pst.close();
					connection.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
			return gebruiker;
		}
	}
}

package zwendelaar.client.ui;

import java.util.ArrayList;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Gebruiker;
import zwendelaar.client.domain.Product;
import zwendelaar.client.domain.Veiling;
import zwendelaar.server.Database;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Blokkeren {
	HorizontalPanel main = new HorizontalPanel();

	VerticalPanel submenu = new VerticalPanel();
	VerticalPanel content = new VerticalPanel();

	FlexTable ft = new FlexTable();

	private Label lblUsers, lblProd, lblvoorNaam, lblachterNaam, lblEmail;
	private Label lblProdID, lblProdNm, lblProdBrand;
	private Button userBlock, prodBlock, userInfo, prodInfo;
	private int i = 1, id = 0, j= 1;
	private String naam = "";

	public Blokkeren(){
		submenu.setStyleName("menu");
		content.setStyleName("content");

		lblUsers = new Label("Gebruikers blokkeren");
		lblUsers.setStyleName("customLink");
		lblUsers.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				content.clear();
				ft.clear();
				loadUsers();
			}
		});

		lblProd = new Label("Producten blokkeren");
		lblProd.setStyleName("customLink");
		lblProd.addClickHandler(new ClickHandler(){
			@Override 
			public void onClick(ClickEvent event){
				content.clear();
				ft.clear();
				loadProducts();
			}
		});	
	}
	public HorizontalPanel createPage(){

		submenu.add(lblUsers);
		submenu.add(lblProd);

		loadUsers();
		main.add(submenu);
		main.add(content);

		return main;
	}
	
	public HorizontalPanel createPage2(){

		submenu.add(lblUsers);
		submenu.add(lblProd);

		loadProducts();
		main.add(submenu);
		main.add(content);

		return main;
	}
	

	private void loadUsers(){
		i = 0;
		content.clear();
		ft.clear();
		ft.setText(0,0,"Voornaam");
		ft.setText(0,1,"Achternaam");
		ft.setText(0,2,"Emailadres");
		ft.setText(0,3, "");
		ft.setText(0,4, "");
		ft.setStyleName("flexTable");



		AsyncCallback<ArrayList<Gebruiker>> callback = new AsyncCallback<ArrayList<Gebruiker>>() {

			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er zijn geen gebruikers gevonden");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Gebruiker> result) {
				if(result.size() == 0){
					NotificationManager.get().createNotification("Er zijn geen gebruikers gevonden.");
				}else{
					for(final Gebruiker u : result){
						ft.setText(0,0,"Voornaam");
						ft.setText(0,1,"Achternaam");
						ft.setText(0,2,"Emailadres");
						ft.setText(0,3, "");
						ft.setText(0,4, "");
						lblvoorNaam = new Label(u.getVoornaam());
						lblachterNaam = new Label(u.getAchternaam());
						lblEmail = new Label(u.getEmailAdres());
						final String email = new String(u.getEmailAdres());
						userBlock = new Button(" ");
						if(!u.getBlock())userBlock.setStyleName("blockKnop");
						if(u.getBlock())userBlock.setStyleName("unblockKnop");
						
						userInfo = new Button("");
						userInfo.setStyleName("infoKnop");

						userBlock.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event){
								if(!email.equals("")){
									// Als niet geblocked.
									if(!u.getBlock()){
										AsyncCallback<Void> callback = new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												caught.printStackTrace();
												NotificationManager.get().createNotification("Er is een fout met de database opgelopen.");
											}

											@Override
											public void onSuccess(Void v) {
												NotificationManager.get().createNotification("Gebruiker met email: " + email + " is geblokkeerd.");
											}
										};
										Zwendelaar.get().blockUser(email, callback);
										Zwendelaar.get().showPage(new Blokkeren().createPage());
									}
									// Als wel geblocked
									if(u.getBlock()){
										AsyncCallback<Void> callback = new AsyncCallback<Void>() {

											@Override
											public void onFailure(Throwable caught) {
												caught.printStackTrace();
												NotificationManager.get().createNotification("Er is een fout met de database opgelopen.");
											}

											@Override
											public void onSuccess(Void v) {
												NotificationManager.get().createNotification("Gebruiker met email: " + email + " is niet langer geblokkeerd.");
											}
										};
										Zwendelaar.get().unblockUser(email, callback);
										Zwendelaar.get().showPage(new Blokkeren().createPage());
									}								
								}
							}
						});
						
						userInfo.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event) {
								History.newItem("userInfo?email="+u.getEmailAdres());	
							}
						});
						
						ft.setWidget(i,0,lblvoorNaam);
						ft.setWidget(i, 1, lblachterNaam);
						ft.setWidget(i,2, lblEmail);
						ft.setWidget(i,3, userBlock);
						ft.setWidget(i, 4, userInfo);

						for(int i=0; i<ft.getRowCount(); i++){
							if(i%2 == 1){
								ft.getFlexCellFormatter().setStyleName(i,0,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,1,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,2,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,3,"tableCell-odd");
							}
						}
						ft.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,3,"tableHeader");

						content.add(ft);
						i++;
					}
				}	
			}

		};	
		new Zwendelaar().getAllUsers(callback);

	}

	private void loadProducts(){
		j = 0;
		content.clear();
		ft.clear();
		ft.setText(0,0,"Product ID");
		ft.setText(0,1,"Productnaam");
		ft.setText(0,2,"Merk");
		ft.setText(0,3, "");
		ft.setStyleName("flexTable");

		AsyncCallback<ArrayList<Product>> callback = new AsyncCallback<ArrayList<Product>>(){
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er zijn geen producten gevonden");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<Product> result) {
				if(result.size() == 0){
					NotificationManager.get().createNotification("Er zijn geen producten gevonden.");
				}else{
					for(final Product p : result){
						ft.setText(0,0,"Product ID");
						ft.setText(0,1,"Productnaam");
						ft.setText(0,2,"Merk");
						ft.setText(0,3, "");
						lblProdID = new Label("" + p.getID());
						lblProdNm = new Label(p.getNaam());
						lblProdBrand = new Label(p.getMerk());

						id = p.getID();
						naam = new String(p.getNaam());

						prodBlock = new Button(" ");
						prodBlock.setStyleName("blockKnop");
						if(!p.getBlock())prodBlock.setStyleName("blockKnop");
						if(p.getBlock())prodBlock.setStyleName("unblockProdKnop");

						prodBlock.addClickHandler(new ClickHandler(){
							@Override
							public void onClick(ClickEvent event){
								if(id != 0){
									// Als niet geblockt
									if(!p.getBlock()){
										AsyncCallback<Void> callback = new AsyncCallback<Void>() {
											@Override
											public void onFailure(Throwable caught) {
												caught.printStackTrace();
												NotificationManager.get().createNotification("Er is een fout met de database opgelopen.");
											}

											@Override
											public void onSuccess(Void v) {
												NotificationManager.get().createNotification("Product " + p.getNaam() + " met product ID: " + p.getID() + " is geblokkeerd.");
											}
										};
										Zwendelaar.get().blockProd(p.getID(), callback);
										Zwendelaar.get().showPage(new Blokkeren().createPage2());
									}
									// Als wel geblockt
									if(p.getBlock()){
										AsyncCallback<Void> callback = new AsyncCallback<Void>() {
											@Override
											public void onFailure(Throwable caught) {
												caught.printStackTrace();
												NotificationManager.get().createNotification("Er is een fout met de database opgelopen.");
											}

											@Override
											public void onSuccess(Void v) {
												NotificationManager.get().createNotification("Product " + p.getNaam() + " met product ID: " + p.getID() + " is niet langer geblokkeerd.");
											}
										};
										Zwendelaar.get().unblockProd(p.getID(), callback);
										Zwendelaar.get().showPage(new Blokkeren().createPage2());
									}
								}
							}
						});
						ft.setWidget(j,0,lblProdID);
						ft.setWidget(j, 1, lblProdNm);
						ft.setWidget(j,2, lblProdBrand);
						ft.setWidget(j,3, prodBlock);

						for(int i=0; i<ft.getRowCount(); i++){
							if(i%2 == 1){
								ft.getFlexCellFormatter().setStyleName(i,0,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,1,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,2,"tableCell-odd");
								ft.getFlexCellFormatter().setStyleName(i,3,"tableCell-odd");
							}
						}
						ft.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
						ft.getFlexCellFormatter().setStyleName(0,3,"tableHeader");

						content.add(ft);
						j++;
					}
				}
			}
		};
		new Zwendelaar().getAlleProducts(callback);
	}


}

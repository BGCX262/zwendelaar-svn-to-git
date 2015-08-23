package zwendelaar.client.ui;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Statistiek {

	HorizontalPanel main = new HorizontalPanel();
	HorizontalPanel info = new HorizontalPanel();
	FlexTable tabel = new FlexTable();
	
	int ID = -1;
	
	Veiling veiling;
	public Statistiek(String ID) {
		if (Zwendelaar.get().getGebruiker() != null && Zwendelaar.get().getGebruiker().getAdmin()) {
			try {
				this.ID = Integer.parseInt(ID);
			} catch (NumberFormatException nfe){
				//TODO Handle Exception
				return;
			}
			if (this.ID > -1) {
	
				AsyncCallback<Veiling> callback = new AsyncCallback<Veiling>() {
	
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
	
					@Override
					public void onSuccess(final Veiling v) {
						if (v != null)
						{
							if (v.getAlleBiedingen().size() > 0)
							{
								tabel.setText(0, 0, "Door");
								tabel.setText(0, 1, "Datum");
								tabel.setText(0, 2, "Bod");
								int i = 1;
								for (Bod b : v.getAlleBiedingen()) {
									tabel.setText(i, 0, b.getEmail());
									tabel.setText(i, 1, b.datum);
									tabel.setText(i, 2, String.valueOf(b.credits));
									i++;
								}
								for(int j=0; j <tabel.getRowCount(); j++){
									if(j%2 == 1){
										tabel.getFlexCellFormatter().setStyleName(j,0,"tableCell-odd");
										tabel.getFlexCellFormatter().setStyleName(j,1,"tableCell-odd");
										tabel.getFlexCellFormatter().setStyleName(j,2,"tableCell-odd");
									}
								}
								tabel.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
								tabel.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
								tabel.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
								tabel.setStyleName("flexTable");
								main.add(tabel);
							} else
								main.add(new Label("Er zijn geen biedingen voor deze veiling"));
							Grid infogrid = new Grid(6,2);
							infogrid.setText(0, 0, "Aanbieder:");
							infogrid.setText(1, 0, "Start:");
							infogrid.setText(2, 0, "Einde:");
							infogrid.setText(3, 0, "Start bedrag:");
							infogrid.setText(4, 0, "Product:");
							infogrid.setText(5, 0, "Omschrijving:");
							infogrid.setText(0, 1, v.getEmail());
							infogrid.setText(1, 1, v.getBeginDatum());
							infogrid.setText(2, 1, v.getSluitDatum());
							infogrid.setText(3, 1, String.valueOf(v.getMinCredits()));
							infogrid.setText(4, 1, v.getProduct().getNaam());
							infogrid.setText(5, 1, v.getProduct().getOmschrijving());
							infogrid.setStyleName("info");
							info.add(infogrid);
							main.add(info);
						} else 
							main.add(new Label("Geen veiling gevonden"));
					}
					
				};			
				Zwendelaar.get().getVeiling(this.ID, callback);	
			} else
				main.add(new Label("Geen geldig ID ingevoerd"));
		} else
			main.add(new Label("U hebt geen toegang tot deze pagina"));
		
	}
	public Widget createpage()
	{
		return main;
	}
	
}

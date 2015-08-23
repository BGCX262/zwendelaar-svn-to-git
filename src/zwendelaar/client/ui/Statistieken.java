package zwendelaar.client.ui;

import java.util.ArrayList;
import java.util.Date;

import zwendelaar.client.Zwendelaar;
import zwendelaar.client.domain.Bod;
import zwendelaar.client.domain.Veiling;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Statistieken {
	
	HorizontalPanel main = new HorizontalPanel();
	
	public Statistieken()
	{
		TabPanel tp = new TabPanel();
	    tp.add(getOverzichtveiling(), "Overzicht alle veilingen");
	    tp.add(getTopVeilingen(), "Populaire Veilingen");
	    tp.selectTab(0);
	    main.add(tp);
	}

	public Widget createpage() {
		return main;
	}
	

	private Widget getTopVeilingen() {
		final VerticalPanel result = new VerticalPanel();
		final VerticalPanel holder = new VerticalPanel();
		
		result.add(holder);
		final FlexTable data = new FlexTable();
		
		AsyncCallback<ArrayList<Veiling>> callback = new AsyncCallback<ArrayList<Veiling>>() {
			@Override
			public void onFailure(Throwable caught) {
				NotificationManager.get().createNotification("Er is een server fout opgetreden. ");
				GWT.log(caught.getMessage());
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(ArrayList<Veiling> result) {
				int i = 1;
				for(final Veiling v : result){
					data.setText(0, 0, "Positie");
					data.setText(0, 1, "Veiling naam");
					data.setText(0, 2, "Begin datum");
					data.setText(0, 3, "Info");
					if(i == 6){
						break;
					}else{
						HTMLPanel p = new HTMLPanel("<a href='#bieden?id="+v.ID+"' class='customLink'>Info</a>");
						data.setText(i, 0, String.valueOf(i));
						data.setText(i, 1, v.getProduct().getNaam());
						data.setText(i, 2, v.getBeginDatum());
						data.setWidget(i, 3, p);
						i++;
					}
					for(int j=0; j < data.getRowCount(); j++){
						if(j%2 == 1){
							data.getFlexCellFormatter().setStyleName(j,0,"tableCell-odd");
							data.getFlexCellFormatter().setStyleName(j,1,"tableCell-odd");
							data.getFlexCellFormatter().setStyleName(j,2,"tableCell-odd");
							data.getFlexCellFormatter().setStyleName(j,3,"tableCell-odd");
						}
					}
					data.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
					data.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
					data.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
					data.getFlexCellFormatter().setStyleName(0,3,"tableHeader");
					data.setStyleName("flexTable");
					
				}
				holder.add(data);
			}
		};
		Zwendelaar.get().getTopVeilingen(callback);
		return result;
	}
	
	public Widget getOverzichtveiling()
	{
		final VerticalPanel result = new VerticalPanel();
		final ListBox widget = new ListBox();
		final VerticalPanel holder = new VerticalPanel();
		result.add(widget);
		result.add(holder);
	    widget.addStyleName("demo-ListBox");
	    widget.addItem("Vandaag");
	    widget.addItem("Laatste week");
	    widget.addItem("Afgelopen maand");
	    widget.addItem("Afgelopen jaar");
	    widget.addChangeHandler(new ChangeHandler() {
			@SuppressWarnings("deprecation")
			@Override
			public void onChange(ChangeEvent event) {
				holder.clear();
				final FlexTable data = new FlexTable();
				AsyncCallback<ArrayList<Bod>> callback = new AsyncCallback<ArrayList<Bod>>() {

					@Override
					public void onFailure(Throwable caught) {
						NotificationManager.get().createNotification("Er is een fout opgetreden", 3000);
					}

					@Override
					public void onSuccess(ArrayList<Bod> result) {
						data.clear();
						data.setText(0, 0, "Bieder");
						data.setText(0, 1, "Datum");
						data.setText(0, 2, "Credits");
						int i = 1;
						for (Bod b : result)
						{
							if (i > 5)
								break;
							data.setText(i, 0, b.getEmail());
							data.setText(i, 1, b.datum);
							data.setText(i, 2, String.valueOf(b.getCredits()));
							i++;
						}
						for(int j=0; j < data.getRowCount(); j++){
							if(j%2 == 1){
								data.getFlexCellFormatter().setStyleName(j,0,"tableCell-odd");
								data.getFlexCellFormatter().setStyleName(j,1,"tableCell-odd");
								data.getFlexCellFormatter().setStyleName(j,2,"tableCell-odd");
							}
						}
						data.getFlexCellFormatter().setStyleName(0,0,"tableHeader");
						data.getFlexCellFormatter().setStyleName(0,1,"tableHeader");
						data.getFlexCellFormatter().setStyleName(0,2,"tableHeader");
						data.setStyleName("flexTable");
					}
					
				};
				int selected = widget.getSelectedIndex();
				Date eind = new Date();
				Date start = null;
				if (selected < 0)
					return;
				else if (selected == 0)
					Zwendelaar.get().getBiedingen(eind, eind, callback);
				else if (selected == 1) {
					start = new Date();
					start.setDate(start.getDate() - 7);
					Zwendelaar.get().getBiedingen(start, eind, callback);
				}
				else if (selected == 2) {
					start = new Date();
					start.setMonth(start.getMonth() - 1);
					Zwendelaar.get().getBiedingen(start, eind, callback);
				}
				else if (selected == 3) {
					start = new Date();
					start.setYear(start.getYear() - 1);
					Zwendelaar.get().getBiedingen(start, eind, callback);
				}
				System.out.println(start);
				holder.add(data);
			}	    	
	    });
	    
		return result;
	}
}

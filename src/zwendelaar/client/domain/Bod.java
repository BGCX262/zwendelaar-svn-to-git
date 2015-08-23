package zwendelaar.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Bod implements IsSerializable{
	
	public int veilingid;
	public int credits;
	public String datum;
	private String email;
	
	public Bod() { }
	
	public Bod(int veilingid, int credit, String date, String email){
		this.veilingid = veilingid;
		this.credits = credit;
		this.datum = date;
		this.email = email;
	}
	
	public int getCredits(){
		return credits;
	}
	
	public String getEmail(){
		return email;
	}
	
	public int getVeilingid(){
		return veilingid;
	}
}
